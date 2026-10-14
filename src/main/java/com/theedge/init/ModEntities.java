package com.theedge.init;

import com.theedge.TheEdgeMod;
import com.theedge.entity.AfterimageEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    private ModEntities() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, TheEdgeMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<AfterimageEntity>> AFTERIMAGE =
            ENTITY_TYPES.register("afterimage", () -> EntityType.Builder
                    .<AfterimageEntity>of(AfterimageEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .noSave()
                    .noSummon()
                    .build(TheEdgeMod.MODID + ":afterimage"));
}
