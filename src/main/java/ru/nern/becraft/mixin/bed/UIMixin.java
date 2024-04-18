package ru.nern.becraft.mixin.bed;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.nern.becraft.bed.api.internal.ZoneBEAccess;

@Mixin(UI.class)
public class UIMixin {
    //TODO: Update. FFE please make atleast GetIntValue public....
    @Inject(method = "drawDebugText", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/ui/FontRenderer;drawText(Lcom/badlogic/gdx/graphics/g2d/SpriteBatch;Lcom/badlogic/gdx/utils/viewport/Viewport;Ljava/lang/String;FF)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addBEDebugText(CallbackInfo ci, @Local(ordinal = 5) LocalRef<String> debugText) {
        debugText.set(debugText.get() + "\nLoaded Block Entity Amount: " + ((ZoneBEAccess)InGame.getLocalPlayer().getZone(InGame.world)).getLoadedBlockEntities().size());
    }
}
