package net.detrovv.themod.items;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.items.custom.BucketOfSoulConcreteMortarItem;
import net.detrovv.themod.items.custom.ExecutionersAxe;
import net.detrovv.themod.items.custom.ModToolTiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TheMod.MOD_ID);

    public static final DeferredItem<Item> BUCKET_OF_SOUL_CONCRETE_MORTAR =
            ITEMS.register("bucket_of_soul_concrete_mortar", () -> new BucketOfSoulConcreteMortarItem(new Item.Properties()
                    .stacksTo(1)));

    public static final DeferredItem<Item> EXECUTIONERS_AXE =
            ITEMS.register("executioners_axe", () -> new ExecutionersAxe(ModToolTiers.EXECUTIONERS_AXE, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.EXECUTIONERS_AXE, 9, -3.5f))
                    .stacksTo(1)));

}
