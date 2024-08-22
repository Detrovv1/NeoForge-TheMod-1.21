package net.detrovv.themod.blocks.custom.SoulStorages;

import net.detrovv.themod.blockEntities.AbstractSoulStorageBlockEntity;
import net.detrovv.themod.blockEntities.BaseSoulStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BaseSoulStorageBlock extends Block implements EntityBlock
{
    public BaseSoulStorageBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState pOldState, Level pLevel, BlockPos pos, BlockState pNewState, boolean pboolean)
    {
        if (pLevel.isClientSide())
        {
            return;
        }
        pLevel.removeBlockEntity(pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockEntity entity = level.getBlockEntity(pos);
            AbstractSoulStorageBlockEntity storage = (AbstractSoulStorageBlockEntity)entity;
            serverPlayer.openMenu(storage, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new BaseSoulStorageBlockEntity(pos, state);
    }
}
