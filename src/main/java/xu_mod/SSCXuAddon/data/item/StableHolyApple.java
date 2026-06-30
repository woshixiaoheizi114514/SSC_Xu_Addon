package xu_mod.SSCXuAddon.data.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.utils.TransformManager;

public class StableHolyApple extends Item {
    public StableHolyApple(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack FinalStack = super.finishUsing(stack, world, user);
        if (world.isClient) {
            return FinalStack;
        }
        if (user instanceof PlayerEntity player) {
            if (RegPlayerForms.ORIGINAL_BEFORE_ENABLE.isPlayerForm(player)) {
                return FinalStack;
            }
            TransformManager.startTransform(player, RegPlayerForms.ORIGINAL_SHIFTER, null);
            player.sendMessage(Text.translatable("message.ssc_xu_addon.item.stable_holy_apple.effect").formatted(Formatting.YELLOW), false);
        }
        return FinalStack;
    }

    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
