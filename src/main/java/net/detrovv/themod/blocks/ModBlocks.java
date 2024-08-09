package net.detrovv.themod.blocks;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.blocks.custom.LowerGargoyleMoldBlock;
import net.detrovv.themod.blocks.custom.UpperGargoyleMoldBlock;
import net.detrovv.themod.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TheMod.MOD_ID);

    public static final DeferredBlock<Block> LOWER_GARGOYLE_MOLD_BLOCK =
            registerBlock("lower_gargoyle_mold_block", () -> new LowerGargoyleMoldBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));

    public static final DeferredBlock<Block> UPPER_GARGOYLE_MOLD_BLOCK =
            registerBlock("upper_gargoyle_mold_block", () -> new UpperGargoyleMoldBlock(LOWER_GARGOYLE_MOLD_BLOCK.get().properties()));

    public static final DeferredBlock<Block> registerBlock(String name, Supplier<Block> block)
    {
        DeferredBlock<Block> blockReg = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(blockReg.get(), new Item.Properties()));
        return blockReg;
    }
}
