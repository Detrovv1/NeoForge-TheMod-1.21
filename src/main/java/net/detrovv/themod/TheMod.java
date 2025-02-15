package net.detrovv.themod;

import net.detrovv.themod.ModAttachments.ModAttachments;
import net.detrovv.themod.blockEntities.ModBlockEntities;
import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.effect.ModEffects;
import net.detrovv.themod.gui.ModMenus;
import net.detrovv.themod.items.ModCreativeTabs;
import net.detrovv.themod.items.ModItems;
import net.detrovv.themod.network.PacketHandler;
import net.detrovv.themod.particles.EtherParticleProvider;
import net.detrovv.themod.particles.ModParticles;
import net.detrovv.themod.screen.SoulStorageScreen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(TheMod.MOD_ID)
public class TheMod
{
    public static final String MOD_ID = "themod";

    public static final Logger LOGGER = LogUtils.getLogger();

    public TheMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);



        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModEffects.register(modEventBus);

        ModCreativeTabs.register(modEventBus);

        ModAttachments.register(modEventBus);

        ModMenus.register(modEventBus);

        ModParticles.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(RegisterMenuScreensEvent event)
        {
            event.register(ModMenus.SOUL_STORAGE_MENU.get(), SoulStorageScreen::new);
        }
        @SubscribeEvent
        public static void packetHandlerRegistration(RegisterPayloadHandlersEvent event)
        {
            PacketHandler.register(event);
        }
        @SubscribeEvent
        public static void registerParticleProviders(RegisterParticleProvidersEvent event)
        {
            event.registerSpriteSet(ModParticles.ETHER.get(), EtherParticleProvider::new);
        }
    }
}
