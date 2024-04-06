package ru.nern.becraft.bed.api;


import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;

public class BlockEntity {
    private final BlockEntityType<?> type;
    private BlockPosition blockPos;
    private final Zone zone;
    private boolean removed;
    private boolean initialized;
    private boolean wasSavedAtLeastOnce;

    public BlockEntity(BlockEntityType<?> type, Zone zone, BlockPosition blockPos) {
        this.blockPos = blockPos;
        this.zone = zone;
        this.type = type;
    }

    public Zone getZone() {
        return this.zone;
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
        compound.putInt("lx", blockPos.localX());
        compound.putInt("ly", blockPos.localY());
        compound.putInt("lz", blockPos.localZ());
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
