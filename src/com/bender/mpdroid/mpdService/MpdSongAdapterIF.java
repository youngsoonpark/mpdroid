package com.bender.mpdroid.mpdService;

/**
 * mpd service song interface
 */
public interface MpdSongAdapterIF {
    String getSongName();

    String getArtist();

    String getAlbumName();

    String getFile();

    String getDate();

    Integer getSongLength();

    int getId();
}
