package xu_mod.SSCXuAddon.powers.skills;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.minion.IPlayerEntityMinion;
import xu_mod.SSCXuAddon.data.entity.minion.SoulWolfMinion;
import xu_mod.SSCXuAddon.init.Init_Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class WolfMergeAction {

    private static final double MANA_DISPLAY_OFFSET = -6.0d;

    public static void merge(SerializableData.Instance data, Entity owner) {
        if (!(owner instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return;
        }
        if (!(player instanceof IPlayerEntityMinion playerMinion)) {
            return;
        }
        List<SoulWolfMinion> wolves = new ArrayList<>();
        for (UUID uuid : playerMinion.shape_shifter_curse$getMinionsByMinionID(SoulWolfMinion.MinionID)) {
            Entity e = world.getEntity(uuid);
            if (e instanceof SoulWolfMinion wolf && wolf.isAlive()) {
                wolves.add(wolf);
            }
        }
        if (wolves.isEmpty()) {
            return;
        }
        int sumLevel = 0;
        for (SoulWolfMinion wolf : wolves) {
            sumLevel += wolf.level;
        }

        double rawSoul = ManaUtils.getPlayerMana(player) + MANA_DISPLAY_OFFSET;
        int soul = (int) Math.floor(rawSoul);
        double rawMaxSoul = ManaUtils.getPlayerMaxMana(player) + MANA_DISPLAY_OFFSET;
        int floorMaxSoul = (int) Math.floor(rawMaxSoul);

        int owed = Math.max(0, -soul);
        int positiveSoul = Math.max(0, soul);
        int total = positiveSoul + sumLevel;
        int soulAfter = Math.min(total, floorMaxSoul);
        int overflow = total - soulAfter;
        int points = overflow + sumLevel / 2 - 2 * owed;
        if (points < 0) {
            points = 0;
        }

        for (SoulWolfMinion wolf : wolves) {
            world.playSound(null, wolf.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, player.getSoundCategory(), 0.6f, 1.2f);
            playerMinion.shape_shifter_curse$removeMinion(SoulWolfMinion.MinionID, wolf.getUuid());
            wolf.discard();
        }

        ManaUtils.gainPlayerMana(player, sumLevel);

        int soulLockTicks = points * 20;
        int absorptionLevel = points / 4;
        int strengthLevel = points / 6;
        int saturationTick = points * 20;

        if (soulLockTicks > 0) {
            player.removeStatusEffect(Init_Effect.SOUL_LOCK);
            player.addStatusEffect(new StatusEffectInstance(Init_Effect.SOUL_LOCK, soulLockTicks, 0, false, true));
        }
        if (absorptionLevel > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, absorptionLevel - 1, false, true));
        }
        if (strengthLevel > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, strengthLevel - 1, false, true));
        }
        if (saturationTick > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, saturationTick, 0, false, true));
        }

        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, player.getSoundCategory(), 0.5f, 1.5f);
        world.spawnParticles(player, ParticleTypes.SOUL_FIRE_FLAME, true, player.getX(), player.getY() + 1.0d, player.getZ(), 30, 1.0d, 1.0d, 1.0d, 0.1d);

        ActionFactory<Entity>.Instance ownerAction = data.get("owner_action");
        if (ownerAction != null) {
            ownerAction.accept(owner);
        }
    }

    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<>(
                xu_mod.SSCXuAddon.SSCXuAddon.identifier("wolf_merge"),
                new SerializableData()
                        .add("owner_action", ApoliDataTypes.ENTITY_ACTION, null),
                (data, entity) -> merge(data, entity)
        ));
    }
}