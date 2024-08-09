package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.custom.UpperGargoyleMoldBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UpperGargoyleMoldBlockEntity extends BlockEntity
{
    private int mortar_level = 0;

    public UpperGargoyleMoldBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UPPER_GARGOYLE_MOLD_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick()
    {
        if (mortar_level == 2)
        {
            UpperGargoyleMoldBlock block = (UpperGargoyleMoldBlock)this.level.getBlockState(this.worldPosition).getBlock();
            block.TryToDryConcrete(this.level, this.worldPosition);
        }
    }

    public void AddMortar()
    {
        if(this.mortar_level < 2)
        {
            this.mortar_level++;
            this.setChanged();
        }
    }
//
    public int GetMortarLevel()
    {
        return mortar_level;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider p_327783_) {
        super.saveAdditional(nbt, p_327783_);
        nbt.putInt("mortar_level", mortar_level);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider p_333170_) {
        super.loadAdditional(nbt, p_333170_);
        mortar_level = nbt.getInt("mortar_level");
    }
}