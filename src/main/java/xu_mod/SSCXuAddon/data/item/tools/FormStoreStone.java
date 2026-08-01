package xu_mod.SSCXuAddon.data.item.tools;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.data.CodexData;
import net.onixary.shapeShifterCurseFabric.networking.ModPacketsS2CServer;
import net.onixary.shapeShifterCurseFabric.player_form.IForm;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.utils.FormUtils;
import net.onixary.shapeShifterCurseFabric.player_form.utils.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;

import java.util.List;

// 群里提的需求 SSC由于平衡不能做这个功能(主要我认为没有终极材料 没法做终极物品)
// 有一个拓展也做了这个功能 由于神奇的逻辑(头一次看用mcfunction做Mod的 怪不得不兼容拓展) 不过从某种方面来说 那个Mod的版本兼容性极高 我做的拓展也不能在我重构部分系统下能兼容

public class FormStoreStone extends Item {
    public FormStoreStone(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 36;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity player) {
            IForm form = FormUtils.getPlayerForm(player);
            IForm storedForm = getStoredForm(stack);
            if (Init_Form.NoFormStoreStone.hasFlag(form)) {
                return stack;
            }
            if (storedForm != null && RegPlayerForms.ORIGINAL_SHIFTER.isPlayerForm(player)) {
                TransformManager.startTransform(player, storedForm, null);
                setStoredForm(stack, form);
            } else if (storedForm == null && !RegPlayerForms.ORIGINAL_SHIFTER.isPlayerForm(player)) {
                TransformManager.startTransform(player, RegPlayerForms.ORIGINAL_SHIFTER, null);
                setStoredForm(stack, form);
            } else if (storedForm != null && RegPlayerForms.ORIGINAL_SHIFTER.isPlayerForm(player)) {
                TransformManager.startTransform(player, storedForm, null);
                setStoredForm(stack, null);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.PLAYERS, 1.0F, 1.0F);
            player.getItemCooldownManager().set(this, 200);  // 10s冷却
        }
        return stack;
    }

    public static @Nullable IForm getStoredFormReal(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("form")) {
            Identifier formID = Identifier.tryParse(nbt.get("form").asString());
            if (formID == null) {
                return null;
            }
            return RegPlayerForms.getPlayerForm(formID);
        }
        return null;
    }

    public static @Nullable IForm getStoredForm(ItemStack stack) {
        IForm form = getStoredFormReal(stack);
        if (form != null && (Init_Form.NoFormStoreStone.hasFlag(form) || RegPlayerForms.ORIGINAL_SHIFTER.isEquals(form))) {
            return null;
        }
        return form;
    }

    public static void setStoredForm(ItemStack stack, @Nullable IForm form) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (form != null) {
            nbt.putString("form", form.getFormID().toString());
        } else {
            nbt.remove("form");
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.form_store_stone.tooltip.1").formatted(Formatting.YELLOW));
        IForm form = getStoredForm(stack);
        tooltip.add(Text.translatable("item.ssc_xu_addon.form_store_stone.tooltip.2", form == null ? "EMPTY" : form.getContentText(CodexData.ContentType.NAME).getString()).formatted(Formatting.YELLOW));
    }
}
