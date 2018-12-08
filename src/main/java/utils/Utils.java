package utils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static List<Path> collect(Stream<Path> stream) {
        return stream.collect(Collectors.toList());
    }
}
