package net.detrovv.themod.blocks.custom.SoulStorages;

import net.detrovv.themod.ModAttachments.ModAttachments;
import net.detrovv.themod.blockEntities.BaseSoulStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
        BaseSoulStorageBlockEntity blockEntity = (BaseSoulStorageBlockEntity)level.getBlockEntity(pos);
        player.sendSystemMessage(Component.literal(blockEntity.capacity + ""));
        player.sendSystemMessage(Component.literal(blockEntity.maxUses + ""));
        player.sendSystemMessage(Component.literal(blockEntity.usesCount + ""));
        player.sendSystemMessage(Component.literal(blockEntity.HasFreeSpaceForSoul() + ""));
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new BaseSoulStorageBlockEntity(pos, state);
    }


}
