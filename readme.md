# Simple Voice Chat Broadcast Enhanced

An updated broadcast addon for [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat), allowing authorised players to transmit their voice to everyone else connected to voice chat on the server.

This project is based on [henkelmax/voicechat-broadcast-plugin](https://github.com/henkelmax/voicechat-broadcast-plugin).

## Requirements

- Minecraft Java Edition
- Paper or a compatible server implementation
- Java 21
- [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat)

## Installation

1. Install Simple Voice Chat on your server.
2. Download the latest release of Simple Voice Chat Broadcast Enhanced.
3. Place the plugin JAR in your server's `plugins` directory.
4. Restart your server.

## Usage

1. Make sure you have the `voicechat_broadcast.broadcast` permission.
2. Open the Simple Voice Chat group interface.
3. Create or join a voice chat group named `broadcast`.
4. Speak while connected to that group.

Your voice will be transmitted to every other online player with an active Simple Voice Chat connection, regardless of their current voice chat group.

The group name is case-insensitive.

## Permissions

| Permission | Description | Default |
|---|---|---|
| `voicechat_broadcast.broadcast` | Allows a player to broadcast their voice | Operator |

Permissions can also be managed using a permissions plugin such as LuckPerms.

## Compatibility

Runtime tested successfully with:

- Leaf 1.21.11
- Java 21
- Simple Voice Chat 2.6.27

Other Paper-compatible server implementations may work, provided they support a compatible Simple Voice Chat version.

## Credits

- Original plugin by Max Henkel: [henkelmax/voicechat-broadcast-plugin](https://github.com/henkelmax/voicechat-broadcast-plugin)
- Fork maintainer: TYPOPLAYS (https://github.com/TYPOPLAYS)
- Voice chat dependency: [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat)

## License

This project is licensed under the MIT License. See [license](license) for details.