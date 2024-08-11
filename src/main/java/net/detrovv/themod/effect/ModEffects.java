package net.detrovv.themod.effect;

import net.detrovv.themod.TheMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModEffects
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, TheMod.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> DISGUISE = MOB_EFFECTS.register("disguise",
            () -> new DisguiseEffect(MobEffectCategory.BENEFICIAL, 2134125));
}
