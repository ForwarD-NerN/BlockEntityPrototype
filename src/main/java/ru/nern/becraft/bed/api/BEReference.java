package ru.nern.becraft.bed.api;

import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.Nullable;
import ru.nern.becraft.bed.handlers.BlockEntitySaveHandler;
import ru.nern.becraft.bed.utils.SimpleBlockPosition;

public class BEReference {
    private final Identifier type;
    private final Zone zone;
    private final BlockEntity blockEntity;
    private final SimpleBlockPosition blockPos;
    private final CompoundTag compound;


    public BEReference(BlockEntity blockEntity, Zone zone, SimpleBlockPosition blockPos, CompoundTag compound) {
        this.type = Identifier.fromString(compound.getString("id"));
        this.zone = zone;
        this.blockEntity = blockEntity;
        this.blockPos = blockPos;
        this.compound = compound;
    }

    public Identifier getType() {
        return type;
    }

    //Is null if the block entity is not loaded
    @Nullable
    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public boolean isLoaded() {
        return blockEntity != null;
    }

    public SimpleBlockPosition getBlockPos() {
        return blockPos;
    }

    public CompoundTag getCompound() {
        return compound;
    }

    public Zone getZone() {
        return zone;
    }

    //If the block entity is loaded, it initialize readData(); if not, it saves the edited data to the drive
    public void applyCompoundChanges() {
        if(isLoaded()) {
            getBlockEntity().readData(compound);
        }else {
            BlockEntitySaveHandler.saveReference(this);
        }
    }


    @Override
    public String toString() {
        return type + "[" + blockPos.toString() + ", loaded=" +isLoaded()+ "] Compound: " + compound.toString();
    }
}
