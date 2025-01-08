# SimpleFly
*Simple Flight, Powerful Control*

A lightweight and user-friendly flight management plugin that gives you complete control over who can fly on your server.

## Features
- Toggle flight with simple commands
- Temporary flight duration support (seconds, minutes, hours, days)
- Adjustable flight speed control
- Combat flight restrictions
- Per-world flight settings
- Permission-based access control
- Customizable prefix
- Reload configuration in-game
- No database required
- Supports Minecraft 1.13+
- Auto-update notifications

## Commands
- `/simplefly` (`/sf`) - Toggle flight mode
- `/simplefly <player>` - Toggle flight for other players
- `/simplefly <duration>` - Enable temporary flight (e.g., 30s, 5m, 2h, 1d)
- `/simplefly speed <1-10>` - Set your flight speed
- `/simplefly speed <player> <1-10>` - Set another player's flight speed
- `/simplefly reload` - Reload plugin configuration
- `/simplefly help` - Show help message

## Permissions
- `simplefly.use` - Allow using flight
- `simplefly.others` - Allow toggling flight for others
- `simplefly.duration` - Allow using temporary flight
- `simplefly.speed` - Allow changing flight speed
- `simplefly.speed.others` - Allow changing others' flight speed
- `simplefly.reload` - Allow reloading the plugin
- `simplefly.bypass.combat` - Bypass combat restrictions
- `simplefly.updates` - Receive update notifications
- `simplefly.*` - All permissions

## Configuration
Simple configuration with:
- Customizable prefix
- Combat duration settings
- Per-world flight restrictions
- Flight state persistence
- Default flight speed

## Support
Need help? Found a bug? Have a suggestion? [Join our Discord](https://discord.gg/qsRhJUP4q5)

## Coming Soon
- Flight zones
- Particle effects
- More customization options