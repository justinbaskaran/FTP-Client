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
import java.util.Enumeration;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;



import javax.swing.*;  
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;



public class client {

    static DefaultMutableTreeNode styles = new DefaultMutableTreeNode();
    static JTree jt= new JTree(styles);

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
        JPanel panel = new JPanel();
        frame.add(panel);

        
        JButton login = new JButton("GET");
        login.setBounds(10, 10, 10, 10);
        panel.add(login);
        panel.updateUI();
        panel.setVisible(true);

        
        DefaultMutableTreeNode exampleNode= new DefaultMutableTreeNode("~");
        styles = getFileStructure(client, "~",exampleNode);
        jt =new JTree(styles);
        jt.setEditable(true);
        jt.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jt.setShowsRootHandles(true);
        panel.add(jt);  
        panel.add(new JScrollPane(jt));
        panel.updateUI();
        panel.setVisible(true);


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);
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
            System.out.print("\n"+"Clicked!" + jTreeVarSelectedPath);
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


    public static DefaultMutableTreeNode getFileStructure(FTPClient client,String workingPath,DefaultMutableTreeNode style )
    {
    
    try {
        System.out.println("\n");
        System.out.println("Working Path: " + workingPath);
        FTPFile[] files = client.listFiles(workingPath);
        DefaultMutableTreeNode node = buildNodeFromString(workingPath);
        DefaultMutableTreeNode lastLeaf = node.getLastLeaf();
        TreePath path = new TreePath(lastLeaf.getPath());
        System.out.println("Path =" + path);
        
        
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)(path.getLastPathComponent());       


        for (FTPFile file : files) {
            if (file.isDirectory())
            {
                getFileStructure(client, workingPath + "/" + file.getName(),style);
            } else {
            DefaultMutableTreeNode root =new DefaultMutableTreeNode();  
            System.out.println("Filename: " + file.getName());
           
            DefaultMutableTreeNode fileDirectory =new DefaultMutableTreeNode(file.getName());

            
            selectedNode.add(fileDirectory);

            System.out.println("Added to node = " + selectedNode.toString());
            System.out.println("First Node = " + selectedNode.getFirstChild().toString());
            System.out.println("Last Node = " + selectedNode.getLastChild().toString());
            System.out.println("Num of Children = " + selectedNode.getLeafCount());
            System.out.println("Depth Count= " + selectedNode.getDepth());
            }
        }


        style.add(selectedNode);
    } catch (IOException ex) {
        System.out.println("IOException:" + ex);
     } 

     
   
     return style;
    }

    private static DefaultMutableTreeNode buildNodeFromString(String path)
    {
        String[] s = path.split("/");
        DefaultMutableTreeNode node, lastNode = null, root = null;
        for(String str : s)
        {

            node = new DefaultMutableTreeNode(str);
            if(root == null)
                root = node;
            if(lastNode != null)
                lastNode.add(node);
            lastNode = node;
        }
        return root;        
    }






}
