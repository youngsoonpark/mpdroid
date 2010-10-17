package com.bender.mpdroid.mpdService;

import android.util.Log;

/**
 * Null object player adapter
 */
class NullPlayerAdapter implements MpdPlayerAdapterIF
{
    private static final String TAG = NullPlayerAdapter.class.getSimpleName();

    public void playOrPause()
    {
        Log.e(TAG, "playOrPause() called on NULL object");
    }

    public void stop()
    {
        Log.e(TAG, "stop() called on NULL object");
    }

    public MpdSongAdapterIF getCurrentSong()
    {
        Log.e(TAG, "getCurrentSong() called on NULL object");
        return new NullSongAdapter();
    }

    public void addSongChangeListener(MpdSongListener listener)
    {
        Log.e(TAG, "addSongChangeListener() called on NULL object");
    }

    public void addPlayStatusListener(MpdPlayStatusListener listener)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public PlayStatus getPlayStatus()
    {
        Log.e(TAG, "getPlayStatus() called on NULL object");
        return PlayStatus.Stopped;
    }

    public void next()
    {
        Log.e(TAG, "next() called on NULL object");
    }

    public void prev()
    {
        Log.e(TAG, "prev() called on NULL object");
    }

    public Integer setVolume(Integer volume)
    {
        Log.e(TAG, "setVolume(volume) called on NULL object");
        return 0;
    }

    public Integer getVolume()
    {
        Log.e(TAG, "getVolume() called on NULL object");
        return 0;
    }

    public Boolean toggleMute()
    {
        Log.e(TAG, "toggleMute() called on NULL object");
        return false;
    }
}
