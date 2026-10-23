package xu_mod.SSCXuAddon.data.entity.minion;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.minion.mobs.AnubisWolfMinionEntityModel;

import static net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric.MOD_ID;
import static net.onixary.shapeShifterCurseFabric.minion.MinionRegisterClient.WOLF_MINION_LAYER;

@Environment(EnvType.CLIENT)
public class SoulWolfMinionRenderer extends MobEntityRenderer<WolfEntity, AnubisWolfMinionEntityModel<WolfEntity>> {
    private static final Identifier WOLF_MINION_TEXTURE = ShapeShifterCurseFabric.identifier("textures/entity/mob/anubis_wolf_minion.png");

    public SoulWolfMinionRenderer(EntityRendererFactory.Context context) {
        super(context, new AnubisWolfMinionEntityModel<>(context.getPart(WOLF_MINION_LAYER)), 0.5F);
    }

    protected float getAnimationProgress(WolfEntity wolfEntity, float f) {
        return wolfEntity.getTailAngle();
    }

    @Override
    public Identifier getTexture(WolfEntity entity) {
        return WOLF_MINION_TEXTURE;
    }
}