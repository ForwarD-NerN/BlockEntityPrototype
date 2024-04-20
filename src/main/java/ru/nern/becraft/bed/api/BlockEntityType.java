package ru.nern.becraft.bed.api;

import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Zone;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockEntityType<T extends BlockEntity> {
    private final Identifier identifier;
    private final BlockEntityFactory<? extends T> factory;
    private final Set<String> supportedBlocks;

    public BlockEntityType(Identifier identifier, BlockEntityFactory<? extends T> factory, String... supportedBlocks) {
        this.identifier = identifier;
        this.factory = factory;
        this.supportedBlocks = new HashSet<>(List.of(supportedBlocks));
    }

    public Identifier getId() {
        return identifier;
    }

    public T instantiate(Zone zone, BlockPosition position) {
       return this.factory.create(zone, position);
    }

    public boolean isBlockSupported(String blockId) {
        return supportedBlocks.contains(blockId);
    }

    public void addSupportedBlock(String blockId) {
        supportedBlocks.add(blockId);
    }

    public Set<String> getSupportedBlocks() {
        return supportedBlocks;
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(Zone zone, BlockPosition position);
    }
}
