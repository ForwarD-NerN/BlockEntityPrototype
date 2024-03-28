package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.chunks.Region;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.nern.becraft.bed.api.internal.RegionBEAccess;

import java.util.HashSet;
import java.util.Set;

@Mixin(Region.class)
public class RegionMixin implements RegionBEAccess {
    //This field stores positions of all the block entities that were removed in a region.
    @Unique
    private final Set<BlockPosition> removedPositions = new HashSet<>();

    @Override
    public Set<BlockPosition> getRemovedBEPositions() {
        return removedPositions;
    }

    @Override
    public void addRemovedBEPosition(BlockPosition position) {
        removedPositions.add(position);
    }

    @Override
    public void removeDeletedBEPosition(BlockPosition position) {
        removedPositions.remove(position);
    }
}
