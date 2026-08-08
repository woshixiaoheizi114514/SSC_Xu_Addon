package xu_mod.SSCXuAddon.data.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.onixary.shapeShifterCurseFabric.player_form.IForm;
import net.onixary.shapeShifterCurseFabric.player_form.utils.FormUtils;
import xu_mod.SSCXuAddon.utils.Utils;

public class ShapeKiller extends Enchantment {
    public ShapeKiller() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 5 + (level - 1) * 8;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 20;
    }

    public int getMaxLevel() {
        return 5;
    }

    public float getAttackDamage(int level, EntityGroup group) {
        // 原版依靠 EntityGroup 区分 还是挂载到 onTargetDamaged 吧
        return 0.0f;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.isAcceptableItem(stack);
    }

    public static float getEntityDamageModifier(Entity target) {
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

    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        float DamageModifier = getEntityDamageModifier(target);
        if (DamageModifier > 0.0f && target instanceof LivingEntity livingEntity) {
            float ldt = livingEntity.lastDamageTaken;
            int tur = livingEntity.timeUntilRegen;
            livingEntity.timeUntilRegen = 0;
            livingEntity.lastDamageTaken = 0.0f;
            livingEntity.damage(target.getDamageSources().magic(), level * DamageModifier);
            livingEntity.lastDamageTaken = ldt;
            livingEntity.timeUntilRegen = tur;
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (int)(level * DamageModifier * 8), 3));
        }
    }
}
