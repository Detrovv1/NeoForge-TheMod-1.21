package net.detrovv.themod.blockEntities;

import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.blocks.custom.SoulStorages.AbstractSoulStorageBlockEntity;
import net.detrovv.themod.blocks.custom.SoulStorages.BaseSoulStorageBlock;
import net.detrovv.themod.souls.Soul;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SoulAltarBlockEntity extends BlockEntity
{
    private final int range = 1;

    public SoulAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOUL_ALTAR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void StoreSoulInStorageNearby(Soul soul)
    {
        Level level = getLevel();

        List<BlockPos> soulStoragesNearby = GetSoulStoragesNearby();

        for (BlockPos blockPos : soulStoragesNearby)
        {
            AbstractSoulStorageBlockEntity storageBlockEntity = (AbstractSoulStorageBlockEntity)level.getBlockEntity(blockPos);
            if (storageBlockEntity.HasFreeSpaceForSoul())
            {
                storageBlockEntity.AddSoul(soul);
            }
        }
    }

    public boolean HasEmptySoulStorageNearby()
    {
        Level level = getLevel();

        List<BlockPos> soulStoragesNearby = GetSoulStoragesNearby();

        for (BlockPos blockPos : soulStoragesNearby)
        {
            AbstractSoulStorageBlockEntity storageBlockEntity = (AbstractSoulStorageBlockEntity)level.getBlockEntity(blockPos);
            if (storageBlockEntity.HasFreeSpaceForSoul())
            {
                return true;
            }
        }
        return false;
    }

    public List<BlockPos> GetSoulStoragesNearby()
    {
        List<BlockPos> blockPositions = GetBlocksNearby();

        List<BlockPos> storages = new ArrayList<BlockPos>();

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

    public List<BlockPos> GetBlocksNearby()
    {
        BlockPos thisPosition = this.getBlockPos();
        int thisX = thisPosition.getX();
        int thisY = thisPosition.getY();
        int thisZ = thisPosition.getZ();

        Level level = getLevel();

        List<BlockPos> blocks = new ArrayList<BlockPos>();

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
