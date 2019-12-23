package com.epam;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Client {

    Socket socket;
    Reader readerStream;
    Writer writer;
    BufferedReader reader4Writer;
    BufferedReader in;
    BufferedWriter out;
    String host;
    int port;

    String nickname;

    private static final Logger log = Logger.getLogger(Client.class);

    public Client(String _host, int _port){
        host = _host;
        port = _port;
        try{
            socket = new Socket(host, port);
            reader4Writer = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            setNickname();
            readerStream = new Reader(in, nickname);
            writer = new Writer(reader4Writer, out, nickname);
            readerStream.start();
            writer.start();
        }
        catch (IOException err){
            log.error(err);
        }
    }

    private void setNickname(){
        try {
            log.info("login >");
            nickname = reader4Writer.readLine();
            out.write("'" + nickname + "'" + " has joined the chat" + '\n');
            out.flush();
        }
        catch (IOException err) {}
    }

    public static void launch(){
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
            Properties property = new Properties();
            property.load(fileInputStream);
            String host = property.getProperty("db.host");
            int port = Integer.parseInt(property.getProperty("db.port"));
            new Client(host, port);
        }
        catch(FileNotFoundException err){
            log.error("config.properties not found");
        }
        catch(IOException err) {
            log.error(err);
        }
    }

}
