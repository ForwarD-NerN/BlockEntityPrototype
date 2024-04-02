package ru.nern.becraft.bed.utils;

import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class MutableBlockPosition extends BlockPosition {
    private int x;
    private int y;
    private int z;

    public MutableBlockPosition(BlockPosition blockPosition) {
        super(null, 0, 0, 0);
        this.x = blockPosition.getGlobalX();
        this.y = blockPosition.getGlobalY();
        this.z = blockPosition.getGlobalZ();
    }

    public MutableBlockPosition(int x, int y, int z) {
        super(null, 0, 0, 0);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getGlobalX() {
        return x;
    }

    @Override
    public int getGlobalY() {
        return y;
    }

    @Override
    public int getGlobalZ() {
        return z;
    }

    public BlockPosition toImmutable(Zone zone) {
        int cx = Math.floorDiv(x, 16);
        int cy = Math.floorDiv(y, 16);
        int cz = Math.floorDiv(z, 16);
        Chunk c = zone.getChunkAtChunkCoords(cx, cy, cz);
        if (c == null) {
            c = new Chunk(cx, cy, cz);
            c.initChunkData();
            zone.addChunk(c);
        }

        x -= 16 * cx;
        y -= 16 * cy;
        z -= 16 * cz;

        return new BlockPosition(c, x, y, z);
    }
}
