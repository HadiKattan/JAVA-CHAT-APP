import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainServerChat{
    public static void main(String[] args) throws IOException {
        new ServerChat();
    }
}

class ServerChat extends JFrame{
     private static final int PORT_NO=1234;
     private String message;
     private PrintWriter out;
     private String clientMessage;
     private JTextArea chatArea;
     private int counter=0;
     private Socket soc;
     private JScrollPane scroll;
     private JPanel p,p2;
     private JTextField chatMessage;
     private ServerSocket serverSoc;
     private ArrayList<String> names = new ArrayList<>();
     public ServerChat() throws IOException{
        names.add("Ahmad");names.add("Ali");
        setTitle("Server");
        setLayout(new BorderLayout(10,10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600,600);
        serverSoc = new ServerSocket(PORT_NO);   
        p = new JPanel(new BorderLayout(5,5));
        chatArea = new JTextArea();
        chatArea.setFont(new Font("MV Boli",Font.BOLD,20));
        chatArea.setEditable(false);
        chatArea.setBorder(new LineBorder(Color.BLACK,1));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        p.add(chatArea,BorderLayout.CENTER);
        scroll = new JScrollPane(chatArea); 
        p.add(scroll);
        add(p);
        chatMessage = new JTextField("Message...");
        chatMessage.setFont(new Font(null,Font.PLAIN,20));
        chatMessage.setBorder(new LineBorder(Color.BLACK,1));
        chatMessage.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(chatMessage.getText().toString().equals("Message..."))
                chatMessage.setText("");
            }
        });
        chatMessage.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                message = chatMessage.getText();
                chatArea.append("Me : "+message+"\n");
                chatMessage.setText("");
                out.println(message);
            }
        });
        p2 = new JPanel(new BorderLayout(5,5));
        p2.add(chatMessage);
        JButton btnSend = new JButton("Send");
        btnSend.setFont(new Font(null,Font.PLAIN,25));
        btnSend.setBackground(Color.GREEN);
        btnSend.setForeground(Color.WHITE);
        btnSend.setFocusable(false);
        btnSend.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                message = chatMessage.getText();
                chatArea.append("Me : "+message+"\n");
                chatMessage.setText("");
                out.println(message);
            }
        });
        p2.add(btnSend,BorderLayout.EAST);
        p.add(p2,BorderLayout.SOUTH);
        TitledBorder panelBorder = new TitledBorder("Chat");
        panelBorder.setTitleFont(new Font(null,Font.BOLD,20));
        p.setBorder(panelBorder);
        setVisible(true);
        while(true){
            soc = serverSoc.accept();
            chatArea.append(names.get(counter)+" connected .."+"\n");
            new MyThread(counter).start();
            counter++;
        }
    }

    class MyThread extends Thread{
        int counter;
        MyThread(int counter) throws IOException
        {out = new PrintWriter(soc.getOutputStream(),true);this.counter=counter;}
        @Override
        public void run(){
            while(true){
                try {
                    Scanner scan = new Scanner(soc.getInputStream());
                    clientMessage = scan.nextLine();
                    chatArea.append(names.get(counter)+" : "+clientMessage+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}