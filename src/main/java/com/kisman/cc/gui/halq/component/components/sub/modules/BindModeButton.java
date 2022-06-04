package com.kisman.cc.gui.halq.component.components.sub.modules;

import com.kisman.cc.gui.halq.HalqGui;
import com.kisman.cc.gui.halq.component.Component;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.Render2DUtil;
import com.kisman.cc.util.render.objects.*;
import i.gishreloaded.gishcode.utils.visual.ColorUtils;

public class BindModeButton extends Component {
    private final Module module;
    private int x, y, offset, count, index;
    private final String[] values;
    private boolean open = false;
    private int width = HalqGui.width;
    private int layer;

    public BindModeButton(Module module, int x, int y, int offset, int count) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.offset = offset;
        this.count = count;
        this.values = new String[] {"Toggle", "Hold"};
        this.index = module.hold ? 1 : 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        this.index = module.hold ? 1 : 0;
        if(HalqGui.shadowCheckBox) {
            Render2DUtil.drawRectWH(x, y + offset, width, getHeight(), HalqGui.backgroundColor.getRGB());
            Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[] {x, y + offset}, new double[] {x + width / 2, y + offset}, new double[] {x + width / 2, y + offset + HalqGui.height}, new double[] {x, y + offset + HalqGui.height}), ColorUtils.injectAlpha(HalqGui.backgroundColor, 1), HalqGui.getGradientColour(count).getColor()));
            Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[] {x + width / 2, y + offset}, new double[] {x + width, y + offset}, new double[] {x + width, y + offset + HalqGui.height}, new double[] {x + width / 2, y + offset + HalqGui.height}), HalqGui.getGradientColour(count).getColor(), ColorUtils.injectAlpha(HalqGui.backgroundColor, 1)));
        } else Render2DUtil.drawRectWH(x, y + offset, width, getHeight(), HalqGui.getGradientColour(count).getRGB());

        HalqGui.drawString("Bind Mode: " + values[index], x, y + offset, width, HalqGui.height);

        if(open) {
            int offsetY = offset + HalqGui.height;
            for(int i = 0; i < values.length; i++) {
                if(i == index) continue;
                HalqGui.drawCenteredString(values[i], x, y + offsetY, width, HalqGui.height);
                offsetY += HalqGui.height;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(isMouseOnButton(mouseX, mouseY) && button == 0) open = !open;
        else if(isMouseOnButton2(mouseX, mouseY) && button == 0 && open) {
            int offsetY = y +  offset + HalqGui.height;
            for(int i = 0; i < values.length; i++) {
                if(i == index) continue;

                if(mouseY >= offsetY && mouseY <= offsetY + HalqGui.height) {
                    index = i;
                    open = false;
                    module.hold = values[i].equals(values[1]);
                    break;
                }
                offsetY += HalqGui.height;
            }
        }
    }

    @Override
    public void updateComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setOff(int newOff) {
        this.offset = newOff;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getHeight() {
        return HalqGui.height + (open ? (values.length - 1) * HalqGui.height : 0);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean visible() {
        return true;
    }

    public void setWidth(int width) {this.width = width;}
    public void setX(int x) {this.x = x;}
    public int getX() {return x;}
    public void setLayer(int layer) {this.layer = layer;}
    public int getLayer() {return layer;}

    private boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y + offset && y < this.y + offset + HalqGui.height;
    }

    private boolean isMouseOnButton2(int x, int y) {
        return x > this.x && x < this.x + width && y > this.y + offset && y < this.y + offset + getHeight();
    }
}
