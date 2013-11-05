package net.problemteam.qrrecognizer.util;

import java.io.File;

public class UniqueFileFactory {
    public static File createUniqueFile(String path, String prefix,
            String suffix) {
        return new File(path + prefix + Randomizer.getAlphabetical(10) + suffix);
    }
}
