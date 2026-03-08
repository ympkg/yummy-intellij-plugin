#!/usr/bin/env bash
set -euo pipefail

# Package the compiled plugin into a ZIP for installation
PLUGIN_NAME="yummy-intellij-plugin"
VERSION="0.1.0"
OUT_DIR="out"
DIST_DIR="dist"

# Ensure compiled
if [ ! -d "$OUT_DIR/classes" ]; then
    echo "No compiled classes found. Run 'ymc build' first."
    exit 1
fi

echo "Packaging plugin..."

mkdir -p "$DIST_DIR/$PLUGIN_NAME/lib"

# Create plugin JAR
cd "$OUT_DIR/classes"
jar cf "../../$DIST_DIR/$PLUGIN_NAME/lib/$PLUGIN_NAME-$VERSION.jar" \
    -C . . \
    -C ../../src/main/resources .
cd ../..

# Create distributable ZIP
cd "$DIST_DIR"
zip -r "../$PLUGIN_NAME-$VERSION.zip" "$PLUGIN_NAME/"
cd ..

echo "Plugin packaged: $PLUGIN_NAME-$VERSION.zip"
echo "Install: IDEA → Settings → Plugins → ⚙ → Install from Disk"
