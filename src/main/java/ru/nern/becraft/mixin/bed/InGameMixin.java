package ru.nern.becraft.mixin.bed;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.esotericsoftware.asm.Opcodes;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.ZoneBEAccess;

import java.util.Iterator;

@Mixin(InGame.class)
public class InGameMixin {
    @Shadow public static World world;
    @Shadow private static PerspectiveCamera rawWorldCamera;


    @Shadow private static Player player;

    //Iterates through all loaded block entities and renders them if they're not removed and initialized.
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/world/BlockSelection;render(Lcom/badlogic/gdx/graphics/Camera;)V", shift = At.Shift.AFTER))
    private void renderBlockEntities(float partTick, CallbackInfo ci) {
        Iterator<BlockEntity> iterator = ((ZoneBEAccess)player.getZone(world)).getLoadedBlockEntities().iterator();

        while (iterator.hasNext()) {
            BlockEntity blockEntity = iterator.next();
            if(blockEntity.isInitialized() && !blockEntity.isRemoved()) {
                BEUtils.renderDispatcher.render(blockEntity, rawWorldCamera);
            }
        }
    }




}
