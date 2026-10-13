# The Edge - Sandevistan

A Minecraft 1.21.1 (NeoForge) mod inspired by the Sandevistan from Cyberpunk 2077.
Activate it to slow time for every entity around you while you stay fast.

## Features

- `Sandevistan` item (right-click to activate), with a cooldown.
- Optional keybind (default `R`) to activate while the item is anywhere in your inventory.
- Slows / freezes other living entities, items and projectiles while active.
- Gives the user a short Speed boost while active.
- Amber screen tint while active.
- Server-side config for duration, cooldown, slowness strength, radius and motion freezing.

## Building (one-click)

Requirements: a JDK (21 recommended) and an internet connection on the first build.

Double-click `build.bat`. It downloads Gradle 8.9 if needed, then compiles the mod.
Gradle auto-provisions JDK 21 through the Foojay toolchain plugin when necessary, so you
do not need to install JDK 21 by hand as long as some Java is available.

The finished jar is copied to `dist/theedge-1.0.0.jar`. Drop that file into your
Minecraft `mods` folder to play.

## Building (IDE)

Import the folder as a Gradle project (IntelliJ IDEA is recommended), then run the
`client` Gradle run configuration. IDEA downloads the Gradle distribution and JDK for you.

## Running from the IDE

Import the folder as a Gradle project (IntelliJ IDEA is recommended), then run the
`client` Gradle run configuration.

## In-game

Get the item from the `The Edge` creative tab, hold it and right-click (or press `R`).
The key can be rebound in Options -> Controls -> Key Binds.
