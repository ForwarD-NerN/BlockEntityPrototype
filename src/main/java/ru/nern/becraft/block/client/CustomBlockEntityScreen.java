package ru.nern.becraft.block.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.UISlider;
import ru.nern.becraft.bed.api.client.SimpleBlockEntityScreen;
import ru.nern.becraft.block.CustomBlockEntity;

public class CustomBlockEntityScreen extends SimpleBlockEntityScreen<CustomBlockEntity> {

    public CustomBlockEntityScreen(CustomBlockEntity blockEntity) {
        super(blockEntity);
    }

    public void create() {
        super.create();
        UIElement monkeyMode = new UIElement(0.0F, -200.0F, 250.0F, 50.0F) {
            @Override
            public void onCreate() {
                super.onCreate();
                updateText();
            }

            public void onClick() {
                super.onClick();
                blockEntity.setMonkeyMode(!blockEntity.isMonkeyModeEnabled());
                updateText();
            }

            @Override
            public void updateText() {
                this.setText("Monkey Mode: " +blockEntity.isMonkeyModeEnabled());
            }
        };
        monkeyMode.show();
        this.uiObjects.add(monkeyMode);

        UISlider scaleSlider = new UISlider(0.01F, 1.5F, blockEntity.scale, 0F, -100.0F, 250.0F, 50.0F) {

            public void onCreate() {
                super.onCreate();
                updateText();
            }

            public void onMouseUp() {
                super.onMouseUp();
                updateText();
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return super.mouseMoved(screenX, screenY);
            }

            public void validate() {
                super.validate();
                updateText();
                blockEntity.scale = this.currentValue;

                blockEntity.translate(blockEntity.monkeyModelInstance);
                blockEntity.translate(blockEntity.cubeModelInstance);

                blockEntity.monkeyModelInstance.transform.scale(blockEntity.scale, blockEntity.scale, blockEntity.scale);
                blockEntity.cubeModelInstance.transform.scale(blockEntity.scale, blockEntity.scale, blockEntity.scale);
            }

            public void updateText() {
                setText("Scale: " + Math.round(this.currentValue * 100));
            }
        };
        scaleSlider.show();
        this.uiObjects.add(scaleSlider);

        UISlider rotationSlider = new UISlider(0F, 360F, blockEntity.rotationSpeed, 0F, 0.0F, 250.0F, 50.0F) {
            public void onCreate() {
                super.onCreate();
                updateText();
            }

            public void onMouseUp() {
                super.onMouseUp();
                updateText();
            }

            public void validate() {
                super.validate();
                updateText();
                blockEntity.rotationSpeed = (int) this.currentValue;
            }

            public void updateText() {
                setText("Rotation: " + Math.round(this.currentValue));
            }
        };
        rotationSlider.show();
        this.uiObjects.add(rotationSlider);

        UIElement catchMode = new UIElement(0.0F, 100.0F, 300.0F, 50.0F) {
            @Override
            public void onCreate() {
                super.onCreate();
                updateText();
            }

            public void onClick() {
                super.onClick();
                blockEntity.catchMode = !blockEntity.catchMode;
                updateText();
            }

            @Override
            public void updateText() {
                this.setText("Catch Me If You Can Mode: " +blockEntity.catchMode);
            }
        };
        catchMode.show();
        this.uiObjects.add(catchMode);
    }

    public void resize(int width, int height) {
        super.resize(width, height);
        IN_GAME.resize(width, height);
    }

    public void render(float partTick) {
        super.render(partTick);
        if (!this.firstFrame && Gdx.input.isKeyJustPressed(111))
            switchToGameState(IN_GAME);
        ScreenUtils.clear(0.1F, 0.1F, 0.2F, 1.0F, true);
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        IN_GAME.render(partTick);
        drawUIElements();
    }
}
