package com.theedge.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AfterimageEntity extends Entity {
    private static final int LIFETIME = 12;

    private int age;

    public AfterimageEntity(EntityType<AfterimageEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        this.age++;
        if (this.age >= LIFETIME) {
            discard();
        }
    }

    public int getAge() {
        return this.age;
    }

    public int getLifetime() {
        return LIFETIME;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return false;
    }
}
