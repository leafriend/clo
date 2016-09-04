package com.leafriend.clo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leafriend.clo.data.Album;
import com.leafriend.clo.data.Track;

public class LyricsDownloader {

    private static final String DB_PROPERTIES = "db.properties";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LyricsDownloader.class);

    private File lyricsDir;

    public LyricsDownloader(File lyricsDir) {
        this.lyricsDir = lyricsDir;
    }

    public void download() throws IOException {
        File txtDir = new File(lyricsDir, "txt");
        txtDir.mkdirs();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream(DB_PROPERTIES);

        Properties db = new Properties();
        try {
            db.load(in);
        } catch (IOException e) {
            LOGGER.error("Failed to load data from " + DB_PROPERTIES, e);
            throw new RuntimeException("Not handled", e);
        }

        List<Album> albums = parse(
                new FileInputStream("resources/source/discography.html"));

        albums.forEach(album -> {

            String releaseDate = album.getReleaseDate();
            String type = album.getType();
            String title;
            if (db.containsKey(releaseDate)) {
                title = db.getProperty(releaseDate);
                LOGGER.trace("[{}] {} ({}) was found from DB", releaseDate,
                        title, type);
                db.remove(releaseDate);
            } else {
                title = album.getTitle();
                LOGGER.warn("[{}] {} ({}) was not found from DB", releaseDate,
                        title, type);
            }

            String albumName = "[" + releaseDate + "] " + escape(title);
            File albumDir = new File(txtDir, albumName);
            if (!albumDir.exists()) {
                LOGGER.debug("Download Album: {}", albumName);
                albumDir.mkdirs();
            }
            album.getTracks().forEach(track -> {
                if (track.getHref().length() == 0)
                    return;
                String titleName = String.format("%02d. %s.txt",
                        track.getTrackNo(), escape(track.getTitle()));
                File trackFile = new File(albumDir, titleName);
                if (!trackFile.exists()) {
                    LOGGER.debug("Download Track: {}", titleName);
                    try {
                        try (FileOutputStream fos = new FileOutputStream(
                                trackFile)) {

                        }
                    } catch (IOException e) {
                        LOGGER.error(
                                "Failed to create lyrics file: " + trackFile,
                                e);
                        throw new RuntimeException("Not handled", e);
                    }
                }
            });
        });

    }

    private String escape(String title) {
        title = title.replaceAll("[:/]", "-");
        title = title.replaceAll("[\"]", "");
        title = title.replaceAll("^Loop Mark\\? U$", "Loop MARK II");
        title = title.replaceAll("^illusion<", "illusion");
        return title;
    }

    private List<Album> parse(InputStream in) throws IOException {
        String encoding = "utf-8";
        String url = "http://www.jpopasia.com/bucktick/discography/";
        Document document = Jsoup.parse(in, encoding, url);

        List<Album> albums = new ArrayList<>();

        for (Element hr : document.getElementsByTag("hr")) {

            Element follower = hr.nextElementSibling();
            if ("div".equals(follower.tagName())) {
                // follower is imageWrapper
                follower = follower.nextElementSibling();
            }

            if (!"a".equals(follower.tagName())) {
                break;
            }

            Element albumElem = follower;
            Album album = extractAlbum(albumElem);

            int trackNo = 0;
            Element next = albumElem.nextElementSibling().nextElementSibling();
            while ("track".equals(next.attr("class"))) {
                trackNo++;
                Track track = extractTrack(trackNo, next);
                album.addTrack(track);
                next = next.nextElementSibling();
            }
            if (trackNo != album.getTrackCount()) {
                LOGGER.warn("Track count mismatch - expected: {} / actual: {}",
                        album.getTrackCount(), trackNo);
            }

            albums.add(album);
        }

        return albums;
    }

    private Album extractAlbum(Element albumElem) {
        String title = albumElem.text();
        Element albumDetail = albumElem.nextElementSibling();
        String[] frags = albumDetail.text().replaceAll("\u00a0", "")
                .replaceAll("&middmiddot;", "\u00b7").split("\u00b7");
        String type = frags[0].trim();
        int trackCount = Integer.parseInt(frags[1].trim().split(" ")[0]);
        String releaseDate = frags[2].trim();

        releaseDate = releaseDate.replaceAll("^1987-04-04$", "1987-04-01");
        releaseDate = releaseDate.replaceAll("^1995-05-05$", "1995-05-15");
        releaseDate = releaseDate.replaceAll("^2002-02-02$", "2002-03-06");
        releaseDate = releaseDate.replaceAll("^2003-01-18$", "2003-01-08");
        releaseDate = releaseDate.replaceAll("^2005-04-02$", "2005-04-20");
        releaseDate = releaseDate.replaceAll("^2005-04-05$", "2005-04-06");
        releaseDate = releaseDate.replaceAll("^2008-12-12$", "2008-12-17");

        return new Album(type, title, releaseDate, trackCount);
    }

    private Track extractTrack(int trackNo, Element next) {
        String[] fs = next.text().split("\\. ");
        String t = trackNo + ".";
        if (!fs[0].equals(String.valueOf(trackNo))) {
            LOGGER.warn("Track number mismatch - expected: {} / actual: {}", t,
                    fs[0]);
        }
        String title = fs[1];

        String href = "";
        Element div = next.child(0);
        if (div != null && div.children().size() > 0) {
            Element a = div.child(0);
            if (a != null) {
                href = a.attr("href");
                if (href.indexOf("/lyrics/") < 0) {
                    href = "";
                }
            }
        }
        LOGGER.trace("Track extracted: {}. {}{}{}", trackNo, title,
                href.isEmpty() ? "" : " -> ", href.isEmpty() ? "" : href);

        Track track = new Track(trackNo, title, href);
        return track;
    }

}
