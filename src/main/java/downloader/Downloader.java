package downloader;

import java.io.IOException;

public interface Downloader {
    Document download(final String url) throws IOException;
}
