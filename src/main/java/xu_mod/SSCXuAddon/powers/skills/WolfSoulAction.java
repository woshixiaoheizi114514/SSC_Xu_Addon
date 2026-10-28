package xu_mod.SSCXuAddon.powers.skills;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.init.Init_Effect;

import java.util.function.Consumer;

public class WolfSoulAction {

    private static final double MANA_DISPLAY_OFFSET = -6.0d;

    public static void negativeKillGain(SerializableData.Instance data, Entity owner) {
        if (!(owner instanceof ServerPlayerEntity player)) {
            return;
        }
        double soul = ManaUtils.getPlayerMana(player);
        double gain = soul * data.getDouble("multiplier");
        ManaUtils.gainPlayerMana(player, gain);
    }

    public static void hurtLose(SerializableData.Instance data, Entity owner) {
        if (!(owner instanceof ServerPlayerEntity player)) {
            return;
        }
        if (player.hasStatusEffect(Init_Effect.SOUL_LOCK)) {
            return;
        }
        double soul = ManaUtils.getPlayerMana(player) + MANA_DISPLAY_OFFSET;
        if (soul <= 0) {
            return;
        }
        double lose = Math.min(soul, soul * data.getDouble("percent"));
        ManaUtils.consumePlayerMana(player, lose);
    }

    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("wolf_soul_negative_kill_gain"),
                new SerializableData()
                        .add("multiplier", SerializableDataTypes.DOUBLE, 0.125d),
                (data, entity) -> negativeKillGain(data, entity)
        ));
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("wolf_soul_hurt_lose"),
                new SerializableData()
                        .add("percent", SerializableDataTypes.DOUBLE, 0.2d),
                (data, entity) -> hurtLose(data, entity)
        ));
    }
}