/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * This class allows the user to change options between every time the program
 * is loaded. It reads and writes from the ROV Options text file from the program
 * folder
 * 
 * @author Marius Nonsvik
 */
public class OptionsFrame extends javax.swing.JFrame {

    File file = new File("ROV Options.txt");
    String defaultIP;
    ArrayList channels = new ArrayList<String>();
    private Data data;

    /**
     * Creates new form OptionsFrame
     * @param data Data containing shared variables.
     */
    public OptionsFrame(Data data) {
        initComponents();
        this.data = data;
        getOptionsFromFile();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelIPHeader = new javax.swing.JLabel();
        jButtonOK = new javax.swing.JButton();
        jButtonApply = new javax.swing.JButton();
        jButtonCanel = new javax.swing.JButton();
        jTextFieldIP = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelChannelsHeader = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldChannel1 = new javax.swing.JTextField();
        jTextFieldChannel2 = new javax.swing.JTextField();
        jTextFieldChannel4 = new javax.swing.JTextField();
        jTextFieldChannel3 = new javax.swing.JTextField();
        jTextFieldChannel5 = new javax.swing.JTextField();
        jTextFieldChannel6 = new javax.swing.JTextField();
        jTextFieldChannel7 = new javax.swing.JTextField();
        jTextFieldChannel8 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");
        setBackground(new java.awt.Color(39, 44, 50));
        setResizable(false);

        jLabelIPHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelIPHeader.setText("Default IP");

        jButtonOK.setText("OK");
        jButtonOK.setPreferredSize(new java.awt.Dimension(65, 25));
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jButtonApply.setText("Apply");
        jButtonApply.setPreferredSize(new java.awt.Dimension(65, 25));
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });

        jButtonCanel.setText("Cancel");
        jButtonCanel.setPreferredSize(new java.awt.Dimension(65, 25));
        jButtonCanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCanelActionPerformed(evt);
            }
        });

        jTextFieldIP.setText("123.123.123.123");
        jTextFieldIP.setToolTipText("");

        jLabelChannelsHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelChannelsHeader.setText("Custom I/O");

        jLabel1.setText("<html>Channel 1<br/>Intput");

        jLabel2.setText("<html>Channel 2<br/>Input");

        jLabel3.setText("<html>Channel 5<br/>Output");

        jLabel4.setText("<html>Channel 3<br/>Input");

        jLabel5.setText("<html>Channel 4<br/>Input");

        jLabel6.setText("<html>Channel 7<br/>Output");

        jLabel7.setText("<html>Channel 6<br/>Output");

        jLabel8.setText("<html>Channel 8<br/>Output");

        jTextFieldChannel1.setText("Channel 1");

        jTextFieldChannel2.setText("Channel 2");

        jTextFieldChannel4.setText("Channel 4");

        jTextFieldChannel3.setText("Channel 3");

        jTextFieldChannel5.setText("Channel 5");
        jTextFieldChannel5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldChannel5ActionPerformed(evt);
            }
        });

        jTextFieldChannel6.setText("Channel 6");
        jTextFieldChannel6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldChannel6ActionPerformed(evt);
            }
        });

        jTextFieldChannel7.setText("Channel 7");

        jTextFieldChannel8.setText("Channel 8");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonApply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCanel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldChannel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldChannel6, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldChannel7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldChannel8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelIPHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelChannelsHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jLabel3)
                                    .addComponent(jTextFieldChannel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldChannel2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextFieldChannel3))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldChannel4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelIPHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelChannelsHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldChannel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldChannel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldChannel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldChannel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldChannel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldChannel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldChannel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonApply, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCanel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(jTextFieldIP.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel1.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel2.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel3.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel4.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel5.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel6.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel7.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel8.getText() + System.getProperty("line.separator"));
            data.setDefaultIP(jTextFieldIP.getText());
            data.setIOLabels(jTextFieldChannel1.getText(), jTextFieldChannel2.getText(), jTextFieldChannel3.getText(), jTextFieldChannel4.getText(), jTextFieldChannel5.getText(), jTextFieldChannel6.getText(), jTextFieldChannel7.getText(), jTextFieldChannel8.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        this.dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(jTextFieldIP.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel1.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel2.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel3.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel4.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel5.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel6.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel7.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel8.getText() + System.getProperty("line.separator"));
            data.setDefaultIP(jTextFieldIP.getText());
            data.setIOLabels(jTextFieldChannel1.getText(), jTextFieldChannel2.getText(), jTextFieldChannel3.getText(), jTextFieldChannel4.getText(), jTextFieldChannel5.getText(), jTextFieldChannel6.getText(), jTextFieldChannel7.getText(), jTextFieldChannel8.getText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_jButtonApplyActionPerformed

    /**
     * Reads the option files and displays the current settings
     */
    public void getOptionsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("ROV Options.txt"))) {
            defaultIP = br.readLine();
            jTextFieldIP.setText(defaultIP);
            for (int i = 0; i < 8; i++) {
                channels.add(i, br.readLine());
            }
            jTextFieldChannel1.setText((String) channels.get(0));
            jTextFieldChannel2.setText((String) channels.get(1));
            jTextFieldChannel3.setText((String) channels.get(2));
            jTextFieldChannel4.setText((String) channels.get(3));
            jTextFieldChannel5.setText((String) channels.get(4));
            jTextFieldChannel6.setText((String) channels.get(5));
            jTextFieldChannel7.setText((String) channels.get(6));
            jTextFieldChannel8.setText((String) channels.get(7));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void jButtonCanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCanelActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCanelActionPerformed

    private void jTextFieldChannel5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldChannel5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldChannel5ActionPerformed

    private void jTextFieldChannel6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldChannel6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldChannel6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCanel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelChannelsHeader;
    private javax.swing.JLabel jLabelIPHeader;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldChannel1;
    private javax.swing.JTextField jTextFieldChannel2;
    private javax.swing.JTextField jTextFieldChannel3;
    private javax.swing.JTextField jTextFieldChannel4;
    private javax.swing.JTextField jTextFieldChannel5;
    private javax.swing.JTextField jTextFieldChannel6;
    private javax.swing.JTextField jTextFieldChannel7;
    private javax.swing.JTextField jTextFieldChannel8;
    private javax.swing.JTextField jTextFieldIP;
    // End of variables declaration//GEN-END:variables
}
