package Messager;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerChat {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket  = null;
        Scanner scanner ;
        PrintWriter writer;
        try {
            System.out.println("Сервео ожидает подключения клиента");
            serverSocket = new ServerSocket(9898);
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            writer = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
            while (true){
                if(scanner.hasNext()){
                    String s = scanner.nextLine();
                    if(s.equalsIgnoreCase("end"))break;
                    writer.println("Эхо : "+ s);
                    writer.flush();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
                socket.close();
                System.out.println("Сервер завершил работу");
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
