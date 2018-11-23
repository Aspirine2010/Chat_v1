package Messager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat extends JFrame {
    JTextArea textArea;
    JTextField textField;
    JButton button;
    Scanner scanner;
    Socket socket = null;
    PrintWriter writer;
    JPanel panel;
    JScrollPane scrollPane;
    ClientChat(){
        try {
            socket = new Socket("localhost",9090);
            writer = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
        }
        catch (Exception er){
            er.printStackTrace();
        }
        setTitle("Client_Chat");
        setBounds(100,100,500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        button = new JButton("send");
        textField = new JTextField();
        panel = new JPanel(new BorderLayout());
        add(panel,BorderLayout.SOUTH);
        panel.add(button,BorderLayout.EAST);
        panel.add(textField,BorderLayout.CENTER);
        setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textField.getText().trim().isEmpty()){
                    sendMsg();
                    textField.grabFocus();
                }
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMsg();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(scanner.hasNext()){
                        String s = scanner.nextLine();
                        if(s.equalsIgnoreCase("end"))break;
                        textArea.append(s);
                        textArea.append("\n");
                    }
                }
            }
        }).start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    writer.println("end");
                    writer.flush();
                    writer.close();
                    scanner.close();
                    socket.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args) {
new ClientChat();
    }
    public void sendMsg(){
        writer.println(textField.getText());
        writer.flush();
        textField.setText("");
    }

}
