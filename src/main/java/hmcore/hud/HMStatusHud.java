package hmcore.hud;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudElements;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class HMStatusHud extends HudElement {
    public static final HudElementInfo<HMStatusHud> INFO = new HudElementInfo<>(
        HudElements.GROUP,
        "hm-status",
        "HM Core status display.",
        HMStatusHud::new
    );

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
        .name("color")
        .description("HM Core text color.")
        .defaultValue(new SettingColor(255, 60, 60))
        .build()
    );

    public HMStatusHud() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        MinecraftClient mc = MinecraftClient.getInstance();

        String title = "HM CORE";
        String status = "ONLINE";

        String coordinates = mc.player != null
            ? String.format(
                "XYZ: %d %d %d",
                (int) mc.player.getX(),
                (int) mc.player.getY(),
                (int) mc.player.getZ()
            )
            : "XYZ: ---";

        String fps = "FPS: " + MinecraftClient.getInstance().getCurrentFps();

        renderer.text(title, x, y, color.get().getPacked(), true);
        renderer.text(status, x, y + 12, color.get().getPacked(), true);
        renderer.text(fps, x, y + 24, color.get().getPacked(), false);
        renderer.text(coordinates, x, y + 36, color.get().getPacked(), false);

        setSize(
            Math.max(
                renderer.textWidth(title, true),
                Math.max(
                    renderer.textWidth(coordinates, false),
                    renderer.textWidth(fps, false)
                )
            ),
            48
        );
    }
}
