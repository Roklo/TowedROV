/*
 * This code is for the bachelor thesis named "Towed-ROV".
 * The purpose is to build a ROV which will be towed behind a surface vessel
 * and act as a multi-sensor platform, were it shall be easy to place new 
 * sensors. There will also be a video stream from the ROV.
 * 
 * The system consists of two Raspberry Pis in the ROV that is connected to
 * several Arduino micro controllers. These micro controllers are connected to
 * feedback from the actuators, the echo sounder and extra optional sensors.
 * The external computer which is on the surface vessel is connected to a GPS,
 * echo sounder over USB, and the ROV over ethernet. It will present and
 * log data in addition to handle user commands for controlling the ROV.
 */
package ntnusubsea.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.commons.io.FileUtils;

/**
 * This class allows the user to change options between every time the program
 * is loaded. It reads and writes from the ROV Options text file from the
 * program folder
 */
public class OptionsFrame extends javax.swing.JFrame {

    File file = null;
    String IP_Rov;
    String IP_Camera;
    ArrayList channels = new ArrayList<String>();
    private Data data;
    private TCPClient client_ROV;
    private double Kp;
    private double Ki;
    private double Kd;

    /**
     * Creates new form OptionsFrame
     *
     * @param data Data containing shared variables.
     * @param client_ROV
     */
    public OptionsFrame(Data data, TCPClient client_ROV) {
        initComponents();
        try {
            this.file = new File("ROV Options.txt");
            FileUtils.touch(file);
        } catch (IOException ex) {
            Logger.getLogger(OptionsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        //setSize(640, 480);
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.data = data;
        this.client_ROV = client_ROV;
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
        jTextFieldIP_Rov = new javax.swing.JTextField();
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
        jTextFieldIP_Camera = new javax.swing.JTextField();
        jLabelIPHeader1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        KpTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        KiTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        KdTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        offset1TextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        offset2TextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");
        setBackground(new java.awt.Color(39, 44, 50));
        setForeground(new java.awt.Color(39, 44, 50));
        setMaximumSize(new java.awt.Dimension(432, 333));
        setMinimumSize(new java.awt.Dimension(432, 333));
        setResizable(false);

        jLabelIPHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelIPHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIPHeader.setText("ROV IP");

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

        jTextFieldIP_Rov.setText("123.123.123.123");
        jTextFieldIP_Rov.setToolTipText("");

        jLabelChannelsHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelChannelsHeader.setText("Custom I/O");

        jLabel1.setText("<html>Channel 1<br/>Input");

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

        jTextFieldIP_Camera.setText("123.123.123.123");
        jTextFieldIP_Camera.setToolTipText("");

        jLabelIPHeader1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabelIPHeader1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIPHeader1.setText("Camera IP");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("PID: ");

        KpTextField.setText("Kp");

        jLabel10.setText("Kp");

        jLabel11.setText("Ki");

        KiTextField.setText("Ki");

        jLabel12.setText("Kd");

        KdTextField.setText("Kd");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Offset:");

        jLabel14.setText("Depth beneath ROV");

        offset1TextField.setText("0.00");

        jLabel15.setText("ROV Depth");

        offset2TextField.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldChannel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldChannel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldChannel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldChannel8))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonApply, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonCanel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                            .addComponent(jTextFieldChannel3))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextFieldChannel4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabelChannelsHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(340, 346, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(KpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(KiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(KdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldIP_Rov)
                            .addComponent(jLabelIPHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldIP_Camera)
                            .addComponent(jLabelIPHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(offset1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(offset2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelIPHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIP_Rov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelIPHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIP_Camera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(KdTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(KiTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(KpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(offset1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offset2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonApply, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCanel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(jTextFieldIP_Rov.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldIP_Camera.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel1.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel2.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel3.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel4.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel5.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel6.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel7.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel8.getText() + System.getProperty("line.separator"));
            writer.write(KpTextField.getText() + System.getProperty("line.separator"));
            writer.write(KiTextField.getText() + System.getProperty("line.separator"));
            writer.write(KdTextField.getText() + System.getProperty("line.separator"));
            writer.write(offset1TextField.getText() + System.getProperty("line.separator"));
            writer.write(offset2TextField.getText() + System.getProperty("line.separator"));

            data.setKp(KpTextField.getText());
            data.setKi(KiTextField.getText());
            data.setKd(KdTextField.getText());
            data.setOffsetDepthBeneathROV(offset1TextField.getText());
            data.setOffsetROVdepth(offset2TextField.getText());
            data.setIP_Rov(jTextFieldIP_Rov.getText());
            data.setIP_Camera(jTextFieldIP_Camera.getText());
            data.setIOLabels(jTextFieldChannel1.getText(), jTextFieldChannel2.getText(), jTextFieldChannel3.getText(), jTextFieldChannel4.getText(), jTextFieldChannel5.getText(), jTextFieldChannel6.getText(), jTextFieldChannel7.getText(), jTextFieldChannel8.getText());
            this.client_ROV.sendCommand("cmd_pid_p:" + KpTextField.getText());
            this.client_ROV.sendCommand("cmd_pid_i:" + KiTextField.getText());
            this.client_ROV.sendCommand("cmd_pid_d:" + KdTextField.getText());
            this.client_ROV.sendCommand("cmd_offsetDepthBeneathROV:" + KdTextField.getText());
            this.client_ROV.sendCommand("cmd_offsetROVdepth:" + KdTextField.getText());

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
            writer.write(jTextFieldIP_Rov.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldIP_Camera.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel1.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel2.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel3.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel4.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel5.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel6.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel7.getText() + System.getProperty("line.separator"));
            writer.write(jTextFieldChannel8.getText() + System.getProperty("line.separator"));
            writer.write(KpTextField.getText() + System.getProperty("line.separator"));
            writer.write(KiTextField.getText() + System.getProperty("line.separator"));
            writer.write(KdTextField.getText() + System.getProperty("line.separator"));
            writer.write(offset1TextField.getText() + System.getProperty("line.separator"));
            writer.write(offset2TextField.getText() + System.getProperty("line.separator"));

            data.setKp(KpTextField.getText());
            data.setKi(KiTextField.getText());
            data.setKd(KdTextField.getText());
            data.setOffsetDepthBeneathROV(offset1TextField.getText());
            data.setOffsetROVdepth(offset2TextField.getText());
            data.setIP_Rov(jTextFieldIP_Rov.getText());
            data.setIP_Camera(jTextFieldIP_Camera.getText());
            data.setIOLabels(jTextFieldChannel1.getText(), jTextFieldChannel2.getText(), jTextFieldChannel3.getText(), jTextFieldChannel4.getText(), jTextFieldChannel5.getText(), jTextFieldChannel6.getText(), jTextFieldChannel7.getText(), jTextFieldChannel8.getText());
            this.client_ROV.sendCommand("cmd_pid_p:" + KpTextField.getText());
            this.client_ROV.sendCommand("cmd_pid_i:" + KiTextField.getText());
            this.client_ROV.sendCommand("cmd_pid_d:" + KdTextField.getText());
            this.client_ROV.sendCommand("cmd_offsetDepthBeneathROV:" + KdTextField.getText());
            this.client_ROV.sendCommand("cmd_offsetROVdepth:" + KdTextField.getText());

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
        try (BufferedReader br = new BufferedReader(new FileReader(new File("ROV Options.txt")))) {
            this.IP_Rov = br.readLine();
            jTextFieldIP_Rov.setText(this.IP_Rov);
            this.IP_Camera = br.readLine();
            jTextFieldIP_Camera.setText(this.IP_Camera);
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
            KpTextField.setText(br.readLine());
            KiTextField.setText(br.readLine());
            KdTextField.setText(br.readLine());
            offset1TextField.setText(br.readLine());
            offset2TextField.setText(br.readLine());
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
    private javax.swing.JTextField KdTextField;
    private javax.swing.JTextField KiTextField;
    private javax.swing.JTextField KpTextField;
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCanel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelChannelsHeader;
    private javax.swing.JLabel jLabelIPHeader;
    private javax.swing.JLabel jLabelIPHeader1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldChannel1;
    private javax.swing.JTextField jTextFieldChannel2;
    private javax.swing.JTextField jTextFieldChannel3;
    private javax.swing.JTextField jTextFieldChannel4;
    private javax.swing.JTextField jTextFieldChannel5;
    private javax.swing.JTextField jTextFieldChannel6;
    private javax.swing.JTextField jTextFieldChannel7;
    private javax.swing.JTextField jTextFieldChannel8;
    private javax.swing.JTextField jTextFieldIP_Camera;
    private javax.swing.JTextField jTextFieldIP_Rov;
    private javax.swing.JTextField offset1TextField;
    private javax.swing.JTextField offset2TextField;
    // End of variables declaration//GEN-END:variables
}
