package Chat_Server_v2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Server server;
    private PrintWriter writer;
    private Scanner scanner;
    private static int PORT = 9090;
    private static String HOST = "localhost";
    private Socket clientSocket = null;
    private static int clients_count =0;
    public ClientHandler(Socket clientSocket, Server server) {
        try {
            clients_count++;
            this.server = server;
            this.clientSocket = clientSocket;
            writer = new PrintWriter(clientSocket.getOutputStream());
            scanner = new Scanner(clientSocket.getInputStream());
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (true){
                server.sendMessageToAllClients("Новый участник вошел в чат...");
                server.sendMessageToAllClients("Клиентов в чате : "+ clients_count);
                break;
            }
            while (true){
                if(scanner.hasNext()){
                    String clientMessage = scanner.nextLine();
                    System.out.println(clientMessage);
                    server.sendMessageToAllClients(clientMessage);
                }
                Thread.sleep(100);
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            this.close();
        }


    }
    public void sendMsg( String message){
        try {
            writer.println(message);
            writer.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void close(){
        server.removeClient(this);
        clients_count--;
        server.sendMessageToAllClients("Клиентов в чате :"+ clients_count);
    }
}
