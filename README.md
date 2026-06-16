# Revenant Ether Loot Tracker

An RuneLite plugin designed to track and display how much **Revenant Ether** the player has received as loot well after the official limit of `65535` is exceeded.

#### But Why Tho?

This plugin was made for people like me who just like seeing number go up. Also I participate in bingo events where revenant ether is a form of contribution, so this plugin will make it easier for me and others with max revenant ether on their collection logs to contribute.

## Core Features
* **Seamless Behavior:** This plugin will lay dorment and let the native collection log counter and UI do all the work, only when the max limit for ether is exceeded will this overlay kick in. The displayed quantity will follow stardard OSRS formatting and text color changes. Example: `100_000 -> 100k (White Text)` or `10_000_000 -> 10M (Green Text)`
* **Isolated Revenant Ether Tracking:** Focuses solely on *revenant ether* collected from drops. Will not track any revenant ether gained from chiseling down *ancient valuables* or *bracelets of ethereum*.
* **Collection Log Sync:** When the revenant section of the collection log is displayed, the internal tracker will update to match the revenant ether count shown.
* **Persistent Tracking:** Leverages RuneLite's plugin configuration system to persist and isolate ether trackers for individual characters and profiles.

## Configuration

You can reset the internal revenant ether tracker by pressing the `reset` button for the plugin.

Displaying the custom overlay for the internal tracker can also be disabled via the settings.
