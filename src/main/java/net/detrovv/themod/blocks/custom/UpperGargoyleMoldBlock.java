package net.detrovv.themod.blocks.custom;

import net.detrovv.themod.blockEntities.ModBlockEntities;
import net.detrovv.themod.blockEntities.UpperGargoyleMoldBlockEntity;
import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class UpperGargoyleMoldBlock extends Block implements EntityBlock
{
    public static final IntegerProperty MOLD_STATE = IntegerProperty.create("mold_state",0,2);
    public UpperGargoyleMoldBlock(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MOLD_STATE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pType) {
        return pLevel.isClientSide() ? null : (level0, pos0, state0, blockEntity) -> ((UpperGargoyleMoldBlockEntity)blockEntity).tick();
    }

    @Override
    protected boolean canSurvive(BlockState pBlockState, LevelReader pLevelReader, BlockPos pBlockPos)
    {
        BlockState lowerBlockState = pLevelReader.getBlockState(pBlockPos.below());
        if (lowerBlockState.is(ModBlocks.LOWER_GARGOYLE_MOLD_BLOCK.get()))
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onPlace(BlockState pBlockState0, Level pLevel, BlockPos pos, BlockState pBlockState1, boolean pboolean)
    {
        if (pBlockState0.getValue(MOLD_STATE) != 1 && pBlockState0.getValue(MOLD_STATE) != 2)
        {
            pLevel.setBlock(pos, pBlockState0.setValue(MOLD_STATE, 0), 3);
        }
    }

    @Override
    protected void onRemove(BlockState pOldState, Level pLevel, BlockPos pos, BlockState pNewState, boolean pboolean)
    {
        if (pLevel.isClientSide())
        {
            return;
        }
        if (pOldState.getBlock() != pNewState.getBlock())
        {
            pLevel.removeBlockEntity(pos);
            BlockPos lowerBlock = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            pLevel.destroyBlock(lowerBlock, false);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pItemStack, BlockState pBlockState, Level pLevel, BlockPos pBlockPos, Player pPlayer, InteractionHand pInteractionHand, BlockHitResult pBlockHitResult)
    {
        if (pLevel.isClientSide())
        {
            return ItemInteractionResult.CONSUME;
        }
        UpperGargoyleMoldBlockEntity entity = (UpperGargoyleMoldBlockEntity)pLevel.getBlockEntity(pBlockPos);
        if (pItemStack.is(ModItems.BUCKET_OF_SOUL_CONCRETE_MORTAR.get()) &&
                entity.GetMortarLevel() < 2 &&
                pBlockHitResult.getDirection() == Direction.UP &&
                !pLevel.isClientSide)
        {
            entity.AddMortar();
            if (entity.GetMortarLevel() == 2)
            {
                pLevel.setBlock(pBlockPos, pBlockState.setValue(MOLD_STATE, 1), 3);
            }
            if (!pPlayer.isCreative())
            {
                ReplaceWithEmptyBucket(pPlayer);
            }
            pPlayer.sendSystemMessage(Component.translatable("" + entity.GetMortarLevel()));
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.CONSUME;
    }

    private void ReplaceWithEmptyBucket(Player player)
    {
        ItemStack empty_bucket = new ItemStack(Items.BUCKET);
        InteractionHand hand = player.getUsedItemHand();
        player.setItemInHand(hand, empty_bucket);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.DESTROY;
    }

    public void TryToDryConcrete(Level pLevel, BlockPos pPos)
    {
        Random rand = new Random();
        if (rand.nextInt(24001) > 23999)
        {
            pLevel.setBlock(pPos, pLevel.getBlockState(pPos).setValue(MOLD_STATE, 2), 3);
        }
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new UpperGargoyleMoldBlockEntity(pos, state);
    }
}
