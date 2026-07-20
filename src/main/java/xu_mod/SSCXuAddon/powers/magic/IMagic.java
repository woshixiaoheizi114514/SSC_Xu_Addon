package xu_mod.SSCXuAddon.powers.magic;

import net.minecraft.entity.player.PlayerEntity;

public interface IMagic {
    boolean canUse(PlayerEntity player);

    void use(PlayerEntity player);
}
