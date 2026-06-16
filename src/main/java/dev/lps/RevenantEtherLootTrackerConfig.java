package dev.lps;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("revenantEtherLootTracker")
public interface RevenantEtherLootTrackerConfig extends Config
{
    @ConfigItem(
        keyName = "hideRevenantEtherCollectionLogOverlay",
        name = "Hide Collection Log Overlay",
        description = "This will dictate whether this custom revenant ether counter will overlay over the revenant ether item slot shown in the collection log",
        position = 1
    )
    default boolean hideRevenantEtherCollectionLogOverlay()
    {
        return false;
    }

    @ConfigItem(
        keyName = "totalRevenantEtherLooted",
        name = "Total Revenant Ether Looted",
        description = "The total amount of revenant ether received as drops",
        hidden = true // Hides it from the plugin settings panel so users don't accidentally edit it
    )
    default long totalRevenantEtherLooted()
    {
        return 0L; // Default value if no data exists yet
    }
}
