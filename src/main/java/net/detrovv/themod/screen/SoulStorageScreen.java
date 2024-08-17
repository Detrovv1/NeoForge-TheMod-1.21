package net.detrovv.themod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.detrovv.themod.TheMod;
import net.detrovv.themod.blocks.custom.SoulStorages.AbstractSoulStorageBlockEntity;
import net.detrovv.themod.gui.SoulStorageMenu;
import net.detrovv.themod.souls.Soul;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SoulStorageScreen extends AbstractContainerScreen<SoulStorageMenu>
{
    private static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(TheMod.MOD_ID, "textures/gui/soul_storage_menu.png");
    private static final Component INSERT_BUTTON = Component.translatable("gui." + TheMod.MOD_ID + ".soul_storage_screen_insert_button");
    private static final Component EXTRACT_BUTTON = Component.translatable("gui." + TheMod.MOD_ID + ".soul_storage_screen_extract_button");
    private int leftPos;
    private int topPos;
    private SoulButton selectedSoulButton;
    private Button insertButton;
    private Button extractButton;
    private Player player;
    private AbstractSoulStorageBlockEntity storage;
    private SoulStorageMenu menu;

    public SoulStorageScreen(SoulStorageMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 175;
        this.inventoryLabelX = 113;
        this.inventoryLabelY = 81;
        this.player = menu.player;
        this.storage = menu.storage;
        this.menu = menu;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.insertButton = addRenderableWidget(
                Button.builder(INSERT_BUTTON, this::handleInsertButton).bounds(leftPos + 107, topPos + 39, 62, 15).build());
        this.extractButton = addRenderableWidget(
                Button.builder(EXTRACT_BUTTON, this::handleExtractButton).bounds(leftPos + 107, topPos + 58, 62, 15).build());

        List<Soul> souls = storage.getSouls();
        menu.remoteStorageSlot.set(storage.getRemoteSoulStorage());


        fillSoulButtons(souls);
    }

    private void fillSoulButtons(List<Soul> souls)
    {
        int x = 8;
        int y = 18;
        for (int i = 0; i < souls.size(); i++)
        {
            Soul soul = souls.get(i);
            SoulButton soulButton = new SoulButton(leftPos + x, topPos + y*(i+1)-i, 88, 17,
                    Component.literal(soul.GetOrigin().toString() + " " + soul.GetPower()), this::handleSoulButton, soul);
            addRenderableWidget(soulButton);
        }
    }

    private void handleSoulButton(Button button)
    {
        SoulButton soulButton = (SoulButton)button;
        selectedSoulButton = soulButton;
    }

    private void handleInsertButton(Button button)
    {

    }

    private void handleExtractButton(Button button)
    {

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1)
    {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, GUI);
        guiGraphics.blit(GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public void onClose() {
        super.onClose();
        ItemStack remoteStorageSlot = menu.remoteStorageSlot.getItem();
        storage.setRemoteSoulStorage(remoteStorageSlot);
    }
}
