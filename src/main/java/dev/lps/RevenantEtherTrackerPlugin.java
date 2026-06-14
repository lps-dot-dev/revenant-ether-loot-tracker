package dev.lps;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
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
public class RevenantEtherTrackerPlugin extends Plugin
{

    @Inject
    private Client client;

    @Inject
    private ConfigManager configManager;

    @Inject
    private RevenantEtherTrackerConfig config;

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
            updateConfig();
        }
    }

    @Subscribe
    public void onScriptPostFire(ScriptPostFired event)
    {
        if (event.getScriptId() == ScriptID.COLLECTION_DRAW_LIST)
        {
            try
            {
                syncWithCollectionLog();
            }
            catch (IllegalStateException __)
            {
                // Do nothing.
            }
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
    RevenantEtherTrackerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(RevenantEtherTrackerConfig.class);
    }

    /**
     * Attempts to sync our custom revenant ether tracker with the number of revenant ether counted in the collection
     * log.
     *
     * @implNote This function is reliant on the collection log interface being present, one way of ensuring this is to
     *           listen for the `ScriptID.COLLECTION_DRAW_LIST` script to finish.
     * @throws IllegalStateException When this method is invoked when the collection log interface is either not
     *                               present or ready.
     */
    private void syncWithCollectionLog() throws IllegalStateException
    {
        /**
         * Safety check: If our custom tracker is already past the 16-bit limit,
         * we never need to trust or look at the collection log window ever again.
         */
        if (totalRevenantEther >= 65535)
        {
            return;
        }

        Widget collectionLogWindow = client.getWidget(InterfaceID.COLLECTION);
        if (collectionLogWindow == null)
        {
            throw new IllegalStateException("This method must only be called after the collection log interface is ready!");
        }

        for (Widget item : collectionLogWindow.getDynamicChildren())
        {
            if (item.getItemId() != ItemID.WILD_CAVE_SHARD)
            {
                continue;
            }

            int collectionLogQuantity = item.getItemQuantity();
            if (collectionLogQuantity > totalRevenantEther)
            {
                totalRevenantEther = collectionLogQuantity;
                updateConfig();
            }
            break;
        }
    }

    private void updateConfig()
    {
        configManager.setConfiguration("revenantEtherTracker", "totalRevenantEther", totalRevenantEther);
    }
}
