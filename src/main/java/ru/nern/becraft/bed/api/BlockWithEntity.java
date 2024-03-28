package ru.nern.becraft.bed.api;

import dev.crmodders.flux.api.block.IFunctionalBlock;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import finalforeach.cosmicreach.world.entities.Player;
import ru.nern.becraft.bed.BEUtils;

public abstract class BlockWithEntity implements IFunctionalBlock {
    public abstract BlockEntity createBlockEntity(World world, BlockPosition position);

    @Override
    public void onPlace(World world, Player player, BlockState blockState, BlockPosition position) {
        BEUtils.addBlockEntity(world, createBlockEntity(world, position));
    }

    @Override
    public void onBreak(World world, Player player, BlockState blockState, BlockPosition position) {
        BEUtils.removeBlockEntity(position);
    }
}
