package ru.nern.becraft.mixin.bed.save;

import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.handlers.BlockEntitySaveHandler;

@Mixin(ChunkSaver.class)
public class ChunkSaverMixin {

    @Inject(method = "saveWorld", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/io/ChunkSaver;saveZone(Lfinalforeach/cosmicreach/world/Zone;)V", shift = At.Shift.AFTER))
    private static void saveBEs(CallbackInfo ci, @Local Zone zone) {
        BlockEntitySaveHandler.saveAllBlockEntities(zone);
    }

}
