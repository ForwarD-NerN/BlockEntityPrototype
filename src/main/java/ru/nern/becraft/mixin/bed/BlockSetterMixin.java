package ru.nern.becraft.mixin.bed;

import com.badlogic.gdx.utils.Queue;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.BlockSetter;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.utils.BEUtils;

@Mixin(BlockSetter.class)
public class BlockSetterMixin {

    @Inject(method = "replaceBlock", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/rendering/IWorldRenderingMeshGenThread;requestImmediateResorting()V", shift = At.Shift.AFTER))
    private static void removeBlockEntityOnReplace(Zone zone, BlockState targetBlockState, BlockPosition blockPos, Queue<BlockPosition> tmpQueue, CallbackInfo ci, @Local(ordinal = 1) BlockState oldBlockState) {
        if(oldBlockState.getBlock() != Block.AIR && oldBlockState.getBlock() != targetBlockState.getBlock()) {
            BEUtils.removeBlockEntity(blockPos);
        }
    }
}
