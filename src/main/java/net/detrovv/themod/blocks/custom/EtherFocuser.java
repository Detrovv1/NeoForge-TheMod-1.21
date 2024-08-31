package net.detrovv.themod.blocks.custom;

import net.detrovv.themod.blockEntities.EtherFocuserBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EtherFocuser extends Block implements EntityBlock
{
    private static final VoxelShape BOTTOM_VOXEL_SHAPE = Block.box(2, 0, 2, 14, 2, 14);
    private static final VoxelShape ROD_VOXEL_SHAPE = Block.box(6, 2, 6, 10, 16, 10);

    public EtherFocuser(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (!level.isClientSide())
        {
            EtherFocuserBlockEntity blockEntity = (EtherFocuserBlockEntity)level.getBlockEntity(pos);
            player.sendSystemMessage(Component.literal(String.valueOf(blockEntity.isWorking)));
            player.sendSystemMessage(Component.literal(String.valueOf(blockEntity.power)));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(BOTTOM_VOXEL_SHAPE, ROD_VOXEL_SHAPE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EtherFocuserBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType)
    {
        return (level0, pos0, state0, blockEntity) -> ((EtherFocuserBlockEntity)blockEntity).tick();
    }
}
