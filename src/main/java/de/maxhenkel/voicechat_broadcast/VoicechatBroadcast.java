package de.maxhenkel.voicechat_broadcast;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class VoicechatBroadcast extends JavaPlugin implements Listener {

    public static final String PLUGIN_ID = "voicechat_broadcast";

    private BroadcastVoicechatPlugin voicechatPlugin;
    private BukkitTask snapshotTask;

    @Override
    public void onEnable() {
        BukkitVoicechatService service = getServer()
                .getServicesManager()
                .load(BukkitVoicechatService.class);

        if (service == null) {
            getLogger().severe(
                    "Failed to register voice chat broadcast plugin: Simple Voice Chat service unavailable."
            );
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        voicechatPlugin = new BroadcastVoicechatPlugin();

        refreshSnapshot();

        getServer().getPluginManager().registerEvents(this, this);

        snapshotTask = getServer().getScheduler().runTaskTimer(
                this,
                this::refreshSnapshot,
                20L,
                20L
        );

        service.registerPlugin(voicechatPlugin);

        getLogger().info("Successfully registered voice chat broadcast plugin.");
    }

    @Override
    public void onDisable() {
        if (snapshotTask != null) {
            snapshotTask.cancel();
            snapshotTask = null;
        }

        if (voicechatPlugin != null) {
            voicechatPlugin.deactivate();
            voicechatPlugin = null;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        refreshSnapshot();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        getServer().getScheduler().runTask(this, this::refreshSnapshot);
    }

    private void refreshSnapshot() {
        if (voicechatPlugin == null) {
            return;
        }

        Set<UUID> broadcasters = new HashSet<>();
        Set<UUID> recipients = new HashSet<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();

            recipients.add(playerId);

            if (player.hasPermission(BroadcastVoicechatPlugin.BROADCAST_PERMISSION)) {
                broadcasters.add(playerId);
            }
        }

        voicechatPlugin.updateSnapshot(broadcasters, recipients);
    }
}