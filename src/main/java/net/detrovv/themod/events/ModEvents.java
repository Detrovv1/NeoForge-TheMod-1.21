package net.detrovv.themod.events;

import net.detrovv.themod.TheMod;
import net.detrovv.themod.items.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;
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
}

