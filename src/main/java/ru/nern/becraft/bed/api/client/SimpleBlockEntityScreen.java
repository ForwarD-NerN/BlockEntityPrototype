package ru.nern.becraft.bed.api.client;

import finalforeach.cosmicreach.gamestates.GameState;
import ru.nern.becraft.bed.api.BlockEntity;

public class SimpleBlockEntityScreen<E extends BlockEntity> extends GameState {

    public final E blockEntity;
    public SimpleBlockEntityScreen(E blockEntity) {
        this.blockEntity = blockEntity;
    }
}
