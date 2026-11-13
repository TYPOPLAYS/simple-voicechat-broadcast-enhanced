package de.maxhenkel.voicechat_broadcast;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.packets.StaticSoundPacket;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public final class BroadcastVoicechatPlugin implements VoicechatPlugin {

    public static final Permission BROADCAST_PERMISSION = new Permission(
            "voicechat_broadcast.broadcast",
            PermissionDefault.OP
    );

    private static final String BROADCAST_GROUP = "broadcast";
    private static final String CHANNEL_PREFIX = "voicechat_broadcast:";

    private volatile PlayerSnapshot snapshot = new PlayerSnapshot(Set.of(), Set.of());
    private volatile boolean active = true;

    @Override
    public String getPluginId() {
        return VoicechatBroadcast.PLUGIN_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(
                MicrophonePacketEvent.class,
                this::onMicrophone
        );
    }

    public void updateSnapshot(Set<UUID> broadcasters, Set<UUID> recipients) {
        snapshot = new PlayerSnapshot(
                Set.copyOf(broadcasters),
                Set.copyOf(recipients)
        );
    }

    public void deactivate() {
        active = false;
        snapshot = new PlayerSnapshot(Set.of(), Set.of());
    }

    private void onMicrophone(MicrophonePacketEvent event) {
        if (!active) {
            return;
        }

        if (event.getSenderConnection() == null) {
            return;
        }

        UUID senderId = event.getSenderConnection().getPlayer().getUuid();
        PlayerSnapshot currentSnapshot = snapshot;

        if (!currentSnapshot.broadcasters().contains(senderId)) {
            return;
        }

        Group group = event.getSenderConnection().getGroup();

        if (group == null || !BROADCAST_GROUP.equalsIgnoreCase(group.getName().strip())) {
            return;
        }

        UUID channelId = UUID.nameUUIDFromBytes(
                (CHANNEL_PREFIX + senderId).getBytes(StandardCharsets.UTF_8)
        );

        StaticSoundPacket packet;

        try {
            packet = event.getPacket()
                    .staticSoundPacketBuilder()
                    .channelId(channelId)
                    .build();
        } catch (IllegalStateException exception) {
            return;
        }

        if (!event.cancel()) {
            return;
        }

        VoicechatServerApi api = event.getVoicechat();

        for (UUID recipientId : currentSnapshot.recipients()) {
            if (recipientId.equals(senderId)) {
                continue;
            }

            VoicechatConnection connection = api.getConnectionOf(recipientId);

            if (connection == null) {
                continue;
            }

            api.sendStaticSoundPacketTo(connection, packet);
        }
    }

    private record PlayerSnapshot(
            Set<UUID> broadcasters,
            Set<UUID> recipients
    ) {
    }
}