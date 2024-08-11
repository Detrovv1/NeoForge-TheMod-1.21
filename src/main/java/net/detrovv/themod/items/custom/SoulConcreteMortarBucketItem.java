package net.detrovv.themod.items.custom;

import net.detrovv.themod.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SoulConcreteMortarBucketItem extends Item
{
    public SoulConcreteMortarBucketItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
//        if (level.isClientSide())
//        {
//            return InteractionResult.CONSUME;
//        }
        BlockPos blockPos = context.getClickedPos();
        BlockPos targetPosition = GetTargetBlock(context);
        if (IsBlockAvailable(level, targetPosition))
        {
            level.setBlock(targetPosition, ModBlocks.SOUL_CONCRETE_POWDER_BLOCK.get().defaultBlockState(), 1);

            Player player = context.getPlayer();
            if (!player.isCreative())
            {
                ReplaceWithEmptyBucket(player);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    private boolean IsBlockAvailable(Level level, BlockPos blockPos)
    {
        if (level.isEmptyBlock(blockPos))
        {
            AABB block = new AABB(blockPos);
            if ((level.getEntities(null, block)).size() == 0)
            {
                return true;
            }
        }
        return false;
    }

    private BlockPos GetTargetBlock(UseOnContext context)
    {
        BlockPos clickedPosition = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos targetPosition = clickedPosition.relative(clickedFace);
        return targetPosition;
    }

    private void ReplaceWithEmptyBucket(Player player)
    {
        InteractionHand hand = player.getUsedItemHand();
        ItemStack emptyBucket = new ItemStack(Items.BUCKET);
        player.setItemInHand(hand, emptyBucket);
    }
}
