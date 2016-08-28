package com.leafriend.clo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {

    private final String type;

    private final String title;

    private final String releaseDate;

    private final List<Track> tracks;

    private final int trackCount;

    public Album(String type, String title, String releaseDate,
            int trackCount) {
        super();
        this.type = type;
        this.title = title;
        this.releaseDate = releaseDate;
        this.tracks = new ArrayList<>();
        this.trackCount = trackCount;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(Track track) {
        int next = tracks.size() + 1;
        if (track.getTrackNo() != next)
            throw new RuntimeException("Expected track no is " + next
                    + ", but was " + track.getTrackNo());
        tracks.add(track);
    }

    public int getTrackCount() {
        return trackCount;
    }

}
