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
    public static final ModConfigSpec.DoubleValue MOTION_SLOW_FACTOR;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("sandevistan");
        DURATION_TICKS = builder.comment("How long the Sandevistan effect lasts, in ticks (20 ticks = 1 second).")
                .defineInRange("durationTicks", 200, 20, 12000);
        COOLDOWN_TICKS = builder.comment("How long the player must wait between activations, in ticks.")
                .defineInRange("cooldownTicks", 600, 0, 12000);
        SLOWNESS_AMPLIFIER = builder.comment("Slowness amplifier applied to other entities (3 = noticeably slow but still moving).")
                .defineInRange("slownessAmplifier", 3, 0, 255);
        SELF_SPEED_AMPLIFIER = builder.comment("Speed amplifier applied to the user while active (-1 disables the speed boost).")
                .defineInRange("selfSpeedAmplifier", 1, -1, 10);
        SLOW_RADIUS = builder.comment("Radius in blocks around the user. 0 means the whole world (every loaded entity).")
                .defineInRange("slowRadius", 0, 0, 1024);
        MOTION_SLOW_FACTOR = builder.comment("How much other entities' motion is scaled each tick (1.0 = normal, 0.4 = strongly slowed).")
                .defineInRange("motionSlowFactor", 0.4, 0.0, 1.0);
        builder.pop();

        SPEC = builder.build();
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
}
