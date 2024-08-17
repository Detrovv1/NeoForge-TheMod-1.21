package net.detrovv.themod.souls;

import com.mojang.serialization.Codec;

public enum SoulOrigins
{
    Undead,
    Primitive,
    Elemental,
    Dream,
    Sentient,
    Monstrous,
    Infernal,
    End;

    public static final Codec<SoulOrigins> CODEC = Codec.STRING.xmap(SoulOrigins::valueOf, SoulOrigins::name);
}
