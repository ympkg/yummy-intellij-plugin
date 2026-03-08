# Yummy IntelliJ Plugin

IntelliJ IDEA integration for the [Yummy (ym)](https://github.com/ympkg/yummy) Java build tool.

**Built with ym itself** — no Gradle, no Maven. Dogfooding at its finest.

## Features

- **Auto-detect**: Opens projects with `package.toml` and runs `ymc idea` to generate IDEA project files
- **Auto-refresh**: FileWatcher monitors `package.toml` changes, offers to re-sync dependencies
- **Tool Window**: Quick buttons for Build / Dev / Test / Refresh
- **Actions Menu**: Tools → Yummy → Build / Dev / Test / Refresh
- **Run Configurations**: Create ymc run configurations (build, dev, test, build --release)

## Requirements

- IntelliJ IDEA 2024.1+
- `ym` and `ymc` binaries on PATH

## Development

```bash
# Download IntelliJ IDEA SDK (first time only)
ym setup

# Build the plugin
ymc build

# Package as installable ZIP
ym package
```

## Installation

1. Download the latest release ZIP from [Releases](https://github.com/ympkg/yummy-intellij-plugin/releases)
2. IDEA → Settings → Plugins → ⚙ → Install Plugin from Disk → select ZIP

## Architecture

```
com.ympkg.idea
├── YummyProjectConfigurator  — Detects package.toml on project open
├── YummyService              — Central service for running ym/ymc commands
├── YummyFileWatcher          — Monitors package.toml changes
├── YummyNotificationProvider — Editor notification bar for package.toml
├── YummyToolWindowFactory    — Tool window with command buttons
├── actions/
│   ├── BuildAction           — Tools → Yummy → Build
│   ├── DevAction             — Tools → Yummy → Dev
│   ├── TestAction            — Tools → Yummy → Test
│   └── RefreshAction         — Tools → Yummy → Refresh
└── run/
    ├── YummyConfigurationType     — Run configuration type
    ├── YummyRunConfiguration      — Run configuration (command + args)
    └── YummyRunConfigurationProducer — Auto-create run configs
```

## How it works

The plugin uses `compiler.libs = ["sdk/lib"]` in `package.toml` to reference IntelliJ Platform SDK JARs. The `ym setup` script downloads the IDEA Community Edition and extracts the SDK to `sdk/lib/`.
