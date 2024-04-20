package ru.nern.becraft.bed.handlers;

import dev.crmodders.flux.registry.registries.AccessableRegistry;
import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.BlockEntityRegistries;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.bed.utils.DataFixerUtil;

import java.io.File;
import java.io.IOException;

public class BlockEntityLoadHandler {
    public static final int FORMAT_VERSION = 1;

    public static void loadChunkBED(Zone zone, Region region, Chunk chunk, CompoundTag rootTag, ListTag<CompoundTag> beTagList) {
        //The version of the BED format.
        int formatVersion = rootTag.getInt("version");

        //Getting the accessible instance of the block entity registry
        AccessableRegistry<BlockEntityType<?>> registry = BlockEntityRegistries.BLOCK_ENTITIES.access();
        beTagList.forEach(beTag -> {
            Identifier id = Identifier.fromString(beTag.getString("id"));

            //Checking if the registry contains the block entity id from nbt
            if(registry.contains(id)) {
                if(formatVersion < BlockEntityLoadHandler.FORMAT_VERSION) {
                    DataFixerUtil.applyFixes(rootTag, beTagList, beTag, formatVersion);
                }

                BlockEntityType<?> type = registry.get(id);

                int lx = beTag.getInt("lx");
                int ly = beTag.getInt("ly");
                int lz = beTag.getInt("lz");
                BlockPosition bePos = new BlockPosition(chunk, lx, ly, lz);


                //Checking if a block entity supports the block at given coordinates.
                //We can't use getBlockState().getBlock() here, as we can't get our block instance in any way.
                String blockId = chunk.getBlockState(lx, ly, lz).getBlock().getStringId();
                if(type.isBlockSupported(blockId)) {

                    BlockEntity blockEntity = type.instantiate(zone, bePos);
                    try {
                        blockEntity.readData(beTag);
                    }catch (ClassCastException e) {
                        //This can occur if the saved rootTag is of a different type than the block entity expects. For example, putFloat("value") and then getInt("value") will throw this exception.
                        BECraft.LOGGER.error("ClastCastException occurred during nbt parsing of " +blockEntity.getClass().getSimpleName() + " block entity. The value type probably doesn't much the one in the writeData()");
                    }
                    blockEntity.setWasSaved(true);
                    BEUtils.addBlockEntity(zone, blockEntity);
                }else{
                    BlockEntitySaveHandler.addToRegionRemovalList(region, bePos);
                    BECraft.LOGGER.info("The block at pos " + bePos.getGlobalX() + " " + bePos.getGlobalY() + " "
                            + bePos.getGlobalZ() + " is not supported by " + type.getId().toString() + ". Expected: ["
                            + String.join(",", type.getSupportedBlocks()) + "]. Got: " + blockId);
                }

            }else {
                BECraft.LOGGER.warn("The block entity registry doesn't contain " +id + ". It was removed?");
            }

        });
    }

    public static CompoundTag loadBECompound(Zone zone, int x, int y, int z) {
        int cx = Math.floorDiv(x, 16);
        int cy = Math.floorDiv(y, 16);
        int cz = Math.floorDiv(z, 16);

        int rx = Math.floorDiv(cx, 16);
        int ry = Math.floorDiv(cy, 16);
        int rz = Math.floorDiv(cz, 16);

        String bedFileName = "bed_" + rx + "_" +ry + "_" + rz + ".bed";
        File bedFile = new File(zone.getFullSaveFolder() + "/bed/" + bedFileName);

        CompoundTag rootTag = readBED(bedFile);

        String chunkPos = "chunk_" +cx + "_" + cy + "_" + cz;
        if(rootTag != null && rootTag.containsKey(chunkPos)) {
            Tag<?> chunkTag = rootTag.get(chunkPos);

            if(chunkTag instanceof ListTag<?>) {
                int lx = x - (16 * cx);
                int ly = y - (16 * cy);
                int lz = z - (16 * cz);

                ListTag<CompoundTag> beTagList = ((ListTag<CompoundTag>) chunkTag).asCompoundTagList();
                for (CompoundTag beTag : beTagList) {
                    if (beTag.getInt("lx") == lx && beTag.getInt("ly") == ly && beTag.getInt("lz") == lz) {
                        return beTag;
                    }
                }
            }

        }
        return null;
    }


    //Reads .bed file
    public static CompoundTag readBED(File bedFile) {
        if(bedFile.exists()) {
            try {
                return (CompoundTag) NBTUtil.read(bedFile).getTag();
            }catch (IOException e) {
                BECraft.LOGGER.error("Something went wrong while reading the BED: " + e);
            }
        }
        return null;
    }


}
