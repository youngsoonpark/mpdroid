package com.bender.mpdlib;

import com.bender.mpdlib.commands.Command;
import com.bender.mpdlib.util.Log;

/**
 */
class CommandRunner
{
    public static final String TAG = "CommandRunner";

    public static <K, T> T runCommand(Command<K, T> command)
    {
        try
        {
            return callCommand(command);
        }
        catch (Exception e)
        {
            Log.e(TAG, e);
        }
        return null;
    }

    public static <K, T> T callCommand(Command<K, T> command) throws Exception
    {
        String debugLine = "callCommand: " + command.getClass().getSimpleName();
        K arg = command.getArg();
        if (arg != null)
        {
            debugLine += "(" + arg + ")";
        }
        Log.i(TAG, debugLine);
        return command.call();
    }
}
