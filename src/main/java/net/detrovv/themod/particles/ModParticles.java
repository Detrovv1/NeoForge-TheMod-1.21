package net.detrovv.themod.particles;

import net.detrovv.themod.TheMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles
{
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, TheMod.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ETHER = PARTICLES.register(
            "ether", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus)
    {
        PARTICLES.register(eventBus);
    }
}
