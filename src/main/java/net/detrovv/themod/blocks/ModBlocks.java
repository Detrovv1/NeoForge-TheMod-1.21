package net.detrovv.themod.blocks;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.blocks.custom.*;
import net.detrovv.themod.blocks.custom.SoulStorages.BaseSoulStorageBlock;
import net.detrovv.themod.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
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

    public static final DeferredBlock<Block> SOUL_CONCRETE_BLOCK =
            registerBlock("soul_concrete_block", () -> new Block(Blocks.BLACK_CONCRETE.properties()));

    public static final DeferredBlock<Block> SOUL_CONCRETE_POWDER_BLOCK =
            registerBlock("soul_concrete_powder_block", () -> new ConcretePowderBlock(SOUL_CONCRETE_BLOCK.get() ,Blocks.BLACK_CONCRETE_POWDER.properties()));

    public static final DeferredBlock<Block> BASE_SOUL_STORAGE_BLOCK =
            registerBlock("base_soul_storage_block", () -> new BaseSoulStorageBlock(Blocks.DEEPSLATE.properties().noOcclusion()));

    public static final DeferredBlock<Block> SOUL_ALTAR_BLOCK =
            registerBlock("soul_altar_block", () -> new SoulAltar(Blocks.DEEPSLATE.properties()));

    public static final DeferredBlock<Block> SOUL_TUBE =
            registerBlock("soul_tube", () -> new SoulTube(Blocks.COPPER_BLOCK.properties()));

    public static final DeferredBlock<Block> ETHER_FOCUSER =
            registerBlock("ether_focuser", () -> new EtherFocuser(Blocks.LODESTONE.properties()));

    public static final DeferredBlock<Block> ETHER_INFUSION_TABLE =
            registerBlock("ether_infusion_table", () -> new EtherInfusionTable(Blocks.ENCHANTING_TABLE.properties()));

    public static final DeferredBlock<Block> ETHER_RECIEVER =
            registerBlock("ether_reciever", () -> new EtherReciever(Blocks.ENCHANTING_TABLE.properties()));


    public static DeferredBlock<Block> registerBlock(String name, Supplier<Block> block)
    {
        DeferredBlock<Block> blockReg = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(blockReg.get(), new Item.Properties()));
        return blockReg;
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}