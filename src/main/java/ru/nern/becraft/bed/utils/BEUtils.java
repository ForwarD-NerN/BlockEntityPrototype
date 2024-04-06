package ru.nern.becraft.bed.utils;

import com.badlogic.gdx.Gdx;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import ru.nern.becraft.bed.api.BEReference;
import ru.nern.becraft.bed.handlers.BlockEntityLoadHandler;
import ru.nern.becraft.bed.handlers.BlockEntityRenderDispatcher;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.client.SimpleBlockEntityScreen;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;
import ru.nern.becraft.bed.handlers.BlockEntitySaveHandler;

import java.util.Optional;

public class BEUtils {
    public static final BlockState AIR = Block.AIR.getDefaultBlockState();
    public static BlockEntityRenderDispatcher renderDispatcher = new BlockEntityRenderDispatcher();
    public static BlockEntity getBlockEntity(BlockPosition position) {
        return ((ChunkBEAccess)position.chunk()).getBlockEntity(position);
    }

    public static void addBlockEntity(Zone zone, BlockEntity blockEntity) {
        ((ChunkBEAccess)blockEntity.getBlockPos().chunk()).setBlockEntity(zone, blockEntity);
    }

    public static void removeBlockEntity(BlockPosition position) {
        ((ChunkBEAccess)position.chunk()).removeBlockEntity(position);
    }

    //TODO: is it better to initialize the screen in the constructor or to instantiate it every time?
    public static void switchToScreen(SimpleBlockEntityScreen<?> screen) {
        Gdx.input.setCursorCatched(false);
        GameState.switchToGameState(screen);
    }

    public static Optional<BEReference> getBEReference(Zone zone, BlockPosition position) {
        return getBEReference(zone, position.getGlobalX(), position.getGlobalY(), position.getGlobalZ());
    }

    //Gets a reference to BE containing id, block pos, compound and the block entity object (if the block entity is loaded)
    public static Optional<BEReference> getBEReference(Zone zone, int x, int y, int z) {
        Chunk chunk = zone.getChunkAtBlock(x, y, z);
        int cx = Math.floorDiv(x, 16);
        int cy = Math.floorDiv(y, 16);
        int cz = Math.floorDiv(z, 16);

        //If the block entity is loaded
        if(chunk != null) {
            BlockPosition position = new BlockPosition(chunk, x - (16 * cx), y - (16 * cy), z - (16 * cz));
            BlockEntity blockEntity = ((ChunkBEAccess)chunk).getBlockEntity(position);

            if(blockEntity != null && !blockEntity.isRemoved()) {
                return Optional.of(new BEReference(blockEntity, blockEntity.getZone(), SimpleBlockPosition.fromBlockPosition(position), blockEntity.writeData(new CompoundTag())));
            }
        //If it's not loaded
        }else{
            CompoundTag tag = BlockEntityLoadHandler.loadBECompound(zone, x, y, z);
            if(tag != null) {
                return Optional.of(new BEReference(null, zone, SimpleBlockPosition.fromNBT(cx, cy, cz, tag), tag));
            }

        }
        return Optional.empty();
    }


    /**
     * Moves block entity to a given position.
     * @param blockEntity - The block entity itself.
     * @param newPosition - Position to move to.
     * @param moveBlock - Whether the block should be moved with the block entity.
     * @return Returns true if the block entity movement was successful.
     */

    public static boolean moveBlockEntity(BlockEntity blockEntity, BlockPosition newPosition, boolean moveBlock) {
        return moveBlockEntity(blockEntity, newPosition, moveBlock, AIR);
    }

    public static boolean moveBlockEntity(BlockEntity blockEntity, BlockPosition newPosition, boolean moveBlock, BlockState replaceTo) {
        Zone zone = blockEntity.getZone();
        BlockPosition oldPosition = blockEntity.getBlockPos();

        Chunk oldChunk = oldPosition.chunk();
        Chunk newChunk = newPosition.chunk();

        if(oldChunk != null && newChunk != null) {
            Region newRegion = newChunk.region;
            ((ChunkBEAccess)oldChunk).getBlockEntities().remove(blockEntity.getBlockPos(), blockEntity);

            if(newRegion != null) {
                BlockEntitySaveHandler.removeFromRegionRemovalList(newRegion, newPosition);
                ((ChunkBEAccess)newChunk).getBlockEntities().put(newPosition, blockEntity);

                if(moveBlock) {
                    BlockState state = oldChunk.getBlockState(oldPosition.localX(), oldPosition.localY(), oldPosition.localZ());
                    oldChunk.setBlockState(replaceTo, oldPosition.localX(), oldPosition.localY(), oldPosition.localZ());
                    newChunk.setBlockState(state, newPosition.localX(), newPosition.localY(), newPosition.localZ());
                }

                if(blockEntity.wasSavedAtLeastOnce())
                    BlockEntitySaveHandler.addToRegionRemovalList(oldChunk.region, oldPosition);

                blockEntity.setBlockPos(newPosition);
                newChunk.flagTouchingChunksForRemeshing(zone, newPosition.localX(), newPosition.localY(), newPosition.localZ(), true);
                oldChunk.flagTouchingChunksForRemeshing(zone, oldPosition.localX(), oldPosition.localY(), oldPosition.localZ(), true);
                GameSingletons.meshGenThread.requestImmediateResorting();

                return true;
            }
        }

        return false;
    }
}
