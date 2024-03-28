package ru.nern.becraft.bed;

import dev.crmodders.flux.registry.registries.FreezingRegistry;
import ru.nern.becraft.bed.api.BlockEntityType;

public class BlockEntityRegistries {
    public static FreezingRegistry<BlockEntityType<?>> BLOCK_ENTITIES = FreezingRegistry.create();
}
