import javax.sound.midi.Sequencer.SyncMode;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;  
import javax.swing.tree.DefaultMutableTreeNode;  



public class client {
  public static void main(String[] args) {
        JFrame frame = new JFrame("FTP Client");
        frame.setSize(300,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        JPanel panel = new JPanel();
        frame.add(panel);
        logonComponents(panel,frame);
        client access = new client();
         
        frame.setVisible(true);
 
    }
public static void logonComponents(JPanel panel,JFrame frame) {
   JTextField IPtext;
   JTextField usertext;
   JPasswordField passtext;

    panel.setLayout(null);
     
    JLabel IPAddr = new JLabel("IP");
    IPAddr.setBounds(10, 10, 80, 25);
    panel.add(IPAddr);

    IPtext = new JTextField(20);
    IPtext.setBounds(100, 10, 160, 25);
    panel.add(IPtext);

    JLabel user = new JLabel("Username");
    user.setBounds(10, 40, 80, 25);
    panel.add(user);
     
    usertext = new JTextField(20);
    usertext.setBounds(100, 40, 160, 25);
    panel.add(usertext);
     
    JLabel pass = new JLabel("Password");
    pass.setBounds(10, 80, 80, 25);
    panel.add(pass);
     
   passtext = new JPasswordField(20);
    passtext.setBounds(100, 80, 160, 25);
    panel.add(passtext);
     
    JButton login = new JButton("Login");
    login.setBounds(10, 120, 120, 25);
    panel.add(login);

    JLabel Message = new JLabel("");
    Message.setBounds(10, 115, 300, 100);
    panel.add(Message);


    login.addActionListener(new ActionListener(){  
      public void actionPerformed(ActionEvent e) {

        Thread thread = new Thread(){
            public void run(){

         try {
            FTPClient client = new FTPClient();
    
               client.connect(IPtext.getText(),21);
             

               int reasonConnect = client.getReplyCode();
               System.out.print("\n"+"Connect Status Code: "+reasonConnect + "\n");

               client.enterLocalPassiveMode();

                String username = usertext.getText().toString();

                String password = passtext.getText().toString();


                System.out.print("IP:"+ IPtext.getText().toString() + "\n"+"Username:" + username + "\n"
                        +  "Password:" + password);


                boolean loginStatus = client.login(username, password); 


                client.changeWorkingDirectory("~");




                int reasonLogin = client.getReplyCode();
                System.out.print("\n"+"Login Status Code: "+reasonLogin + "\n");


                client.setFileType(client.BINARY_FILE_TYPE);
                client.setControlEncoding("UTF-8");
                
                
           

                if (loginStatus)
                {
                    System.out.print("\n"+"Sucess" + "\n");
                    Message.setText("Sucess!");
                    frame.remove(panel);
                    frame.setSize(501,500);
                    frame.setBackground(Color.WHITE);
                    showFTP(frame,client);
                    client.logout();
                    client.disconnect();
                }
                else 
                {
                    System.out.print("\n"+"Failure" + "\n");
                    Message.setText("Wrong Password or Username!");
                    client.logout();
                   client.disconnect();
                }
            
                } catch (IOException ex) {
                    System.out.println("IOException:" + ex);
                 } catch (NoClassDefFoundError exception) {
                    System.out.println("No Class Def Found Error");
                 }
            }
        };
        thread.start();
        }  
    });  
 
    }

    public static void showFTP( JFrame frame,FTPClient client)
    {
        try {
        FTPFile[] files = client.listFiles("~");
        DefaultMutableTreeNode style=new DefaultMutableTreeNode("~");
        for (FTPFile file : files) {
            DefaultMutableTreeNode file_file=new DefaultMutableTreeNode(file.getName());  
            style.add(file_file);
            
        }
        JPanel panel = new JPanel();
        frame.add(panel);

        JTree jt =new JTree(style);
        panel.add(jt);  
        panel.updateUI();
        panel.setVisible(true);

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = jt.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = jt.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        mySingleClick(selRow, selPath);
                    }
                    else if(e.getClickCount() == 2) {
                        myDoubleClick(selRow, selPath);
                    }
                }
            }
        };
        jt.addMouseListener(ml);


    } catch (IOException ex) {
        System.out.println("IOException:" + ex);
     } 

    }
}
