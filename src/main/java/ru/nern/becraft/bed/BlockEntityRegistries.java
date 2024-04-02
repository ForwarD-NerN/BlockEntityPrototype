package ru.nern.becraft.bed;

import dev.crmodders.flux.registry.registries.FreezingRegistry;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;

public class BlockEntityRegistries {
    public static final FreezingRegistry<BlockEntityType<?>> BLOCK_ENTITIES = FreezingRegistry.create();

    public static void register(BlockEntityType<? extends BlockEntity> type) {
        BLOCK_ENTITIES.register(type.getId(), type);
    }
}
