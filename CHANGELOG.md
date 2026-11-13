# Changelog

## v1.0.0

First enhanced release.

### Features
- Updated the project for modern Paper-compatible server versions.
- Updated the Simple Voice Chat API dependency to 2.6.27.
- Added permission-controlled voice broadcasting.
- Broadcasts audio from players in the `broadcast` voice chat group.
- Forwards audio to all other online players with an active Simple Voice Chat connection, regardless of their current group.
- Uses a stable per-broadcaster audio channel identifier.

### Compatibility
- Java 21
- Paper-compatible server implementations
- Simple Voice Chat 2.6.27

### Testing
- Successfully built with Gradle.
- Runtime tested on Leaf 1.21.11 with Simple Voice Chat 2.6.27.