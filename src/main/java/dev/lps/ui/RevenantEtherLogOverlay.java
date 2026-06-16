package dev.lps.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.inject.Inject;

import dev.lps.RevenantEtherLootTrackerPlugin;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.util.QuantityFormatter;

public class RevenantEtherLogOverlay extends WidgetItemOverlay
{
    private final RevenantEtherLootTrackerPlugin plugin;

    @Inject
    public RevenantEtherLogOverlay(RevenantEtherLootTrackerPlugin plugin)
    {
        this.plugin = plugin;

        // This ensures your custom drawings are layered cleanly on top of interface graphics
        drawAfterInterface(InterfaceID.COLLECTION);
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
    {
        /**
         * If the custom tracker hasn't surpassed the maximum 16-bit capacity yet,
         * let the game's native UI display the itemQuantitys naturally and exit early.
         */
        if (plugin.getConfig().hideRevenantEtherCollectionLogOverlay() || plugin.getTotalRevenantEtherLooted() <= RevenantEtherLootTrackerPlugin.UNSIGNED_SHORT_MAX_VALUE)
        {
            return;
        }

        if (itemId != ItemID.WILD_CAVE_SHARD)
        {
            return;
        }

        Rectangle canvasBounds = widgetItem.getCanvasBounds();

        // Give the item slot a black transparent background
        Color backgroundColor = new Color(0, 0, 0, 50);
        graphics.setColor(backgroundColor);
        graphics.fill3DRect((int) canvasBounds.getX(), (int) canvasBounds.getY(), (int) canvasBounds.getWidth(), (int) canvasBounds.getHeight(), false);

        long itemQuantity = plugin.getTotalRevenantEtherLooted();
        String textToDraw = QuantityFormatter.quantityToStackSize(itemQuantity);

        // Position text near the bottom left
        int textX = (int) canvasBounds.getX() + 1;
        int textY = (int) canvasBounds.getMaxY() - 1;

        // Render shadow first to ensure the numbers are easily readable over dark icons
        graphics.setFont(FontManager.getRunescapeSmallFont());
        graphics.setColor(Color.BLACK);
        graphics.drawString(textToDraw, textX + 1, textY + 1);

        // Render actual foreground numbers
        Color textColor = getStackColor(itemQuantity);
        graphics.setColor(textColor);
        graphics.drawString(textToDraw, textX, textY);
    }

    /**
     * Returns the native in-game color based on the given item quantity.
     *
     * @param itemQuantity
     * @return The native in-game color of text that corresponds to the given item quantity
     */
    private Color getStackColor(long itemQuantity)
    {
        if (itemQuantity >= 10_000_000L)
        {
            return ColorScheme.PROGRESS_COMPLETE_COLOR;
        }
        if (itemQuantity >= 100_000L)
        {
            return Color.WHITE;
        }
        return Color.YELLOW;
    }
}
