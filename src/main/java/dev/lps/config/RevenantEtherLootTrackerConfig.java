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
        keyName = "hasSeenCollectionLogSyncWarning",
        name = "Seen Collection Log Sync Warning",
        description = "Whether or not the player has seen the warning about syncing the internal tracker with the collection log",
        hidden = true
    )
    default boolean hasSeenCollectionLogSyncWarning()
    {
        return false;
    }

    @ConfigItem(
        keyName = "totalRevenantEtherLooted",
        name = "Total Revenant Ether Looted",
        description = "The total amount of revenant ether received as drops",
        hidden = true
    )
    default long totalRevenantEtherLooted()
    {
        return 0L;
    }
}
