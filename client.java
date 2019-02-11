import javax.sound.midi.Sequencer.SyncMode;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


import javax.swing.*;  
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;




public class client {

    static DefaultMutableTreeNode styles = new DefaultMutableTreeNode();
    static JTree jt= new JTree(styles);

    static DefaultMutableTreeNode getTree = new DefaultMutableTreeNode();
    static JTree local =new JTree(getTree);

    static JTextField IPtext;
    static JTextField usertext;
    static JPasswordField passtext;
    static JLabel Message;

  public static void main(String[] args) {
        // String home = System.getProperty("user.home");
        // File folder = new File(home);
        // File[] listOfFiles = folder.listFiles();
        // // System.out.println("listOfFiles size:"+ listOfFiles.length);
        // for (File f: listOfFiles)
        // {
        //     // System.out.println(f.getName());
        // }

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

        try {
        FTPClient client = new FTPClient();
    
        client.connect(IPtext.getText(),21);
        
        // client.connect("153.106.73.93",21);

        int reasonConnect = client.getReplyCode();
        // System.out.print("\n"+"Connect Status Code: "+reasonConnect + "\n");

        client.enterLocalPassiveMode();

        String username = usertext.getText().toString();

        String password = passtext.getText().toString();


        // System.out.print("IP:"+ IPtext.getText().toString() + "\n"+"Username:" + username + "\n"+  "Password:" + password);


        boolean loginStatus = client.login(username, password); 


        client.changeWorkingDirectory("~");




        int reasonLogin = client.getReplyCode();
        // System.out.print("\n"+"Login Status Code: "+reasonLogin + "\n");


        client.setFileType(client.BINARY_FILE_TYPE);
        client.setControlEncoding("UTF-8");
        
        


        if (loginStatus)
        {
            // System.out.print("\n"+"Sucess" + "\n");
            Message.setText("Sucess!");
            frame.remove(panel);
            frame.setSize(750,750);
            frame.setBackground(Color.WHITE);
            showFTP(frame,client);
        
    
        }
        else 
        {
            // System.out.print("\n"+"Failure" + "\n");
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
    });  
 
    }

