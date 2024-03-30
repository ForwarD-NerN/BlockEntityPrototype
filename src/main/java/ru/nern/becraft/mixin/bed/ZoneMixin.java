package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.ZoneBEAccess;

import java.util.HashSet;
import java.util.Set;

@Mixin(Zone.class)
public abstract class ZoneMixin implements ZoneBEAccess {
    @Unique private final Set<BlockEntity> loadedBlockEntities = new HashSet<>();
    @Override
    public Set<BlockEntity> getLoadedBlockEntities() {
        return this.loadedBlockEntities;
    }
}
