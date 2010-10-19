package com.bender.mpdlib.simulator;

import com.bender.mpdlib.SocketStreamProviderIF;
import com.bender.mpdlib.commands.MpdCommands;
import com.bender.mpdlib.commands.Response;
import com.bender.mpdlib.simulator.commands.*;

import java.io.*;
import java.net.SocketAddress;
import java.util.StringTokenizer;

/**
 * todo: replace with documentation
 */
public class MpdServerSimulator
{

    private static final String VERSION = "sim v1.0";

    public SocketStreamProviderIF createMpdSocket()
    {
        return new CommandStreamProvider();
    }


    private class CommandStreamProvider implements SocketStreamProviderIF
    {

        private boolean connected;
        private ServerThread serverThread;

        private PipedReader clientPipedReader;
        private BufferedWriter simBufferedWriter;
        private BufferedReader simBufferedReader;
        private PipedWriter clientPipedWriter;

        public void connect(SocketAddress socketAddress) throws IOException
        {
            System.out.println("connect(addr=" + socketAddress + ")");
            clientPipedReader = new PipedReader();
            PipedWriter simPipedWriter = new PipedWriter(clientPipedReader);
            simBufferedWriter = new BufferedWriter(simPipedWriter)
            {
                @Override
                public void write(String s) throws IOException
                {
                    super.write(s);
                    if (!s.equals("\n"))
                    {
                        newLine();
                        flush();
                    }
                }
            };
            PipedReader simPipedReader = new PipedReader();
            simBufferedReader = new BufferedReader(simPipedReader);
            clientPipedWriter = new PipedWriter(simPipedReader);
            connected = true;
            serverThread = new ServerThread(this);
            serverThread.start();
            simBufferedWriter.write(Response.OK + " " + VERSION);
            System.out.println("connect()");
        }

        public BufferedReader getBufferedReader() throws IOException
        {
            return new BufferedReader(clientPipedReader);
        }

        public BufferedWriter getBufferedWriter() throws IOException
        {
            return new BufferedWriter(clientPipedWriter);
        }

        public void disconnect() throws IOException
        {
            connected = false;
            serverThread.interrupt();
            clientPipedReader.close();
            clientPipedWriter.close();
            simBufferedWriter.close();
            simBufferedReader.close();
            System.out.println("disconnect()");
        }

        public boolean isConnected()
        {
            return connected;
        }
    }

    private static class ServerThread extends Thread
    {
        private CommandStreamProvider provider;
        private BufferedReader simBufferedReader;
        private BufferedWriter simBufferedWriter;
        private static int count;

        public ServerThread(CommandStreamProvider commandStreamProvider)
        {
            super("Sim-" + count++ + ":");
            provider = commandStreamProvider;
            simBufferedReader = provider.simBufferedReader;
            simBufferedWriter = provider.simBufferedWriter;
        }

        @Override
        public void run()
        {
            while (connected())
            {
                try
                {
                    String line = simBufferedReader.readLine();
                    if (!connected())
                    {
                        return;
                    }
                    process(line);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        private void process(String line) throws IOException
        {
            StringTokenizer stringTokenizer = new StringTokenizer(line);
            String commandString = stringTokenizer.nextToken().trim();
            MpdCommands command = MpdCommands.parse(commandString);
            if (command == null)
            {
                simBufferedWriter.write(Response.ACK + " [5@0] {} unknown command \"" + commandString + "\"");
                return;
            }
            System.out.println(getName() + "process(): " + command);
            switch (command)
            {
                case close:
                    provider.disconnect();
                    break;
                case idle:
                    IdleSimCommand idleSimCommand = new IdleSimCommand(simBufferedWriter);
                    idleSimCommand.run();
                    break;
                case status:
                    StatusSimCommand statusSimCommand = new StatusSimCommand(simBufferedWriter);
                    statusSimCommand.run();
                    break;
                case play:
                    PlaySimSimCommand playSimCommand = new PlaySimSimCommand(simBufferedWriter);
                    playSimCommand.run();
                    break;
                case pause:
                    PauseSimCommand pauseSimCommand = new PauseSimCommand(simBufferedWriter);
                    pauseSimCommand.run();
                    break;
                case stop:
                    StopSimCommand stopSimCommand = new StopSimCommand(simBufferedWriter);
                    stopSimCommand.run();
                    break;
                case setvol:
                    VolumeSimCommand volumeSimCommand = new VolumeSimCommand(simBufferedWriter, stringTokenizer);
                    volumeSimCommand.run();
                    break;
                default:
                    simBufferedWriter.write(Response.OK.toString());
                    break;
            }
            System.out.println(getName() + command + " DONE");
        }

        private boolean connected()
        {
            return provider.isConnected();
        }
    }

    public static void main(String[] args) throws Exception
    {
        MpdServerSimulator mpdServerSimulator = new MpdServerSimulator();
        SocketStreamProviderIF mpdSocket = mpdServerSimulator.createMpdSocket();

        mpdSocket.connect(null);
        BufferedWriter bufferedWriter = mpdSocket.getBufferedWriter();
        final BufferedReader bufferedReader = mpdSocket.getBufferedReader();
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    while (true)
                    {
                        System.err.println(bufferedReader.readLine());
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        {
            bufferedWriter.write(reader.readLine());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }

}
