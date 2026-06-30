package xu_mod.SSCXuAddon.data.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.utils.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;

import java.util.List;

public class WindGem extends Item {
    public static final Identifier lootTableId = new Identifier("minecraft", "chests/end_city_treasure");

    public WindGem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 24;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            if (RegPlayerForms.OCELOT_3.isPlayerForm(player)) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.wind_gem.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.startTransform(player, Init_Form.OcelotJungle, null);
            }
            else if (Init_Form.OcelotJungle.isPlayerForm(player)) {
                ManaUtils.gainPlayerMana(player, ManaUtils.getPlayerMaxMana(player));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2400, 1, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 2400, 1, false, true));
                player.getItemCooldownManager().set(this, 600);
            }
            else {
                // 加速5min
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 6000, 1, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 6000, 1, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 6000, 0, false, true));
                player.getItemCooldownManager().set(this, 600);
            }
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.wind_gem.tooltip.1").formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("item.ssc_xu_addon.wind_gem.tooltip.2").formatted(Formatting.AQUA));
    }
}
