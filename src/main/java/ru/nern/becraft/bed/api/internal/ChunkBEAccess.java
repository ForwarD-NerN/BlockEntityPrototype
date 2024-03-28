package ru.nern.becraft.bed.api.internal;

import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import ru.nern.becraft.bed.api.BlockEntity;

import java.util.Map;
import java.util.Set;

public interface ChunkBEAccess {
    /**
     * Returns the block entity at the specified position.
     */
    BlockEntity getBlockEntity(BlockPosition position);

    /**
     * Sets a block entity at the specified position.
     */
    void setBlockEntity(World world, BlockEntity blockEntity);

    /**
     * Removes the block entity at the specified position.
     */
    void removeBlockEntity(BlockPosition position);

    /**
     * Returns a list of all block entities in a chunk.
     */
    Map<BlockPosition, BlockEntity> getBlockEntities();

    /**
     * Returns true if there are any block entities in the chunk.
     */
    boolean hasBlockEntities();
}
