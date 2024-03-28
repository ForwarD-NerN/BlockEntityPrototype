package ru.nern.becraft.mixin.bed.save;

import com.badlogic.gdx.utils.Array;
import com.llamalad7.mixinextras.sugar.Local;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.chunks.Chunk;
import finalforeach.cosmicreach.world.worldgen.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.BlockEntitySaveHandler;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {

    //Saving the block entities before unloading far away chunks
    @Inject(method = "unloadFarAwayChunks", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/World;removeChunk(Lfinalforeach/cosmicreach/world/chunks/Chunk;)V"))
    private void saveBEsBeforeUnloading(World world, int chunkRadius, int playerChunkX, int playerChunkZ, Array<Chunk> tmpColChunks, CallbackInfo ci, @Local Chunk chunk) {
        if(!((ChunkBEAccess)chunk).hasBlockEntities()) {
            BlockEntitySaveHandler.saveBlockEntitiesInRegion(world, chunk.getRegion(world));
        }

    }
}
