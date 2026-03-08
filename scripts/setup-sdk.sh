#!/usr/bin/env bash
set -euo pipefail

# Download IntelliJ IDEA Community Edition SDK for plugin compilation
IDEA_VERSION="2024.1.7"
SDK_DIR="sdk"

if [ -d "$SDK_DIR/lib" ] && [ "$(ls -1 $SDK_DIR/lib/*.jar 2>/dev/null | wc -l)" -gt 10 ]; then
    echo "SDK already downloaded in $SDK_DIR/"
    exit 0
fi

echo "Downloading IntelliJ IDEA CE $IDEA_VERSION SDK..."

OS=$(uname -s)
case "$OS" in
    Linux*)  PLATFORM="linux"; EXT="tar.gz" ;;
    Darwin*) PLATFORM="mac"; EXT="dmg" ;;
    MINGW*|MSYS*|CYGWIN*) PLATFORM="win"; EXT="zip" ;;
    *)
        # WSL
        if grep -qi microsoft /proc/version 2>/dev/null; then
            PLATFORM="linux"; EXT="tar.gz"
        else
            echo "Unsupported OS: $OS"; exit 1
        fi
        ;;
esac

URL="https://download.jetbrains.com/idea/ideaIC-${IDEA_VERSION}.${EXT}"
DOWNLOAD_FILE="/tmp/ideaIC-${IDEA_VERSION}.${EXT}"

if [ ! -f "$DOWNLOAD_FILE" ]; then
    echo "Downloading from $URL ..."
    curl -fSL -o "$DOWNLOAD_FILE" "$URL"
fi

mkdir -p "$SDK_DIR"

if [ "$EXT" = "tar.gz" ]; then
    echo "Extracting lib/ directory..."
    tar -xzf "$DOWNLOAD_FILE" --strip-components=1 -C "$SDK_DIR" "*/lib/"
elif [ "$EXT" = "zip" ]; then
    echo "Extracting lib/ directory..."
    TMP_EXTRACT="/tmp/idea-extract-$$"
    mkdir -p "$TMP_EXTRACT"
    unzip -q "$DOWNLOAD_FILE" -d "$TMP_EXTRACT"
    IDEA_DIR=$(ls -d "$TMP_EXTRACT"/*/  | head -1)
    cp -r "$IDEA_DIR/lib" "$SDK_DIR/"
    rm -rf "$TMP_EXTRACT"
fi

JAR_COUNT=$(ls -1 "$SDK_DIR/lib/"*.jar 2>/dev/null | wc -l)
echo "SDK ready: $JAR_COUNT JARs in $SDK_DIR/lib/"
