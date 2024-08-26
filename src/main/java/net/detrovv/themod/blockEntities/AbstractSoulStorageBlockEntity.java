package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.custom.SoulGiver;
import net.detrovv.themod.blocks.custom.SoulReciever;
import net.detrovv.themod.blocks.custom.SoulTube;
import net.detrovv.themod.gui.SoulStorageMenu;
import net.detrovv.themod.souls.Soul;
import net.detrovv.themod.souls.SoulOrigins;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractSoulStorageBlockEntity extends BlockEntity implements MenuProvider, SoulReciever, SoulGiver
{
    protected final int capacity;
    protected List<Soul> storedSouls = new ArrayList<Soul>();
    protected ItemStackHandler remoteSoulStorage = new ItemStackHandler(1);
    protected Set<SoulGiver> soulSources = new HashSet<>();
    protected int counterForTicks = 0;

    public AbstractSoulStorageBlockEntity(int capacity, BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        this.capacity = capacity;
    }

    public boolean addSoul(Soul soul)
    {
        if (!level.isClientSide() && hasFreeSpaceForSoul())
        {
            storedSouls.add(soul);
            getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            setChanged();
            return true;
        }
        return false;
    }

    public boolean hasFreeSpaceForSoul()
    {
        if (storedSouls.size() < capacity)
        {
            return true;
        }
        return false;
    }

    public void removeSoul(Soul soul)
    {
        if (!level.isClientSide() && storedSouls.contains(soul))
        {
            storedSouls.remove(soul);
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            setChanged();
        }
    }

    public boolean containsSoul(Soul soul)
    {
        if (storedSouls.contains(soul))
        {
            return true;
        }
        return false;
    }

    public boolean hasSoulOfType(SoulOrigins origin, int power)
    {
        for (Soul soul : storedSouls)
        {
            if (soul.getPower() >= power && soul.getOrigin() == origin)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasSoulOfType(SoulOrigins origin)
    {
        for (Soul soul : storedSouls)
        {
            if (soul.getOrigin() == origin)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasSoulOfType(int power)
    {
        for (Soul soul : storedSouls)
        {
            if (soul.getPower() >= power)
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
        return new SoulStorageMenu(id, inventory, this, remoteSoulStorage);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("soul_storage");
    }

    public void setRemoteSoulStorage(ItemStack remoteSoulStorage)
    {
        if (!level.isClientSide())
        {
            this.remoteSoulStorage.insertItem(0, remoteSoulStorage, true);
            getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            setChanged();
        }
    }

    public ItemStackHandler getRemoteSoulStorage()
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

        CompoundTag itemTag = compoundTag.getCompound("remoteSoulStorage");
        remoteSoulStorage.deserializeNBT(registries, itemTag);
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


        CompoundTag itemTag = remoteSoulStorage.serializeNBT(registries);
        compoundTag.put("remoteSoulStorage", itemTag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (Soul soul : storedSouls)
        {
            listTag.add(soul.serializeNBT(registries));
        }

        compoundTag.put("storedSouls", listTag);
        return compoundTag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider)
    {
        super.onDataPacket(net, pkt, lookupProvider);
        this.loadAdditional(pkt.getTag(), lookupProvider);
    }



    public void tick()
    {
        Level level = getLevel();
        if (!level.isClientSide())
        {
            counterForTicks++;
            if (counterForTicks == 20)
            {
                tryToTakeSoulFromSource();
                counterForTicks = 0;
            }
        }
    }

    private void tryToTakeSoulFromSource()
    {
        updateIncomingTubes();
        for (SoulGiver giver : soulSources)
        {
            if (giver instanceof AbstractSoulStorageBlockEntity storage)
            {
                if (hasFreeSpaceForSoul())
                {
                    List<Soul> souls = storage.getSouls();
                    if (souls.size() > 0)
                    {
                        addSoul(souls.get(0));
                        storage.removeSoul(souls.get(0));
                    }
                }
            }
        }
    }

    private void updateIncomingTubes()
    {
        Level level = getLevel();
        BlockPos thisPosition = getBlockPos();
        Set<SoulGiver> soulGivers = new HashSet<>();
        List<BlockPos> neighborBlocks = SoulTube.getNeighborPositions(thisPosition);
        for (BlockPos blockPos : neighborBlocks)
        {
            if (level.getBlockEntity(blockPos) instanceof SoulTubeBlockEntity soulTube)
            {
                if (SoulTube.isTubeFacingBlock(level.getBlockState(blockPos), blockPos, thisPosition))
                {
                    for (SoulGiver giver : soulTube.getSoulSources())
                    {
                        soulGivers.add(giver);
                    }
                }
            }
        }

        soulSources = soulGivers;
    }
}
