package ru.nern.becraft.block;

import dev.crmodders.flux.api.block.IModBlock;
import dev.crmodders.flux.api.generators.BlockGenerator;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;
import ru.nern.becraft.bed.api.BlockEntity;
import ru.nern.becraft.bed.api.BlockWithEntity;
import ru.nern.becraft.bed.utils.BEUtils;

public class BETestBlock extends BlockWithEntity implements IModBlock {
    BlockGenerator generator;

    public BETestBlock() {
        generator = BlockGenerator.createGenerator();
    }


    @Override
    public void onInteract(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        if(BEUtils.getBlockEntity(position) instanceof CustomBlockEntity be) {
            be.onInteract();
        }
    }

    @Override
    public void onPlace(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        super.onPlace(zone, player, blockState, position);
        System.out.println("ON PLACE");
    }

    @Override
    public void onBreak(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        super.onBreak(zone, player, blockState, position);
        System.out.println("ON BREAK");
    }

    @Override
    public BlockGenerator getGenerator() {
        return generator;
    }

    @Override
    public BlockEntity createBlockEntity(Zone zone, BlockPosition position) {
        return new CustomBlockEntity(zone, position);
    }
}
