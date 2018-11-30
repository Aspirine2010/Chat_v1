package Chat_Client_v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWindow extends JFrame {
    private static final String SERVER_HOST = "localhost";
    private static final int PORT = 9090;
    private Socket clientSocket ;
    private Scanner scanner;
    private PrintWriter writer;
    private JTextField jname;
    private JTextField jmessage;
    private JTextArea textArea;
    private String clientName  = "";
    public String getClientName(){
        return this.clientName;
    }
    public ClientWindow(){
        try{
            clientSocket = new Socket(SERVER_HOST,PORT);
            scanner = new Scanner(clientSocket.getInputStream());
            writer = new PrintWriter(clientSocket.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setTitle("");
        setBounds(600,300,600,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        JLabel numberOfClients = new JLabel("Количество клиентов в чате : ");
        add(numberOfClients,BorderLayout.NORTH);
        JPanel panel = new JPanel(new BorderLayout());
        add(panel,BorderLayout.SOUTH);
        JButton button = new JButton("Отправить");
        panel.add(button,BorderLayout.EAST);
        jmessage = new JTextField("Введите ваше собщение : ");
        panel.add(jmessage,BorderLayout.CENTER);
        jname = new JTextField("Введите Ваше имя :");
        panel.add(jname,BorderLayout.WEST);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!jmessage.getText().trim().isEmpty()&&jname.getText().trim().isEmpty()){
                    clientName = jname.getText();
                    sendMsg();
                    jmessage.grabFocus();
                }
            }
        });
        jmessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                jmessage.setText("");
            }
        });
        jname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                jname.setText("");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        if(scanner.hasNext()){
                            String inMes = scanner.nextLine();
                            String clientsInChat = " Клиентов в  чате : ";
                            if(inMes.indexOf(clientsInChat)==0){
                                numberOfClients.setText(inMes);
                            }
                            else {
                                textArea.append(inMes);
                                textArea.append("\n");
                            }
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try{
                    if(!clientName.isEmpty()&&clientName!="Введите Ваше имя :"){
                        writer.println(clientName + " вышел из чата ");
                    }
                    else writer.println("Клиент вышел так и не представившись, падонок");
                    writer.flush();
                    writer.close();
                    scanner.close();
                    clientSocket.close();
                }
                catch (Exception er){
                    er.printStackTrace();
                }

            }
        });
        setVisible(true);
    }
    public void sendMsg(){
        String messageStr = jname.getText() + ":"+ jmessage.getText();
        writer.println(messageStr);
        writer.flush();
        jmessage.setText("");
    }
}
