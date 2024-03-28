package ru.nern.becraft.block;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import net.querz.nbt.tag.CompoundTag;
import ru.nern.becraft.BECraft;
import ru.nern.becraft.bed.BEUtils;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.block.client.CustomBlockEntityRenderer;
import ru.nern.becraft.block.client.CustomBlockEntityScreen;

public class CustomBlockEntity extends BlockEntity {
    private int count = 0;
    private boolean renderMonkey;
    public float scale = 0.2f;
    public int rotationSpeed = 50;

    public ModelInstance cubeModelInstance;
    public ModelInstance monkeyModelInstance;
    public ModelInstance currentModelInstance;

    public CustomBlockEntity(World world, BlockPosition blockPos) {
        super(BECraft.CUSTOM_BE_TYPE, world, blockPos);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        cubeModelInstance = new ModelInstance(CustomBlockEntityRenderer.MODEL);
        translate(cubeModelInstance);
        cubeModelInstance.transform.scl(scale);

        monkeyModelInstance = new ModelInstance(CustomBlockEntityRenderer.MONKEY_MODEL);
        translate(monkeyModelInstance);
        monkeyModelInstance.transform.scl(scale);

        setMonkeyMode(renderMonkey);
    }

    public void translate(ModelInstance model) {
        BlockPosition position = getBlockPos();
        model.transform.setToTranslation(position.getGlobalX()+0.5f, position.getGlobalY()+2, position.getGlobalZ()+0.5f);
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
        return compound;
    }

    @Override
    public void readData(CompoundTag compound) {
        super.readData(compound);
        this.count = compound.getInt("Counter");
        this.scale = compound.getFloat("Scale");
        this.rotationSpeed = compound.getInt("Rotation");
        this.renderMonkey = compound.getBoolean("MonkeyMode");
    }

    public void setMonkeyMode(boolean renderMonkey) {
        this.currentModelInstance = renderMonkey ? monkeyModelInstance : cubeModelInstance;
        this.renderMonkey = renderMonkey;
    }

    public boolean isMonkeyModeEnabled() {
        return renderMonkey;
    }

    /*
    @Override
    public void tick() {
        System.out.println("WE'RE TICKING!");
    }
     */
}
