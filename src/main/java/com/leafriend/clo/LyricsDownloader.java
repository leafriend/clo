package com.leafriend.clo;

import java.io.File;

public class LyricsDownloader {

    private File lyricsDir;

    public LyricsDownloader(File lyricsDir) {
        this.lyricsDir = lyricsDir;
    }

    public void download() {
        new File(lyricsDir, "txt").mkdirs();
    }

}
