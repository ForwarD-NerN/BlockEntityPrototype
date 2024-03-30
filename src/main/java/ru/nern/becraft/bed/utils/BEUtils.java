package ru.nern.becraft.bed.utils;

import com.badlogic.gdx.Gdx;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.blocks.BlockState;
import ru.nern.becraft.bed.BlockEntityRenderDispatcher;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.client.SimpleBlockEntityScreen;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;
import ru.nern.becraft.bed.api.internal.RegionBEAccess;

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
                BEUtils.removeFromRegionRemovalList(newRegion, newPosition);
                ((ChunkBEAccess)newChunk).getBlockEntities().put(newPosition, blockEntity);

                if(moveBlock) {
                    BlockState state = oldChunk.getBlockState(oldPosition.localX(), oldPosition.localY(), oldPosition.localZ());
                    oldChunk.setBlockState(replaceTo, oldPosition.localX(), oldPosition.localY(), oldPosition.localZ());
                    newChunk.setBlockState(state, newPosition.localX(), newPosition.localY(), newPosition.localZ());
                }

                if(blockEntity.wasSavedAtLeastOnce())
                    BEUtils.addToRegionRemovalList(oldChunk.region, oldPosition);

                blockEntity.setBlockPos(newPosition);
                newChunk.flagTouchingChunksForRemeshing(zone, newPosition.localX(), newPosition.localY(), newPosition.localZ(), true);
                oldChunk.flagTouchingChunksForRemeshing(zone, oldPosition.localX(), oldPosition.localY(), oldPosition.localZ(), true);
                GameSingletons.meshGenThread.requestImmediateResorting();

                return true;
            }
        }

        return false;
    }



    public static void addToRegionRemovalList(Region region, BlockPosition position) {
        ((RegionBEAccess)region).addRemovedBEPosition(position);
    }

    public static void removeFromRegionRemovalList(Region region, BlockPosition position) {
        ((RegionBEAccess)region).removeDeletedBEPosition(position);
    }
}
