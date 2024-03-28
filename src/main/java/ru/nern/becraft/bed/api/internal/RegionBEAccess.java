package ru.nern.becraft.bed.api.internal;

import finalforeach.cosmicreach.world.BlockPosition;

import java.util.Set;

public interface RegionBEAccess {
    /**
     * Returns positions of all the block entities that were removed in a region.
     */
    Set<BlockPosition> getRemovedBEPositions();

    /**
     * Adds a block entity position to the list.
     */
    void addRemovedBEPosition(BlockPosition position);

    /**
     * Removes a block entity position from the list.
     */
    void removeDeletedBEPosition(BlockPosition position);
}
