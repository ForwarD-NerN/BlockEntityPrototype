package ru.nern.becraft.bed;

import com.badlogic.gdx.Gdx;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.chunks.Region;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.client.SimpleBlockEntityScreen;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;
import ru.nern.becraft.bed.api.internal.RegionBEAccess;

public class BEUtils {
    public static BlockEntityRenderDispatcher renderDispatcher = new BlockEntityRenderDispatcher();
    public static BlockEntity getBlockEntity(BlockPosition position) {
        return ((ChunkBEAccess)position.chunk()).getBlockEntity(position);
    }

    public static void addBlockEntity(World world, BlockEntity blockEntity) {
        ((ChunkBEAccess)blockEntity.getBlockPos().chunk()).setBlockEntity(world, blockEntity);
    }

    public static void removeBlockEntity(BlockPosition position) {
        ((ChunkBEAccess)position.chunk()).removeBlockEntity(position);
    }

    //TODO: is it better to initialize the screen in the constructor or to instantiate it every time?
    public static void switchToScreen(SimpleBlockEntityScreen<?> screen) {
        Gdx.input.setCursorCatched(false);
        GameState.switchToGameState(screen);
    }

    public static void addToRegionRemovalList(Region region, BlockPosition position) {
        ((RegionBEAccess)region).addRemovedBEPosition(position);
    }

    public static void removeFromRegionRemovalList(Region region, BlockPosition position) {
        ((RegionBEAccess)region).removeDeletedBEPosition(position);
    }
}
