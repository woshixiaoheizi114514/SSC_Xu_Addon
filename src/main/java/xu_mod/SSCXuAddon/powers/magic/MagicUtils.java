package xu_mod.SSCXuAddon.powers.magic;

// Apoli的能力引擎处理动态参数的能力不太方便 还是new一个新机制吧

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.SSCXuAddon;

import java.util.HashMap;
import java.util.function.Consumer;

public class MagicUtils {
    public static final HashMap<Identifier, IMagic> magicRegistry = new HashMap<>();

    public static void registerMagic(Identifier id, IMagic magic) {
        magicRegistry.put(id, magic);
    }

    public static @Nullable IMagic getMagic(Identifier id) {
        return magicRegistry.get(id);
    }

    public static void InvokeMagic(Entity entity, @Nullable Identifier id, boolean force) {
        if (!(entity instanceof PlayerEntity player) || id == null) {
            return;
        }
        IMagic magic = magicRegistry.get(id);
        if (magic != null && (magic.canUse(player) || force)) {
            magic.use(player);
        }
    }

    public static boolean canUseMagic(Entity entity, @Nullable Identifier id) {
        if (!(entity instanceof PlayerEntity player) || id == null) {
            return false;
        }
        IMagic magic = magicRegistry.get(id);
        if (magic != null) {
            return magic.canUse(player);
        }
        return false;
    }

    public static void registerAction(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("invoke_magic"),
                new SerializableData()
                        .add("magic", SerializableDataTypes.IDENTIFIER)
                        .add("force", SerializableDataTypes.BOOLEAN, false),
                (data, entity) -> InvokeMagic(entity, data.get("magic"), data.getBoolean("force"))
        ));
    }

    public static void registerConditions(Consumer<ConditionFactory<Entity>> ConditionRegister) {
        ConditionRegister.accept(new ConditionFactory<>(
                SSCXuAddon.identifier("can_use_magic"),
                new SerializableData()
                        .add("magic", SerializableDataTypes.IDENTIFIER),
                (data, entity) -> canUseMagic((PlayerEntity) entity, data.get("magic"))
        ));
    }
}
