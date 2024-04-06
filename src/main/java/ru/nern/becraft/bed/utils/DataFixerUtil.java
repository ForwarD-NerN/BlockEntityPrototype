package ru.nern.becraft.bed.utils;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import ru.nern.becraft.BECraft;

public class DataFixerUtil {
    public static void applyFixes(CompoundTag rootTag, ListTag<CompoundTag> beListTag, CompoundTag beTag, int oldFormatVersion) {
        if(oldFormatVersion == 0) {
            if(beTag.containsKey("x") || beTag.containsKey("y") || beTag.containsKey("z")) {
                int x = beTag.getInt("x");
                int y = beTag.getInt("y");
                int z = beTag.getInt("z");

                BECraft.LOGGER.info("Found legacy coordinates in the block entity data: " + x + " " + y + " " + z + ". Converting them...");

                int cx = Math.floorDiv(x, 16);
                int cy = Math.floorDiv(y, 16);
                int cz = Math.floorDiv(z, 16);

                x -= 16 * cx;
                y -= 16 * cy;
                z -= 16 * cz;

                beTag.putInt("lx", x);
                beTag.putInt("ly", y);
                beTag.putInt("lz", z);

                beTag.remove("x");
                beTag.remove("y");
                beTag.remove("z");
            }
        }

    }
}
