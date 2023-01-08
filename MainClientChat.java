import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainClientChat{
    public static void main(String[] args) throws UnknownHostException, IOException {
        new ClientChat();
    }
}
class ClientChat extends JFrame {
    private static final int PORT_NO=1234;
    private Scanner scan;
    private PrintWriter out;
    private JTextArea chatArea;
    private String message,serverMessage;
    private Socket soc;
    private JPanel p,p2;
    private JScrollPane scroll;
    private JTextField chatMessage;
    public ClientChat() throws UnknownHostException, IOException{
        setTitle("Client");
        setLayout(new BorderLayout(10,10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600,600);
        soc = new Socket("localhost",PORT_NO);
        scan = new Scanner(soc.getInputStream());
        out = new PrintWriter(soc.getOutputStream(),true);
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
        p.setBorder(new TitledBorder("Chat"));
        TitledBorder panelBorder = new TitledBorder("Chat");
        panelBorder.setTitleFont(new Font("Arial",Font.BOLD,20));
        p.setBorder(panelBorder);
        setVisible(true);
        while(true){
            serverMessage = scan.nextLine();
            chatArea.append("Server : "+serverMessage+"\n");
        }
    }
}