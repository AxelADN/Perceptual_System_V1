/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import kmiddle.nodes.NodeConfiguration;
import semanticmemory.areas.amy.Amygdala;
import semanticmemory.areas.blackbox.ExternalAmyConnection;
import semanticmemory.areas.ca1.CornuAmmonis1;
import semanticmemory.areas.ca3.CornuAmmonis3;
import semanticmemory.areas.dg.DentateGyrus;
import semanticmemory.areas.enc.EntorhinalCortex;
import semanticmemory.areas.hpa.HPA;
import semanticmemory.areas.itca.ITCAnterior;
import semanticmemory.areas.itcl.ITCLateral;
import semanticmemory.areas.itcm.ITCMedial;
import semanticmemory.areas.lpc.LateralPrefrontalCortex;
import semanticmemory.areas.pdo.PDO;
import semanticmemory.areas.pm.PremotorCortex;
import semanticmemory.areas.prc.PerirhinalCortex;
import semanticmemory.areas.sb.Subiculum;
import semanticmemory.areas.vai.VisualArea;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import static semanticmemory.config.AreaNames.Amygdala;
import semanticmemory.data.SemanticObjects;
import semanticmemory.data.SimpleSemanticObject;
import semanticmemory.gui.DrawingPanel;
import semanticmemory.spikes.DebugSpike;
import semanticmemory.utils.GeneralUtils;
import semanticmemory.utils.ObjectGenerator;


/**
 *
 * @author Luis
 */
public class SemanticMemoryGUI extends javax.swing.JFrame {

    /**
     * Creates new form SemanticMemoryGUI
     */
    
    private char testClassData[][] = new char[][]{
        {'R', 0, 'Z', 'Z', 0, 0, 0, 'Z', 'Z'},
        {'R', 'A', 'Z', 'Z', 0, 0, 0, 'Z', 'Z'},
        {'R', 0, 'Z', 'Z', 0, 0, 0, 'Z', 'Z'},
        {'R', 'A', 'Z', 'Z', 0, 0, 'P', 'Z', 'Z'},
        {'R', 'A', 'Z', 'Z', 'B', 0, 'P', 'Z', 'Z'},
        {'R', 'A', 'Z', 'Z', 'B', 'B', 'P', 'Z', 'Z'}
    }; //T4
    
    private int currentTest = 0;
    private int currentTest2 = 0;
     
    private int relativeTime = 1;
    private DrawingPanel dPanel;
    private long startTime = 0;
    
    private SimpleSemanticObject tmpSemObj;
    private SemanticObjects currentObjects;
    private ArrayList<SimpleSemanticObject> sceneObjects;

    private VisualArea vai;
    
    private ITCMedial itcm;
    private ITCLateral itcl;
    private PremotorCortex pm;
    private ITCAnterior itca;
    
    private DentateGyrus dg;
    
    private PerirhinalCortex prc;
    private EntorhinalCortex enc;
    
    private CornuAmmonis1 ca1;
    private CornuAmmonis3 ca3;
    
    private Subiculum sb;
    
    private LateralPrefrontalCortex lpc;
       
    private PDO pdo;
    
    private Amygdala amy;
    private HPA hpa;
    
    //BlackBox
    
    private ExternalAmyConnection externalAmy;
    

