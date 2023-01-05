package com.example.testdataelasticsearch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoadFileUtil {
    private static final Logger log = LoggerFactory.getLogger(LoadFileUtil.class.getSimpleName());

    public static String loadAsString(final String path) {
        try {
            return new String(
                    Files.readAllBytes(
                            new ClassPathResource(path).getFile().toPath()
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
