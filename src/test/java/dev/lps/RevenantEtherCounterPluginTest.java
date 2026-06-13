package dev.lps;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RevenantEtherCounterPluginTest
{

    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(RevenantEtherCounterPlugin.class);
        RuneLite.main(args);
    }
}
