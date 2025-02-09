/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import utils.FileUtils;

/**
 * Generador de Big Nodes y Small nodes sencillo
 * Para construir el generador y ejecutarlo sin compilar, tendrán que cambiar la clase principal 
 * en las propiedades del IDE
 * @author Luis Humanoide
 */
public class NodeGenerator extends javax.swing.JFrame {

    /**
     * La ruta donde se deberían generar los nodos
     */
    String route;

    /**
     * Variables para la creación del arbol de directorios
     */
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    //Frame para elegir la ubicación de los nuevos directorios a crear
    DirectoryFrame dframe;

    /**
     * Constructor de la clase
     */
    public NodeGenerator() {
        //Leer la ruta principal
        route=FileUtils.readFile(new File("route.txt")).trim();
        //Iniciar componentes
        initComponents();
        //Raiz del arbol de elección de archivos
        root = new DefaultMutableTreeNode("Nodes", true);
        //Actualizar rbol
        updateTree();
        //El boton de crear directorio esta deshabilitado hasta que se seleccione una ruta
        jButton3.setEnabled(false);
        //Declarar el frame de creación de directorio
        dframe = new DirectoryFrame(this);
        dframe.setLocation((int) this.getLocation().getX(), this.getHeight() + this.getY());
        //Iniciar el arbol
        renderTree();
        //leer los archivos de templates
        readConfigs();
    }

    /**
     * Renderizar el arbol de rutas
     */
    void renderTree() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTree1.getCellRenderer();
        Icon closedIcon = new ImageIcon("icon.png");
        Icon openIcon = new ImageIcon("icon.png");
        Icon leafIcon = new ImageIcon("icon.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
    }

    /**
     * Obtener la lista para la creación del arbol de directorios
     * @param node
     * @param f 
     */
    public void getList(DefaultMutableTreeNode node, File f) {
        if (f.isDirectory()) {
            // We keep only JAVA source file for display in this HowTo
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
            node.add(child);
            File fList[] = f.listFiles();
            for (int i = 0; i < fList.length; i++) {
                getList(child, fList[i]);
            }
        }
    }

    /**
     * Actualizar el arbol, siempre deben de tener una carpeta llamada Nodes
     */
    void updateTree() {
        root.removeAllChildren();
        getList(root, new File(route + "nodes/"));
        treeModel = new DefaultTreeModel(root);
        jTree1.setModel(treeModel);
        expandAllNodes();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    List possible;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel3 = new javax.swing.JLabel();
        possible = new ArrayList();
        jTextField1 = new Java2sAutoTextField(possible);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("K-Middleware Node Generador");
        setBackground(new java.awt.Color(51, 51, 51));
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(35, 35, 35));

        jScrollPane2.setToolTipText("");

        jTree1.setBackground(new java.awt.Color(153, 153, 153));
        jTree1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTree1.setForeground(new java.awt.Color(204, 204, 255));
        jTree1.setRootVisible(false);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Route:");

        jTextField1.setBackground(new java.awt.Color(51, 51, 51));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setToolTipText("Area name");

        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Big node name:");

        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Small nodes:");

        jButton1.setBackground(new java.awt.Color(44, 56, 56));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Create nodes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setWheelScrollingEnabled(false);

        jTextArea1.setBackground(new java.awt.Color(51, 51, 51));
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jTextArea1.setToolTipText("");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setAutoscrolls(false);
        jScrollPane1.setViewportView(jTextArea1);

