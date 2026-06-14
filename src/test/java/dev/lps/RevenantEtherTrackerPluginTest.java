package dev.lps;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RevenantEtherTrackerPluginTest
{

    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(RevenantEtherTrackerPlugin.class);
        RuneLite.main(args);
    }
}
