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

    public static final Supplier<BlockEntityType<BaseSoulStorageBlockEntity>> BASE_SOUL_STORAGE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("base_soul_storage_block_entity", () -> BlockEntityType.Builder.of(
                    BaseSoulStorageBlockEntity::new, ModBlocks.BASE_SOUL_STORAGE_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<SoulAltarBlockEntity>> SOUL_ALTAR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_altar_block_entity", () -> BlockEntityType.Builder.of(
                    SoulAltarBlockEntity::new, ModBlocks.SOUL_ALTAR_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<SoulTubeBlockEntity>> SOUL_TUBE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_tube_block_entity", () -> BlockEntityType.Builder.of(
                    SoulTubeBlockEntity::new, ModBlocks.SOUL_TUBE.get()).build(null));

    public static final Supplier<BlockEntityType<EtherFocuserBlockEntity>> ETHER_FOCUSER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ether_focuser_block_entity", () -> BlockEntityType.Builder.of(
                    EtherFocuserBlockEntity::new, ModBlocks.ETHER_FOCUSER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
