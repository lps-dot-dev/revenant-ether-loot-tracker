package dev.lps;

import javax.inject.Inject;

import com.google.inject.Provides;

import dev.lps.ui.RevenantEtherLogOverlay;
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
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
    name = "Revenant Ether Loot Tracker",
    description = "Tracks revenant ether, even after the collection log stops!",
    tags = {"revenants", "revs", "ether", "tracker"}
)
public class RevenantEtherLootTrackerPlugin extends Plugin
{

    @Getter
    @Inject
    private Client client;

    @Inject
    private ConfigManager configManager;

    @Inject
    private OverlayManager overlayManager;

    @Getter
    @Inject
    private RevenantEtherLootTrackerConfig config;

    @Inject
    private RevenantEtherLogOverlay revenantEtherLogOverlay;

    @Getter
    private long totalRevenantEtherLooted = 0L;

    @Subscribe
    public void onLootReceived(LootReceived event)
    {
        long revenantEtherLooted = 0L;

        for (ItemStack itemStack : event.getItems())
        {
            if (itemStack.getId() == ItemID.WILD_CAVE_SHARD)
            {
                revenantEtherLooted = itemStack.getQuantity();
                break;
            }
        }

        if (revenantEtherLooted > 0L)
        {
            totalRevenantEtherLooted += revenantEtherLooted;
            updateConfig();
        }
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired event)
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
        totalRevenantEtherLooted = config.totalRevenantEtherLooted();
        overlayManager.add(revenantEtherLogOverlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(revenantEtherLogOverlay);
    }

    @Provides
    RevenantEtherLootTrackerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(RevenantEtherLootTrackerConfig.class);
    }

    /**
     * Attempts to sync our custom revenant ether tracker with the number of
     * revenant ether counted in the collection log.
     *
     * @implNote This function is reliant on the collection log interface being
     *           present, one way of ensuring this is to
     *           listen for the `ScriptID.COLLECTION_DRAW_LIST` script to finish.
     * @throws IllegalStateException When this method is invoked when the collection
     *                               log interface is either not
     *                               present or ready.
     */
    private void syncWithCollectionLog() throws IllegalStateException
    {
        /**
         * Safety check: If our custom tracker is already past the 16-bit limit,
         * we never need to trust or look at the collection log window ever again.
         */
        if (totalRevenantEtherLooted >= 65535)
        {
            return;
        }

        Widget collectionLogItems = client.getWidget(InterfaceID.Collection.ITEMS_CONTENTS);
        if (collectionLogItems == null)
        {
            throw new IllegalStateException(
                "This method must only be called after the collection log interface is ready!");
        }

        for (Widget item : collectionLogItems.getChildren())
        {
            if (item.getItemId() != ItemID.WILD_CAVE_SHARD)
            {
                continue;
            }

            int collectionLogQuantity = item.getItemQuantity();
            if (collectionLogQuantity > totalRevenantEtherLooted)
            {
                totalRevenantEtherLooted = collectionLogQuantity;
                updateConfig();
            }
            break;
        }
    }

    private void updateConfig()
    {
        configManager.setConfiguration("revenantEtherLootTracker", "totalRevenantEtherLooted", totalRevenantEtherLooted);
        log.debug(Long.toString(totalRevenantEtherLooted));
    }
}
