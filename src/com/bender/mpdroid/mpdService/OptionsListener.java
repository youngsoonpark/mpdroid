package com.bender.mpdroid.mpdService;

/**
 */
public interface OptionsListener
{
    void repeatUpdated(boolean repeat);

    void randomUpdated(boolean newRandom);
}
