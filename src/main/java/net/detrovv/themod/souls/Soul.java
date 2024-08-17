package net.detrovv.themod.souls;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Random;

public class Soul implements INBTSerializable
{
    private SoulOrigins origin;
    private int soulPower;

    public Soul(){

    }

    public Soul(SoulData data)
    {
        Random rand = new Random();
        soulPower = rand.nextInt(data.minPower, data.maxPower);
        origin = data.origin;
    }

    public Soul(SoulOrigins pOrigin, int pSoulPower)
    {
        origin = pOrigin;
        soulPower = pSoulPower;
    }

    public int GetPower()
    {
        return soulPower;
    }

    public SoulOrigins GetOrigin()
    {
        return origin;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("soulOrigin", origin.name());
        nbt.putInt("soulPower", soulPower);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        CompoundTag compoundTag = (CompoundTag)tag;
        this.origin = SoulOrigins.valueOf(compoundTag.getString("soulOrigin"));
        this.soulPower = compoundTag.getInt("soulPower");
    }
}
