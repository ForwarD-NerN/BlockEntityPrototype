package ru.nern.becraft.bed.utils;

import finalforeach.cosmicreach.blocks.BlockPosition;
import net.querz.nbt.tag.CompoundTag;

public record SimpleBlockPosition(int globalX, int globalY, int globalZ, int localX, int localY, int localZ) {
    public static SimpleBlockPosition fromBlockPosition(BlockPosition position) {
        return new SimpleBlockPosition(position.getGlobalX(), position.getGlobalY(), position.getGlobalZ(), position.localX(), position.localY(), position.localZ());
    }

    public static SimpleBlockPosition fromNBT(int chunkX, int chunkY, int chunkZ, CompoundTag tag) {
        int localX = tag.getInt("lx");
        int localY = tag.getInt("ly");
        int localZ = tag.getInt("lz");

        return new SimpleBlockPosition((chunkX*16) + localX, (chunkY*16) + localY, (chunkZ*16) + localZ, localX, localY, localZ);
    }

    @Override
    public String toString() {
        return "position{" +
                "x=" + globalX +
                ", y=" + globalY +
                ", z=" + globalZ +
                ", localX=" + localX +
                ", localY=" + localY +
                ", localZ=" + localZ +
                '}';
    }
}
