package net.detrovv.themod.ModAttachments;

import com.mojang.serialization.Codec;
import net.detrovv.themod.TheMod;
import net.detrovv.themod.souls.ListSoulSupplier;
import net.detrovv.themod.souls.Soul;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class ModAttachments
{
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheMod.MOD_ID);

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }

    public static final Supplier<AttachmentType<List<Soul>>> STORED_SOULS = ATTACHMENT_TYPES.register(
            "stored_souls", () -> AttachmentType.builder(new ListSoulSupplier()).build());

}
