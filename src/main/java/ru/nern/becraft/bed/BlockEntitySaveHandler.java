package ru.nern.becraft.bed;

import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.chunks.Chunk;
import finalforeach.cosmicreach.world.chunks.Region;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;
import ru.nern.becraft.bed.api.internal.RegionBEAccess;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockEntitySaveHandler {
    private static final CompoundTag saveCompound = new CompoundTag();

    //Saves all block entities in the world
    public static void saveAllBlockEntities(World world) {
        try {
            BECraft.LOGGER.info("Started saving BED");
            for(Region region : world.regions.values()) {
                //Gdx doesn't like nested iterators, so we'll use old school loop
                for(int i = 0; i < region.getChunks().size; i++) {
                    fetchBED(region.getChunks().get(i));
                }
                saveBED(region, world.getFullSaveFolder());
            }
            BECraft.LOGGER.info("Finished saving BED");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Saves all block entities in the region
    public static void saveBlockEntitiesInRegion(World world, Region region) {
        try {
            for (Chunk chunk : region.getChunks()) {
                fetchBED(chunk);
            }
            saveBED(region, world.getFullSaveFolder());

        }catch (Exception e) {
            e.printStackTrace();
        }

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

    //Iterates through every block entity in a given chunk and saves their data to the saveCompound.
    private static void fetchBED(Chunk chunk) {
        Map<BlockPosition, BlockEntity> blockEntities = ((ChunkBEAccess)chunk).getBlockEntities();

        ListTag<CompoundTag> chunkBlockEntityRoot = new ListTag<>(CompoundTag.class);
        for (Map.Entry<BlockPosition, BlockEntity> entry : blockEntities.entrySet()) {
            entry.getValue().setWasSaved(true);
            chunkBlockEntityRoot.add(entry.getValue().writeData(new CompoundTag()));
        }

        if(chunkBlockEntityRoot.size() > 0) {
            saveCompound.put("chunk_"+chunk.chunkX+"_"+chunk.chunkY+"_"+chunk.chunkZ, chunkBlockEntityRoot);
        }
    }


    private static void saveBED(Region region, String worldFolderName) {
        try {
            if(saveCompound.size() > 0 || !((RegionBEAccess)region).getRemovedBEPositions().isEmpty()) {
                String bedFolderName = worldFolderName + "/zones/base/moon/bed";
                String bedFileName = bedFolderName + "/bed_" + region.regionX + "_" + region.regionY + "_" + region.regionZ + ".bed";
                (new File(bedFolderName)).mkdirs();
                File bedFile = new File(bedFileName);

                //Merging our save compound and compound from the drive to avoid overwriting
                mergeCompoundLists(saveCompound, readBED(bedFile));
                removeDeletedBlockEntities(region);
                NBTUtil.write(saveCompound, bedFile, true);

                saveCompound.clear();
            }
        }catch (IOException | NumberFormatException e) {
            BECraft.LOGGER.info("Something went wrong while saving the BED: " +e);
        }
    }

    //TODO: make method name less cursed
    private static void removeDeletedBlockEntities(Region region) {
        Set<BlockPosition> removedBlockEntities = ((RegionBEAccess)region).getRemovedBEPositions();

        for(BlockPosition position : removedBlockEntities) {
            Chunk chunk = position.chunk();
            String chunkCompoundId = "chunk_" + chunk.chunkX + "_" + chunk.chunkY + "_" + chunk.chunkZ;
            if(!saveCompound.containsKey(chunkCompoundId)) continue;

            ListTag<CompoundTag> list = saveCompound.getListTag(chunkCompoundId).asCompoundTagList();
            Iterator<CompoundTag> compoundIterator = list.iterator();

            while (compoundIterator.hasNext()) {
                CompoundTag tag = compoundIterator.next();

                if(tag.getInt("x") == position.getGlobalX() && tag.getInt("y") == position.getGlobalY() && tag.getInt("z") == position.getGlobalZ()) {
                    compoundIterator.remove();
                    break;
                }
            }
            if(list.size() == 0) {
                saveCompound.remove(chunkCompoundId);
            }
        }

        removedBlockEntities.clear();
    }

    public static void mergeCompoundLists(CompoundTag to, CompoundTag from) {
        if(from != null) {
            for(Map.Entry<String, Tag<?>> entry : from.entrySet()) {
                if(!to.containsKey(entry.getKey())) {
                   to.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
