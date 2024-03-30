package ru.nern.becraft.mixin.bed;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.becraft.bed.utils.BEUtils;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.internal.ChunkBEAccess;
import ru.nern.becraft.bed.api.internal.ZoneBEAccess;

import java.util.HashMap;
import java.util.Map;

@Mixin(Chunk.class)
public class ChunkMixin implements ChunkBEAccess {
    @Shadow public Region region;
    @Unique
    private final Map<BlockPosition, BlockEntity> blockEntityMap = new HashMap<>();

    @Override
    public BlockEntity getBlockEntity(BlockPosition position) {
        return blockEntityMap.get(position);
    }

    @Override
    public void setBlockEntity(Zone zone, BlockEntity blockEntity) {
        ((ZoneBEAccess)zone).getLoadedBlockEntities().add(blockEntity);

        if(region != null)
            BEUtils.removeFromRegionRemovalList(region, blockEntity.getBlockPos());

        blockEntityMap.put(blockEntity.getBlockPos(), blockEntity);
    }

    @Override
    public void removeBlockEntity(BlockPosition position) {
        BlockEntity blockEntity = blockEntityMap.get(position);
        if(blockEntity != null) {
            blockEntity.setRemoved();
            blockEntityMap.remove(position);
            if(blockEntity.wasSavedAtLeastOnce())  {
                BEUtils.addToRegionRemovalList(region, position);
            }
        }
    }
    @Override
    public Map<BlockPosition, BlockEntity> getBlockEntities() {
        return blockEntityMap;
    }

    @Override
    public boolean hasBlockEntities() {
        return !blockEntityMap.isEmpty();
    }

    //Marks all the block entities in the chunk removed, when it's unloaded.
    @Inject(method = "dispose", at = @At("TAIL"))
    private void onUnload(CallbackInfo ci) {
        blockEntityMap.forEach((position, blockEntity) -> blockEntity.setRemoved());
        blockEntityMap.clear();
    }
}
