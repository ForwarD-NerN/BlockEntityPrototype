package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.ZoneBEAccess;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Mixin(Zone.class)
public abstract class ZoneMixin implements ZoneBEAccess {
    @Unique private final Set<BlockEntity> loadedBlockEntities = new HashSet<>();
    @Override
    public Set<BlockEntity> getLoadedBlockEntities() {
        return this.loadedBlockEntities;
    }

    @Inject(method = "runScheduledTriggers", at = @At(value = "TAIL"))
    private void tickBlockEntities(CallbackInfo ci) {
        Iterator<BlockEntity> iterator = getLoadedBlockEntities().iterator();
        while (iterator.hasNext()) {
            BlockEntity blockEntity = iterator.next();
            if (blockEntity.isRemoved()) {
                iterator.remove();
            } else {
                if (!blockEntity.isInitialized()) blockEntity.onLoad();
                blockEntity.tick();
            }
        }
    }
}
