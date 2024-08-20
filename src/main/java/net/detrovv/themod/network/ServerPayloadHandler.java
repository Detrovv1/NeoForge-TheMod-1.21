package net.detrovv.themod.network;

import net.detrovv.themod.blocks.custom.SoulStorages.AbstractSoulStorageBlockEntity;
import net.detrovv.themod.souls.Soul;
import net.detrovv.themod.toServer.GUIButtonPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.awt.*;

public class ServerPayloadHandler {
    public static void handleGUIButtonPayload(final GUIButtonPayload data, final IPayloadContext context) {
        // Do something with the data, on the network thread

        // Do things with gui button payloads
        switch (data.buttonType()) {
            case GUIButtonPayload.ButtonType.Button:
                handleButtonPayload(data, context);
            break;
            // Can add another button for another block here
            default:
                break;
        }
    }

    private static void handleButtonPayload(final GUIButtonPayload data, final IPayloadContext context) {
        Level level = context.player().level();
        BlockPos pos = data.pos();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof AbstractSoulStorageBlockEntity entity)
        {
        }
    }
}