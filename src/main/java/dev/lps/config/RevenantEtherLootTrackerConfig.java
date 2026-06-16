package dev.lps.config;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("revenantEtherLootTracker")
public interface RevenantEtherLootTrackerConfig extends Config
{
    @ConfigItem(
        keyName = "displayRevenantEtherCollectionLogOverlay",
        name = "Display Collection Log Overlay",
        description = "Present overlay over the revenant ether item slot shown in the collection log",
        position = 1
    )
    default boolean displayRevenantEtherCollectionLogOverlay()
    {
        return true;
    }

    @ConfigItem(
        keyName = "totalRevenantEtherLooted",
        name = "Total Revenant Ether Looted",
        description = "The total amount of revenant ether received as drops",
        hidden = true // Hides it from the plugin settings panel so users don't accidentally edit it
    )
    default long totalRevenantEtherLooted()
    {
        return 0L;
    }
}
