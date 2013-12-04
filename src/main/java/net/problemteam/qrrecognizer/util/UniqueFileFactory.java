package net.problemteam.qrrecognizer.util;

import java.io.File;

public interface UniqueFileFactory {
    File createUniqueFile(String path, String prefix,
            String suffix);
}
