package Messager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat extends JFrame {
    Socket socket = null;
    JPanel panel;
    JButton button;
    JTextField textField;
    JTextArea textArea;
    JScrollPane scrollPane;
    Scanner scanner;
    PrintWriter writer;
    public static void main(String[] args) {
        new ClientChat();
    }
    ClientChat(){
        try {
            socket = new Socket("localhost",9898);
            writer = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        setTitle("Chat");
        setBounds(100,100,500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        button = new JButton("send");
        textField = new JTextField();
        panel = new JPanel(new BorderLayout());
        add(panel,BorderLayout.SOUTH);
        panel.add(button,BorderLayout.EAST);
        panel.add(textField,BorderLayout.CENTER);
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
                catch (Exception er){
                    er.printStackTrace();
                }

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(scanner.hasNext()){
                        String message = scanner.nextLine();
                        textArea.append(message);
                        textArea.append("\n");
                        //textField.setText("");
                    }
                }

            }
        }).start();
        setVisible(true);

    }
    public  void sendMsg(){
        writer.println(textField.getText());
        writer.flush();
        textField.setText("");
    }
}
