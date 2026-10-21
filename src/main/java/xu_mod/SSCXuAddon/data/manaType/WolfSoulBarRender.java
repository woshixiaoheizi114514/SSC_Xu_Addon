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

    private static final int SOUL_WIDTH = 7;
    private static final int SOUL_HEIGHT = 9;
    private static final int SOUL_GAP = 1;
    private static final int SOUL_START_X = 2;
    private static final int SOUL_START_Y = 1;
    private static final int NEGATIVE_START_SLOT = 1;
    private static final int NEXT_SOUL_X = 71;
    private static final int NEXT_SOUL_Y = 1;
    private static final double MANA_DISPLAY_OFFSET = -6.0;

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
        double mana = ManaUtils.getPlayerMana(mc.player) + MANA_DISPLAY_OFFSET;
        double maxMana = ManaUtils.getPlayerMaxMana(mc.player) + MANA_DISPLAY_OFFSET;

        barSprite.draw(context, x, y);

        int floorMax = (int) Math.floor(maxMana);
        int overChargeCount = floorMax / 2;
        int threshold = floorMax - overChargeCount;

        if (mana >= 0) {
            int fullCount = (int) Math.floor(mana);
            for (int j = 0; j < threshold; j++) {
                int sx = x + SOUL_START_X + j * (SOUL_WIDTH + SOUL_GAP);
                int sy = y + SOUL_START_Y;
                if (j + threshold < fullCount) {
                    overChargeSoulSprite.draw(context, sx, sy);
                } else if (j < fullCount) {
                    normalSoulSprite.draw(context, sx, sy);
                }
            }
        } else {
            int emptyCount = (int) Math.floor(-mana);
            for (int j = 0; j < emptyCount; j++) {
                int sx = x + SOUL_START_X + (NEGATIVE_START_SLOT + j) * (SOUL_WIDTH + SOUL_GAP);
                int sy = y + SOUL_START_Y;
                emptySoulSprite.draw(context, sx, sy);
            }
        }

        int nsx = x + NEXT_SOUL_X;
        int nsy = y + NEXT_SOUL_Y;
        if (mana >= 0) {
            int nextIndex = (int) Math.floor(mana);
            float frac = (float) (mana - nextIndex);
            if (frac > 0.0f) {
                int h = Math.round(frac * SOUL_HEIGHT);
                if (h > 0) {
                    ISprite s = (nextIndex >= threshold) ? overChargeSoulSprite : normalSoulSprite;
                    s.draw(context, nsx, nsx + SOUL_WIDTH, nsy + SOUL_HEIGHT - h, nsy + SOUL_HEIGHT, 0, SOUL_WIDTH, h, 0, SOUL_HEIGHT - h);
                }
            }
        } else {
            double negValue = -mana;
            float frac = (float) (negValue - Math.floor(negValue));
            emptySoulSprite.draw(context, nsx, nsy);
            if (frac > 0.0f) {
                int h = Math.round((1.0f - frac) * SOUL_HEIGHT);
                if (h > 0) {
                    normalSoulSprite.draw(context, nsx, nsx + SOUL_WIDTH, nsy + SOUL_HEIGHT - h, nsy + SOUL_HEIGHT, 0, SOUL_WIDTH, h, 0, SOUL_HEIGHT - h);
                }
            }
        }
    }
}