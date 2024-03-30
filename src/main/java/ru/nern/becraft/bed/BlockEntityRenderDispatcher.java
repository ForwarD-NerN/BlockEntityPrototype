package ru.nern.becraft.bed;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.blocks.BlockPosition;
import org.jetbrains.annotations.Nullable;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockEntityType;
import ru.nern.becraft.bed.api.client.BlockEntityRenderer;
import ru.nern.becraft.bed.utils.BEModUtils;
import ru.nern.becraft.bed.utils.BEUtils;

import java.util.HashMap;
import java.util.Map;

public class BlockEntityRenderDispatcher {
    private final Map<BlockEntityType<?>, BlockEntityRenderer<? extends BlockEntity>> renderers = new HashMap<>();

    @Nullable
    public <E extends BlockEntity> BlockEntityRenderer<E> get(E blockEntity) {
        return (BlockEntityRenderer<E>) renderers.get(blockEntity.getType());
    }

    public <E extends BlockEntity> void registerRender(BlockEntityType<E> type, BlockEntityRenderer<E> renderer) {
        renderers.put(type, renderer);
    }

    public <E extends BlockEntity> void render(E blockEntity, Camera camera) {
        BlockEntityRenderer<E> renderer = get(blockEntity);
        Vector3 position = InGame.getLocalPlayer().getEntity().getPosition();

        if(renderer != null && BEModUtils.distanceTo(blockEntity.getBlockPos(), position) < renderer.getRenderDistance()) {
            renderer.render(blockEntity, camera);
        }
    }
}
