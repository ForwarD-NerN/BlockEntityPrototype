package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.WorldBEAccess;

import java.util.HashSet;
import java.util.Set;

@Mixin(World.class)
public abstract class WorldMixin implements WorldBEAccess {
    @Unique private final Set<BlockEntity> loadedBlockEntities = new HashSet<>();
    @Override
    public Set<BlockEntity> getLoadedBlockEntities() {
        return this.loadedBlockEntities;
    }
}
