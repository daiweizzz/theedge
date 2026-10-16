package com.theedge.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class TheEdgeConfig {
    private TheEdgeConfig() {
    }

    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue NORMAL_DIVISOR;
    public static final ModConfigSpec.IntValue OVERLOAD_DIVISOR;
    public static final ModConfigSpec.DoubleValue CAPACITY_SECONDS;
    public static final ModConfigSpec.DoubleValue RECHARGE_PER_SECOND;
    public static final ModConfigSpec.DoubleValue OVERLOAD_DRAIN_MULTIPLIER;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("sandevistan");
        NORMAL_DIVISOR = builder.comment("How much time is slowed in normal mode (8 = 1/8 speed).")
                .defineInRange("normalDivisor", 8, 1, 60);
        OVERLOAD_DIVISOR = builder.comment("How much time is slowed in overload mode (15 = 1/15 speed).")
                .defineInRange("overloadDivisor", 15, 1, 60);
        CAPACITY_SECONDS = builder.comment("Stamina capacity in real seconds.")
                .defineInRange("capacitySeconds", 10.0, 1.0, 3600.0);
        RECHARGE_PER_SECOND = builder.comment("How much stamina recovers per real second while inactive.")
                .defineInRange("rechargePerSecond", 0.5, 0.0, 100.0);
        OVERLOAD_DRAIN_MULTIPLIER = builder.comment("Stamina drain multiplier while in overload mode.")
                .defineInRange("overloadDrainMultiplier", 2.0, 1.0, 100.0);
        builder.pop();

        SPEC = builder.build();
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
}
