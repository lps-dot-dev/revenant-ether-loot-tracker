package dev.lps;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loottracker.LootReceived;

@Slf4j
@PluginDescriptor(
    name = "Revenant Ether Counter"
)
public class RevenantEtherCounterPlugin extends Plugin
{

    @Inject
    private ConfigManager configManager;

    @Inject
    private RevenantEtherCounterConfig config;

    @Getter
    private long totalRevenantEther = 0L;

    @Subscribe
    public void onLootReceived(LootReceived event)
    {
        long revenantEtherLooted = 0L;

        for (ItemStack itemStack : event.getItems())
        {
            if (itemStack.getId() == ItemID.WILD_CAVE_SHARD)
            {
                revenantEtherLooted += itemStack.getQuantity();
            }
        }

        if (revenantEtherLooted > 0L)
        {
            totalRevenantEther += revenantEtherLooted;
            configManager.setConfiguration("revenantEtherCounter", "totalEtherDropped", totalRevenantEther);
        }
    }

    @Override
    protected void startUp() throws Exception
    {
        totalRevenantEther = config.totalRevenantEther();
    }

    @Override
    protected void shutDown() throws Exception
    {
        // Do nothing.
    }

    @Provides
    RevenantEtherCounterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(RevenantEtherCounterConfig.class);
    }
}
