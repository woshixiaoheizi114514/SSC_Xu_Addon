package xu_mod.SSCXuAddon.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class Init_Effect {
    public static final StatusEffect SOUL_LOCK = Registry.register(Registries.STATUS_EFFECT, SSCXuAddon.identifier("soul_lock"), new StatusEffect(StatusEffectCategory.BENEFICIAL, 0x7B68EE));

    public static void init() {
    }
}