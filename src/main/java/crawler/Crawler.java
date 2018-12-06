package crawler;

import downloader.Result;

public interface Crawler extends AutoCloseable {
    Result download(String url, int depth);

    void close();
}
