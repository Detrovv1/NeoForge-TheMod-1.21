package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.souls.Soul;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SoulAltarBlockEntity extends BlockEntity
{
    public static int soulCatchRange = 5;
    public static int soulStoreRange = 1;

    public SoulAltarBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(ModBlockEntities.SOUL_ALTAR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void storeSoulInStorageNearby(Soul soul)
    {
        Level level = getLevel();
        if (!level.isClientSide())
        {
            List<BlockPos> soulStoragesNearby = getSoulStoragesNearby();

            for (BlockPos blockPos : soulStoragesNearby)
            {
                AbstractSoulStorageBlockEntity storageBlockEntity = (AbstractSoulStorageBlockEntity)level.getBlockEntity(blockPos);
                if (storageBlockEntity.hasFreeSpaceForSoul())
                {
                    storageBlockEntity.addSoul(soul);
                    return;
                }
            }
        }
    }

    public boolean hasEmptySoulStorageNearby()
    {
        Level level = getLevel();

        List<BlockPos> soulStoragesNearby = getSoulStoragesNearby();

        for (BlockPos blockPos : soulStoragesNearby)
        {
            AbstractSoulStorageBlockEntity storageBlockEntity = (AbstractSoulStorageBlockEntity)level.getBlockEntity(blockPos);
            if (storageBlockEntity.hasFreeSpaceForSoul())
            {
                return true;
            }
        }
        return false;
    }

    public List<BlockPos> getSoulStoragesNearby()
    {
        List<BlockPos> blockPositions = getBlocksInRangeAroundBlock(getBlockPos(), soulStoreRange);

        List<BlockPos> storages = new ArrayList<>();

        for (BlockPos blockPos : blockPositions)
        {
            Block block = level.getBlockState(blockPos).getBlock();
            if (block == ModBlocks.BASE_SOUL_STORAGE_BLOCK.get())
            {
                storages.add(blockPos);
            }
        }
        return storages;
    }

    public static List<BlockPos> getBlocksInRangeAroundBlock(BlockPos position, int range)
    {
        int thisX = position.getX();
        int thisY = position.getY();
        int thisZ = position.getZ();

        List<BlockPos> blocks = new ArrayList<>();

        for (int x = thisX - range; x <= thisX + range; x++)
        {
            for (int y = thisY - range; y <= thisY + range; y++)
            {
                for (int z = thisZ - range; z <= thisZ + range; z++)
                {
                    BlockPos currentPosition = new BlockPos(x, y, z);
                    blocks.add(currentPosition);
                }
            }
        }
        return blocks;
    }
}
