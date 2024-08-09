package net.detrovv.themod.blocks.custom;

import net.detrovv.themod.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public class LowerGargoyleMoldBlock extends Block
{
    public LowerGargoyleMoldBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState pBlockState, LevelReader pLevelReader, BlockPos pBlockPos)
    {
        BlockState lowerBlockState = pLevelReader.getBlockState(pBlockPos.below());
        if (lowerBlockState.is(ModBlocks.LOWER_GARGOYLE_MOLD_BLOCK.get()) ||
                lowerBlockState.is(ModBlocks.UPPER_GARGOYLE_MOLD_BLOCK.get())   )
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    protected void onRemove(BlockState pBlockState0, Level pLevel, BlockPos pos, BlockState pBlockState1, boolean pboolean)
    {
        if (pLevel.isClientSide())
        {
            return;
        }
        BlockPos upperBlock = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
        if (pLevel.getBlockState(upperBlock).getBlock() == ModBlocks.UPPER_GARGOYLE_MOLD_BLOCK.get())
        {
            pLevel.destroyBlock(upperBlock, false);
        }
    }
}
