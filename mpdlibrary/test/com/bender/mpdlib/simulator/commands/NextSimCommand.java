package com.bender.mpdlib.simulator.commands;

import com.bender.mpdlib.commands.Response;
import com.bender.mpdlib.simulator.SimPlayer;
import com.bender.mpdlib.simulator.library.Playlist;

import java.io.PrintWriter;

/**
 */
public class NextSimCommand extends SimCommand
{
    private Playlist playlist;
    private SimPlayer simPlayer;

    public NextSimCommand(PrintWriter simBufferedWriter, Playlist playlist, SimPlayer simPlayer)
    {
        super(simBufferedWriter);
        this.playlist = playlist;
        this.simPlayer = simPlayer;
    }

    @Override
    public void run() throws Exception
    {
        playlist.next();
        simPlayer.next();
        writer.println(Response.OK);
    }
}
