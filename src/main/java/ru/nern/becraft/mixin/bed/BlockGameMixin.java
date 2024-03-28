package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.BlockGame;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.WorldBEAccess;

import java.util.Iterator;

@Mixin(BlockGame.class)
public class BlockGameMixin {

    @Inject(method = "create", at = @At(value = "TAIL"))
    private void onInit(CallbackInfo ci) {
        BlockEntityRegistries.BLOCK_ENTITIES.freeze();
        BECraft.initRenderers();
    }

    @Inject(method = "runTicks", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/blockevents/ScheduledTrigger;runScheduledTriggers()V", shift = At.Shift.AFTER))
    private void tickBlockEntities(CallbackInfo ci) {
        if(InGame.world != null) {
            Iterator<BlockEntity> iterator = ((WorldBEAccess)InGame.world).getLoadedBlockEntities().iterator();
            while (iterator.hasNext()) {
                BlockEntity blockEntity = iterator.next();

                if(InGame.currentGameState == GameState.IN_GAME) {
                    if(blockEntity.isRemoved()) {
                        iterator.remove();
                    }else{
                        if(!blockEntity.isInitialized()) blockEntity.onLoad();
                        blockEntity.tick();
                    }
                }
            }
        }
    }
}