    public SemanticMemoryGUI() {
        initComponents();
        

        currentObjects = new SemanticObjects();
        sceneObjects = new ArrayList<>();

        startTime = System.currentTimeMillis();

        dPanel = new DrawingPanel();
        dPanel.setListener(this);

        contentPanel.add(dPanel);
        
        sceneObjectsList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    
                    SimpleSemanticObject sso = sceneObjects.get(index);
                    sso.setTimeStamp(relativeTime);
                    
                    currentObjects.add(sso);
                                        
                    dPanel.addObject(sso);
                    
                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = list.locationToIndex(evt.getPoint());
                }else{
                    
                    int index = list.locationToIndex(evt.getPoint());
                    
                    SimpleSemanticObject sso = sceneObjects.get(index);
                                       
                    tmpSemObj = new SimpleSemanticObject(sso.getObjectId());
                    tmpSemObj.setAttributes(sso.getAttributes());
                    tmpSemObj.setIntensity(sso.getIntensity());
                    tmpSemObj.setDimensions((int)(Math.random()*dPanel.getWidth()),(int)(Math.random()*dPanel.getHeight()), 50, 50);
                    tmpSemObj.setType(sso.getType());
                    tmpSemObj.setTimeStamp(relativeTime);
                                       
                    newIdTxt.setText(sso.getObjectId()+"");
                            
                }
            }
         });

      start();
    }
    
    public int getSelectedType(){
        return this.tipoObjeto.getSelectedIndex();
    }
    
    public void addSceneObject(SimpleSemanticObject object){
        
        DefaultListModel dlm = (DefaultListModel) sceneObjectsList.getModel();
         
        
        sceneObjects.add(object);
        currentObjects.add(object);
       
        dlm.addElement(object);
        
        //objectsHistory.append(object.toString() + "\n");
    }

    public SimpleSemanticObject generateObject(int x, int y) {
        
        SemanticObjects so = ObjectGenerator.generate(1, relativeTime, x, y, tipoObjeto.getSelectedIndex());

        SimpleSemanticObject sso = (SimpleSemanticObject) so.get(0);

        currentObjects.add(sso);

        //this.objectsHistory.append(sso.toString() + "\n");
        
        return sso;

    }

    /*
    * Inicia los procesos del middleware
     */
    public void start() {

        /*
        * Configuración para los nodos, en caso de que se requiera una diferente se creara una por nodo.
         */
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        conf.setLoadBalance(AppConfig.LOAD_BALANCE);

        vai = new VisualArea(AreaNames.VisualArea, conf);

        /*
        pm = new PremotorCortex(AreaNames.PremotorCortex, conf);
        itca = new ITCAnterior(AreaNames.ITCAnterior, conf);
        */
        
        
        itcm = new ITCMedial(AreaNames.ITCMedial, conf);
        itcl = new ITCLateral(AreaNames.ITCLateral, conf);
        
        
        prc = new PerirhinalCortex(AreaNames.PerirhinalCortex, conf);
        enc = new EntorhinalCortex(AreaNames.EntorhinalCortex, conf);
        
        
        
        ca1 = new CornuAmmonis1(AreaNames.CornuAmmonis1, conf);
        ca3 = new CornuAmmonis3(AreaNames.CornuAmmonis3, conf);
        
        sb = new Subiculum(AreaNames.Subiculum, conf);
        
        
        
        lpc = new LateralPrefrontalCortex(AreaNames.LateralPrefrontalCortex, conf);
        
        pdo = new PDO(AreaNames.PDO, conf);
        
        dg = new DentateGyrus(AreaNames.DentateGyrus, conf);
        
        amy = new Amygdala(AreaNames.Amygdala, conf);
       
        hpa = new HPA(AreaNames.HPA, conf);
        
        externalAmy = new ExternalAmyConnection(AreaNames.AmyExternalConnection, conf);
       
        /*       
        MedialPrefrontalCortex mpc = new MedialPrefrontalCortex(AreaNames.MedialPrefrontalCortex, conf);
        AnteriorPrefrontalCortex apc = new AnteriorPrefrontalCortex(AreaNames.AnteriorPrefrontalCortex, conf);
        */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        sceneObjectsList = new javax.swing.JList<>();
        contentPanel = new javax.swing.JPanel();
        enviar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        tipoObjeto = new javax.swing.JComboBox<>();
        tiempoTxt = new javax.swing.JLabel();
        newIdTxt = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Semantic Memory");

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Objects"));

        jScrollPane2.setViewportView(sceneObjectsList);
        sceneObjectsList.setModel(new DefaultListModel<String>());

        contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Scene"));
        contentPanel.setLayout(new java.awt.BorderLayout());

        enviar.setText("Start");
        enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarActionPerformed(evt);
            }
        });

        jButton1.setText("Amy");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Time");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        tipoObjeto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Animated", "Inanimated", "Both" }));
        tipoObjeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoObjetoActionPerformed(evt);
            }
        });

        tiempoTxt.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        tiempoTxt.setText("1");

        jButton4.setText("Add");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("New ID:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(enviar)
                        .addGap(18, 18, 18)
                        .addComponent(tipoObjeto, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tiempoTxt)
                        .addGap(22, 22, 22)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(enviar)
                        .addComponent(jButton1)
                        .addComponent(tipoObjeto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3)
                        .addComponent(tiempoTxt)
                        .addComponent(newIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4)
                        .addComponent(jLabel1))
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarActionPerformed
        
        vai.afferents(AreaNames.Default, GeneralUtils.objectToBytes(currentObjects));

    }//GEN-LAST:event_enviarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:      
        externalAmy.setIntensity(jSlider1.getValue());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        currentObjects.clear();
        dPanel.clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        relativeTime++;
        tiempoTxt.setText(relativeTime+"");
        dPanel.setRelativeTime(relativeTime);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        tmpSemObj.setObjectId(ObjectGenerator.getNextID());
        
        addSceneObject(tmpSemObj);
        dPanel.addObject(tmpSemObj);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tipoObjetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoObjetoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoObjetoActionPerformed

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
            java.util.logging.Logger.getLogger(SemanticMemoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SemanticMemoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SemanticMemoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SemanticMemoryGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SemanticMemoryGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton enviar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField newIdTxt;
    private javax.swing.JList<String> sceneObjectsList;
    private javax.swing.JLabel tiempoTxt;
    private javax.swing.JComboBox<String> tipoObjeto;
    // End of variables declaration//GEN-END:variables
}
