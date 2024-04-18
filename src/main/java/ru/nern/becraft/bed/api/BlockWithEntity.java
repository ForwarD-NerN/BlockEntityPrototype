package ru.nern.becraft.bed.api;

import dev.crmodders.flux.api.v5.block.IModBlock;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import ru.nern.becraft.bed.utils.BEUtils;

public abstract class BlockWithEntity implements IModBlock {
    public abstract BlockEntity createBlockEntity(Zone zone, BlockPosition position);

    @Override
    public void onPlace(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        BEUtils.addBlockEntity(zone, createBlockEntity(zone, position));
    }

    @Override
    public void onBreak(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        BEUtils.removeBlockEntity(position);
    }
}
