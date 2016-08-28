package com.leafriend.clo.data;

public class Track {

    private final int trackNo;

    private final String title;

    private final String href;

    public Track(int trackNo, String title, String href) {
        this.trackNo = trackNo;
        this.title = title;
        this.href = href;
    }

    public int getTrackNo() {
        return trackNo;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

}
