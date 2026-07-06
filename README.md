# Portal Cornerlink

Control Nether portal linking by placing glazed terracotta at portal corners. Portals connect to destinations with matching corner patterns.

**Fork of**: [Corner Portal Linking](https://gitlab.com/mc-starbidou/corner-portal-linking) by starbidou
**This version**: Ported to Minecraft 1.21.1 on NeoForge with performance improvements (4-8x faster portal matching)

## How It Works

1. Place any colored glazed terracotta at the 4 corners of a Nether portal
2. On the other side, portals with matching corner patterns link together
3. No corners = vanilla behavior

Patterns are matched by block type, orientation doesn't matter (flipped portals still match).

## Build

### Standard Build
```bash
./gradlew build
```
Output: `build/libs/matthew_mbg-cornerlink-neoforge-1.0.0.jar`

### Development
```bash
./gradlew runClient  # Launch client with mod
./gradlew runServer  # Launch server with mod
```

### Nix Shell
```bash
nix develop  # Enter dev environment with JDK 21 and Gradle
```

The flake provides a reproducible development environment with all required dependencies.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.x
- Java 21

## License

LGPL v2.1
