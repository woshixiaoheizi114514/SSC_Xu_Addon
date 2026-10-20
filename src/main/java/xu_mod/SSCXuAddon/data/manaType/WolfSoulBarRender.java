package xu_mod.SSCXuAddon.data.manaType;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.mana.IManaRender;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.util.UIPositionUtils;
import net.onixary.shapeShifterCurseFabric.util.util.BaseSprite;
import net.onixary.shapeShifterCurseFabric.util.util.ISprite;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class WolfSoulBarRender implements IManaRender {
    @Override
    public boolean OverrideInstinctBar() {
        return false;
    }

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Identifier texture = SSCXuAddon.identifier("textures/gui/wolf_soul_bar.png");
    private static final ISprite barSprite = new BaseSprite(texture, 80, 32, 0, 0, 80, 11);
    private static final ISprite normalSoulSprite = new BaseSprite(texture, 80, 32, 0, 16, 7, 9);
    private static final ISprite overChargeSoulSprite = new BaseSprite(texture, 80, 32, 8, 16, 7, 9);
    private static final ISprite emptySoulSprite = new BaseSprite(texture, 80, 32, 16, 16, 7, 9);

    @Override
    public void render(DrawContext drawContext, float v) {
        if (!mc.options.hudHidden) {
            Pair<Integer, Integer> pos = UIPositionUtils.getCorrectPosition(ShapeShifterCurseFabric.clientConfig.manaBarPosType, ShapeShifterCurseFabric.clientConfig.manaBarPosOffsetX, ShapeShifterCurseFabric.clientConfig.manaBarPosOffsetY);
            this.renderBar(drawContext, v, (Integer)pos.getLeft(), (Integer)pos.getRight());
        }
    }

    private void renderBar(DrawContext context, float tickDelta, int x, int y) {
        if (mc.player == null) {
            return;
        }
        double mana = ManaUtils.getPlayerMana(mc.player);
        double maxMana = ManaUtils.getPlayerMaxMana(mc.player);
    }
}
