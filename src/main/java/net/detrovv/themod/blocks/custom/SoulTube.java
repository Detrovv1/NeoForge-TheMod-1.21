package net.detrovv.themod.blocks.custom;

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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoulTube extends Block implements EntityBlock
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    public static final List<BooleanProperty> DIRECTION_TUBES = new ArrayList<>();
    public static List<Direction> DIRECTIONS = new ArrayList<>();
    static
    {
        DIRECTION_TUBES.add(BooleanProperty.create("up_tube"));
        DIRECTION_TUBES.add(BooleanProperty.create("down_tube"));
        DIRECTION_TUBES.add(BooleanProperty.create("south_tube"));
        DIRECTION_TUBES.add(BooleanProperty.create("north_tube"));
        DIRECTION_TUBES.add(BooleanProperty.create("west_tube"));
        DIRECTION_TUBES.add(BooleanProperty.create("east_tube"));

        DIRECTIONS.add(Direction.UP);
        DIRECTIONS.add(Direction.DOWN);
        DIRECTIONS.add(Direction.SOUTH);
        DIRECTIONS.add(Direction.NORTH);
        DIRECTIONS.add(Direction.WEST);
        DIRECTIONS.add(Direction.EAST);
    }

    public SoulTube(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(DIRECTION_TUBES.get(0), false)
                .setValue(DIRECTION_TUBES.get(1), false)
                .setValue(DIRECTION_TUBES.get(2), false)
                .setValue(DIRECTION_TUBES.get(3), false)
                .setValue(DIRECTION_TUBES.get(4), false)
                .setValue(DIRECTION_TUBES.get(5), false)
        );
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
    {
        SoulTubeBlockEntity entity = (SoulTubeBlockEntity)level.getBlockEntity(pos);
        entity.updateProperties();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        SoulTubeBlockEntity blockEntity = (SoulTubeBlockEntity)level.getBlockEntity(pos);
        if (blockEntity == null)
        {
            return Block.box(6,6,6,12,12,12);
        }
        VoxelShape shape = blockEntity.shape;
        VoxelShape additiosShape = getVoxelShapeForTubeAdditions(blockEntity.getLevel(), pos);
        return Shapes.or(shape, additiosShape);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = ModBlocks.SOUL_TUBE.get().defaultBlockState();
        Direction direction = context.getClickedFace().getOpposite();

        state = state.setValue(FACING, direction);
        VoxelShape shape = getVoxelShape(direction);
        SoulTubeBlockEntity blockEntity = (SoulTubeBlockEntity)context.getLevel().getBlockEntity(context.getClickedPos());
        if (blockEntity != null)
        {
            blockEntity.shape = shape;
        }

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING,
                    DIRECTION_TUBES.get(0),
                    DIRECTION_TUBES.get(1),
                    DIRECTION_TUBES.get(2),
                    DIRECTION_TUBES.get(3),
                    DIRECTION_TUBES.get(4),
                    DIRECTION_TUBES.get(5) );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new SoulTubeBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType)
    {
        return pLevel.isClientSide() ? null : (level0, pos0, state0, blockEntity) -> ((SoulTubeBlockEntity)blockEntity).tick();
    }

    public static BlockPos getNeighborPositionInDirection(BlockPos position, Direction direction)
    {
        switch (direction)
        {
            case Direction.UP -> {return position.above();}
            case Direction.DOWN -> {return position.below();}
            case Direction.SOUTH -> {return position.south();}
            case Direction.NORTH -> {return position.north();}
            case Direction.WEST -> {return position.west();}
            case Direction.EAST -> {return position.east();}
        }
        return BlockPos.ZERO;
    }

    public static BlockPos getNeighborPositionInDirection(BlockPos position, Direction direction, int number)
    {
        for (int i = 0; i < number; i++)
        {
            position = getNeighborPositionInDirection(position, direction);
        }
        return position;
    }

    public static boolean isTubeFacingBlock(BlockState tubeState, BlockPos tubePos, BlockPos blockPos)
    {
        Direction tubeFacing = tubeState.getValue(SoulTube.FACING);
        BlockPos targetPosition = getNeighborPositionInDirection(tubePos, tubeFacing);

        return targetPosition.equals(blockPos);
    }

    public static VoxelShape getVoxelShapeForTubeAdditions(Level level, BlockPos blockPos)
    {
        VoxelShape shape = Block.box(0,0,0,0,0,0);
        BlockState state = level.getBlockState(blockPos);

        for (int i = 0; i < 6; i++)
        {
            if (state.getValue(DIRECTION_TUBES.get(i)))
            {
                shape = Shapes.or(shape, getVoxelShapeForTubeAddition(DIRECTIONS.get(i)));
            }
        }
        return shape;
    }

    public static VoxelShape getVoxelShape(Direction direction)
    {
        switch (direction)
        {
            case Direction.UP -> {return Block.box(5,5,5, 11,16,11);}
            case Direction.DOWN -> {return Block.box(5,0,5, 11,11,11);}
            case Direction.SOUTH -> {return Block.box(5,5,5, 11,11,16);}
            case Direction.NORTH -> {return Block.box(5,5,0, 11,11,11);}
            case Direction.WEST -> {return Block.box(0,5,5, 11,11,11);}
            case Direction.EAST -> {return Block.box(5,5,5, 16,11,11);}
        }
        return Block.box(6,6,6,12,12,12);
    }

    public static VoxelShape getVoxelShapeForTubeAddition(Direction direction)
    {
        switch (direction)
        {
            case Direction.UP -> {return Block.box(5,11,5, 11,16,11);}
            case Direction.DOWN -> {return Block.box(5,0,5, 11,5,11);}
            case Direction.SOUTH -> {return Block.box(5,5,11, 11,11,16);}
            case Direction.NORTH -> {return Block.box(5,5,0, 11,11,5);}
            case Direction.WEST -> {return Block.box(0,5,5, 5,11,11);}
            case Direction.EAST -> {return Block.box(5,5,5, 16,11,11);}
        }
        return Block.box(6,6,6,12,12,12);
    }

    public static List<BlockPos> getNeighborPositions(BlockPos position)
    {
        List<BlockPos> blocks = new ArrayList<BlockPos>();

        blocks.add(position.above());
        blocks.add(position.below());
        blocks.add(position.south());
        blocks.add(position.north());
        blocks.add(position.west());
        blocks.add(position.east());

        return blocks;
    }

    public static int getDirectionCode(Direction direction)
    {
        switch (direction)
        {
            case Direction.UP: {return 0;}
            case Direction.DOWN: {return 1;}
            case Direction.SOUTH: {return 2;}
            case Direction.NORTH: {return 3;}
            case Direction.WEST: {return 4;}
            case Direction.EAST: {return 5;}
        }
        throw new IllegalArgumentException("an attempt was made to obtain the wrong direction code");
    }
}
