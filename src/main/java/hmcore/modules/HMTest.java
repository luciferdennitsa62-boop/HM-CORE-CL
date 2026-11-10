/*
 * This file is part of the HM Core project.
 * The project is based on Meteor Client; retain the upstream license notices
 * in files derived from Meteor Client.
 */

package hmcore.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura;
import meteordevelopment.meteorclient.systems.modules.movement.Scaffold;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EntityType;

import java.util.List;
import java.util.Set;

public class HMTest extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> giveKit = sgGeneral.add(new BoolSetting.Builder()
        .name("give-pvp-kit")
        .description("Sends the PvP kit commands. Requires permission to use /give.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> enableKillAura = sgGeneral.add(new BoolSetting.Builder()
        .name("enable-auto-attack")
        .description("Enables Kill Aura, restricted to players under your crosshair.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> enableScaffold = sgGeneral.add(new BoolSetting.Builder()
        .name("enable-auto-bridge")
        .description("Enables Meteor Scaffold to place blocks below you.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Keybind> activateBind = sgGeneral.add(new KeybindSetting.Builder()
        .name("activate-pvp-mode")
        .description("Runs the PvP kit and enables the selected PvP modules.")
        .defaultValue(Keybind.none())
        .action(this::activatePvPMode)
        .build()
    );

    private static final List<String> KIT_COMMANDS = List.of(
        "/give @s minecraft:netherite_helmet[enchantments={levels:{\"minecraft:protection\":4,\"minecraft:unbreaking\":3,\"minecraft:mending\":1,\"minecraft:respiration\":3,\"minecraft:aqua_affinity\":1}}] 1",
        "/give @s minecraft:netherite_chestplate[enchantments={levels:{\"minecraft:protection\":4,\"minecraft:unbreaking\":3,\"minecraft:mending\":1}}] 1",
        "/give @s minecraft:netherite_leggings[enchantments={levels:{\"minecraft:protection\":4,\"minecraft:unbreaking\":3,\"minecraft:mending\":1,\"minecraft:swift_sneak\":3}}] 1",
        "/give @s minecraft:netherite_boots[enchantments={levels:{\"minecraft:protection\":4,\"minecraft:unbreaking\":3,\"minecraft:mending\":1,\"minecraft:feather_falling\":4,\"minecraft:depth_strider\":3,\"minecraft:soul_speed\":3}}] 1",
        "/give @s minecraft:netherite_sword[enchantments={levels:{\"minecraft:sharpness\":5,\"minecraft:unbreaking\":3,\"minecraft:mending\":1,\"minecraft:fire_aspect\":2,\"minecraft:looting\":3,\"minecraft:sweeping_edge\":3}}] 1",
        "/give @s minecraft:netherite_axe[enchantments={levels:{\"minecraft:sharpness\":5,\"minecraft:unbreaking\":3,\"minecraft:mending\":1,\"minecraft:efficiency\":5}}] 1",
        "/give @s minecraft:bow[enchantments={levels:{\"minecraft:power\":5,\"minecraft:punch\":2,\"minecraft:flame\":1,\"minecraft:infinity\":1,\"minecraft:unbreaking\":3}}] 1",
        "/give @s minecraft:crossbow[enchantments={levels:{\"minecraft:quick_charge\":3,\"minecraft:multishot\":1,\"minecraft:unbreaking\":3}}] 1",
        "/give @s minecraft:shield[enchantments={levels:{\"minecraft:unbreaking\":3,\"minecraft:mending\":1}}] 1",
        "/give @s minecraft:totem_of_undying 16",
        "/give @s minecraft:golden_apple 64",
        "/give @s minecraft:enchanted_golden_apple 64",
        "/give @s minecraft:ender_pearl 16",
        "/give @s minecraft:obsidian 64",
        "/give @s minecraft:cobblestone 64",
        "/give @s minecraft:arrow 64",
        "/give @s minecraft:golden_carrot 64",
        "/give @s minecraft:water_bucket 1",
        "/give @s minecraft:lava_bucket 1"
    );

    private int nextKitCommand;
    private boolean kitInProgress;
    private boolean activatedKillAura;
    private boolean activatedScaffold;

    public HMTest() {
        super(
            Categories.Misc,
            "hm-test",
            "HM Core PvP mode test: kit, player-only crosshair attacks and auto-bridging."
        );
    }

    @Override
    public void onActivate() {
        info("HM Core test module is active. Set the PvP mode bind in its settings.");
    }

    @Override
    public void onDeactivate() {
        kitInProgress = false;
        nextKitCommand = 0;

        if (activatedKillAura) {
            Modules.get().get(KillAura.class).disable();
            activatedKillAura = false;
        }

        if (activatedScaffold) {
            Modules.get().get(Scaffold.class).disable();
            activatedScaffold = false;
        }

        info("HM Core test module is inactive.");
    }

    private void activatePvPMode() {
        if (mc.player == null || mc.world == null) {
            warning("Join a world before activating PvP mode.");
            return;
        }

        if (kitInProgress) {
            warning("The PvP kit is already being sent.");
            return;
        }

        nextKitCommand = 0;
        kitInProgress = giveKit.get();

        info("Starting PvP mode...");

        if (!kitInProgress) {
            enablePvPModules();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!kitInProgress || mc.player == null || mc.world == null) return;

        if (nextKitCommand < KIT_COMMANDS.size()) {
            ChatUtils.sendPlayerMsg(KIT_COMMANDS.get(nextKitCommand++));
            return;
        }

        kitInProgress = false;
        info("PvP kit commands sent. Enabling PvP modules.");
        enablePvPModules();
    }

    @SuppressWarnings("unchecked")
    private void enablePvPModules() {
        if (enableKillAura.get()) {
            KillAura killAura = Modules.get().get(KillAura.class);

            Setting<Boolean> onlyOnLook = killAura.settings.get("only-on-look", Boolean.class);
            Setting<Set<EntityType<?>>> entities =
                (Setting<Set<EntityType<?>>>) killAura.settings.get("entities");

            if (onlyOnLook != null) onlyOnLook.set(true);
            if (entities != null) entities.set(Set.of(EntityType.PLAYER));

            activatedKillAura = !killAura.isActive();
            killAura.enable();
            info("Auto-attack enabled: players under your crosshair only.");
        }

        if (enableScaffold.get()) {
            Scaffold scaffold = Modules.get().get(Scaffold.class);
            activatedScaffold = !scaffold.isActive();
            scaffold.enable();
            info("AutoBridge enabled (Meteor Scaffold).");
        }
    }
}
