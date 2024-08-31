package net.detrovv.themod.blocks.custom;

import net.detrovv.themod.blockEntities.SoulAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SoulAltar extends Block implements EntityBlock
{
    public static final VoxelShape SHAPE = Block.box( 0, 0, 0, 16, 13, 16);

    public SoulAltar(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        SoulAltarBlockEntity blockEntity = (SoulAltarBlockEntity)level.getBlockEntity(pos);
        player.sendSystemMessage(Component.literal( blockEntity.hasEmptySoulStorageNearby() + " пустые хранилища"));
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoulAltarBlockEntity(blockPos, blockState);
    }
}
