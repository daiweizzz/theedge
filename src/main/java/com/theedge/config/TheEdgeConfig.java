package com.theedge.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class TheEdgeConfig {
    private TheEdgeConfig() {
    }

    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue DURATION_TICKS;
    public static final ModConfigSpec.IntValue COOLDOWN_TICKS;
    public static final ModConfigSpec.IntValue SLOWNESS_AMPLIFIER;
    public static final ModConfigSpec.IntValue SELF_SPEED_AMPLIFIER;
    public static final ModConfigSpec.IntValue SLOW_RADIUS;
    public static final ModConfigSpec.BooleanValue FREEZE_MOTION;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("sandevistan");
        DURATION_TICKS = builder.comment("How long the Sandevistan effect lasts, in ticks (20 ticks = 1 second).")
                .defineInRange("durationTicks", 200, 20, 12000);
        COOLDOWN_TICKS = builder.comment("How long the player must wait between activations, in ticks.")
                .defineInRange("cooldownTicks", 600, 0, 12000);
        SLOWNESS_AMPLIFIER = builder.comment("Slowness amplifier applied to other entities (6 or higher freezes most movement).")
                .defineInRange("slownessAmplifier", 6, 0, 255);
        SELF_SPEED_AMPLIFIER = builder.comment("Speed amplifier applied to the user while active (-1 disables the speed boost).")
                .defineInRange("selfSpeedAmplifier", 1, -1, 10);
        SLOW_RADIUS = builder.comment("Radius in blocks around the user. 0 means the whole world (every loaded entity).")
                .defineInRange("slowRadius", 0, 0, 1024);
        FREEZE_MOTION = builder.comment("When true, other entities' motion is frozen so they stop mid-air.")
                .define("freezeMotion", true);
        builder.pop();

        SPEC = builder.build();
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
}
