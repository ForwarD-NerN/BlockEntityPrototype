package ru.nern.becraft.mixin.bed.save;

import finalforeach.cosmicreach.io.ChunkSaver;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.BlockEntitySaveHandler;

@Mixin(ChunkSaver.class)
public class ChunkSaverMixin {

    @Inject(method = "saveWorld", at = @At(value = "INVOKE",
            target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V", ordinal = 1))
    private static void saveBEs(World world, CallbackInfo ci) {
        BlockEntitySaveHandler.saveAllBlockEntities(world);
    }

}
