package net.detrovv.themod.ModAttachments;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class ItemStackSupplier implements Supplier<ItemStack>
{
    @Override
    public ItemStack get() {
        return ItemStack.EMPTY;
    }
}
