package net.detrovv.themod.blocks.custom;

import net.detrovv.themod.blockEntities.EtherFocuserBlockEntity;
import net.detrovv.themod.blockEntities.SoulTubeBlockEntity;
import net.detrovv.themod.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EtherReciever extends Block
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    private static final VoxelShape BOTTOM_VOXEL_SHAPE = Block.box(2, 0, 2, 14, 1, 14);

    public EtherReciever(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction direction = context.getHorizontalDirection();
        BlockState state = ModBlocks.ETHER_RECIEVER.get().defaultBlockState();

        state = state.setValue(EtherReciever.FACING, direction);

        return state;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(BOTTOM_VOXEL_SHAPE, getVoxelShape(state));
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        Direction direction = state.getValue(EtherReciever.FACING);
        if (    direction == Direction.SOUTH ||
                direction == Direction.NORTH   )
        {
            return Block.box(2, 1, 7, 14, 16, 9);
        }
        return Block.box(7, 1, 2, 9, 16, 14);
    }
}
