package dev.opan.modules.impl.visuals;

import dev.opan.events.SubscribeEvent;
import dev.opan.events.impl.RenderOverlayEvent;
import dev.opan.modules.Module;
import dev.opan.modules.RegisterModule;
import dev.opan.settings.impl.BooleanSetting;
import dev.opan.settings.impl.ColorSetting;
import dev.opan.settings.impl.NumberSetting;
import dev.opan.utils.color.ColorUtils;
import dev.opan.utils.graphics.Renderer2D;
import dev.opan.utils.minecraft.MovementUtils;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

@RegisterModule(name = "Crosshair", description = "Renders a custom crosshair.", category = Module.Category.VISUALS)
public class CrosshairModule extends Module {
    public NumberSetting width = new NumberSetting("Width", "The width of the crosshair.", 1.5f, 0.0f, 5.0f);
    public NumberSetting height = new NumberSetting("Height", "The height of the crosshair.", 4.0f, 0.0f, 10.0f);
    public NumberSetting gap = new NumberSetting("Gap", "The gap of the crosshair.", 2.0f, 0.0f, 10.0f);
    public BooleanSetting dynamic = new BooleanSetting("Dynamic", "Makes the crosshair dynamic.", false);
    public BooleanSetting outline = new BooleanSetting("Outline", "Whether or not to render an outline for the crosshair.", true);
    public ColorSetting color = new ColorSetting("Color", "The color used for the rendering.", ColorUtils.getDefaultColor());

    @SubscribeEvent
    public void onRenderOverlay(RenderOverlayEvent event) {
        if(getNull()) return;

        MatrixStack matrices = event.getMatrices();

        float x = mc.getWindow().getScaledWidth()/2f;
        float y = mc.getWindow().getScaledHeight()/2f;

        float w = width.getValue().floatValue()/2f;
        float h = height.getValue().floatValue();
        float g = gap.getValue().floatValue() + (moving() ? 2 : 0);

        Renderer2D.renderQuad(matrices, x - w, y - h - g, x + w, y - g, color.getColor()); // N
        Renderer2D.renderQuad(matrices, x + g, y - w, x + h + g, y + w, color.getColor()); // E
        Renderer2D.renderQuad(matrices, x - w, y + g, x + w, y + h + g, color.getColor()); // S
        Renderer2D.renderQuad(matrices, x - h - g, y - w, x - g, y + w, color.getColor()); // W

        if (outline.getValue()) {
            Renderer2D.renderOutline(matrices, x - w, y - h - g, x + w, y - g, Color.BLACK); // N
            Renderer2D.renderOutline(matrices, x + g, y - w, x + h + g, y + w, Color.BLACK); // E
            Renderer2D.renderOutline(matrices, x - w, y + g, x + w, y + h + g, Color.BLACK); // S
            Renderer2D.renderOutline(matrices, x - h - g, y - w, x - g, y + w, Color.BLACK); // W
        }
    }

    private boolean moving() {
        return (mc.player.isSneaking() || MovementUtils.isMoving() || !mc.player.isOnGround()) && dynamic.getValue();
    }
}
