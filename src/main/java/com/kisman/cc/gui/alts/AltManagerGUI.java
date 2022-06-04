package com.kisman.cc.gui.alts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.kisman.cc.gui.alts.microsoft.MSAuthScreen;
import com.kisman.cc.mixin.mixins.accessor.ISession;
import com.kisman.cc.util.customfont.CustomFontUtil;
import i.gishreloaded.gishcode.utils.visual.ColorUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.GL11;

public class AltManagerGUI extends GuiScreen {
	private GuiButton delete;
	private GuiScreen lastGui;
	private AltSlotList altList;
	private GuiTextField crackedNameField;
	
	public AltManagerGUI(GuiScreen lastGui) {
		this.lastGui = lastGui;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.crackedNameField = new GuiTextField(69, mc.fontRenderer, 4, 20, 95, 15);
		buttonList.add(new GuiButton(4, 4 + 4 + 95, 20, "Microsoft"));
		this.crackedNameField.setText(mc.getSession().getUsername());
		this.crackedNameField.setMaxStringLength(16);
		this.altList = new AltSlotList(this, this.mc, this.width, this.height, 40, this.height - 60, 36);
		buttonList.add(new GuiButton(1, this.width / 2 - 75, this.height - 52, 75, 20, "Add"));
		this.delete = new GuiButton(2, this.width / 2 + 1, this.height - 52, 75, 20, "Delete");
		buttonList.add(delete);
		buttonList.add(new GuiButton(3, this.width / 2 - 75, this.height - 30, 150, 20, "Back"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.altList.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		CustomFontUtil.drawCenteredStringWithShadow("Alts", width / 4, 6, ColorUtils.astolfoColors(100, 100));
		GL11.glPopMatrix();
		String s = "Signed in as ";
		this.drawString(mc.fontRenderer, s, 4, 6, 0xFFAAAAAA);
		this.drawString(mc.fontRenderer, mc.getSession().getUsername(), mc.fontRenderer.getStringWidth(s) + 3, 6, -1);
		this.crackedNameField.drawTextBox();
		if(!this.crackedNameField.isFocused()) this.crackedNameField.setText(mc.getSession().getUsername());
		delete.enabled = this.altList.getVisibility().get();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.crackedNameField.mouseClicked(mouseX, mouseY, mouseButton);
		this.altList.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		this.altList.handleMouseInput();
		super.handleMouseInput();
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(this.lastGui);
			return;
		}
		this.crackedNameField.textboxKeyTyped(typedChar, keyCode);
		if(keyCode == Keyboard.KEY_RETURN) {
			((ISession) mc.getSession()).setUsername(this.crackedNameField.getText());
			this.crackedNameField.setFocused(false);
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		this.altList.actionPerformed(button);
		switch(button.id) {
			case 1 : {
				mc.displayGuiScreen(new AltCreatorGUI(this));
				break;
			}
			case 2 : {
				if(this.altList.getVisibility().get()) {
					AltEntry e = this.altList.getAlts().get(this.altList.getSelectedId());
					this.altList.getAlts().remove(e);
					AltManager.getAlts().remove(e);
					/* TODO handle removing alts in config */
				}
				break;
			}
			case 3 : {
				mc.displayGuiScreen(this.lastGui);
				break;
			}
			case 4:
				mc.displayGuiScreen(new MSAuthScreen(this));
				break;
			default : break;
		}
	}
	
	private static class AltSlotList extends GuiListExtended {
		private final List<AltEntry> alts = new ArrayList<>();
		private int selectedId = -1;
		
		public AltSlotList(AltManagerGUI parentGui, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
			super(mc, width, height, top, bottom, slotHeight);
			alts.clear();
			AltManager.getAlts().forEach(alt -> this.alts.add(alt));
			/* TODO Add saved alts here once configging is done. */
		}

		@Override
		public AltEntry getListEntry(int index) {
			return this.alts.get(index);
		}
		
		@Override
		public int getListWidth() {
			return super.getListWidth() + 50;
		}
		
		@Override
		protected void elementClicked(int i, boolean b, int i1, int i2) {
			this.selectElement(i);
		}

		@Override
		protected int getSize() {
			return this.alts.size();
		}
		
		@Override
		protected int getScrollBarX() {
			return super.getScrollBarX() + 20;
		}
		
		@Override
		protected boolean isSelected(int slotIndex) {
			return this.selectedId == slotIndex;
		}
		
		protected Supplier<Boolean> getVisibility() {
			return () -> this.selectedId > -1;
		}
		
		protected List<AltEntry> getAlts() {
			return this.alts;
		}
		
		protected int getSelectedId() {
			return this.selectedId;
		}
		
		private void selectElement(int element) {
			this.selectedId = element;
			this.showSelectionBox = true;
			this.selectedElement = element;
		}
	}
}
