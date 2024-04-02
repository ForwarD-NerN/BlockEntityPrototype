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
import ru.nern.becraft.bed.BlockEntitySaveHandler;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.utils.BEUtils;

import java.io.File;

@Mixin(ChunkLoader.class)
public class ChunkLoaderMixin {

    //First, we read the .bed file that corresponds to the region that the game loads, get the compound, and store it.
    @Inject(method = "readChunkColumn", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/Zone;getFullSaveFolder()Ljava/lang/String;", shift = At.Shift.AFTER))
    private static void setupBEReading(Zone zone, ChunkColumn cc, CallbackInfo ci,
                                       @Local(ordinal = 0) int rx, @Local(ordinal = 1) int ry, @Local(ordinal = 2) int rz,
                                       @Share("bedCompound") LocalRef<CompoundTag> compound) {
        String bedFileName = "bed_" + rx + "_" +ry + "_" + rz + ".bed";
        File bedFile = new File(zone.getFullSaveFolder() + "/zones/base/moon/bed/" + bedFileName);
        compound.set(BlockEntitySaveHandler.readBED(bedFile));
    }

    //Then we're iterating through the compound and loading the block entities one by one.
    @Inject(method = "readChunkColumn", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/worldgen/ChunkColumn;addChunk(Lfinalforeach/cosmicreach/savelib/ISavedChunk;)V"))
    private static void instantiateBEs(Zone zone, ChunkColumn cc, CallbackInfo ci,
                                       @Local(ordinal = 0) Chunk chunk, @Local(ordinal = 0) Region region, @Share("bedCompound") LocalRef<CompoundTag> compound) {
        CompoundTag tag = compound.get();
        if(tag != null) {
            try {
                Tag<?> compoundTag = tag.get("chunk_" + chunk.chunkX + "_" + chunk.chunkY + "_" + chunk.chunkZ);
                if(!(compoundTag instanceof ListTag<?>)) return;

                ListTag<CompoundTag> blockEntityList =
                        ((ListTag<?>) compoundTag).asCompoundTagList();

                if(blockEntityList != null) {
                    //Getting the accessible instance of the block entity registry
                    AccessableRegistry<BlockEntityType<?>> registry = (AccessableRegistry<BlockEntityType<?>>) BlockEntityRegistries.BLOCK_ENTITIES;
                    blockEntityList.forEach(beCompound -> {
                        Identifier id = Identifier.fromString(beCompound.getString("id"));

                        //Checking if the registry contains the block entity id from nbt
                        if(registry.contains(id)) {
                            BlockEntityType<?> type = registry.get(id);

                            //Converting the global coordinates of our block entity to the local chunk coordinates
                            int x = beCompound.getInt("x");
                            int y = beCompound.getInt("y");
                            int z = beCompound.getInt("z");

                            int cx = Math.floorDiv(x, 16);
                            int cy = Math.floorDiv(y, 16);
                            int cz = Math.floorDiv(z, 16);

                            x -= 16 * cx;
                            y -= 16 * cy;
                            z -= 16 * cz;

                            //Checking if a block entity supports the block at given coordinates.
                            //We can't use getBlockState().getBlock() here, as we can't get our block instance in any way.
                            if(type.isBlockSupported(chunk.getBlockState(x, y, z).getBlock().getStringId())) {
                                BlockEntity blockEntity = type.instantiate(zone, new BlockPosition(chunk, x, y, z));
                                try {
                                    blockEntity.readData(beCompound);
                                }catch (ClassCastException e) {
                                    //This can occur if the saved tag is of a different type than the block entity expects. For example, putFloat("value") and then getInt("value") will throw this exception.
                                    BECraft.LOGGER.error("ClastCastException occurred during nbt parsing of " +blockEntity.getClass().getSimpleName() + " block entity. The value type probably doesn't much the one in the writeData()");
                                }
                                blockEntity.setWasSaved(true);
                                BEUtils.addBlockEntity(zone, blockEntity);
                            }else{
                                BEUtils.addToRegionRemovalList(region, new BlockPosition(chunk, x, y, z));
                                BECraft.LOGGER.info("The block at pos " + beCompound.getInt("x") + " " + beCompound.getInt("y") + " " +beCompound.getInt("z") + " is not supported by the block entity type " +type.getId().toString() + ". The block entity would be removed.");
                            }

                        }else {
                            BECraft.LOGGER.debug("The block entity registry doesn't contain " +id + ". It was removed?");
                        }

                    });
                }
            }catch (Exception e) {
                BECraft.LOGGER.error("Something went wrong during nbt parsing: " + e);
            }

        }
    }
}
