package net.detrovv.themod.souls;

import net.minecraft.world.entity.EntityType;
import java.util.*;

public class MobToSoulTranslator
{
    public Map<EntityType, SoulData> origins = new HashMap<>();

    public MobToSoulTranslator()
    {
        origins.put(EntityType.SKELETON, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.STRAY, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.BOGGED, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.WITHER_SKELETON, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.SKELETON_HORSE, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.ZOMBIE, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.HUSK, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.DROWNED, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.ZOMBIFIED_PIGLIN, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.ZOMBIE_VILLAGER, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.ZOGLIN, new SoulData(SoulOrigins.Undead, 1, 1));
        origins.put(EntityType.WITHER, new SoulData(SoulOrigins.Undead, 1, 1));

        origins.put(EntityType.ENDER_DRAGON, new SoulData(SoulOrigins.End, 180, 200));
        origins.put(EntityType.ENDERMAN, new SoulData(SoulOrigins.End, 15, 25));
        origins.put(EntityType.SHULKER, new SoulData(SoulOrigins.End, 2, 7));
        origins.put(EntityType.ENDERMITE, new SoulData(SoulOrigins.End, 2, 3));

        origins.put(EntityType.CREEPER, new SoulData(SoulOrigins.Monstrous, 7, 13));

        origins.put(EntityType.GHAST, new SoulData(SoulOrigins.Infernal, 10, 20));
        origins.put(EntityType.STRIDER, new SoulData(SoulOrigins.Infernal, 5, 15));

        origins.put(EntityType.PILLAGER, new SoulData(SoulOrigins.Sentient, 12, 22));
        origins.put(EntityType.VINDICATOR, new SoulData(SoulOrigins.Sentient, 20, 30));
        origins.put(EntityType.WANDERING_TRADER, new SoulData(SoulOrigins.Sentient, 15, 25));
        origins.put(EntityType.VILLAGER, new SoulData(SoulOrigins.Sentient, 10, 15));
        origins.put(EntityType.PIGLIN, new SoulData(SoulOrigins.Sentient, 10, 15));
        origins.put(EntityType.WITCH, new SoulData(SoulOrigins.Sentient, 20, 30));
        origins.put(EntityType.EVOKER, new SoulData(SoulOrigins.Sentient, 20, 30));

        origins.put(EntityType.ALLAY, new SoulData(SoulOrigins.Dream, 25, 35));
        origins.put(EntityType.VEX, new SoulData(SoulOrigins.Dream, 15, 25));
        origins.put(EntityType.WARDEN, new SoulData(SoulOrigins.Dream, 90, 110));
        origins.put(EntityType.PHANTOM, new SoulData(SoulOrigins.Dream, 1, 1));

        origins.put(EntityType.BLAZE, new SoulData(SoulOrigins.Elemental, 15, 25));
        origins.put(EntityType.BREEZE, new SoulData(SoulOrigins.Elemental, 15, 25));
        origins.put(EntityType.IRON_GOLEM, new SoulData(SoulOrigins.Elemental, 10, 20));
        origins.put(EntityType.SNOW_GOLEM, new SoulData(SoulOrigins.Elemental, 5, 15));
        origins.put(EntityType.MAGMA_CUBE, new SoulData(SoulOrigins.Elemental, 2, 3));
        origins.put(EntityType.GUARDIAN, new SoulData(SoulOrigins.Elemental, 15, 25));
        origins.put(EntityType.ELDER_GUARDIAN, new SoulData(SoulOrigins.Elemental, 40, 60));

    }

    public SoulData translate(EntityType entity)
    {
        return origins.get(entity);
    }
}
