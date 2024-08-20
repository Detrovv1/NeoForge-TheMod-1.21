package net.detrovv.themod.screen;

import net.detrovv.themod.souls.Soul;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class SoulButton extends Button
{
    public final Soul soul;

    public SoulButton(int x, int y, int width, int height, Component message, OnPress onPress, Soul soul)
    {
        super(x, y, width, height, message, onPress,
                (button) -> Component.translatable("narration.soulbutton",soul.getOrigin().toString() + " " + soul.getPower()));
        this.soul = soul;
    }
}
