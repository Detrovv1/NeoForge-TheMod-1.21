package net.detrovv.themod.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EtherParticleProvider implements ParticleProvider<SimpleParticleType>
{
    private final SpriteSet spriteSet;

    public EtherParticleProvider(SpriteSet spriteSet)
    {
        this.spriteSet = spriteSet;
    }

    public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                             double v, double v1, double v2, double v3, double v4, double v5)
    {
        return new EtherParticle(clientLevel, v, v1, v2, (int)v3, spriteSet);
    }
}
