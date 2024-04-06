package ru.nern.becraft.mixin.bed;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.io.ChunkLoader;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.handlers.BlockEntityLoadHandler;
import ru.nern.becraft.bed.handlers.BlockEntitySaveHandler;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.bed.utils.DataFixerUtil;

import java.io.File;

@Mixin(ChunkLoader.class)
public class ChunkLoaderMixin {

    //First, we read the .bed file that corresponds to the region that the game loads, get the compound, and store it.
    @Inject(method = "readChunkColumn", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/Zone;getFullSaveFolder()Ljava/lang/String;", shift = At.Shift.AFTER))
    private static void setupBEReading(Zone zone, ChunkColumn cc, CallbackInfo ci,
                                       @Local(ordinal = 0) int rx, @Local(ordinal = 1) int ry, @Local(ordinal = 2) int rz,
                                       @Share("bedCompound") LocalRef<CompoundTag> compound) {
        String bedFileName = "bed_" + rx + "_" +ry + "_" + rz + ".bed";
        File bedFile = new File(zone.getFullSaveFolder() + "/bed/" + bedFileName);
        compound.set(BlockEntityLoadHandler.readBED(bedFile));
    }

    //Then we're iterating through the compound and loading the block entities one by one.
    @Inject(method = "readChunkColumn", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/worldgen/ChunkColumn;addChunk(Lfinalforeach/cosmicreach/savelib/ISavedChunk;)V"))
    private static void instantiateBEs(Zone zone, ChunkColumn cc, CallbackInfo ci,
                                       @Local(ordinal = 0) Chunk chunk, @Local(ordinal = 0) Region region, @Share("bedCompound") LocalRef<CompoundTag> compound) {
        CompoundTag rootTag = compound.get();
        if(rootTag != null) {
            try {
                //Chunk nbt tag
                Tag<?> chunkTag = rootTag.get("chunk_" + chunk.chunkX + "_" + chunk.chunkY + "_" + chunk.chunkZ);
                if(!(chunkTag instanceof ListTag<?>)) return;

                ListTag<CompoundTag> beTagList =
                        ((ListTag<?>) chunkTag).asCompoundTagList();

                if(beTagList != null) {
                    BlockEntityLoadHandler.loadChunkBED(zone, region, chunk, rootTag, beTagList);
                }
            }catch (Exception e) {
                BECraft.LOGGER.error("Something went wrong during nbt parsing: " + e);
            }

        }
    }
}
