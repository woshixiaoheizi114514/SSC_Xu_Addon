package xu_mod.SSCXuAddon.data.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.onixary.shapeShifterCurseFabric.player_form.IForm;
import net.onixary.shapeShifterCurseFabric.player_form.utils.FormUtils;
import xu_mod.SSCXuAddon.utils.Utils;

public class ShapeProtection extends Enchantment {
    public ShapeProtection() {
        super(Rarity.VERY_RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 10 + (level - 1) * 8;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 20;
    }

    public int getMaxLevel() {
        return 4;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack);
    }

    public static float getEntityProtectModifier(Entity target) {
        if (Utils.IsTransformativeMob(target)) {
            return 2.0f;
        }
        if (target instanceof PlayerEntity player) {
            IForm form = FormUtils.getPlayerForm(player);
            if (form.getFormTier() <= 0) {
                return 0.0f;
            }
            float base = FormUtils.SpecialForm.hasFlag(form) ? 1.0f : 0.5f;
            return Math.max(0.0f, base + form.getFormTier() * 0.5f);
        }
        return 0.0f;
    }


    public int getProtectionAmount(int level, DamageSource source) {
        Entity attacker = source.getAttacker();
        return Math.round(getEntityProtectModifier(attacker) * level);
    }
}
