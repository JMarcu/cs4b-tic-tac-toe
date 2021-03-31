package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HandleClientConnection implements Runnable{
    
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public HandleClientConnection(Socket client){
        try {
            this.client = client;
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    @Override
    public void run(){
        
    }
}
