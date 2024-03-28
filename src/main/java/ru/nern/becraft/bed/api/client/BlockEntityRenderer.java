package ru.nern.becraft.bed.api.client;

import com.badlogic.gdx.graphics.Camera;
import ru.nern.becraft.bed.api.BlockEntity;

public abstract class BlockEntityRenderer<T extends BlockEntity> {
    public int getRenderDistance() {
        return 64;
    }

    public abstract void render(T blockEntity, Camera camera);
}
