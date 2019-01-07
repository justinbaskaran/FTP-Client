import javax.sound.midi.Sequencer.SyncMode;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
        int counter=0;
        JPanel panel = new JPanel();
        frame.add(panel);
        DefaultMutableTreeNode style = getFileStructure(client, "~");
        JTree jt =new JTree(style);
        panel.add(jt);  
        panel.updateUI();
        panel.setVisible(true);

        JButton login = new JButton("GET");
        login.setBounds(10, 120, 120, 25);
        panel.add(login);
        panel.updateUI();
        panel.setVisible(true);

        login.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {
                String jTreeVarSelectedPath = "";
                Object[] paths = jt.getSelectionPath().getPath();
                for (int i=0; i<paths.length; i++) {
                    jTreeVarSelectedPath += paths[i];
                    if (i+1 <paths.length ) {
                        jTreeVarSelectedPath += "/";
                    }

                }
                //System.out.print("\n"+"Clicked!" + jTreeVarSelectedPath);
                try {
                if (client == null)
                {
                    System.out.print("\n"+"NULL!");
  
                }
            
            String[] tokens = jTreeVarSelectedPath.split("/");

            String absoultePath="";

            for (int i=0; i<tokens.length-1; i++)
            {
                absoultePath += tokens[i];
            }

            System.out.println("\n AB: " + absoultePath);

            client.changeWorkingDirectory(absoultePath);

           String fileName = tokens[tokens.length-1];
           //String fileName = Arrays.toString(tokens);

            System.out.println("\n FileName: " + fileName);

            OutputStream os = new FileOutputStream(fileName);

            boolean status = client.retrieveFile(fileName, os);

            if (status)
            {
                System.out.print("Downloading Succesfful!");
            }
            else 
            {
                System.out.print("Downloading Failure!");
            }
             
            } catch (IOException ex) {
                System.out.println("IOException:" + ex);
             } 


            }
        });

    }
    public static int downloadFolder(FTPClient client,String workingPath )
    {
        // File file;

        // if (file.isDirectory())
        // {
        //     // Change working Directory to this directory.
        //     client.changeWorkingDirectory(file.getName());

        //     // Create the directory locally - in the right place
        //     File newDir = new File (base + "/" + ftpClient.printWorkingDirectory());
        //     newDir.mkdirs();

        //     // Recursive call to this method.
        //     download(client.printWorkingDirectory(), base);

        //     // Come back out to the parent level.
        //     client.changeToParentDirectory();
        // }
        return 0;
    }
    public static int downloadFile(FTPClient client,String workingPath )
    {
        return 0;
    }


    public static DefaultMutableTreeNode getFileStructure(FTPClient client,String workingPath )
    {
    
    DefaultMutableTreeNode style = null;
    try {
      
        FTPFile[] files = client.listFiles(workingPath);
        style=new DefaultMutableTreeNode(workingPath);
        for (FTPFile file : files) {
            if (file.isDirectory())
            {
                DefaultMutableTreeNode fileDirectory =new DefaultMutableTreeNode(file.getName());
                System.out.print("\n" +   workingPath + "/" + file.getName());
                style.add(fileDirectory);
                getFileStructure(client, workingPath + "/" + file.getName());
            } else {
                

            //     String[] tokens = client.printWorkingDirectory().split("/");
    
            //   //  style=new DefaultMutableTreeNode(tokens[tokens.length -1]);
            //   style=new DefaultMutableTreeNode("Desktop");

            //     System.out.print("\n" + "\t"+ file.getName());

                DefaultMutableTreeNode file_file=new DefaultMutableTreeNode(file.getName());  
                style.add(file_file);
                //style=new DefaultMutableTreeNode(workingPath);
           
            }


        }
  
     
    } catch (IOException ex) {
        System.out.println("IOException:" + ex);
     } 

     
     DefaultMutableTreeNode Mutastyle=new DefaultMutableTreeNode("Documnets");  

     DefaultMutableTreeNode file_file=new DefaultMutableTreeNode("file.getName()");  

     Mutastyle.add(file_file);
     return style;
    }
}
