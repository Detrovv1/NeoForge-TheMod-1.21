package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.blocks.custom.SoulReciever;
import net.detrovv.themod.blocks.custom.SoulTube;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class SoulTubeBlockEntity extends BlockEntity implements SoulReciever
{
    public VoxelShape shape;
    private VoxelShape additionsShape = Block.box(0, 0, 0, 0, 0, 0);

    public SoulTubeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOUL_TUBE_BLOCK_ENTITY.get(), pos, blockState);
        Direction direction = blockState.getValue(SoulTube.FACING);
        shape = SoulTube.getVoxelShape(direction);
    }

    public VoxelShape getVoxelShape()
    {
        return Shapes.or(shape, additionsShape);
    }

    public void updateProperties()
    {
        if (!getLevel().isClientSide())
        {
            List<Boolean> facingTubes = getDirectionMaskForTubeAdditions();
            BlockState state = level.getBlockState(getBlockPos());
            additionsShape = Block.box(0,0,0,0,0,0);
            for (int i = 0; i < 6; i++)
            {
                state = state.setValue(SoulTube.DIRECTION_TUBES.get(i), facingTubes.get(i));
                if (facingTubes.get(i))
                {
                    additionsShape = Shapes.or(additionsShape, SoulTube.getVoxelShape(SoulTube.getDirectionsInRightOrder().get(i)));
                }
            }
            level.setBlock(getBlockPos(), state, Block.UPDATE_NEIGHBORS);
        }
    }

    private List<Boolean> getDirectionMaskForTubeAdditions()
    {
        Level level = getLevel();
        BlockPos position = getBlockPos();
        List<Boolean> directionMask = new ArrayList<>();
        List<Boolean> connectorsNearby = getTubeConnectorsNearby();
        List<Direction> directions = SoulTube.getDirectionsInRightOrder();
        BlockState thisState = level.getBlockState(position);

        for (int i = 0; i < 6; i++)
        {
            if (connectorsNearby.get(i) == true)
            {
                BlockPos connectorPosition = position.relative(directions.get(i));
                BlockState connectorState = level.getBlockState(connectorPosition);
                directionMask.add(checkConnector(thisState, connectorState, i));
                continue;
            }
            directionMask.add(false);
        }
        return directionMask;
    }

    private boolean checkConnector(BlockState thisState, BlockState connectorState, int DirectionCode)
    {
        List<Direction> directions = SoulTube.getDirectionsInRightOrder();
        Direction thisFacing = thisState.getValue(SoulTube.FACING);

        // if connector behind tube
        if (thisFacing == directions.get(DirectionCode).getOpposite())
        {
            return true;
        }

        if (connectorState.getBlock() == ModBlocks.SOUL_TUBE.get())
        {
            Direction connectorFacing = connectorState.getValue(SoulTube.FACING);

            return isTubesNotParallel(thisFacing, connectorFacing, DirectionCode);
        }

        return false;
    }

    private List<Boolean> getTubeConnectorsNearby()
    {
        Level level = getLevel();
        BlockPos thisPosition = getBlockPos();
        List<BlockPos> neighborBlocks = SoulTube.getNeighborPositions(thisPosition);
        List<Boolean> tubeConnectors = new ArrayList<Boolean>();

        for (BlockPos block : neighborBlocks)
        {
            BlockEntity blockEntity = level.getBlockEntity(block);

            tubeConnectors.add(blockEntity instanceof SoulReciever);
        }

        return tubeConnectors;
    }

    private boolean isTubesNotParallel(Direction facingOne, Direction facingTwo, int directionCode)
    {
        List<Direction> directions = SoulTube.getDirectionsInRightOrder();

        return (facingOne != directions.get(directionCode) &&
                facingOne != directions.get(directionCode).getOpposite() &&
                facingOne != facingTwo &&
                facingOne != facingTwo.getOpposite());
    }
}
