package utils;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static<T> List<T> collect(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }
}
