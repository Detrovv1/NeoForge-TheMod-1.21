package net.detrovv.themod.events;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.blockEntities.SoulAltarBlockEntity;
import net.detrovv.themod.blocks.ModBlocks;
import net.detrovv.themod.effect.ModEffects;
import net.detrovv.themod.items.ModItems;
import net.detrovv.themod.souls.MobToSoulTranslator;
import net.detrovv.themod.souls.Soul;
import net.detrovv.themod.souls.SoulData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EventBusSubscriber(modid = TheMod.MOD_ID)
public class ModEvents
{
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SubscribeEvent()
    public static void OnKillingMobWithExecutionersAxe(LivingDeathEvent event)
    {
        Level level = event.getEntity().level();
        if (level.isClientSide())
        {
            return;
        }
        if (event.getSource().getEntity() instanceof Player)
        {
            Player player = (Player)event.getSource().getEntity();
            if (player.getMainHandItem().getItem() == ModItems.EXECUTIONERS_AXE.get())
            {
                player.getInventory().add(new ItemStack(Items.ACACIA_FENCE));
            }
        }
    }

    @SubscribeEvent
    public static void OnNamingVindicatorExecutioner(PlayerInteractEvent.EntityInteractSpecific event)
    {
        Level level = event.getLevel();
        if (level.isClientSide())
        {
            return;
        }
        Player player = event.getEntity();
        Item itemInHand = player.getItemInHand(player.getUsedItemHand()).getItem();
        if (itemInHand == Items.NAME_TAG)
        {
            scheduler.schedule(() ->
            {
                Entity entity = event.getTarget();
                if (entity instanceof Vindicator)
                {
                    Vindicator vindicator = (Vindicator)entity;
                    if (vindicator.getCustomName().getString().contains("Executioner"))
                    {
                        vindicator.setCustomName(Component.translatable("Johnny"));
                        vindicator.setCustomName(Component.translatable("Executioner"));
                        vindicator.setItemInHand(vindicator.getUsedItemHand(),new ItemStack(ModItems.EXECUTIONERS_AXE.get()));
                        vindicator.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                    }
                }
            }, 50, TimeUnit.MICROSECONDS);
        }
    }

    @SubscribeEvent
    public static void DisguiseEffectProtection(LivingChangeTargetEvent event)
    {
        LivingEntity livingEntity = event.getEntity();
        HolderSet<EntityType<?>> illagers = BuiltInRegistries.ENTITY_TYPE.getOrCreateTag(EntityTypeTags.ILLAGER);
        if (livingEntity.getType().is(illagers))
        {
            Mob mob = (Mob)livingEntity;
            if (event.getNewAboutToBeSetTarget() instanceof Player)
            {
                Player player = (Player)event.getNewAboutToBeSetTarget();
                if (player.hasEffect(ModEffects.DISGUISE))
                {
                    if (mob instanceof Evoker)
                    {
                        MobEffectInstance slowness_effect = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 7, false, false);
                        mob.addEffect(slowness_effect);
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void TryToCatchSoul(LivingDeathEvent event)
    {
        LivingEntity mob = event.getEntity();
        BlockPos mobPosition = mob.getOnPos();
        Level level = event.getEntity().level();

        if (level.isClientSide())
        {
            return;
        }

        int thisX = mobPosition.getX();
        int thisY = mobPosition.getY() + 1;
        int thisZ = mobPosition.getZ();
        int range = 5;

        for (int x = thisX - range; x <= thisX + range; x++)
        {
            for (int y = thisY - range; y <= thisY + range; y++)
            {
                for (int z = thisZ - range; z <= thisZ + range; z++)
                {
                    BlockPos currentPosition = new BlockPos(x, y, z);
                    if (level.getBlockState(currentPosition).getBlock() == ModBlocks.SOUL_ALTAR_BLOCK.get())
                    {
                        SoulAltarBlockEntity altar = (SoulAltarBlockEntity)level.getBlockEntity(currentPosition);
                        if (altar.HasEmptySoulStorageNearby())
                        {
                            MobToSoulTranslator translator = new MobToSoulTranslator();
                            SoulData soulData = translator.translate(mob.getType());
                            Soul soul = new Soul(soulData);
                            altar.StoreSoulInStorageNearby(soul);
                            return;
                        }
                    }
                }
            }
        }
    }
}

