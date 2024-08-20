package net.detrovv.themod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.detrovv.themod.TheMod;
import net.detrovv.themod.gui.SoulStorageMenu;
import net.detrovv.themod.souls.Soul;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SoulStorageScreen extends AbstractContainerScreen<SoulStorageMenu>
{
    private static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(TheMod.MOD_ID, "textures/gui/soul_storage_menu.png");
    private static final Component INSERT_BUTTON = Component.translatable("gui." + TheMod.MOD_ID + ".soul_storage_screen_insert_button");
    private static final Component EXTRACT_BUTTON = Component.translatable("gui." + TheMod.MOD_ID + ".soul_storage_screen_extract_button");
    private int leftPos;
    private int topPos;
    private Button insertButton;
    private Button extractButton;
    private Button forwardButton;
    private Button backwardButton;
    private SoulButton selectedSoulButton;
    private List<SoulButton> soulButtons = new ArrayList<SoulButton>();
    private SoulStorageMenu menu;
    private static final int SOULS_ON_ONE_PAGE = 4;
    private int maxPages;
    private int currentPage = 1;

    public SoulStorageScreen(SoulStorageMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 175;
        this.inventoryLabelX = 113;
        this.inventoryLabelY = 81;
        this.menu = menu;
        this.maxPages = menu.blockEntity.getSouls().size() / 4 + ((menu.blockEntity.getSouls().size() % 4 != 0) ? 1 : 0);
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
        this.forwardButton = addRenderableWidget(
                Button.builder(Component.literal(""), this::handleForwardButton).bounds(leftPos + 97, topPos + 42, 6, 10).build());
        this.backwardButton = addRenderableWidget(
                Button.builder(Component.literal(""), this::handleBackwardButton).bounds(leftPos + 97, topPos + 53, 6, 10).build());

        fillSoulButtons();
    }

    private void handleForwardButton(Button button)
    {
        if (currentPage < maxPages)
        {
            currentPage++;
            fillSoulButtons();
        }
    }

    private void handleBackwardButton(Button button)
    {
        if (currentPage > 1)
        {
            currentPage--;
            fillSoulButtons();
        }
    }

    private void handleSoulButton(Button button)
    {
        SoulButton soulButton = (SoulButton)button;
        selectedSoulButton = soulButton;
    }

    private void handleInsertButton(Button button)
    {
//        PacketDistributor.sendToServer(new GUIButtonPayload(GUIButtonPayload.ButtonType.Button, menu.blockEntity.getBlockPos()));
    }

    private void handleExtractButton(Button button)
    {

    }

    private void fillSoulButtons()
    {
        int xMargin = 8;
        int yMargin = 18;

        List<Soul> currentPageSouls = getSoulsByPage(this.menu.blockEntity.getSouls(), currentPage);
        removeSoulButtons();
        selectedSoulButton = null;

        for (int i = 0; i < currentPageSouls.size(); i++)
        {
            Soul soul = currentPageSouls.get(i);
            Component soulButtonText = Component.literal(soul.getOrigin().toString() + " " + soul.getPower());
            SoulButton soulButton = new SoulButton(leftPos + xMargin, topPos + yMargin*(i+1) - i,
                                      88, 17, soulButtonText, this::handleSoulButton, soul);
            addRenderableWidget(soulButton);
            soulButtons.add(soulButton);
        }
    }

    private List<Soul> getSoulsByPage(List<Soul> souls, int page)
    {
        int firstSoul = SOULS_ON_ONE_PAGE * (page - 1) + 1;
        int lastSoul;

        if (souls.size() < firstSoul + 3)
        {
            lastSoul = souls.size();
        }
        else lastSoul = firstSoul + 3;

        List<Soul> soulsOnCurrentPage = new ArrayList<Soul>();

        for (int i = firstSoul - 1; i < lastSoul; i++)
        {
            soulsOnCurrentPage.add(souls.get(i));
        }

        return soulsOnCurrentPage;
    }

    private void removeSoulButtons()
    {
        for (SoulButton button : soulButtons)
        {
            this.removeWidget(button);
        }
        soulButtons = new ArrayList<SoulButton>();
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
}
