package dev.lps;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("revenantEtherTracker")
public interface RevenantEtherTrackerConfig extends Config
{
    @ConfigItem(
        keyName = "totalRevenantEther",
        name = "Total Revenant Ether",
        description = "The total amount of revenant ether received as drops",
        hidden = true // Hides it from the plugin settings panel so users don't accidentally edit it
    )
    default long totalRevenantEther()
    {
        return 0L; // Default value if no data exists yet
    }
}
