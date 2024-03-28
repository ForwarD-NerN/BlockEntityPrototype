package ru.nern.becraft.block;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BlockGenerator;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import finalforeach.cosmicreach.world.entities.Player;
import ru.nern.becraft.bed.BEUtils;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockWithEntity;

public class BETestBlock extends BlockWithEntity implements IModBlock {
    BlockGenerator generator;

    public BETestBlock() {
        generator = BlockGenerator.createGenerator();
    }


    @Override
    public void onInteract(World world, Player player, BlockState blockState, BlockPosition position) {
        if(BEUtils.getBlockEntity(position) instanceof CustomBlockEntity be) {
            be.onInteract();
        }
    }

    @Override
    public void onPlace(World world, Player player, BlockState blockState, BlockPosition position) {
        super.onPlace(world, player, blockState, position);
        System.out.println("ON PLACE");
    }

    @Override
    public void onBreak(World world, Player player, BlockState blockState, BlockPosition position) {
        super.onBreak(world, player, blockState, position);
        System.out.println("ON BREAK");
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

    @Override
    public BlockEntity createBlockEntity(World world, BlockPosition position) {
        return new CustomBlockEntity(world, position);
    }
}
