package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.blocks.custom.SoulGiver;
import net.detrovv.themod.blocks.custom.SoulReciever;
import net.detrovv.themod.blocks.custom.SoulTube;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoulTubeBlockEntity extends BlockEntity implements SoulReciever
{
    public VoxelShape shape;
    private Set<SoulGiver> soulSources = new HashSet<>();

    public SoulTubeBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.SOUL_TUBE_BLOCK_ENTITY.get(), pos, blockState);

        Direction direction = blockState.getValue(SoulTube.FACING);
        shape = SoulTube.getVoxelShape(direction);
    }

    public Set<SoulGiver> getSoulSources()
    {
        Set<SoulGiver> soulSources = new HashSet<>();
        Level level = getLevel();
        BlockPos thisPosition = getBlockPos();
        BlockState thisState = level.getBlockState(thisPosition);

        for (SoulGiver giver : getBehindTubeSoulSources(thisPosition))
        {
            soulSources.add(giver);
        }
        for (SoulGiver giver : getAroundTubesSoulSources(thisPosition))
        {
            soulSources.add(giver);
        }

        return soulSources;
    }

    private Set<SoulGiver> getBehindTubeSoulSources(BlockPos position)
    {
        Set<SoulGiver> sources = new HashSet<>();
        Level level = getLevel();
        BlockState state = level.getBlockState(position);
        Direction facing = state.getValue(SoulTube.FACING);
        BlockPos behindPosition = SoulTube.getNeighborPositionInDirection(position, facing.getOpposite());
        BlockEntity behindBlockEntity = level.getBlockEntity(behindPosition);

        if (behindBlockEntity instanceof SoulGiver soulGiver)
        {
            sources.add(soulGiver);
        }

        if (behindBlockEntity instanceof SoulTubeBlockEntity behindTube)
        {
            for (SoulGiver source : behindTube.getSoulSources())
            {
                sources.add(source);
            }
        }

        return sources;
    }

    private Set<SoulGiver> getAroundTubesSoulSources(BlockPos position)
    {
        Set<SoulGiver> sources = new HashSet<>();
        Level level = getLevel();
        List<Direction> directions = SoulTube.getDirectionsInRightOrder();

        for (int i = 0; i < 6; i++)
        {
            BlockPos neighborInDirection = SoulTube.getNeighborPositionInDirection(position, directions.get(i));
            BlockEntity neighborEntity = level.getBlockEntity(neighborInDirection);

            if (neighborEntity instanceof SoulTubeBlockEntity soulTube)
            {
                BlockState tubeState = level.getBlockState(neighborInDirection);
                if (SoulTube.isTubeFacingBlock(tubeState, neighborInDirection, position))
                {
                    for (SoulGiver source : soulTube.getSoulSources())
                    {
                        sources.add(source);
                    }
                }
            }
        }

        return sources;
    }

    public void tick()
    {
        updateProperties();
    }

    public void updateProperties()
    {
        if (!getLevel().isClientSide())
        {
            List<Boolean> connections = getDirectionMaskForTubeAdditions();
            BlockState state = level.getBlockState(getBlockPos());
            BlockState copy = state;

            for (int i = 0; i < 6; i++)
            {
                state = state.setValue(SoulTube.DIRECTION_TUBES.get(i), connections.get(i));
            }

            if (state != copy)
            {
                level.setBlock(getBlockPos(), state, Block.UPDATE_ALL);
            }
        }
    }

    private List<Boolean> getDirectionMaskForTubeAdditions()
    {
        Level level = getLevel();
        BlockPos position = getBlockPos();
        List<Boolean> directionMask = new ArrayList<>();
        List<Boolean> connectorsNearby = getTubeConnectorsNearby(level, position);
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

    public static List<Boolean> getTubeConnectorsNearby(Level level, BlockPos thisPosition)
    {
        List<BlockPos> neighborBlocks = SoulTube.getNeighborPositions(thisPosition);
        List<Boolean> tubeConnectors = new ArrayList<Boolean>();

        for (BlockPos block : neighborBlocks)
        {
            BlockEntity blockEntity = level.getBlockEntity(block);

            tubeConnectors.add(blockEntity instanceof SoulReciever);
        }

        return tubeConnectors;
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

    private boolean isTubesNotParallel(Direction facingOne, Direction facingTwo, int directionCode)
    {
        List<Direction> directions = SoulTube.getDirectionsInRightOrder();

        return (facingOne != directions.get(directionCode) &&
                facingOne != directions.get(directionCode).getOpposite() &&
                facingOne != facingTwo &&
                facingOne != facingTwo.getOpposite());
    }
}
