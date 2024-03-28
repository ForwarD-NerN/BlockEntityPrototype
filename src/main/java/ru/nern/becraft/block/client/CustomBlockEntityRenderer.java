package ru.nern.becraft.block.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import finalforeach.cosmicreach.GameAssetLoader;
import ru.nern.becraft.bed.api.client.BlockEntityRenderer;
import ru.nern.becraft.block.CustomBlockEntity;

public class CustomBlockEntityRenderer extends BlockEntityRenderer<CustomBlockEntity> {
    private final ModelBatch modelBatch;
    public static Model MODEL = new ModelBuilder().createBox(5f, 5f, 5f,
            new Material(ColorAttribute.createDiffuse(Color.BLUE)),
    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

    public static Model MONKEY_MODEL = new ObjLoader().loadModel(GameAssetLoader.loadAsset("becraft:model/monkey.obj"), true);


    public CustomBlockEntityRenderer() {
        this.modelBatch = new ModelBatch();
    }

    @Override
    public void render(CustomBlockEntity blockEntity, Camera camera) {
        modelBatch.begin(camera);
        modelBatch.render(blockEntity.currentModelInstance);

        blockEntity.currentModelInstance.transform.rotate(0,1,0, blockEntity.rotationSpeed*Gdx.graphics.getDeltaTime());
        modelBatch.end();
    }
}
