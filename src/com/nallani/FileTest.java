package com.nallani;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileTest {

    public void test() {

        String dirLocation = "/";

        try {
            List<File> files = Files.list(Paths.get(dirLocation))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            files.forEach(System.out::println);
        } catch (IOException e) {
            // Error while reading the directory
        }
    }
}
