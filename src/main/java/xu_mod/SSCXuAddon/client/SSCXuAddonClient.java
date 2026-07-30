package xu_mod.SSCXuAddon.client;

import io.github.apace100.apoli.ApoliClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import org.lwjgl.glfw.GLFW;
import xu_mod.SSCXuAddon.init.Client.Init;

public class SSCXuAddonClient implements ClientModInitializer {

    public static KeyBinding useActiveSkill3PowerKeybind;
    public static KeyBinding useActiveSkill4PowerKeybind;
    public static KeyBinding useActiveSkill5PowerKeybind;
    public static KeyBinding useActiveSkill6PowerKeybind;

    @Override
    public void onInitializeClient() {
        Init.init();

        // 由于SSC在1.10.0才加入额外的4个按键 先在拓展里整一套临时的
        // 至于按键ID 我作为SSC的主程 就当是给SSC的动态Patch了
        useActiveSkill3PowerKeybind = new KeyBinding("key.shape-shifter-curse.active_skill_3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + ShapeShifterCurseFabric.MOD_ID);
        useActiveSkill4PowerKeybind = new KeyBinding("key.shape-shifter-curse.active_skill_4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + ShapeShifterCurseFabric.MOD_ID);
        useActiveSkill5PowerKeybind = new KeyBinding("key.shape-shifter-curse.active_skill_5", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + ShapeShifterCurseFabric.MOD_ID);
        useActiveSkill6PowerKeybind = new KeyBinding("key.shape-shifter-curse.active_skill_6", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category." + ShapeShifterCurseFabric.MOD_ID);
        ApoliClient.registerPowerKeybinding("key.shape-shifter-curse.active_skill_3", useActiveSkill3PowerKeybind);
        ApoliClient.registerPowerKeybinding("key.shape-shifter-curse.active_skill_4", useActiveSkill4PowerKeybind);
        ApoliClient.registerPowerKeybinding("key.shape-shifter-curse.active_skill_5", useActiveSkill5PowerKeybind);
        ApoliClient.registerPowerKeybinding("key.shape-shifter-curse.active_skill_6", useActiveSkill6PowerKeybind);
        KeyBindingHelper.registerKeyBinding(useActiveSkill3PowerKeybind);
        KeyBindingHelper.registerKeyBinding(useActiveSkill4PowerKeybind);
        KeyBindingHelper.registerKeyBinding(useActiveSkill5PowerKeybind);
        KeyBindingHelper.registerKeyBinding(useActiveSkill6PowerKeybind);
    }
}