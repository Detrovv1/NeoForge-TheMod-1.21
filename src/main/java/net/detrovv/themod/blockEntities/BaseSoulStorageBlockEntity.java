package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.custom.SoulStorages.AbstractSoulStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BaseSoulStorageBlockEntity extends AbstractSoulStorageBlockEntity
{
    public BaseSoulStorageBlockEntity(BlockPos pos, BlockState state) {
        super(10, ModBlockEntities.BASE_SOUL_STORAGE_BLOCK_ENTITY.get(), pos, state);
    }
}
