package ru.nern.becraft.block;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import finalforeach.cosmicreach.audio.SoundManager;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.world.Zone;
import net.querz.nbt.tag.CompoundTag;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.utils.BEModUtils;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.block.client.CustomBlockEntityRenderer;
import ru.nern.becraft.block.client.CustomBlockEntityScreen;

import java.util.Random;

public class CustomBlockEntity extends BlockEntity {
    private int count = 0;
    private boolean renderMonkey;
    public boolean catchMode;
    public float scale = 0.2f;
    public int rotationSpeed = 50;

    public ModelInstance cubeModelInstance;
    public ModelInstance monkeyModelInstance;
    public ModelInstance currentModelInstance;
    private static final Random random = new Random();

    public CustomBlockEntity(Zone zone, BlockPosition blockPos) {
        super(BECraft.CUSTOM_BE_TYPE, zone, blockPos);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        cubeModelInstance = new ModelInstance(CustomBlockEntityRenderer.MODEL);
        translate(cubeModelInstance);

        monkeyModelInstance = new ModelInstance(CustomBlockEntityRenderer.MONKEY_MODEL);
        translate(monkeyModelInstance);

        setMonkeyMode(renderMonkey);
    }

    public void translate(ModelInstance model) {
        BlockPosition position = getBlockPos();
        model.transform.setToTranslation(position.getGlobalX()+0.5f, position.getGlobalY()+2, position.getGlobalZ()+0.5f);
        model.transform.scale(scale, scale, scale);
    }

    public void onInteract() {
        BEUtils.switchToScreen(new CustomBlockEntityScreen(this));
    }

    @Override
    public CompoundTag writeData(CompoundTag compound) {
        super.writeData(compound);
        compound.putInt("Counter", count);
        compound.putFloat("Scale", scale);
        compound.putInt("Rotation", rotationSpeed);
        compound.putBoolean("MonkeyMode", renderMonkey);
        compound.putBoolean("CatchMode", catchMode);
        return compound;
    }

    @Override
    public void readData(CompoundTag compound) {
        super.readData(compound);
        this.count = compound.getInt("Counter");
        this.scale = compound.getFloat("Scale");
        this.rotationSpeed = compound.getInt("Rotation");
        this.renderMonkey = compound.getBoolean("MonkeyMode");
        this.catchMode = compound.getBoolean("CatchMode");
    }

    public void setMonkeyMode(boolean renderMonkey) {
        this.currentModelInstance = renderMonkey ? monkeyModelInstance : cubeModelInstance;
        this.renderMonkey = renderMonkey;
    }

    public boolean isMonkeyModeEnabled() {
        return renderMonkey;
    }

    @Override
    public void tick() {
        if(catchMode) {
            Entity player = InGame.getLocalPlayer().getEntity();
            BlockPosition blockPos = getBlockPos();

            double distanceToPlayer = BEModUtils.distanceTo(blockPos, player.getPosition());
            if(distanceToPlayer < 10 && !player.isSneaking) {
                int newX = blockPos.getGlobalX() + random.nextInt(-30, 30);
                int newZ = blockPos.getGlobalZ() + random.nextInt(-30, 30);
                SoundManager.playSound(UIElement.onHoverSound, 40, -5);
                BEUtils.moveBlockEntity(this, BEModUtils.globalToPosition(getZone(), newX, 70, newZ), true);
            }
        }
    }

    @Override
    public void setBlockPos(BlockPosition blockPos) {
        super.setBlockPos(blockPos);
        translate(monkeyModelInstance);
        translate(cubeModelInstance);
    }
}
