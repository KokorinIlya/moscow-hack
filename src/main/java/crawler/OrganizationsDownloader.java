package crawler;

import downloader.CachingDownloader;
import downloader.Result;
import downloader.URLUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OrganizationsDownloader {

    private static FileVisitor<Path> directoryCleaner = new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    private final static Pattern CATEGORY_URL_TEMPLATE =
            Pattern.compile("^https://www.rusprofile.ru/codes/\\d+/moskva(.*)?");
    private final static String COMPANY_URL = "https://www.rusprofile.ru/id/";
    private final static String BIBL_RECORD_BEGIN = "<div id=\"bibliographic_record\">";
    private final static String BIBL_RECORD_END = "</div>";
    private final static byte OK_MARKER = '+';

    private static int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private static int MIN_YEAR = MAX_YEAR - 4;

    private static void download(Path dirForDownloadedFiles, String code) {
        try {
            Files.createDirectories(dirForDownloadedFiles);
        } catch (IOException e) {
            System.err.println("Cannot create directory for downloaded files" + e.getMessage());
            return;
        }
        String host;
        String mainPage = "https://www.rusprofile.ru/codes/" + code + "0000/moskva";
        try {
            host = URLUtils.getHost(mainPage);
        } catch (MalformedURLException e) {
            System.err.println("Cannot get host of " + mainPage + " " + e.getMessage());
            return;
        }

        try (
                Crawler crawler = new WebCrawler(
                        new CachingDownloader(dirForDownloadedFiles), 20, 20, 100,
                        url -> {
                            try {
                                if (!URLUtils.getHost(url).equals(host)) {
                                    return false;
                                }
                            } catch (MalformedURLException e) {
                                return false;
                            }
                            return CATEGORY_URL_TEMPLATE.matcher(url).matches() ||
                                    url.startsWith(COMPANY_URL);

                        }
                )
        ) {
            Result result = crawler.download(mainPage, Integer.MAX_VALUE);
        } catch (IOException e) {
            System.err.println("Error downloading " + e.getMessage());
        }
    }

    private static void parse(Path dirForDownloadedFiles, Path pathToRes) {
        try {
            Files.createDirectories(pathToRes.toAbsolutePath().getParent());
            Files.createDirectories(dirForDownloadedFiles);
        } catch (IOException e) {
            System.err.println("Cannot create directory" + e.getMessage());
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(pathToRes)) {
            Files.list(dirForDownloadedFiles).forEach(
                    page -> {
                        if (!Files.isRegularFile(page) || page.equals(pathToRes)) {
                            return;
                        }
                        try (InputStream is = Files.newInputStream(page)) {
                            int x = is.read();
                            if (x == -1 || (byte) x != OK_MARKER) {
                                return;
                            }
                        } catch (IOException e) {
                            System.err.println("Error trying to open file " + e.getMessage());
                            return;
                        }
                        String htmlCode;
                        try (BufferedReader reader = Files.newBufferedReader(page)) {
                            htmlCode = reader.lines().collect(Collectors.joining());
                        } catch (IOException e) {
                            System.err.println("Error while reading file " + page.getFileName().toString());
                            System.err.println(e.getMessage());
                            return;
                        }
                        String oneString = htmlCode.replaceAll(">\\p{javaWhitespace}+<", "><");
                        for (int year = MIN_YEAR; year <= MAX_YEAR; year++) {
                            if (oneString.contains("<dt>Год:</dt><dd>" + Integer.toString(year) + "</dd>")) {
                                int beginIndex = oneString.indexOf(BIBL_RECORD_BEGIN);
                                if (beginIndex == -1) {
                                    return;
                                }
                                int endIndex = oneString.indexOf(BIBL_RECORD_END, beginIndex);
                                if (endIndex == -1) {
                                    return;
                                }
                                String bookInfo = oneString.substring(
                                        beginIndex + BIBL_RECORD_BEGIN.length(),
                                        endIndex
                                );
                                try {
                                    writer.write(bookInfo.trim());
                                    writer.newLine();
                                } catch (IOException e) {
                                    System.err.println("Cannot write information about book " + bookInfo + "to file");
                                    System.err.println("I/O error occurred " + e.getMessage());
                                }
                                return;
                            }
                        }
                    }
            );
        } catch (IOException e) {
            System.err.println("Error while writing to file " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        if (args[0].equals("--download")) {
            Path dirForDownloadedFiles = Paths.get(args[1]);
            for (int i = 1; i <= 9; i++) {
                String catName = "" + i;
                Path forDownload = dirForDownloadedFiles.resolve(catName);
                download(forDownload, catName);
            }
            return;
        }

        if (args[0].equals("--clean")) {
            if (args.length != 2 || args[1] == null) {
                return;
            }

            try {
                Path dirForDownloadFiles = Paths.get(args[1]);
                Files.walkFileTree(dirForDownloadFiles, directoryCleaner);
            } catch (InvalidPathException e) {
                System.err.println("Invalid path to download files " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error while cleaning directory " + e.getMessage());
            }
        }
    }
}

