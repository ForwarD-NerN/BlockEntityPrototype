package ru.nern.becraft.bed.api;

import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import net.querz.nbt.tag.CompoundTag;

public class BlockEntity {
    private final BlockEntityType<?> type;
    private BlockPosition blockPos;
    private final World world;
    private boolean removed;
    private boolean initialized;
    private boolean wasSavedAtLeastOnce;

    public BlockEntity(BlockEntityType<?> type, World world, BlockPosition blockPos) {
        this.blockPos = blockPos;
        this.world = world;
        this.type = type;
    }

    public World getWorld() {
        return this.world;
    }

    public void setBlockPos(BlockPosition blockPos) {
        this.blockPos = blockPos;
    }
    public BlockPosition getBlockPos() {
        return this.blockPos;
    }

    public void setRemoved() {
        this.removed = true;
    }
    public boolean isRemoved() {
        return this.removed;
    }

    public void onLoad() {
        this.initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public CompoundTag writeData(CompoundTag compound) {
        compound.putString("id", type.getId().toString());
        compound.putInt("x", blockPos.getGlobalX());
        compound.putInt("y", blockPos.getGlobalY());
        compound.putInt("z", blockPos.getGlobalZ());
        return compound;
    }
    public void readData(CompoundTag compound) {}

    public BlockEntityType<?> getType() {
        return this.type;
    }
    public void tick() {}

    public void setWasSaved(boolean wasSavedAtLeastOnce) {
        this.wasSavedAtLeastOnce = wasSavedAtLeastOnce;
    }

    public boolean wasSavedAtLeastOnce() {
        return wasSavedAtLeastOnce;
    }
}
