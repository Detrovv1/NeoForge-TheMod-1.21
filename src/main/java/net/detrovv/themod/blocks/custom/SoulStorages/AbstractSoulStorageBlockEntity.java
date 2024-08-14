package net.detrovv.themod.blocks.custom.SoulStorages;

import net.detrovv.themod.ModAttachments.ModAttachments;
import net.detrovv.themod.souls.Soul;
import net.detrovv.themod.souls.SoulOrigins;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractSoulStorageBlockEntity extends BlockEntity
{
    protected final int capacity;
    protected final int maxUses;
    protected int usesCount = 0;

    public AbstractSoulStorageBlockEntity(int pMaxUses, int pCapacity, BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        maxUses = pMaxUses;
        capacity = pCapacity;
    }

    public boolean HasFreeSpaceForSoul()
    {
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
        if (storedSouls.size() < capacity)
        {
            return true;
        }
        return false;
    }

    public boolean AddSoul(Soul soul)
    {
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
        if (HasFreeSpaceForSoul())
        {
            storedSouls.add(soul);
            this.setData(ModAttachments.STORED_SOULS, storedSouls);
            return true;
        }
        return false;
    }

    public void RemoveSoul(Soul soul)
    {
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
        for (int i = 0; i < capacity; i++)
        {
            if (storedSouls.get(i) == soul)
            {
                storedSouls.remove(i);
                usesCount++;
                this.setChanged();
                return;
            }
        }
    }

    public boolean ContainsSoul(Soul soul)
    {
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
        if (storedSouls.contains(soul))
        {
            return true;
        }
        return false;
    }

    public boolean HasSoulOfType(SoulOrigins origin, int power)
    {
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
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
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
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
        List<Soul> storedSouls = this.getData(ModAttachments.STORED_SOULS);
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

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider p_327783_) {
        super.saveAdditional(nbt, p_327783_);
        nbt.putInt("uses_count", usesCount);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider p_333170_) {
        super.loadAdditional(nbt, p_333170_);
        usesCount = nbt.getInt("uses_count");
    }
}
