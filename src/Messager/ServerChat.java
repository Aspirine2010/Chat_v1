package Messager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerChat {
    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
            socket = serverSocket.accept();
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            while (true){
                String s = scanner.nextLine();
                if(s.equalsIgnoreCase("end"))break;
                writer.println("Эхо : "+ s);
                writer.flush();
            }
        }
        catch (IOException e){
            System.out.println("Ошибка чтения ");
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
