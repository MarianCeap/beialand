package com.beia.solomon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectToJavaServerRunnable implements Runnable
{
    @Override
    public void run() {
        try
        {
            //solomon-beacon.beia-consult.ro(virtual machine adress)
            LoginActivity.socket = new Socket("10.0.9.33", 8000);
            LoginActivity.objectOutputStream = new ObjectOutputStream(LoginActivity.socket.getOutputStream());
            LoginActivity.objectInputStream = new ObjectInputStream(LoginActivity.socket.getInputStream());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}