        jButton2.setBackground(new java.awt.Color(102, 102, 102));
        jButton2.setText("X");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(44, 56, 56));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Create new directory");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Templates para la creación de BigNodes y SmallNodes
     */
    String BigNodeTemplate = FileUtils.readFile(new File("TemplateBigNode.java"));
    String ProcessTemplate = FileUtils.readFile(new File("TemplateProcess.java"));
    String AreaNames;
    String InitClass;
    String BigNodeClass;
    String[] Process;
    boolean exist = false;

    /**
     * Lee los archivos de las templates
     */
    public void readConfigs() {
        AreaNames = FileUtils.readFile(new File(route + "config/AreaNames.java"));
        InitClass = FileUtils.readFile(new File(route + "config/Init.java"));
    }

    /**
     * Expande todos los nodos del arbol
     */
    private void expandAllNodes() {
        int j = jTree1.getRowCount();
        int i = 0;
        while (i < j) {
            jTree1.expandRow(i);
            i += 1;
            j = jTree1.getRowCount();
        }
    }

    /**
     * Checa si existe el BigNode cuando se pone el nombre en el field de BigNode
     * No poner smallNode
     * @param path
     * @param name
     * @return
     */
    public boolean bigNodeExist(String path, String name) {
        String[] files = FileUtils.getFiles(path);
        boolean isRepeated = false;
        for (String file : files) {
            if (file.equals(path + "\\" + name + ".java")) {
                JOptionPane.showMessageDialog(null, "Process to the existing big node " + file + " will be added");
                isRepeated = true;
                break;
            }
        }
        return isRepeated;
    }

    /**
     * Código que se ejecuta al presionar el botón de creación de nodos
     * @param evt 
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       // TODO add your handling code here:
        //Los procesos (SmallNodes) serán separados por enters
        Process = jTextArea1.getText().split("(?=\\s)");
        //Nombre del BigNode
        String Name = jTextField1.getText().trim();
        
        if (Name.trim().length() > 0) {
            //checar si el BigNode se repite, así se agregarán los procesos al BigNode existente
            exist = bigNodeExist(path, Name);
            BigNodeClass = BigNodeTemplate.replaceAll("@Name", Name);
            String addProcess = "";
            String sendProcess = "";
            String addAreas = "";
            String initNodes = "";
            String Import = "";
            String Pack = "";
            String existingClass = "";
            if (exist) {
                existingClass = FileUtils.readFile(new File(path + "\\" + Name + ".java"));
            }
            
            Pack = "package " + path.replace("src\\", "").replace("\\", ".") + ";\n\n";
            String packroute=route.replace("src/", "").replaceAll("/", ".");
            packroute=packroute.substring(0, packroute.length()-1);
            
            if (path != null) {
                for (String process : Process) {
                    if (process.length() > 0) {
                        addProcess = addProcess + "addProcess(" + process.trim() + ".class);\n\t";
                        sendProcess = sendProcess + "send(AreaNames." + process.trim() + ",data);\n\t";
                        //Escribe un nuevo archivo Java a partir del template de Process
                        FileUtils.write(path + "/" + process.trim(), ProcessTemplate.replaceAll("@Process", process.trim()).replaceAll("@route", packroute).replace("@package", Pack), "java");
                        //Añade el proceso al BigNode existente
                        addAreas = addAreas + "public static int " + process.trim() + " = IDHelper.generateID(\"" + Name + "\", @insertNumber , 0);\n\t";
                    }
                }
                /*
                Si el Big Node es Nuevo
                 */

                if (!exist) {
                    /**
                     * Modificación del template de BigNode
                     */
                    BigNodeClass = BigNodeClass.replaceAll("@AddProcess", addProcess + "\n//@AddProcess");
                    BigNodeClass = BigNodeClass.replaceAll("@package", Pack);
                    BigNodeClass = BigNodeClass.replaceAll("@route", packroute);
                    BigNodeClass = BigNodeClass.replaceAll("@SendProcess", sendProcess + "\n//@SendProcess");

                    addAreas = "public static int " + Name + " = IDHelper.generateID(\"" + Name + "\", 0, 0);\n\t" + addAreas;
                    initNodes = Name + ".class.getName(),";
                    Import = "import " + path.replace("src\\", "").replace("\\", ".") + "." + Name;
                    //Crea la clase del BigNode
                    FileUtils.write(path + "/" + Name, BigNodeClass, "java");
                    //Modifica la clase AreaNames.java
                    FileUtils.write(route + "config/AreaNames", AreaNames.replace("//@addNodes", addAreas + "\n\t" + "//@addNodes"), "java");
                    //Agrega el area a init
                    FileUtils.write(route + "config/Init", InitClass.replace("//@addNodes", initNodes + "\n\t\t" + "//@addNodes")
                            .replace("//@import", Import + ";\n" + "//@import"), "java");
                    //Actualiza las variables que almacenan las templates
                    readConfigs();
                }
                //Si el BigNode existe
                if (exist) {
                    //Reescribe en la clase del BigNode existente
                    existingClass = existingClass.replaceAll("//@AddProcess", addProcess + "\n\t//@AddProcess");
                    existingClass = existingClass.replaceAll("//@SendProcess", sendProcess + "\n\t//@SendProcess");
                    //Agrega el proceso a AreaNames
                    FileUtils.write(route + "config/AreaNames", AreaNames.replace("//@addNodes", addAreas + "\n\t" + "//@addNodes"), "java");
                    FileUtils.write(path + "/" + Name, existingClass, "java");
                    readConfigs();
                }
                
                //Reescribe el archivo AreaNames para asignar indices de procesos
                rewriteAreaNames(Name);

                jTextField1.setText("");
                jTextArea1.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "choose a valid path");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

     private int matches(String keyword, String text) {
        int matches = 0;
        Matcher matcher = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE).matcher(text);
        while (matcher.find()) {
            matches++;
        }
        return matches;
    }

    String replace(String text, String key, String areaName) {
        int m = matches(key, text);
        int t = matches("\"" + areaName + "\"", text);
        for (int i = t - m; i < t; i++) {
            text = text.replaceFirst(key, "" + (i));
        }
        return text;
    }

    /*
    Reescribir AreaNames para asignar el ID a los procesos
    */
    void rewriteAreaNames(String areaName) {
        String newAreaNames = replace(AreaNames, "@insertNumber", areaName);
        FileUtils.write(route + "config/AreaNames", newAreaNames, "java");
        readConfigs();
    }

    

    static String path;
    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        path = evt.getPath().getLastPathComponent().toString();
        dframe.setText(path+"\\");
        listBigNodes(path);
        if (possible.size() > 0) {
            jTextField1.setText((String) possible.get(0));
        }
        jButton3.setEnabled(true);
    }//GEN-LAST:event_jTree1ValueChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        dframe.setVisible(true);
        dframe.setText(path + "\\");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // TODO add your handling code here:
        dframe.setLocation((int) this.getLocation().getX(), this.getHeight() + this.getY());
    }//GEN-LAST:event_formComponentMoved

    /**
     * Crear nuevo directorio
     * @param dir 
     */
    public void createDir(String dir) {
        File directorio = new File(dir);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                updateTree();
            } else {
                JOptionPane.showMessageDialog(null, "Problem creating the directory");
            }
        }
    }

    /**
     * Hace la lista de BigNodes existente, para la función de autocompletar
     * @param path 
     */
    void listBigNodes(String path) {
        possible.clear();
        String[] files = FileUtils.getFiles(path);
        for (int i = 0; i < files.length; i++) {

            String fileText = FileUtils.readFile(new File(files[i]));
            if (fileText.contains("extends Area")) {
                String fileName = files[i].replaceAll(route, "").replaceAll(".java", "");
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                possible.add(fileName);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NodeGenerator.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NodeGenerator.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NodeGenerator.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NodeGenerator.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NodeGenerator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
