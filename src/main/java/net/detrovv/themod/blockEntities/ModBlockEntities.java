package net.detrovv.themod.blockEntities;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheMod.MOD_ID);

    public static final Supplier<BlockEntityType<UpperGargoyleMoldBlockEntity>> UPPER_GARGOYLE_MOLD_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("upper_gargoyle_mold_block_entity", () -> BlockEntityType.Builder.of(
                    UpperGargoyleMoldBlockEntity::new, ModBlocks.UPPER_GARGOYLE_MOLD_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