    public static void showFTP( JFrame frame,FTPClient client)
    {
        JPanel panel = new JPanel();
        frame.add(panel);

        Message = new JLabel("");
        Message.setBounds(10, 115, 10, 10);
        panel.add(Message);
        
        JButton login = new JButton("Download");
        login.setBounds(10, 10, 10, 10);
        panel.add(login);
        panel.updateUI();
        panel.setVisible(true);

        

        JButton reconnect = new JButton("Reconnect");
        reconnect.setBounds(10, 10, 10, 10);
        panel.add(reconnect);
        panel.updateUI();
        panel.setVisible(true);

     


        JButton sendFile  = new JButton("Upload");
        sendFile.setBounds(10, 10, 10, 10);
        panel.add(sendFile);
        panel.updateUI();
        panel.setVisible(true);

        sendFile.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {
                ////////////////////Local Path /////////////////
                String localPath = "";
                Object[] localpaths = local.getSelectionPath().getPath();
                for (int i=0; i<localpaths.length; i++) {
                    localPath += localpaths[i];
                    if (i+1 <localpaths.length ) {
                        localPath += "/";
                    }

                }
                // System.out.print("\n"+"Clicked!" + localPath);
                //////////////////// Remote Path ////////////////////
                String remotePath = "";
                Object[] remotePaths = jt.getSelectionPath().getPath();
                for (int i=0; i<remotePaths.length; i++) {
                    remotePath += remotePaths[i];
                    if (i+1 <remotePaths.length ) {
                        remotePath += "/";
                    }

                }
                // System.out.print("\n"+"Clicked!" + remotePath);
                ////////////////////////////////////////////////////////////////////////

                File filetoSend = new File(localPath);

                FTPClient sendFileClient = new FTPClient();
                try {
                    sendFileClient.connect(IPtext.getText().toString());
                    sendFileClient.login(usertext.getText().toString(), passtext.getText().toString());
                    sendFileClient.changeWorkingDirectory(remotePath);
                    sendFileClient.setFileType(sendFileClient.BINARY_FILE_TYPE);
                    InputStream targetStream = new FileInputStream(filetoSend);


                   boolean status = sendFileClient.storeFile(filetoSend.getName(), targetStream);
                   sendFileClient.logout();
                   if (status)
                   {
                        Message.setText("Successfully uploaded!");
                   } else {
                        Message.setText("Failure to upload!");
                   }


                } catch (IOException a) {
                    // System.out.println("IOException" + a);
                }




            }
        });

        //////////////Remote FileSystem///////////////////
        DefaultMutableTreeNode exampleNode= new DefaultMutableTreeNode("~");
        styles = getFileStructure(client, "~",exampleNode);
        jt =new JTree(styles);
        jt.setEditable(true);
        jt.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jt.setShowsRootHandles(true);
        panel.add(jt);  
        panel.setBounds(100,100,100,100);
        panel.add(new JScrollPane(jt));
        panel.updateUI();
        panel.setVisible(true);

        reconnect.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e) {
                try {
                styles.removeAllChildren();
                panel.revalidate();
                panel.repaint();
                client.disconnect();
                client.connect(IPtext.getText().toString(),21);
                client.login(usertext.getText().toString(),passtext.getText().toString());
                DefaultMutableTreeNode styles = getFileStructure(client, "~",exampleNode);
                DefaultTreeModel model = new DefaultTreeModel(styles);
                jt.setModel(model);
                jt.setEditable(true);
                panel.updateUI();
              

            } catch (IOException ex) {
                // System.out.println("IOException:" + ex);
             } catch (NoClassDefFoundError exception) {
                // System.out.println("No Class Def Found Error");
             }
 
            }
        });


     ////////////////////////////////////////////////////////////////////


    //////////////Local FileSystem///////////////////


        String home = System.getProperty("user.home");
        File currentDir = new File(home); // current directory
    
        DefaultMutableTreeNode rootNode= new DefaultMutableTreeNode(home);
        getTree = getLocalStructure( currentDir,rootNode);
        local =new JTree(getTree);
        local.setEditable(true);
        local.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        local.setShowsRootHandles(true);
        panel.add(local);  
        panel.setBounds(100,100,100,100);
        panel.add(new JScrollPane(local));
        panel.updateUI();
        panel.setVisible(true);
    ////////////////////////////////////////////////////////////



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
            // System.out.print("\n"+"Clicked!" + jTreeVarSelectedPath);
                try {
                if (client == null)
                {
                    // System.out.print("\n"+"NULL!");
  
                }
            
            String[] tokens = jTreeVarSelectedPath.split("/");

            String absoultePath="";

            for (int i=0; i<tokens.length-1; i++)
            {
                absoultePath += tokens[i] + "/";
            }

            // System.out.println("\n AB: " + absoultePath);

            client.changeWorkingDirectory(absoultePath);

           String fileName = tokens[tokens.length-1];
           //String fileName = Arrays.toString(tokens);

            // System.out.println("\n FileName: " + fileName);

            OutputStream os = new FileOutputStream(fileName);

            boolean status = client.retrieveFile(fileName, os);

            if (status)
            {
                Message.setText("Sucessfully downloaded!");
            }
            else 
            {
                Message.setText("Failure to download!");
            }
             
            } catch (IOException ex) {
                // System.out.println("IOException:" + ex);
             } 


            }
        });

    }

    public static DefaultMutableTreeNode getLocalStructure(File workingPath,DefaultMutableTreeNode style )
    {
        // System.out.println("Working Path: " + workingPath.toString());
        File[] listOfFiles = workingPath.listFiles();

        DefaultMutableTreeNode node = buildNodeFromString(workingPath.toString());
        DefaultMutableTreeNode lastLeaf = node.getLastLeaf();
        TreePath path = new TreePath(lastLeaf.getPath());

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)(path.getLastPathComponent());       
        // System.out.println("Num of Files:" +listOfFiles.length);

        for (File file : listOfFiles) {
        // System.out.println(file.getName());

            if (file.isDirectory() && !file.isHidden())
            {

              // System.out.println("getFileStructure-selectedNodeFORLOOP:" +selectedNode.getChildCount() );
              DefaultTreeModel model = new DefaultTreeModel(style);

              insertNodes(model,findLocalNodeParent(model, workingPath.toString()) , selectedNode); 

              getLocalStructure(new File(workingPath.toString() + "/" + file.getName()),style);
            } else if (!file.isHidden()) {
            DefaultMutableTreeNode root =new DefaultMutableTreeNode();  
            // System.out.println("getFileStructure-Filename:" + file.getName());
           
            DefaultMutableTreeNode fileDirectory =new DefaultMutableTreeNode(file.getName());

            
            selectedNode.add(fileDirectory);


            // System.out.println("Depth Count= " + selectedNode.getChildCount());
            }
        }
        // System.out.println("getFileStructure-selectedNode:" +"Heello you got here!" );
        // System.out.println("getFileStructure-selectedNode:" +selectedNode.getChildCount() );

        DefaultTreeModel model = new DefaultTreeModel(style);
        if (model == null) {/* System.out.println("MODEL IS NULL");*/}
    if (findNodeParent(model, workingPath.toString()) == null) {/* System.out.println("findNodeParent(model, workingPath) IS NULL");*/}
        if (selectedNode == null) {/* System.out.println("selectedNode IS NULL");*/}

       
        insertNodes(model,findLocalNodeParent(model, workingPath.toString()) , selectedNode); 
   
 

     return style;
    }
    protected static DefaultMutableTreeNode findLocalNodeParent(DefaultTreeModel model, String path) {
        String root= "/home/justin";

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
        
        // System.out.println("Path:" + path);
        // System.out.println("Length of Path:" + path.split("/home/justin").length);

        if (path.split("/home/justin").length >0) {
        String parts_array = path.split("/home/justin")[path.split("/home/justin").length-1];
        String[] partst = parts_array.split("/");       
        ArrayList<String> list = new ArrayList<String>();
        for (String lol: partst)
        {
            if (!lol.trim().equals(""))
            {
                list.add(lol.trim());

            }
        }
        List<String> lists = new ArrayList<String>();
        for(String s : list) {
           if(s != null && s.length() > 0) {
            lists.add(s);
           }
        }
        String[] parts = new String[lists.size()];
        parts = lists.toArray(new String[list.size()]);

     

        // System.out.println("File extension:" + Arrays.asList(parts));

        ArrayList<String> ae = new ArrayList<String> ();
        ae.add(root);
        for (String s: parts)
        {
            ae.add(s);
        }
        String[] stringArray = ae.toArray(new String[parts.length + 1]);
        if (node.getUserObject().toString().equals(stringArray[0])) {
            return findLocalNodeParent(node, Arrays.copyOfRange(stringArray, 1, stringArray.length));
       }

        }else {
            ArrayList<String> stringArrays = new ArrayList<String>();
            stringArrays.add(root);
            String[] stringArray = stringArrays.toArray(new String[1]);
        
        if (node.getUserObject().toString().equals(stringArray[0])) {
             return findLocalNodeParent(node, Arrays.copyOfRange(stringArray, 0, 1));
        }
        }

    
        
        return null;
    }  
    protected static DefaultMutableTreeNode findLocalNodeParent(DefaultMutableTreeNode node, String[] path) {
        if (path.length == 1) {
            // System.out.println("findNode-ParentNode:" + node.getUserObject().toString()
            //                    + " findNode-ParentNodeChildrenCount:" + node.getChildCount());
            return node;
        }
    
        Enumeration<TreeNode> children =  node.children();
    
        if (path.length > 0) {
        
      //  DefaultMutableTreeNode tv = new DefaultMutableTreeNode(path[0]);
     
        while (children.hasMoreElements()) {
          //  // System.out.println("2");
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
    
    
          //  // System.out.println("findNode-childName:" + child.getUserObject().toString());
         //  // System.out.println("findNode-currentFolder:" + path[0]);
    
    
            if (child == null) {/* System.out.println("findNode-child IS NULL");*/}
            if ( Arrays.copyOfRange(path, 1, path.length) == null) {/* System.out.println("findNode-newNode IS NULL");*/}
    
    
            if (child.getUserObject().toString().equals(path[0])) {
                return findNodeParent(child, Arrays.copyOfRange(path, 1, path.length));
                
            }
        }
       
        }
        else {
            return node;
        }
        return null;
    }

    public static DefaultMutableTreeNode getFileStructure(FTPClient client,String workingPath,DefaultMutableTreeNode style )
    {
        try {
        // System.out.println("\n");
        // System.out.println("Working Path: " + workingPath);
            FTPFile[] files = client.listFiles(workingPath);
            DefaultMutableTreeNode node = buildNodeFromString(workingPath);
            DefaultMutableTreeNode lastLeaf = node.getLastLeaf();
            TreePath path = new TreePath(lastLeaf.getPath());
        // // System.out.println("Path =" + path);
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)(path.getLastPathComponent());       
        //  // System.out.println("Add to Node:" + selectedNode.toString());
            for (FTPFile file : files) {
                if (file.isDirectory())
                {
                //   DefaultMutableTreeNode fileDirectory =new DefaultMutableTreeNode(file.getName());
                //   style.add(fileDirectory);

                //  // System.out.println("\t Folder Name: " + file.getName());
                // System.out.println("getFileStructure-selectedNodeFORLOOP:" +selectedNode.getChildCount() );
                DefaultTreeModel model = new DefaultTreeModel(style);

                DefaultMutableTreeNode parent = findNodeParent(model, workingPath);
                insertNodes(model,parent , selectedNode);   
                getFileStructure(client, workingPath + "/" + file.getName(),style);
                } else {
                DefaultMutableTreeNode root =new DefaultMutableTreeNode();  
                // System.out.println("getFileStructure-Filename:" + file.getName());
            
                DefaultMutableTreeNode fileDirectory =new DefaultMutableTreeNode(file.getName());

                
                selectedNode.add(fileDirectory);

            // // System.out.println("Added to node = " + selectedNode.toString());
            //  // System.out.println("First Node = " + selectedNode.getFirstChild().toString());
            //  // System.out.println("Last Node = " + selectedNode.getLastChild().toString());
            //  // System.out.println("Num of Children = " + selectedNode.getLeafCount());
                // System.out.println("Depth Count= " + selectedNode.getChildCount());
                }
            }
            
            // System.out.println("getFileStructure-selectedNode:" +"Heello you got here!" );
            // System.out.println("getFileStructure-selectedNode:" +selectedNode.getChildCount() );

            DefaultTreeModel model = new DefaultTreeModel(style);
            if (model == null) {/* System.out.println("MODEL IS NULL")*/}
            if (findNodeParent(model, workingPath) == null) {/* System.out.println("findNodeParent(model, workingPath) IS NULL");*/}
            if (selectedNode == null) {/* System.out.println("selectedNode IS NULL");*/}

            DefaultMutableTreeNode parent = findNodeParent(model, workingPath);
            insertNodes(model,parent , selectedNode); 
        } catch (IOException ex) {
            // System.out.println("IOException:" + ex);
        } 
        return style;
    }


    private static void insertNodes(
        DefaultTreeModel model,
        DefaultMutableTreeNode parent,
        DefaultMutableTreeNode newNode) {
    
        //    // System.out.println("insertNodes-Entered Function");
            

        if (parent == null) {/* System.out.println("insertNodes-Parent IS NULL");*/}
        if (newNode == null) {/* System.out.println("insertNodes-newNode IS NULL");*/}
        // System.out.println("insertNodes-childCount:" + parent.toString());

        // DefaultMutableTreeNode tn = findNodeParent(model,"~/Documents");
        // if (tn != null) {/* System.out.println("insertNodes- Documents exists!");}

        parent.add(newNode);
        model.nodesWereInserted(parent, new int[]{parent.getChildCount() - 1});
        }
    

   
    
    
   
        
        
    protected static DefaultMutableTreeNode findNodeParent(DefaultTreeModel model, String path) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
        // path = "~"+ path;
        String[] parts = path.split("/");
        // System.out.println("findNodeParent-model:" + node.getUserObject().toString());
        // System.out.println("findNodeParent-path:" + Arrays.toString(parts));
        // System.out.println("findNodeParent-path0:" + parts[0]);

        if (node.getUserObject().toString().equals(parts[0])) {
                return findNodeParent(node, Arrays.copyOfRange(parts, 1, parts.length));
        }
        return null;
        }



    
        protected static DefaultMutableTreeNode findNodeParent(DefaultMutableTreeNode node, String[] path) {
            if (path.length == 1) {
                // System.out.println("findNode-ParentNode:" + node.getUserObject().toString() + " findNode-ParentNodeChildrenCount:" + node.getChildCount());
                return node;
            }

            Enumeration<TreeNode> children =  node.children();

            if (path.length > 0) {

            DefaultMutableTreeNode tv = new DefaultMutableTreeNode(path[0]);

            while (children.hasMoreElements()) {
                //  // System.out.println("2");
                DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();


                // System.out.println("findNode-childName:" + child.getUserObject().toString());
                // System.out.println("findNode-currentFolder:" + path[0]);


                if (child == null) {/* System.out.println("findNode-child IS NULL");*/}
            if ( Arrays.copyOfRange(path, 1, path.length) == null) {/* System.out.println("findNode-newNode IS NULL");*/}


                if (child.getUserObject().toString().equals(path[0])) {
                    return findNodeParent(child, Arrays.copyOfRange(path, 1, path.length));
                    
                }


            }

            }
            else {
            return node;
            }
            
            return null;
    
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
