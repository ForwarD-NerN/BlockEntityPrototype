package ru.nern.becraft.bed.handlers;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.api.BEReference;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;
import ru.nern.becraft.bed.api.internal.RegionBEAccess;
import ru.nern.becraft.bed.utils.SimpleBlockPosition;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class BlockEntitySaveHandler {
    private static final CompoundTag saveCompound = new CompoundTag();

    //Saves all block entities in the zone
    public static void saveAllBlockEntities(Zone zone) {
        try {
            BECraft.LOGGER.info("Started saving BED");
            for(Region region : zone.regions.values()) {
                //Gdx doesn't like nested iterators, so we'll use old school loop
                for(int i = 0; i < region.getChunks().size; i++) {
                    fetchBED(region.getChunks().get(i));
                }
                saveBED(region, zone.getFullSaveFolder());
            }
            BECraft.LOGGER.info("Finished saving BED");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Saves all block entities in the region
    public static void saveBlockEntitiesInRegion(Zone zone, Region region) {
        try {
            for (Chunk chunk : region.getChunks()) {
                fetchBED(chunk);
            }
            saveBED(region, zone.getFullSaveFolder());

        }catch (Exception e) {
            e.printStackTrace();
        }

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


    //TODO: CHANGE
    private static void saveBED(Region region, String zoneFolderName) {
        try {
            if(saveCompound.size() > 0 || !((RegionBEAccess)region).getRemovedBEPositions().isEmpty()) {
                String bedFolderName = zoneFolderName + "/bed";
                String bedFileName = bedFolderName + "/bed_" + region.regionX + "_" + region.regionY + "_" + region.regionZ + ".bed";
                (new File(bedFolderName)).mkdir();
                File bedFile = new File(bedFileName);

                //Merging our save compound and compound from the drive to avoid overwriting
                mergeCompoundLists(saveCompound, BlockEntityLoadHandler.readBED(bedFile));
                removeDeletedBlockEntities(region);

                saveCompound.putInt("version", BlockEntityLoadHandler.FORMAT_VERSION);
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

                if(tag.getInt("lx") == position.localX() && tag.getInt("ly") == position.localY() && tag.getInt("lz") == position.localZ()) {
                    BECraft.LOGGER.info("Removing block entity at pos " + position.getGlobalX() + " " +position.getGlobalY() + " " + position.getGlobalZ());
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

    //Saves the BEReference to the drive.
    public static void saveReference(BEReference reference) {
        if(reference.isLoaded()) return;

        SimpleBlockPosition position = reference.getBlockPos();
        int cx = Math.floorDiv(position.globalX(), 16);
        int cy = Math.floorDiv(position.globalY(), 16);
        int cz = Math.floorDiv(position.globalZ(), 16);

        int rx = Math.floorDiv(cx, 16);
        int ry = Math.floorDiv(cy, 16);
        int rz = Math.floorDiv(cz, 16);

        try {
            String bedFolderName = reference.getZone().getFullSaveFolder() + "/bed";
            String bedFileName = bedFolderName + "/bed_" + rx + "_" + ry + "_" + rz + ".bed";
            (new File(bedFolderName)).mkdir();

            File bedFile = new File(bedFileName);

            CompoundTag rootTag = BlockEntityLoadHandler.readBED(bedFile);
            if(rootTag == null) return;

            String chunkCompoundId = "chunk_" + cx + "_" + cy + "_" + cz;
            ListTag<CompoundTag> beTagList;

            if(rootTag.containsKey(chunkCompoundId)) {
                beTagList = rootTag.getListTag(chunkCompoundId).asCompoundTagList();
                Iterator<CompoundTag> iterator = beTagList.iterator();
                while (iterator.hasNext()) {
                    CompoundTag tag = iterator.next();
                    if(tag.getInt("lx") == position.localX() && tag.getInt("ly") == position.localY() && tag.getInt("lz") == position.localZ()) {
                        iterator.remove();
                        break;
                    }
                }
            }else {
                beTagList = new ListTag<>(CompoundTag.class);
            }
            beTagList.add(reference.getCompound());


            NBTUtil.write(rootTag, bedFile, true);
        }catch (IOException | NumberFormatException e) {
            BECraft.LOGGER.info("Something went wrong while saving the BE reference: " +e);
        }
    }

    public static void addToRegionRemovalList(Region region, BlockPosition position) {
        ((RegionBEAccess)region).addRemovedBEPosition(position);
    }

    public static void removeFromRegionRemovalList(Region region, BlockPosition position) {
        ((RegionBEAccess)region).removeDeletedBEPosition(position);
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
