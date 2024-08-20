package net.detrovv.themod.gui;

import net.detrovv.themod.blocks.custom.SoulStorages.AbstractSoulStorageBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SoulStorageMenu extends AbstractContainerMenu
{
    public final Player player;
    public final AbstractSoulStorageBlockEntity blockEntity;
    public final Slot remoteSoulStorageSlot;

    public SoulStorageMenu(int id, Inventory inventory, RegistryFriendlyByteBuf extraData)
    {
        this(   id,
                inventory,
                (AbstractSoulStorageBlockEntity)inventory.player.level().getBlockEntity(extraData.readBlockPos()),
                new ItemStackHandler(1));
    }

    public SoulStorageMenu(int id, Inventory playerInventory, AbstractSoulStorageBlockEntity blockEntity, IItemHandler data)
    {
        super(ModMenus.SOUL_STORAGE_MENU.get(), id);

        this.player = playerInventory.player;
        this.blockEntity = blockEntity;
        this.remoteSoulStorageSlot = this.addSlot(new SlotItemHandler(data, 0, 131, 18));

        addInventorySlots(playerInventory);
        addHotbarSlots(playerInventory);
    }

    private void addInventorySlots(Inventory inventory)
    {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, 93 + i * 18));
            }
        }
    }

    private void addHotbarSlots(Inventory inventory)
    {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 151));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
