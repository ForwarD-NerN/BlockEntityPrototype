package ru.nern.becraft.bed.api.internal;

import ru.nern.becraft.bed.api.BlockEntity;

import java.util.Set;

public interface ZoneBEAccess {
    /**
     * Returns the list of all loaded block entities
     */
    Set<BlockEntity> getLoadedBlockEntities();
}