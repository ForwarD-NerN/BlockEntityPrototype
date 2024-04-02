package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.BlockGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.BlockEntityRegistries;

@Mixin(BlockGame.class)
public class BlockGameMixin {

    @Inject(method = "create", at = @At(value = "TAIL"))
    private void onInit(CallbackInfo ci) {
        BlockEntityRegistries.BLOCK_ENTITIES.freeze();
        BECraft.initRenderers();
    }
}
