package net.detrovv.themod.items.custom;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {
    public static final Tier EXECUTIONERS_AXE = new SimpleTier(Tiers.STONE.getIncorrectBlocksForDrops(),
            100, 0.5f, 0, 20, null);
}