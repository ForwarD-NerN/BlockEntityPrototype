package ru.nern.becraft.bed.utils;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;

public class BEModUtils {
    public static double distanceTo(BlockPosition position, Vector3 vector) {
        double x = vector.x - position.getGlobalX();
        double y = vector.y - position.getGlobalY();
        double z = vector.z - position.getGlobalZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public static BlockPosition globalToPosition(Zone zone, int x, int y, int z) {
        int cx = Math.floorDiv(x, 16);
        int cy = Math.floorDiv(y, 16);
        int cz = Math.floorDiv(z, 16);
        Chunk c = getChunkOrGenerate(zone, cx, cy, cz);
        x -= 16 * cx;
        y -= 16 * cy;
        z -= 16 * cz;
        return new BlockPosition(c, x, y, z);
    }

    public static Chunk getChunkOrGenerate(Zone zone, int cx, int cy, int cz) {
        Chunk c = zone.getChunkAtChunkCoords(cx, cy, cz);
        if (c == null) {
            c = new Chunk(cx, cy, cz);
            c.initChunkData();
            zone.addChunk(c);
        }
        return c;
    }
}
