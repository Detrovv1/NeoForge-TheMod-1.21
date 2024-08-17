package net.detrovv.themod.ModAttachments;

import com.mojang.serialization.Codec;
import net.detrovv.themod.TheMod;
import net.detrovv.themod.souls.ListSoulSupplier;
import net.detrovv.themod.souls.Soul;
import net.detrovv.themod.souls.SoulOrigins;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
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

//    private static final Supplier<AttachmentType<Soul>> STORED_SOULS = ATTACHMENT_TYPES.register(
//            "stored_souls", () -> AttachmentType.serializable(() -> new Soul()).build()
//    );
//
//    public static final Supplier<AttachmentType<ItemStack>> REMOTE_SOUL_STORAGE_SLOT = ATTACHMENT_TYPES.register(
//            "remote_soul_storage_slot", () -> AttachmentType.builder(new ItemStackSupplier()).build());
//

}
