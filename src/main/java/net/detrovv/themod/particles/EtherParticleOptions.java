package net.detrovv.themod.particles;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public class EtherParticleOptions implements ParticleOptions
{
    public static final MapCodec<EtherParticleOptions> CODEC = MapCodec.unit(new EtherParticleOptions());

    public static final StreamCodec<ByteBuf, EtherParticleOptions> STREAM_CODEC = StreamCodec.unit(new EtherParticleOptions());

    public EtherParticleOptions() {}

    @Override
    public ParticleType<?> getType() {
        return ModParticles.ETHER.get();
    }
}
