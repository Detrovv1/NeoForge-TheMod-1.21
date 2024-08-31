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
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SoulTubeBlockEntity extends BlockEntity implements SoulReciever
{
    public VoxelShape shape;

    public SoulTubeBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.SOUL_TUBE_BLOCK_ENTITY.get(), pos, blockState);

        Direction direction = blockState.getValue(SoulTube.FACING);
        shape = SoulTube.getVoxelShape(direction);
    }

    public Set<AbstractSoulStorageBlockEntity> getSoulSources(Set<BlockPos> checkedPositions)
    {
        Set<AbstractSoulStorageBlockEntity> soulSources = new HashSet<>();
        BlockPos thisPosition = getBlockPos();

        soulSources.addAll(getBehindTubeSoulSources(thisPosition, checkedPositions));
        soulSources.addAll(getAroundTubesSoulSources(thisPosition, checkedPositions));

        return soulSources;
    }

    private Set<AbstractSoulStorageBlockEntity> getBehindTubeSoulSources(BlockPos position, Set<BlockPos> checkedPositions)
    {
        Set<AbstractSoulStorageBlockEntity> soulSources = new HashSet<>();
        Level level = getLevel();
        BlockState state = level.getBlockState(position);
        Direction facing = state.getValue(SoulTube.FACING);
        BlockPos behindPosition = SoulTube.getNeighborPositionInDirection(position, facing.getOpposite());
        BlockEntity behindBlockEntity = level.getBlockEntity(behindPosition);

        if (behindBlockEntity instanceof AbstractSoulStorageBlockEntity soulGiver)
        {
            soulSources.add(soulGiver);
        }

        if (behindBlockEntity instanceof SoulTubeBlockEntity behindTube)
        {
            if (!checkedPositions.contains(behindPosition))
            {
                soulSources.addAll(behindTube.getSoulSources(checkedPositions));
                checkedPositions.add(behindPosition);
            }
        }

        return soulSources;
    }

    private Set<AbstractSoulStorageBlockEntity> getAroundTubesSoulSources(BlockPos position, Set<BlockPos> checkedPositions)
    {
        Set<AbstractSoulStorageBlockEntity> soulSources = new HashSet<>();
        Level level = getLevel();
        List<Direction> directions = SoulTube.DIRECTIONS;

        for (int i = 0; i < 6; i++)
        {
            BlockPos neighborInDirection = SoulTube.getNeighborPositionInDirection(position, directions.get(i));
            BlockEntity neighborEntity = level.getBlockEntity(neighborInDirection);
            if (!checkedPositions.contains(neighborInDirection))
            {
                if (neighborEntity instanceof SoulTubeBlockEntity soulTube)
                {
                    BlockState tubeState = level.getBlockState(neighborInDirection);
                    if (SoulTube.isTubeFacingBlock(tubeState, neighborInDirection, position))
                    {
                        soulSources.addAll(soulTube.getSoulSources(checkedPositions));
                    }
                }
                checkedPositions.add(neighborInDirection);
            }
        }

        return soulSources;
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
            BlockState stateBeforeUpdate = state;

            for (int i = 0; i < 6; i++)
            {
                state = state.setValue(SoulTube.DIRECTION_TUBES.get(i), connections.get(i));
            }

            if (state != stateBeforeUpdate)
            {
                level.setBlock(getBlockPos(), state, Block.UPDATE_ALL);
            }
        }
    }

    private List<Boolean> getDirectionMaskForTubeAdditions()
    {
        Level level = getLevel();
        BlockPos thisPosition = getBlockPos();
        List<Boolean> directionMask = new ArrayList<>();
        List<Boolean> connectorsNearby = getTubeConnectorsNearby(level, thisPosition);
        List<Direction> directions = SoulTube.DIRECTIONS;
        BlockState thisState = level.getBlockState(thisPosition);

        for (int i = 0; i < 6; i++)
        {
            if (connectorsNearby.get(i) == true)
            {
                BlockPos connectorPosition = thisPosition.relative(directions.get(i));
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
        List<Boolean> tubeConnectors = new ArrayList<>();

        for (BlockPos neighborBlock : neighborBlocks)
        {
            BlockEntity neighborBlockEntity = level.getBlockEntity(neighborBlock);

            tubeConnectors.add(neighborBlockEntity instanceof SoulReciever);
        }

        return tubeConnectors;
    }

    private boolean checkConnector(BlockState thisState, BlockState connectorState, int DirectionCode)
    {
        List<Direction> directions = SoulTube.DIRECTIONS;
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
        List<Direction> directions = SoulTube.DIRECTIONS;

        return (facingOne != directions.get(directionCode) &&
                facingOne != directions.get(directionCode).getOpposite() &&
                facingOne != facingTwo &&
                facingOne != facingTwo.getOpposite());
    }
}
