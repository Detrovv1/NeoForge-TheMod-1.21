package net.detrovv.themod.items;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TheMod.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THEMOD_TAB = CREATIVE_MODE_TABS.register("themod_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BUCKET_OF_SOUL_CONCRETE_MORTAR.get()))
                    .title(Component.translatable("creativetab.themod_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.BUCKET_OF_SOUL_CONCRETE_MORTAR.get());
                        output.accept(ModItems.EXECUTIONERS_AXE.get());
                        output.accept(ModBlocks.LOWER_GARGOYLE_MOLD_BLOCK.get());
                        output.accept(ModBlocks.UPPER_GARGOYLE_MOLD_BLOCK.get());
                        output.accept(ModBlocks.SOUL_CONCRETE_BLOCK.get());
                        output.accept(ModBlocks.SOUL_CONCRETE_POWDER_BLOCK.get());
                        output.accept(ModBlocks.BASE_SOUL_STORAGE_BLOCK.get());
                        output.accept(ModBlocks.SOUL_ALTAR_BLOCK.get());
                        output.accept(ModBlocks.SOUL_TUBE.get());
                        output.accept(ModBlocks.ETHER_FOCUSER.get());
                        output.accept(ModBlocks.ETHER_INFUSION_TABLE.get());
                        output.accept(ModBlocks.ETHER_RECIEVER.get());
                    })
                    .build());
}
