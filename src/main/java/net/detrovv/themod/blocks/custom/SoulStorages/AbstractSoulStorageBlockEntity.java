package net.detrovv.themod.blocks.custom.SoulStorages;

import net.detrovv.themod.ModAttachments.ModAttachments;
import net.detrovv.themod.gui.SoulStorageMenu;
import net.detrovv.themod.souls.Soul;
import net.detrovv.themod.souls.SoulOrigins;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSoulStorageBlockEntity extends BlockEntity implements MenuProvider
{
    protected final int capacity;
    protected List<Soul> storedSouls = new ArrayList<Soul>();
    protected ItemStack remoteSoulStorage = ItemStack.EMPTY;

    public AbstractSoulStorageBlockEntity(int pCapacity, BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        capacity = pCapacity;
    }

    public boolean HasFreeSpaceForSoul()
    {
        if (storedSouls.size() < capacity)
        {
            return true;
        }
        return false;
    }

    public boolean AddSoul(Soul soul)
    {
        if (HasFreeSpaceForSoul())
        {
            storedSouls.add(soul);
            return true;
        }
        return false;
    }

    public void RemoveSoul(Soul soul)
    {
        for (int i = 0; i < capacity; i++)
        {
            if (storedSouls.get(i) == soul)
            {
                storedSouls.remove(i);
                return;
            }
        }
    }

    public boolean ContainsSoul(Soul soul)
    {
        if (storedSouls.contains(soul))
        {
            return true;
        }
        return false;
    }

    public boolean HasSoulOfType(SoulOrigins origin, int power)
    {
        for (int i = 0; i < capacity; i++)
        {
            Soul currentSoul = storedSouls.get(i);
            if (currentSoul.GetOrigin() == origin &&
                    currentSoul.GetPower() >= power)
            {
                return true;
            }
        }
        return false;
    }

    public boolean HasSoulOfType(SoulOrigins origin)
    {
        for (int i = 0; i < capacity; i++)
        {
            Soul currentSoul = storedSouls.get(i);
            if (currentSoul.GetOrigin() == origin)
            {
                return true;
            }
        }
        return false;
    }

    public boolean HasSoulOfType(int power)
    {
        for (int i = 0; i < capacity; i++)
        {
            Soul currentSoul = storedSouls.get(i);
            if (currentSoul.GetPower() >= power)
            {
                return true;
            }
        }
        return false;
    }

    public List<Soul> getSouls()
    {
        return storedSouls;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player)
    {
        return new SoulStorageMenu(id, inventory, player, this, new ItemStackHandler(1));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("soul_storage");
    }

    public void setRemoteSoulStorage(ItemStack remoteSoulStorage)
    {
        this.remoteSoulStorage = remoteSoulStorage;
    }

    public ItemStack getRemoteSoulStorage()
    {
        return remoteSoulStorage;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider registries)
    {
        super.loadAdditional(compoundTag, registries);
        ListTag listTag = compoundTag.getList("storedSouls", 10);
        storedSouls.clear();
        for(int i = 0; i < listTag.size(); i++)
        {
            Soul soul = new Soul();
            soul.deserializeNBT(registries, listTag.get(i));
            storedSouls.add(soul);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider registries)
    {
        super.saveAdditional(compoundTag, registries);
        ListTag listTag = new ListTag();
        for (Soul soul : storedSouls)
        {
            listTag.add(soul.serializeNBT(registries));
        }
        compoundTag.put("storedSouls", listTag);
    }
}
