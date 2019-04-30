/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Main frame of the application. Lets the user connect, watch the video stream,
 * observe sensor values, control the lights, open all the extra tools etc.
 *
 * @author Marius Nonsvik
 */
public class ROVFrame extends javax.swing.JFrame implements Runnable, Observer
{

    ImagePanel videoSheet;
    ImagePanel fullscreenVideoSheet;
    private BufferedImage videoImage;
    private Data data;
    private Double setpoint = 0.00;
    private Double previousSetpoint = 0.00;
    private Double redoSetpoint = 0.00;
    private int mode = 0;
    private EchoSounderFrame echoSounder;
    private OptionsFrame options;
    private TCPClient client_ROV;
    private TCPClient client_Camera;
    private UDPClient udpClient;
    private ScheduledExecutorService clientThreadExecutor;
    private IOControlFrame io;
    private final String LIGHTSID = "<Lights>";
    private final String MODEID = "<Mode>";
    private final String SETPOINTID = "<Setpoint>";
    private int cameraPitchValue = 0;
    private double photoModeDelay = 1;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private int cmd_actuatorPS = 0;
    private int cmd_actuatorSB = 0;

    /**
     * Creates new form ROVFrame
     *
     * @param echoSounder Echo sounder frame to show graphs
     * @param data Data containing shared variables
     * @param client TCP client to send commands and receive data
     * @param io I/O frame to control inputs and outputs
     */
    public ROVFrame(EchoSounderFrame echoSounder, Data data, IOControlFrame io, TCPClient client_ROV, TCPClient client_Camera, UDPClient udpClient)
    {
        this.clientThreadExecutor = null;
        initComponents();
        this.data = data;
        this.echoSounder = echoSounder;
        this.options = new OptionsFrame(this.data);
        this.client_ROV = client_ROV;
        this.client_Camera = client_Camera;
        this.udpClient = udpClient;
        this.io = io;
        this.getContentPane().setBackground(new Color(39, 44, 50));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        videoSheet = new ImagePanel(cameraPanel.getWidth(), cameraPanel.getHeight());
        videoSheet.setBackground(cameraPanel.getBackground());
        fullscreenVideoSheet = new ImagePanel(cameraPanel1.getWidth(), cameraPanel1.getHeight());
        fullscreenVideoSheet.setBackground(cameraPanel1.getBackground());
        videoSheet.setOpaque(false);
        fullscreenVideoSheet.setOpaque(false);
        cameraPanel.add(videoSheet);
        cameraPanel1.add(fullscreenVideoSheet);
        setpointLabel.setText("Current setpoint: " + setpoint + "m");
        exitFullscreenButton.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitFullscreen");
//        depthInputTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "sendInput");
        exitFullscreenButton.getActionMap().put("exitFullscreen", exitFullscreenAction);
//        depthInputTextField.getActionMap().put("sendInput", sendInputAction);
        try
        {
            this.client_Camera.sendCommand("setPitch:57");
        } catch (IOException ex)
        {
            System.out.println("IOException setPitch in ROVFrame constructor: " + ex.getMessage());
        }
    }

    /**
     * Calls the paintSheet method which updates the diplayed image.
     *
     * @param image The image to be displayed on the GUI
     */
    public void showImage(BufferedImage image)
    {
        if (fullscreen.isVisible())
        {
            fullscreenVideoSheet.setSize(cameraPanel1.getSize());
            fullscreenVideoSheet.paintSheet(ImageUtils.resize(image, fullscreenVideoSheet.getParent().getWidth(), fullscreenVideoSheet.getParent().getHeight()));
        } else
        {
            videoSheet.paintSheet(ImageUtils.resize(image, videoSheet.getParent().getWidth(), videoSheet.getParent().getHeight()));
            videoSheet.setSize(cameraPanel.getSize());

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        fullscreen = new javax.swing.JFrame();
        cameraPanel1 = new javax.swing.JPanel();
        exitFullscreenButton = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        helpframe = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        helpFrameOKbutton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        window = new javax.swing.JPanel();
        background = new javax.swing.JPanel();
        cameraPanel = new javax.swing.JPanel();
        fullscreenButton = new javax.swing.JButton();
        controlPanel = new javax.swing.JPanel();
        depthPanel = new javax.swing.JPanel();
        depthInputTextField = new javax.swing.JFormattedTextField();
        depthHeader = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        depthModeButton = new javax.swing.JRadioButton();
        seafloorModeButton = new javax.swing.JRadioButton();
        setpointLabel = new javax.swing.JLabel();
        manualControlButton = new javax.swing.JToggleButton();
        jLabel5 = new javax.swing.JLabel();
        resetManualControlButton = new javax.swing.JButton();
        actuatorControlPS = new javax.swing.JSlider();
        actuatorControlSB = new javax.swing.JSlider();
        actuatorPosLabel = new javax.swing.JLabel();
        lockButton = new javax.swing.JToggleButton();
        lightPanel = new javax.swing.JPanel();
        lightHeader = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        lightSwitch = new javax.swing.JToggleButton();
        lightSwitch_lbl = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 180), new java.awt.Dimension(0, 180), new java.awt.Dimension(32767, 180));
        lightSlider = new javax.swing.JSlider();
        lightSwitchBlueLED = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        emergencyPanel = new javax.swing.JPanel();
        emergencyHeader = new javax.swing.JLabel();
        emergencyStopButton = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 100), new java.awt.Dimension(0, 100), new java.awt.Dimension(32767, 100));
        positionPanel = new javax.swing.JPanel();
        positionHeader = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        headingLabel = new javax.swing.JLabel();
        latitudeLabel = new javax.swing.JLabel();
        longitudeLabel = new javax.swing.JLabel();
        cameraControlPanel = new javax.swing.JPanel();
        delayTextField = new javax.swing.JFormattedTextField();
        cameraHeader = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        photoModeButton = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cameraPitchSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        cameraPitchTextField = new javax.swing.JFormattedTextField();
        cameraPitchLabel = new javax.swing.JLabel();
        photoModeDelayLabel = new javax.swing.JLabel();
        getPhotosButton = new javax.swing.JButton();
        photoModeDelay_FB_Label = new javax.swing.JLabel();
        clearImagesButton = new javax.swing.JButton();
        imageNumberLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        infoPanel = new javax.swing.JPanel();
        pitchLabel = new javax.swing.JLabel();
        seafloorDepthRovLabel = new javax.swing.JLabel();
        rovDepthLabel = new javax.swing.JLabel();
        wingLabel = new javax.swing.JLabel();
        actuatorPanel1 = new javax.swing.JPanel();
        actuatorHeader1 = new javax.swing.JLabel();
        actuatorDutyCycleBar1 = new javax.swing.JProgressBar();
        warningLabel1 = new javax.swing.JLabel();
        actuatorPanel2 = new javax.swing.JPanel();
        actuatorHeader2 = new javax.swing.JLabel();
        actuatorDutyCycleBar2 = new javax.swing.JProgressBar();
        warningLabel2 = new javax.swing.JLabel();
        seafloorDepthBoatLabel = new javax.swing.JLabel();
        rollLabel = new javax.swing.JLabel();
        leakLabel = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jMenuBar = new javax.swing.JMenuBar();
        jMenuConnect = new javax.swing.JMenu();
        jMenuItemConnect = new javax.swing.JMenuItem();
        jMenuItemDisconnect = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuEchosounder = new javax.swing.JMenuItem();
        jMenuIOController = new javax.swing.JMenuItem();
        jMenuOptions = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenuItem();
        jMenuCalibrate = new javax.swing.JMenu();
        calibrateMenuItem = new javax.swing.JMenuItem();

        fullscreen.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fullscreen.setBackground(new java.awt.Color(39, 44, 50));
        fullscreen.setFocusTraversalPolicyProvider(true);
        fullscreen.setForeground(new java.awt.Color(39, 44, 50));
        fullscreen.setLocationByPlatform(true);
        fullscreen.setUndecorated(true);
        fullscreen.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                fullscreenKeyPressed(evt);
            }
        });

        cameraPanel1.setBackground(new java.awt.Color(39, 44, 50));
        cameraPanel1.setForeground(new java.awt.Color(39, 44, 50));
        cameraPanel1.setToolTipText("");
        cameraPanel1.setMinimumSize(new java.awt.Dimension(450, 320));
        cameraPanel1.setPreferredSize(new java.awt.Dimension(718, 580));

        exitFullscreenButton.setBackground(new java.awt.Color(0, 0, 0));
        exitFullscreenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/exitfullscreen1.gif"))); // NOI18N
        exitFullscreenButton.setBorder(null);
        exitFullscreenButton.setContentAreaFilled(false);
        exitFullscreenButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitFullscreenButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitFullscreenButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cameraPanel1Layout = new javax.swing.GroupLayout(cameraPanel1);
        cameraPanel1.setLayout(cameraPanel1Layout);
        cameraPanel1Layout.setHorizontalGroup(
            cameraPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraPanel1Layout.createSequentialGroup()
                .addGap(0, 708, Short.MAX_VALUE)
                .addComponent(exitFullscreenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        cameraPanel1Layout.setVerticalGroup(
            cameraPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraPanel1Layout.createSequentialGroup()
                .addGap(0, 572, Short.MAX_VALUE)
                .addComponent(exitFullscreenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout fullscreenLayout = new javax.swing.GroupLayout(fullscreen.getContentPane());
        fullscreen.getContentPane().setLayout(fullscreenLayout);
        fullscreenLayout.setHorizontalGroup(
            fullscreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cameraPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
        );
        fullscreenLayout.setVerticalGroup(
            fullscreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cameraPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );

        helpframe.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        helpframe.setTitle("About");
        helpframe.setBackground(new java.awt.Color(39, 44, 50));
        helpframe.setForeground(new java.awt.Color(39, 44, 50));
        helpframe.setType(java.awt.Window.Type.POPUP);

        jPanel1.setBackground(new java.awt.Color(39, 44, 50));
        jPanel1.setForeground(new java.awt.Color(39, 44, 50));
        jPanel1.setMaximumSize(new java.awt.Dimension(395, 304));
        jPanel1.setMinimumSize(new java.awt.Dimension(395, 304));
        jPanel1.setPreferredSize(new java.awt.Dimension(395, 304));

        helpFrameOKbutton.setBackground(new java.awt.Color(39, 44, 50));
        helpFrameOKbutton.setForeground(new java.awt.Color(255, 255, 255));
        helpFrameOKbutton.setText("Close");
        helpFrameOKbutton.setFocusPainted(false);
        helpFrameOKbutton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                helpFrameOKbuttonActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("<html>This code is for the bachelor thesis named \"Towed-ROV\". The purpose is to build a ROV which will be towed behind a surface vessel and act as a multi-sensor platform, were it shall be easy to place new sensors. There is also a video stream from the ROV.  <br/><br/>The system consists of a Raspberry Pi on the ROV that is connected to several Arduino microcontrollers that are connected to sensors and programmed with the control system. The external computer which is on the surface vessel is connected to a GPS, echo sounder and the ROV. It will present and save data in addition to handle user commands.<br/><br/> Created by Håkon Longva Haram, Robin Stamnes Thorholm and Bjørnar Magnus Tennfjord.<html>");
        jLabel1.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 160, Short.MAX_VALUE)
                        .addComponent(helpFrameOKbutton)
                        .addGap(0, 161, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpFrameOKbutton)
                .addContainerGap())
        );

        javax.swing.GroupLayout helpframeLayout = new javax.swing.GroupLayout(helpframe.getContentPane());
        helpframe.getContentPane().setLayout(helpframeLayout);
        helpframeLayout.setHorizontalGroup(
            helpframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
        );
        helpframeLayout.setVerticalGroup(
            helpframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Towed ROV");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(39, 44, 50));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(39, 44, 50));
        setMaximumSize(new java.awt.Dimension(1460, 890));
        setMinimumSize(new java.awt.Dimension(1460, 890));
        setPreferredSize(new java.awt.Dimension(1460, 890));
        setSize(new java.awt.Dimension(1445, 853));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        window.setBackground(new java.awt.Color(39, 44, 50));
        window.setForeground(new java.awt.Color(39, 44, 50));
        window.setMinimumSize(new java.awt.Dimension(1445, 830));
        window.setPreferredSize(new java.awt.Dimension(1445, 830));
        window.setLayout(new java.awt.GridBagLayout());

        background.setBackground(new java.awt.Color(39, 44, 50));
        background.setForeground(new java.awt.Color(39, 44, 50));
        background.setToolTipText("");
        background.setAlignmentX(5.0F);
        background.setMinimumSize(new java.awt.Dimension(1445, 830));
        background.setPreferredSize(new java.awt.Dimension(1445, 830));

        cameraPanel.setBackground(new java.awt.Color(39, 44, 50));
        cameraPanel.setForeground(new java.awt.Color(39, 44, 50));
        cameraPanel.setToolTipText("");
        cameraPanel.setAlignmentX(0.8F);
        cameraPanel.setAlignmentY(0.8F);
        cameraPanel.setMaximumSize(new java.awt.Dimension(985, 605));
        cameraPanel.setMinimumSize(new java.awt.Dimension(985, 605));
        cameraPanel.setPreferredSize(new java.awt.Dimension(985, 605));

        fullscreenButton.setBackground(new java.awt.Color(0, 0, 0));
        fullscreenButton.setForeground(new java.awt.Color(255, 255, 255));
        fullscreenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/fullscreen1.gif"))); // NOI18N
        fullscreenButton.setBorder(null);
        fullscreenButton.setContentAreaFilled(false);
        fullscreenButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fullscreenButton.setHideActionText(true);
        fullscreenButton.setName(""); // NOI18N
        fullscreenButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fullscreenButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cameraPanelLayout = new javax.swing.GroupLayout(cameraPanel);
        cameraPanel.setLayout(cameraPanelLayout);
        cameraPanelLayout.setHorizontalGroup(
            cameraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraPanelLayout.createSequentialGroup()
                .addContainerGap(955, Short.MAX_VALUE)
                .addComponent(fullscreenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        cameraPanelLayout.setVerticalGroup(
            cameraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraPanelLayout.createSequentialGroup()
                .addContainerGap(575, Short.MAX_VALUE)
                .addComponent(fullscreenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        controlPanel.setBackground(new java.awt.Color(39, 44, 50));
        controlPanel.setForeground(new java.awt.Color(39, 44, 50));
        controlPanel.setMinimumSize(new java.awt.Dimension(150, 140));
        controlPanel.setPreferredSize(new java.awt.Dimension(768, 190));

        depthPanel.setBackground(new java.awt.Color(39, 44, 50));
        depthPanel.setMaximumSize(new java.awt.Dimension(260, 210));

        depthInputTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        depthInputTextField.setToolTipText("Depth (m)");
        depthInputTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                depthInputTextFieldActionPerformed(evt);
            }
        });

        depthHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        depthHeader.setForeground(new java.awt.Color(255, 255, 255));
        depthHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        depthHeader.setText("Depth (meters)");

        jSeparator4.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator4.setForeground(new java.awt.Color(67, 72, 83));

        depthModeButton.setBackground(new java.awt.Color(39, 44, 50));
        buttonGroup1.add(depthModeButton);
        depthModeButton.setForeground(new java.awt.Color(255, 255, 255));
        depthModeButton.setSelected(true);
        depthModeButton.setText("Depth");
        depthModeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        depthModeButton.setFocusPainted(false);
        depthModeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                depthModeButtonActionPerformed(evt);
            }
        });

        seafloorModeButton.setBackground(new java.awt.Color(39, 44, 50));
        buttonGroup1.add(seafloorModeButton);
        seafloorModeButton.setForeground(new java.awt.Color(255, 255, 255));
        seafloorModeButton.setText("Distance from seafloor");
        seafloorModeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        seafloorModeButton.setFocusPainted(false);
        seafloorModeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                seafloorModeButtonActionPerformed(evt);
            }
        });

        setpointLabel.setBackground(new java.awt.Color(39, 44, 50));
        setpointLabel.setForeground(new java.awt.Color(255, 255, 255));
        setpointLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        setpointLabel.setText("Current setpoint: 0m");
        setpointLabel.setToolTipText("");
        setpointLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        setpointLabel.setOpaque(true);

        manualControlButton.setBackground(new java.awt.Color(39, 44, 50));
        buttonGroup1.add(manualControlButton);
        manualControlButton.setForeground(new java.awt.Color(39, 44, 50));
        manualControlButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        manualControlButton.setBorder(null);
        manualControlButton.setContentAreaFilled(false);
        manualControlButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        manualControlButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        manualControlButton.setFocusPainted(false);
        manualControlButton.setFocusable(false);
        manualControlButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-on.gif"))); // NOI18N
        manualControlButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                manualControlButtonActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Manual Control:");

        resetManualControlButton.setBackground(new java.awt.Color(39, 44, 50));
        resetManualControlButton.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        resetManualControlButton.setForeground(new java.awt.Color(255, 255, 255));
        resetManualControlButton.setText("RESET");
        resetManualControlButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        resetManualControlButton.setFocusPainted(false);
        resetManualControlButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                resetManualControlButtonActionPerformed(evt);
            }
        });

        actuatorControlPS.setBackground(new java.awt.Color(39, 44, 50));
        actuatorControlPS.setMaximum(254);
        actuatorControlPS.setMinimum(1);
        actuatorControlPS.setOrientation(javax.swing.JSlider.VERTICAL);
        actuatorControlPS.setValue(127);
        actuatorControlPS.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        actuatorControlPS.setEnabled(false);
        actuatorControlPS.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                actuatorControlPSMouseReleased(evt);
            }
        });

        actuatorControlSB.setBackground(new java.awt.Color(39, 44, 50));
        actuatorControlSB.setMaximum(254);
        actuatorControlSB.setMinimum(1);
        actuatorControlSB.setOrientation(javax.swing.JSlider.VERTICAL);
        actuatorControlSB.setValue(127);
        actuatorControlSB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        actuatorControlSB.setEnabled(false);
        actuatorControlSB.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                actuatorControlSBMouseReleased(evt);
            }
        });

        actuatorPosLabel.setForeground(new java.awt.Color(255, 255, 255));
        actuatorPosLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        actuatorPosLabel.setText("<html>PS: 127<br/><br/>SB: 127");

        lockButton.setBackground(new java.awt.Color(39, 44, 50));
        lockButton.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        lockButton.setForeground(new java.awt.Color(255, 255, 255));
        lockButton.setText("LOCK");
        lockButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lockButton.setFocusPainted(false);
        lockButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                lockButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout depthPanelLayout = new javax.swing.GroupLayout(depthPanel);
        depthPanel.setLayout(depthPanelLayout);
        depthPanelLayout.setHorizontalGroup(
            depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(depthHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(depthPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(setpointLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(depthInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(depthPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(depthModeButton)
                .addGap(6, 6, 6)
                .addComponent(seafloorModeButton))
            .addGroup(depthPanelLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(depthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(manualControlButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(depthPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(resetManualControlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lockButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 0, 0)
                .addComponent(actuatorControlPS, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(actuatorControlSB, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(actuatorPosLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        depthPanelLayout.setVerticalGroup(
            depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(depthPanelLayout.createSequentialGroup()
                .addComponent(depthHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setpointLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(depthInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(depthModeButton)
                    .addGroup(depthPanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(seafloorModeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(11, 11, 11)
                .addGroup(depthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(depthPanelLayout.createSequentialGroup()
                        .addComponent(manualControlButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(resetManualControlButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(actuatorControlPS, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(actuatorControlSB, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(actuatorPosLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        lightPanel.setBackground(new java.awt.Color(39, 44, 50));
        lightPanel.setMaximumSize(new java.awt.Dimension(156, 213));

        lightHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lightHeader.setForeground(new java.awt.Color(255, 255, 255));
        lightHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lightHeader.setText("Lights");

        jSeparator5.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator5.setForeground(new java.awt.Color(67, 72, 83));

        lightSwitch.setBackground(new java.awt.Color(39, 44, 50));
        lightSwitch.setForeground(new java.awt.Color(39, 44, 50));
        lightSwitch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        lightSwitch.setBorder(null);
        lightSwitch.setContentAreaFilled(false);
        lightSwitch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lightSwitch.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        lightSwitch.setFocusPainted(false);
        lightSwitch.setFocusable(false);
        lightSwitch.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-on.gif"))); // NOI18N
        lightSwitch.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                lightSwitchActionPerformed(evt);
            }
        });

        ImageIcon myimage = new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/light-bulb.png"));
        Image img = myimage.getImage();
        Image img2 = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon imgIcon = new ImageIcon(img2);
        lightSwitch_lbl.setIcon(imgIcon);
        lightSwitch_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lightSwitch_lbl.setMaximumSize(new java.awt.Dimension(63, 100));
        lightSwitch_lbl.setMinimumSize(new java.awt.Dimension(63, 100));

        lightSlider.setBackground(new java.awt.Color(39, 44, 50));
        lightSlider.setMaximum(40);
        lightSlider.setMinimum(19);
        lightSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lightSlider.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                lightSliderMouseReleased(evt);
            }
        });

        lightSwitchBlueLED.setBackground(new java.awt.Color(39, 44, 50));
        lightSwitchBlueLED.setForeground(new java.awt.Color(39, 44, 50));
        lightSwitchBlueLED.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        lightSwitchBlueLED.setBorder(null);
        lightSwitchBlueLED.setContentAreaFilled(false);
        lightSwitchBlueLED.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lightSwitchBlueLED.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        lightSwitchBlueLED.setFocusPainted(false);
        lightSwitchBlueLED.setFocusable(false);
        lightSwitchBlueLED.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-on.gif"))); // NOI18N
        lightSwitchBlueLED.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                lightSwitchBlueLEDActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Blue LEDs");

        javax.swing.GroupLayout lightPanelLayout = new javax.swing.GroupLayout(lightPanel);
        lightPanel.setLayout(lightPanelLayout);
        lightPanelLayout.setHorizontalGroup(
            lightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lightPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(lightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lightHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator5)
                    .addGroup(lightPanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(lightSwitch_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(42, Short.MAX_VALUE))
                    .addGroup(lightPanelLayout.createSequentialGroup()
                        .addGroup(lightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(lightPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lightSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(lightPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(lightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lightSwitchBlueLED, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lightSwitch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(28, 28, 28))))
        );
        lightPanelLayout.setVerticalGroup(
            lightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lightPanelLayout.createSequentialGroup()
                .addComponent(lightHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lightSwitch_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lightSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lightSwitch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(lightSwitchBlueLED, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        emergencyPanel.setBackground(new java.awt.Color(39, 44, 50));
        emergencyPanel.setMaximumSize(new java.awt.Dimension(153, 213));

        emergencyHeader.setBackground(new java.awt.Color(28, 28, 28));
        emergencyHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        emergencyHeader.setForeground(new java.awt.Color(255, 255, 255));
        emergencyHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        emergencyHeader.setText("Emergency surfacing");

        emergencyStopButton.setBackground(new java.awt.Color(39, 44, 50));
        emergencyStopButton.setForeground(new java.awt.Color(28, 28, 28));
        ImageIcon egm_myimage = new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/emg-stop.gif"));
        Image egm_img = egm_myimage.getImage();
        Image egm_img2 = egm_img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon egm_imgIcon = new ImageIcon(egm_img2);
        emergencyStopButton.setIcon(egm_imgIcon);
        emergencyStopButton.setBorder(null);
        emergencyStopButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        emergencyStopButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/emg-stop2.gif"))); // NOI18N
        emergencyStopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                emergencyStopButtonActionPerformed(evt);
            }
        });

        jSeparator6.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator6.setForeground(new java.awt.Color(67, 72, 83));

        javax.swing.GroupLayout emergencyPanelLayout = new javax.swing.GroupLayout(emergencyPanel);
        emergencyPanel.setLayout(emergencyPanelLayout);
        emergencyPanelLayout.setHorizontalGroup(
            emergencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emergencyPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(emergencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6)
                    .addComponent(emergencyHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emergencyPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(emergencyStopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        emergencyPanelLayout.setVerticalGroup(
            emergencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emergencyPanelLayout.createSequentialGroup()
                .addComponent(emergencyHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(emergencyStopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGroup(emergencyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        positionPanel.setBackground(new java.awt.Color(39, 44, 50));
        positionPanel.setMaximumSize(new java.awt.Dimension(153, 213));
        positionPanel.setPreferredSize(new java.awt.Dimension(153, 213));

        positionHeader.setBackground(new java.awt.Color(28, 28, 28));
        positionHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        positionHeader.setForeground(new java.awt.Color(255, 255, 255));
        positionHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        positionHeader.setText("Position");

        jSeparator7.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator7.setForeground(new java.awt.Color(67, 72, 83));

        headingLabel.setBackground(new java.awt.Color(28, 28, 28));
        headingLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        headingLabel.setForeground(new java.awt.Color(255, 255, 255));
        headingLabel.setText("Heading: ");

        latitudeLabel.setBackground(new java.awt.Color(28, 28, 28));
        latitudeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        latitudeLabel.setForeground(new java.awt.Color(255, 255, 255));
        latitudeLabel.setText("Latitude: ");

        longitudeLabel.setBackground(new java.awt.Color(28, 28, 28));
        longitudeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        longitudeLabel.setForeground(new java.awt.Color(255, 255, 255));
        longitudeLabel.setText("Longitude: ");

        javax.swing.GroupLayout positionPanelLayout = new javax.swing.GroupLayout(positionPanel);
        positionPanel.setLayout(positionPanelLayout);
        positionPanelLayout.setHorizontalGroup(
            positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(positionHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator7)
            .addGroup(positionPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(longitudeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(latitudeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        positionPanelLayout.setVerticalGroup(
            positionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(positionPanelLayout.createSequentialGroup()
                .addComponent(positionHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(headingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(latitudeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(longitudeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        cameraControlPanel.setBackground(new java.awt.Color(39, 44, 50));
        cameraControlPanel.setMaximumSize(new java.awt.Dimension(190, 213));
        cameraControlPanel.setPreferredSize(new java.awt.Dimension(190, 213));

        delayTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        delayTextField.setToolTipText("Time between each frame (0-99). - Press enter to send command.");
        delayTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                delayTextFieldActionPerformed(evt);
            }
        });

        cameraHeader.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cameraHeader.setForeground(new java.awt.Color(255, 255, 255));
        cameraHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cameraHeader.setText("Camera Control");
        cameraHeader.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jSeparator9.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator9.setForeground(new java.awt.Color(67, 72, 83));

        photoModeButton.setBackground(new java.awt.Color(39, 44, 50));
        photoModeButton.setForeground(new java.awt.Color(39, 44, 50));
        photoModeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        photoModeButton.setBorder(null);
        photoModeButton.setContentAreaFilled(false);
        photoModeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        photoModeButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-off.gif"))); // NOI18N
        photoModeButton.setFocusPainted(false);
        photoModeButton.setFocusable(false);
        photoModeButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/switch-on.gif"))); // NOI18N
        photoModeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                photoModeButtonActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Photo Mode");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Delay:");

        cameraPitchSlider.setBackground(new java.awt.Color(39, 44, 50));
        cameraPitchSlider.setMaximum(75);
        cameraPitchSlider.setMinimum(50);
        cameraPitchSlider.setValue(57);
        cameraPitchSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cameraPitchSlider.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                cameraPitchSliderMouseReleased(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Camera Pitch:");

        cameraPitchTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cameraPitchTextField.setToolTipText("Camera Pitch (50-75). - Press enter to send command.");
        cameraPitchTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cameraPitchTextFieldActionPerformed(evt);
            }
        });

        cameraPitchLabel.setForeground(new java.awt.Color(255, 255, 255));
        cameraPitchLabel.setText("57");

        photoModeDelayLabel.setForeground(new java.awt.Color(255, 255, 255));
        photoModeDelayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        photoModeDelayLabel.setText("1.0 s");

        getPhotosButton.setBackground(new java.awt.Color(39, 44, 50));
        getPhotosButton.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        getPhotosButton.setForeground(new java.awt.Color(255, 255, 255));
        getPhotosButton.setText("Get IMGs");
        getPhotosButton.setToolTipText("Retrieves the photos from the ROV and saves it to C:/TowedROV/ROV_Photos");
        getPhotosButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getPhotosButton.setFocusPainted(false);
        getPhotosButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                getPhotosButtonActionPerformed(evt);
            }
        });

        photoModeDelay_FB_Label.setForeground(new java.awt.Color(255, 255, 255));
        photoModeDelay_FB_Label.setText("1.0 s");

        clearImagesButton.setBackground(new java.awt.Color(39, 44, 50));
        clearImagesButton.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        clearImagesButton.setForeground(new java.awt.Color(255, 255, 255));
        clearImagesButton.setToolTipText("Clears the image folder on the ROV RPi.");
        clearImagesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clearImagesButton.setFocusPainted(false);
        clearImagesButton.setLabel("CLR IMGs");
        clearImagesButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                clearImagesButtonActionPerformed(evt);
            }
        });

        imageNumberLabel.setForeground(new java.awt.Color(255, 255, 255));
        imageNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageNumberLabel.setText("0 / 1000");

        javax.swing.GroupLayout cameraControlPanelLayout = new javax.swing.GroupLayout(cameraControlPanel);
        cameraControlPanel.setLayout(cameraControlPanelLayout);
        cameraControlPanelLayout.setHorizontalGroup(
            cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator9)
            .addGroup(cameraControlPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(cameraControlPanelLayout.createSequentialGroup()
                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(cameraControlPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(photoModeDelayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cameraControlPanelLayout.createSequentialGroup()
                                .addComponent(cameraPitchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addComponent(photoModeDelay_FB_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(cameraControlPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(cameraControlPanelLayout.createSequentialGroup()
                                .addComponent(cameraPitchSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cameraPitchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(cameraControlPanelLayout.createSequentialGroup()
                                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(photoModeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(cameraControlPanelLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(imageNumberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(clearImagesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(getPhotosButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(cameraControlPanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(cameraHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        cameraControlPanelLayout.setVerticalGroup(
            cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cameraControlPanelLayout.createSequentialGroup()
                .addComponent(cameraHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(photoModeDelayLabel)
                    .addComponent(photoModeDelay_FB_Label))
                .addGap(0, 0, 0)
                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cameraPitchLabel))
                .addGap(2, 2, 2)
                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cameraPitchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cameraPitchSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(cameraControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(cameraControlPanelLayout.createSequentialGroup()
                        .addComponent(getPhotosButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(clearImagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cameraControlPanelLayout.createSequentialGroup()
                        .addComponent(photoModeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(imageNumberLabel)))
                .addGap(246, 246, 246))
        );

        delayTextField.getAccessibleContext().setAccessibleDescription("Time between each frame");

        jSeparator2.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator2.setForeground(new java.awt.Color(67, 72, 83));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator3.setForeground(new java.awt.Color(67, 72, 83));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator8.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator8.setForeground(new java.awt.Color(67, 72, 83));
        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator1.setBackground(new java.awt.Color(67, 72, 83));
        jSeparator1.setForeground(new java.awt.Color(67, 72, 83));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addComponent(depthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cameraControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(emergencyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(positionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cameraControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(positionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emergencyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(depthPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1, Short.MAX_VALUE))
        );

        infoPanel.setBackground(new java.awt.Color(39, 44, 50));
        infoPanel.setForeground(new java.awt.Color(39, 44, 50));
        infoPanel.setMaximumSize(new java.awt.Dimension(455, 509));
        infoPanel.setMinimumSize(new java.awt.Dimension(455, 509));

        pitchLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pitchLabel.setForeground(new java.awt.Color(255, 255, 255));
        pitchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pitchLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        pitchLabel.setText("<html>Pitch angle:<br/><br/><center/>10");
        pitchLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        pitchLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        seafloorDepthRovLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        seafloorDepthRovLabel.setForeground(new java.awt.Color(255, 255, 255));
        seafloorDepthRovLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seafloorDepthRovLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        seafloorDepthRovLabel.setText("<html>Depth beneath ROV:<br/><br/><center/>10");
        seafloorDepthRovLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        seafloorDepthRovLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        rovDepthLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rovDepthLabel.setForeground(new java.awt.Color(255, 255, 255));
        rovDepthLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rovDepthLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        rovDepthLabel.setText("<html>ROV depth:<br/><br/><center/>10");
        rovDepthLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        rovDepthLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        wingLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        wingLabel.setForeground(new java.awt.Color(255, 255, 255));
        wingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wingLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        wingLabel.setText("<html>Wing angle:<br/><br/><center/>10");
        wingLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        wingLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        actuatorPanel1.setBackground(new java.awt.Color(42, 48, 57));
        actuatorPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        actuatorPanel1.setForeground(new java.awt.Color(255, 255, 255));

        actuatorHeader1.setBackground(new java.awt.Color(42, 48, 57));
        actuatorHeader1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        actuatorHeader1.setForeground(new java.awt.Color(255, 255, 255));
        actuatorHeader1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        actuatorHeader1.setText("Actuator 1 duty cycle");

        actuatorDutyCycleBar1.setBackground(new java.awt.Color(42, 48, 57));
        actuatorDutyCycleBar1.setForeground(new java.awt.Color(26, 173, 75));
        actuatorDutyCycleBar1.setMaximum(511);
        actuatorDutyCycleBar1.setValue(400);
        actuatorDutyCycleBar1.setBorder(null);
        actuatorDutyCycleBar1.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                actuatorDutyCycleBar1StateChanged(evt);
            }
        });

        warningLabel1.setBackground(new java.awt.Color(42, 48, 57));
        warningLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        warningLabel1.setForeground(new java.awt.Color(255, 255, 255));
        warningLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        warningLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        warningLabel1.setOpaque(true);

        javax.swing.GroupLayout actuatorPanel1Layout = new javax.swing.GroupLayout(actuatorPanel1);
        actuatorPanel1.setLayout(actuatorPanel1Layout);
        actuatorPanel1Layout.setHorizontalGroup(
            actuatorPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actuatorHeader1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(warningLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(actuatorDutyCycleBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        actuatorPanel1Layout.setVerticalGroup(
            actuatorPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actuatorPanel1Layout.createSequentialGroup()
                .addComponent(actuatorHeader1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actuatorDutyCycleBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(warningLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        actuatorPanel2.setBackground(new java.awt.Color(42, 48, 57));
        actuatorPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        actuatorPanel2.setForeground(new java.awt.Color(255, 255, 255));

        actuatorHeader2.setBackground(new java.awt.Color(39, 44, 50));
        actuatorHeader2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        actuatorHeader2.setForeground(new java.awt.Color(255, 255, 255));
        actuatorHeader2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        actuatorHeader2.setText("Actuator 2 duty cycle");

        actuatorDutyCycleBar2.setBackground(new java.awt.Color(39, 44, 50));
        actuatorDutyCycleBar2.setForeground(new java.awt.Color(26, 173, 75));
        actuatorDutyCycleBar2.setMaximum(512);
        actuatorDutyCycleBar2.setToolTipText("");
        actuatorDutyCycleBar2.setValue(180);
        actuatorDutyCycleBar2.setBorder(null);
        actuatorDutyCycleBar2.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                actuatorDutyCycleBar2StateChanged(evt);
            }
        });

        warningLabel2.setBackground(new java.awt.Color(39, 44, 50));
        warningLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        warningLabel2.setForeground(new java.awt.Color(255, 255, 255));
        warningLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        warningLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout actuatorPanel2Layout = new javax.swing.GroupLayout(actuatorPanel2);
        actuatorPanel2.setLayout(actuatorPanel2Layout);
        actuatorPanel2Layout.setHorizontalGroup(
            actuatorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actuatorHeader2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(actuatorDutyCycleBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(warningLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        actuatorPanel2Layout.setVerticalGroup(
            actuatorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actuatorPanel2Layout.createSequentialGroup()
                .addComponent(actuatorHeader2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(actuatorDutyCycleBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(warningLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        seafloorDepthBoatLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        seafloorDepthBoatLabel.setForeground(new java.awt.Color(255, 255, 255));
        seafloorDepthBoatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seafloorDepthBoatLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        seafloorDepthBoatLabel.setText("<html>Depth beneath boat:<br/><br/><center/>10");
        seafloorDepthBoatLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        seafloorDepthBoatLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        rollLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rollLabel.setForeground(new java.awt.Color(255, 255, 255));
        rollLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rollLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        rollLabel.setText("<html>Roll angle:<br/><br/><center/>10");
        rollLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        rollLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rollLabel.setMinimumSize(new java.awt.Dimension(140, 110));

        leakLabel.setBackground(new java.awt.Color(28, 28, 28));
        leakLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leakLabel.setForeground(new java.awt.Color(255, 255, 255));
        leakLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leakLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/Box.gif"))); // NOI18N
        leakLabel.setText("<html>Leak detection:<br/><br/><center/>No leak detected");
        leakLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(45, 53, 62), 1, true));
        leakLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        leakLabel.setPreferredSize(new java.awt.Dimension(170, 100));

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actuatorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(actuatorPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wingLabel)
                    .addComponent(pitchLabel)
                    .addComponent(rollLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 40, Short.MAX_VALUE)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seafloorDepthRovLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seafloorDepthBoatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rovDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 43, Short.MAX_VALUE))
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(leakLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seafloorDepthBoatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rollLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seafloorDepthRovLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pitchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rovDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(55, 55, 55)
                .addComponent(leakLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(actuatorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actuatorPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                    .addComponent(cameraPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(infoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backgroundLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backgroundLayout.createSequentialGroup()
                        .addComponent(cameraPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(155, 155, 155))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 856;
        gridBagConstraints.ipady = 318;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        window.add(background, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(window, gridBagConstraints);
        getContentPane().add(filler3, new java.awt.GridBagConstraints());

        jMenuBar.setForeground(new java.awt.Color(39, 44, 50));

        jMenuConnect.setText("Connect");
        jMenuConnect.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuConnect.setFocusPainted(true);

        jMenuItemConnect.setText("Connect");
        jMenuItemConnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemConnectActionPerformed(evt);
            }
        });
        jMenuConnect.add(jMenuItemConnect);

        jMenuItemDisconnect.setText("Disconnect");
        jMenuItemDisconnect.setEnabled(false);
        jMenuItemDisconnect.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemDisconnectActionPerformed(evt);
            }
        });
        jMenuConnect.add(jMenuItemDisconnect);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuConnect.add(jMenuItemExit);

        jMenuBar.add(jMenuConnect);

        jMenuTools.setText("Tools");
        jMenuTools.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuEchosounder.setText("Echo sounder");
        jMenuEchosounder.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuEchosounderActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuEchosounder);

        jMenuIOController.setText("I/O Controller");
        jMenuIOController.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuIOControllerActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuIOController);

        jMenuOptions.setText("Options");
        jMenuOptions.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuOptionsActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuOptions);

        jMenuBar.add(jMenuTools);

        jMenuHelp.setText("Help");
        jMenuHelp.setToolTipText("");
        jMenuHelp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuAbout.setText("About");
        jMenuAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuAbout);

        jMenuBar.add(jMenuHelp);

        jMenuCalibrate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ntnusubsea/gui/Images/NotCalibrated.gif"))); // NOI18N
        jMenuCalibrate.setText("Not Calibrated!");
        jMenuCalibrate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuCalibrate.setDisabledIcon(null);

        calibrateMenuItem.setText("Calibrate");
        calibrateMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                calibrateMenuItemActionPerformed(evt);
            }
        });
        jMenuCalibrate.add(calibrateMenuItem);

        jMenuBar.add(jMenuCalibrate);

        setJMenuBar(jMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fullscreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullscreenButtonActionPerformed
        Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = (int) maximumWindowBounds.getWidth();
        int height = (int) maximumWindowBounds.getHeight();
        fullscreen.setLocationRelativeTo(this);
        this.setVisible(false);
        fullscreen.setExtendedState(MAXIMIZED_BOTH);
        fullscreen.setUndecorated(true);
        fullscreen.setVisible(true);
        exitFullscreenButton.setBounds(width - exitFullscreenButton.getWidth(), height - exitFullscreenButton.getHeight() - 25, 30, 30);
    }//GEN-LAST:event_fullscreenButtonActionPerformed

    private void exitFullscreenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitFullscreenButtonActionPerformed
        fullscreen.setVisible(false);
        fullscreen.dispose();
        this.setVisible(true);
    }//GEN-LAST:event_exitFullscreenButtonActionPerformed

    private void fullscreenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fullscreenKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE)
        {
            fullscreen.dispose();
        }
        System.out.println(key);
    }//GEN-LAST:event_fullscreenKeyPressed

    private void actuatorDutyCycleBar1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_actuatorDutyCycleBar1StateChanged
        int actuatorTime1 = actuatorDutyCycleBar1.getValue();
        if (actuatorTime1 <= 255)
        {
            actuatorDutyCycleBar1.setForeground(new Color(actuatorTime1, 255, 0));
            warningLabel1.setText("");
            warningLabel1.setBackground(new Color(28, 28, 28));
        } else
        {
            actuatorDutyCycleBar1.setForeground(new Color(255, 255 + (256 - actuatorTime1), 0));
            warningLabel1.setText("Warning!");
            warningLabel1.setBackground(Color.red);
        }
    }//GEN-LAST:event_actuatorDutyCycleBar1StateChanged

    private void seafloorModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seafloorModeButtonActionPerformed
        actuatorControlPS.setValue(127);
        actuatorControlSB.setValue(127);
        actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
        actuatorControlPS.setEnabled(false);
        actuatorControlSB.setEnabled(false);
        if (this.mode != 1)
        {
            this.mode = 1;
            System.out.println("Mode 1 - Distance from seafloor");
            try
            {
                this.client_ROV.sendCommand("cmd_mode:" + String.valueOf(this.mode));
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            }
        } else
        {
            System.out.println("Already in seafloor mode.");
        }
    }//GEN-LAST:event_seafloorModeButtonActionPerformed

    private void lightSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightSwitchActionPerformed
        if (lightSwitch.isSelected())
        {
            try
            {
                int value = lightSlider.getValue();
                client_Camera.sendCommand("setLed:" + String.valueOf(value));
                //lightSlider.setValue(40);
            } catch (IOException ex)
            {
                Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else
        {
            try
            {
                client_Camera.sendCommand("setLed:0");
                //lightSlider.setValue(19);
            } catch (IOException ex)
            {
                Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_lightSwitchActionPerformed

    private void emergencyStopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emergencyStopButtonActionPerformed
//        previousSetpoint = setpoint;
//        setpoint = 0.000; 
        setpointLabel.setText("EMERGENCY STOP: " + String.valueOf(setpoint) + "m");
        setpointLabel.setBackground(new Color(255, 0, 0));
        depthModeButton.doClick();
        depthInputTextField.setText("0");
        this.mode = 0;
        try
        {
            this.client_ROV.sendCommand("cmd_emergencySurface:true");
            this.client_ROV.sendCommand("cmd_mode:0");
            //sendButton.doClick();
        } catch (IOException ex)
        {
            System.out.println("IOException in emergencyStopButtonActionPerformed: " + ex.getMessage());
        }
    }//GEN-LAST:event_emergencyStopButtonActionPerformed

    private void jMenuItemConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConnectActionPerformed
//        String ip = (String) JOptionPane.showInputDialog(this, "Enter IP", "Connection", JOptionPane.PLAIN_MESSAGE, null, null, data.getIP_Rov());
        try
        {

            this.clientThreadExecutor = Executors.newScheduledThreadPool(3);
            clientThreadExecutor.scheduleAtFixedRate(client_ROV,
                    0, 100, TimeUnit.MILLISECONDS);
            clientThreadExecutor.scheduleAtFixedRate(client_Camera,
                    0, 100, TimeUnit.MILLISECONDS);
            clientThreadExecutor.scheduleAtFixedRate(udpClient,
                    0, 20, TimeUnit.MILLISECONDS);

            lightSwitch.setEnabled(true);
            emergencyStopButton.setEnabled(true);
            io.enableIO();
//            client.sendCommand("<KP>" + data.getKp());
//            Thread.sleep(100);
//            client.sendCommand("<KI>" + data.getKi());
//            Thread.sleep(100);
//            client.sendCommand("<KD>" + data.getKd());
            jMenuItemDisconnect.setEnabled(true);
            jMenuItemConnect.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    "Successfully connected to the ROV RPi and the camera RPi.",
                    "Connected",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Connection failed.",
                    "Conncetion error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemConnectActionPerformed

    private void jMenuItemDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDisconnectActionPerformed

        try
        {

            //client.sendCommand("Quit");
            client_ROV.disconnect();
            client_Camera.disconnect();
            lightSwitch.setEnabled(false);
            emergencyStopButton.setEnabled(false);
            io.disableIO();

            if (clientThreadExecutor != null)
            {
                clientThreadExecutor.shutdown();
            }
            JOptionPane.showMessageDialog(this,
                    "Successfully disconnected from the ROV RPi and the camera RPi.",
                    "Disconnected",
                    JOptionPane.PLAIN_MESSAGE);
            videoImage = ImageIO.read(getClass().getResource("/ntnusubsea/gui/Images/TowedROV.jpg"));
            data.setVideoImage(videoImage);
            jMenuItemDisconnect.setEnabled(false);
            jMenuItemConnect.setEnabled(true);
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Failed to disconnect.",
                    "Disconnect error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemDisconnectActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed


    private void jMenuEchosounderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEchosounderActionPerformed
        echoSounder.setVisible(true);
    }//GEN-LAST:event_jMenuEchosounderActionPerformed

    private void helpFrameOKbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpFrameOKbuttonActionPerformed
        helpframe.dispose();
    }//GEN-LAST:event_helpFrameOKbuttonActionPerformed

    private void jMenuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAboutActionPerformed
        helpframe.setVisible(true);
        helpframe.setSize(helpframe.getPreferredSize());
        helpframe.pack();
        helpframe.setLocationRelativeTo(null);
        //helpframe.setLocation(this.getLocation().x, this.getLocation().y);
    }//GEN-LAST:event_jMenuAboutActionPerformed

    private void actuatorDutyCycleBar2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_actuatorDutyCycleBar2StateChanged
        int actuatorTime2 = actuatorDutyCycleBar2.getValue();
        if (actuatorTime2 <= 255)
        {
            actuatorDutyCycleBar2.setForeground(new Color(actuatorTime2, 255, 0));
            warningLabel2.setText("");
            warningLabel2.setBackground(new Color(28, 28, 28));
        } else
        {
            actuatorDutyCycleBar2.setForeground(new Color(255, 255 + (256 - actuatorTime2), 0));
            warningLabel2.setText("Warning!");
            warningLabel2.setBackground(Color.red);
        }
    }//GEN-LAST:event_actuatorDutyCycleBar2StateChanged

    private void jMenuOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuOptionsActionPerformed
        options.setVisible(true);
    }//GEN-LAST:event_jMenuOptionsActionPerformed

    private void depthModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depthModeButtonActionPerformed
        actuatorControlPS.setValue(127);
        actuatorControlSB.setValue(127);
        actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
        actuatorControlPS.setEnabled(false);
        actuatorControlSB.setEnabled(false);
        if (this.mode != 0)
        {
            this.mode = 0;
            System.out.println("Mode 0 - Depth");
            try
            {
                this.client_ROV.sendCommand("cmd_mode:" + String.valueOf(this.mode));
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            }
        } else
        {
            System.out.println("Already in depth mode.");
        }
    }//GEN-LAST:event_depthModeButtonActionPerformed

    private void jMenuIOControllerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuIOControllerActionPerformed
        io.setVisible(true);
    }//GEN-LAST:event_jMenuIOControllerActionPerformed

    private void cameraPitchTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cameraPitchTextFieldActionPerformed
    {//GEN-HEADEREND:event_cameraPitchTextFieldActionPerformed

        try
        {
            cameraPitchTextField.commitEdit();
            if (cameraPitchTextField.getText() != null && isInteger(cameraPitchTextField.getText()))
            {
                this.cameraPitchValue = Integer.parseInt(cameraPitchTextField.getText());
            }
            if (this.cameraPitchValue > 100)
            {
                this.cameraPitchValue = 100;
                System.out.println("Camera Pitch input too high! Set to max (100)");
            } else if (this.cameraPitchValue < 0)
            {
                this.cameraPitchValue = 0;
                System.out.println("Camera Pitch input too low! Set to min (0)");
            }

            if (this.cameraPitchValue <= 75 && this.cameraPitchValue >= 50)
            {
                cameraPitchLabel.setBackground(new Color(28, 28, 28));
                cameraPitchSlider.setValue(this.cameraPitchValue);
                cameraPitchLabel.setText(String.valueOf(this.cameraPitchValue));
                data.setCameraPitchValue(this.cameraPitchValue);
                System.out.println("Camera Pitch set to " + this.cameraPitchValue);
                this.client_Camera.sendCommand("setPitch:" + String.valueOf(this.cameraPitchValue));
                // Send this to the python TcpController program running on the Camera RPi

            } else
            {
                cameraPitchTextField.setValue(null);
                JOptionPane.showMessageDialog(this,
                        "Input is invalid. Valid integer values are 50-75.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (ParseException ex)
        {
            System.out.println(ex.getMessage());

        } catch (IOException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_cameraPitchTextFieldActionPerformed

    private void cameraPitchSliderMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_cameraPitchSliderMouseReleased
    {//GEN-HEADEREND:event_cameraPitchSliderMouseReleased
        try
        {
            this.cameraPitchValue = cameraPitchSlider.getValue();
            cameraPitchLabel.setText(Integer.toString(this.cameraPitchValue));
            cameraPitchTextField.setText(Integer.toString(this.cameraPitchValue));
            data.setCameraPitchValue(cameraPitchValue);
            System.out.println("Camera Pitch set to " + cameraPitchSlider.getValue());
            this.client_Camera.sendCommand("setPitch:" + String.valueOf(this.cameraPitchValue));
            // Send this to the java program running on the Camera RPi
        } catch (Exception ex)
        {
            Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cameraPitchSliderMouseReleased

    private void delayTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_delayTextFieldActionPerformed
    {//GEN-HEADEREND:event_delayTextFieldActionPerformed
        try
        {
            if (delayTextField.getText() != null && isNumeric(delayTextField.getText()))
            {
                this.photoModeDelay = Double.parseDouble(delayTextField.getText());
                if (this.photoModeDelay > 99)
                {
                    this.photoModeDelay = 99;
                    System.out.println("Photo Mode Delay input too high! Set to max (99)");
                } else if (this.photoModeDelay < 0)
                {
                    this.photoModeDelay = 0;
                    System.out.println("Photo Mode Delay input too low! Set to min (0)");
                }
                photoModeDelayLabel.setText(String.valueOf(this.photoModeDelay) + " s");
                data.setPhotoModeDelay(this.photoModeDelay);
                System.out.println("Photo Mode Delay set to " + String.valueOf(this.photoModeDelay));

                this.udpClient.sendDelayCommand();
            } else
            {
                System.out.println("Invalid delay entered.");
            }
        } catch (NumberFormatException ex)
        {
            Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_delayTextFieldActionPerformed

    private void photoModeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_photoModeButtonActionPerformed
    {//GEN-HEADEREND:event_photoModeButtonActionPerformed
        if (photoModeButton.isSelected())
        {
            try
            {
                data.setPhotoMode(true);
            } catch (Exception ex)
            {
                Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else
        {
            try
            {
                data.setPhotoMode(false);
            } catch (Exception ex)
            {
                Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_photoModeButtonActionPerformed

    private void getPhotosButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_getPhotosButtonActionPerformed
    {//GEN-HEADEREND:event_getPhotosButtonActionPerformed
//        GetImagesFrame imgframe = new GetImagesFrame();
//        imgframe.setVisible(true);
//        imgframe.setLocation(this.getLocation().x, this.getLocation().y);
        Object[] options =
        {
            "OK", "Cancel"
        };
        int choice = JOptionPane.showOptionDialog(this,
                "Warning! This might take a few minutes and will freeze the GUI.\nPress OK to continue, or Cancel to abort.",
                "Warning!",
                JOptionPane.YES_NO_OPTION, //int optionType
                JOptionPane.INFORMATION_MESSAGE, //int messageType
                null, //Icon icon,
                options, //Object[] options,
                options[0]);//Object initialValue 
        if (choice == 0)
        {
            FtpClient ftp = null;
            File dir = null;
            try
            {
                try
                {
                    dir = new File("C:\\TowedROV\\ROV_Photos\\");
                    if (!dir.exists() || !dir.isDirectory())
                    {
                        System.out.println("No directory found, creating a new one at C://TowedROV/ROV_Photos/");
                        dir.mkdir();
                    }
                } catch (Exception e)
                {
                    System.out.println("No directory found, creating a new one at C://TowedROV/ROV_Photos/");
                    dir.mkdir();
                }

                // Test FTP Client (WORKING)
                ftp = new FtpClient(this.client_Camera.getIP());
                Thread ftpThread = new Thread(ftp);
                ftpThread.start();
                ftp.open();
                //int count = 1;
                Collection<String> list = ftp.getFileList("ftp/images");
                for (String s : list)
                {
                    ftp.downloadFile("ftp/images/" + s, dir.getPath() + "\\" + s, dir.getPath());
                    //imgframe.progressLabel.setText(String.valueOf(count) + " / " + String.valueOf(list.size()));
                    System.out.println(s);
                    //count++;
                }

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            } finally
            {
                if (ftp != null)
                {
                    ftp.disconnect();
                }
            }
        } else if (choice == 0)
        {
            System.out.println("Cancelled getting the images.");
        } else
        {
            System.out.println("No option chosen: Cancelled getting the images.");
        }
    }//GEN-LAST:event_getPhotosButtonActionPerformed

    private void lightSliderMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lightSliderMouseReleased
    {//GEN-HEADEREND:event_lightSliderMouseReleased
        try
        {
            if (lightSwitch.isSelected())
            {
                //data.setCameraPitchValue(cameraPitchValue);
                int value = lightSlider.getValue();
                System.out.println("ROV Lights set to " + value);
                this.client_Camera.sendCommand("setLed:" + String.valueOf(value));
                // Send this to the python TcpController program running on the Camera RPi
            }
        } catch (Exception ex)
        {
            Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lightSliderMouseReleased

    private void manualControlButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_manualControlButtonActionPerformed
    {//GEN-HEADEREND:event_manualControlButtonActionPerformed
        if (this.mode != 2)
        {
            actuatorControlPS.setEnabled(true);
            actuatorControlSB.setEnabled(true);
            this.mode = 2;
            System.out.println("Mode 2 - Manual wing control");
            try
            {
                this.client_ROV.sendCommand("cmd_mode:" + String.valueOf(this.mode));
                this.client_ROV.sendCommand("cmd_manualWingControl:" + String.valueOf(manualControlButton.isSelected()));
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            }
        } else
        {
            depthModeButton.doClick();
        }
    }//GEN-LAST:event_manualControlButtonActionPerformed

    private void resetManualControlButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetManualControlButtonActionPerformed
    {//GEN-HEADEREND:event_resetManualControlButtonActionPerformed
        actuatorControlPS.setValue(127);
        actuatorControlSB.setValue(127);
        actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
        if (manualControlButton.isSelected())
        {
            try
            {
                this.client_ROV.sendCommand("cmd_actuatorPS:127");
                this.client_ROV.sendCommand("cmd_actuatorSB:127");
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            }
        } else
        {
            //System.out.println("Not in manual control mode.");
        }
    }//GEN-LAST:event_resetManualControlButtonActionPerformed

    private void actuatorControlPSMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_actuatorControlPSMouseReleased
    {//GEN-HEADEREND:event_actuatorControlPSMouseReleased
        if (manualControlButton.isSelected())
        {
            if (lockButton.isSelected())
            {
                actuatorControlSB.setValue(actuatorControlPS.getValue());
                actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
                try
                {
                    this.client_ROV.sendCommand("cmd_actuatorPS:" + String.valueOf(actuatorControlPS.getValue()));
                    this.client_ROV.sendCommand("cmd_actuatorSB:" + String.valueOf(actuatorControlSB.getValue()));
                } catch (IOException ex)
                {
                    System.out.println("IOException: " + ex.getMessage());
                }
            } else
            {
                actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
                try
                {
                    this.client_ROV.sendCommand("cmd_actuatorPS:" + String.valueOf(actuatorControlPS.getValue()));
                } catch (IOException ex)
                {
                    System.out.println("IOException: " + ex.getMessage());
                }
            }

        } else
        {
            //System.out.println("Not in manual control mode.");
        }

    }//GEN-LAST:event_actuatorControlPSMouseReleased

    private void actuatorControlSBMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_actuatorControlSBMouseReleased
    {//GEN-HEADEREND:event_actuatorControlSBMouseReleased
        if (manualControlButton.isSelected())
        {
            if (lockButton.isSelected())
            {
                actuatorControlPS.setValue(actuatorControlSB.getValue());
                actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
                try
                {
                    this.client_ROV.sendCommand("cmd_actuatorPS:" + String.valueOf(actuatorControlPS.getValue()));
                    this.client_ROV.sendCommand("cmd_actuatorSB:" + String.valueOf(actuatorControlSB.getValue()));
                } catch (IOException ex)
                {
                    System.out.println("IOException: " + ex.getMessage());
                }
            } else
            {

                actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
                try
                {
                    this.client_ROV.sendCommand("cmd_actuatorSB:" + String.valueOf(actuatorControlSB.getValue()));
                } catch (IOException ex)
                {
                    System.out.println("IOException: " + ex.getMessage());
                }
            }
        } else
        {
            //System.out.println("Not in manual control mode.");
        }
    }//GEN-LAST:event_actuatorControlSBMouseReleased

    private void depthInputTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_depthInputTextFieldActionPerformed
    {//GEN-HEADEREND:event_depthInputTextFieldActionPerformed
        try
        {
            depthInputTextField.commitEdit();
            if (depthInputTextField.getText() != null && isNumeric(depthInputTextField.getText()))
            {
                double newSetpoint;
                try
                {
                    newSetpoint = Double.parseDouble(depthInputTextField.getText());
                } catch (ClassCastException ex)
                {
                    Long newSetpointLong = Long.parseLong(depthInputTextField.getText());
                    newSetpoint = newSetpointLong.doubleValue();
                }

                if (newSetpoint <= 50 && newSetpoint >= 0)
                {
                    setpointLabel.setBackground(new Color(39, 44, 50));
//                    previousSetpoint = setpoint;
//                    setpoint = newSetpoint;
//                    depthInputTextField.setValue(null);
                    setpointLabel.setText("Current setpoint: " + newSetpoint + "m");
                    System.out.println("Depth set to " + String.valueOf(newSetpoint));
                    this.client_ROV.sendCommand("cmd_depth:" + String.valueOf(newSetpoint));
                } else
                {
                    depthInputTextField.setValue(null);
                    depthInputTextField.setText("");
                    JOptionPane.showMessageDialog(this,
                            "Input is invalid. (Max depth 50m)",
                            "Input error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } else
            {
                System.out.println("Invalid input entered.");
            }
        } catch (IOException ex)
        {
            System.out.println("IOException: " + ex.getMessage());
        } catch (ParseException ex)
        {
            System.out.println("ParseException: " + ex.getMessage());
        }
    }//GEN-LAST:event_depthInputTextFieldActionPerformed

    private void lockButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_lockButtonActionPerformed
    {//GEN-HEADEREND:event_lockButtonActionPerformed
        if (actuatorControlPS.getValue() != 127)
        {
            actuatorControlPS.setValue(127);
            actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
            try
            {
                this.client_ROV.sendCommand("cmd_actuatorPS:" + String.valueOf(actuatorControlPS.getValue()));
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            }
        }
        if (actuatorControlSB.getValue() != 127)
        {
            actuatorControlSB.setValue(127);
            actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));
            try
            {
                this.client_ROV.sendCommand("cmd_actuatorSB:" + String.valueOf(actuatorControlSB.getValue()));
            } catch (IOException ex)
            {
                System.out.println("IOException: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_lockButtonActionPerformed

    private void clearImagesButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearImagesButtonActionPerformed
    {//GEN-HEADEREND:event_clearImagesButtonActionPerformed
        try
        {
            this.client_Camera.sendCommand("clearImages");
            this.udpClient.sendResetIMGcommand();
            int tempNum = data.getImageNumber();
            data.setImageNumber(0);
            imageNumberLabel.setText("0 / 1000");
            JOptionPane.showMessageDialog(this,
                    "Successfully cleared " + String.valueOf(tempNum) + " of " + String.valueOf(tempNum) + " images on the RPi.",
                    "Cleared Images",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex)
        {
            Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_clearImagesButtonActionPerformed

    private void calibrateMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_calibrateMenuItemActionPerformed
    {//GEN-HEADEREND:event_calibrateMenuItemActionPerformed
        try
        {
            // TODO add your handling code here:
            // Kjør kalibrering!
            jMenuCalibrate.setText("Calibrated!");
            jMenuCalibrate.setIcon(new ImageIcon(ImageIO.read(new File("src/ntnusubsea/gui/Images/Calibrated.gif"))));
        } catch (IOException ex)
        {
            System.out.println("IOException when calibrating: " + ex.getMessage());

        }
    }//GEN-LAST:event_calibrateMenuItemActionPerformed

    private void lightSwitchBlueLEDActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_lightSwitchBlueLEDActionPerformed
    {//GEN-HEADEREND:event_lightSwitchBlueLEDActionPerformed
        if (lightSwitchBlueLED.isSelected())
        {
            try
            {
                client_Camera.sendCommand("cmd_lightMode:1");
                //lightSlider.setValue(40);
            } catch (IOException ex)
            {
                System.out.println("Error while turning the blue LEDs on: " + ex.getMessage());
            }
        } else
        {
            try
            {
                client_Camera.sendCommand("cmd_lightMode:0");
                //lightSlider.setValue(19);
            } catch (IOException ex)
            {
                System.out.println("Error while turning the blue LEDs off: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_lightSwitchBlueLEDActionPerformed

    Action exitFullscreenAction = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            exitFullscreenButton.doClick();
        }
    };

//    Action sendInputAction = new AbstractAction()
//    {
//        public void actionPerformed(ActionEvent e)
//        {
//            if (depthInputTextField.isFocusOwner())
//            {
//                //sendButton.doClick();
//            }
//
//        }
//    };
    public static boolean isNumeric(String string)
    {
        boolean numeric = true;
        try
        {
            Double num = Double.parseDouble(string);
        } catch (NumberFormatException e)
        {
            numeric = false;
        }
        return numeric;
    }

    public static boolean isInteger(String str)
    {
        if (str == null)
        {
            return false;
        }
        if (str.isEmpty())
        {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-')
        {
            if (1 == str.length())
            {
                return false;
            }
            i = 1;
        }
        for (; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c < '0' || c > '9')
            {
                return false;
            }
        }
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider actuatorControlPS;
    private javax.swing.JSlider actuatorControlSB;
    private javax.swing.JProgressBar actuatorDutyCycleBar1;
    private javax.swing.JProgressBar actuatorDutyCycleBar2;
    private javax.swing.JLabel actuatorHeader1;
    private javax.swing.JLabel actuatorHeader2;
    private javax.swing.JPanel actuatorPanel1;
    private javax.swing.JPanel actuatorPanel2;
    private javax.swing.JLabel actuatorPosLabel;
    private javax.swing.JPanel background;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem calibrateMenuItem;
    private javax.swing.JPanel cameraControlPanel;
    private javax.swing.JLabel cameraHeader;
    private javax.swing.JPanel cameraPanel;
    private javax.swing.JPanel cameraPanel1;
    private javax.swing.JLabel cameraPitchLabel;
    private javax.swing.JSlider cameraPitchSlider;
    private javax.swing.JFormattedTextField cameraPitchTextField;
    private javax.swing.JButton clearImagesButton;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JFormattedTextField delayTextField;
    private javax.swing.JLabel depthHeader;
    private javax.swing.JFormattedTextField depthInputTextField;
    private javax.swing.JRadioButton depthModeButton;
    private javax.swing.JPanel depthPanel;
    private javax.swing.JLabel emergencyHeader;
    private javax.swing.JPanel emergencyPanel;
    private javax.swing.JButton emergencyStopButton;
    private javax.swing.JButton exitFullscreenButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JFrame fullscreen;
    private javax.swing.JButton fullscreenButton;
    private javax.swing.JButton getPhotosButton;
    private javax.swing.JLabel headingLabel;
    private javax.swing.JButton helpFrameOKbutton;
    private javax.swing.JFrame helpframe;
    private javax.swing.JLabel imageNumberLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuAbout;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuCalibrate;
    private javax.swing.JMenu jMenuConnect;
    private javax.swing.JMenuItem jMenuEchosounder;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuIOController;
    private javax.swing.JMenuItem jMenuItemConnect;
    private javax.swing.JMenuItem jMenuItemDisconnect;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuOptions;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel latitudeLabel;
    private javax.swing.JLabel leakLabel;
    private javax.swing.JLabel lightHeader;
    private javax.swing.JPanel lightPanel;
    private javax.swing.JSlider lightSlider;
    private javax.swing.JToggleButton lightSwitch;
    private javax.swing.JToggleButton lightSwitchBlueLED;
    private javax.swing.JLabel lightSwitch_lbl;
    private javax.swing.JToggleButton lockButton;
    private javax.swing.JLabel longitudeLabel;
    private javax.swing.JToggleButton manualControlButton;
    private javax.swing.JToggleButton photoModeButton;
    private javax.swing.JLabel photoModeDelayLabel;
    private javax.swing.JLabel photoModeDelay_FB_Label;
    private javax.swing.JLabel pitchLabel;
    private javax.swing.JLabel positionHeader;
    private javax.swing.JPanel positionPanel;
    private javax.swing.JButton resetManualControlButton;
    private javax.swing.JLabel rollLabel;
    private javax.swing.JLabel rovDepthLabel;
    private javax.swing.JLabel seafloorDepthBoatLabel;
    private javax.swing.JLabel seafloorDepthRovLabel;
    private javax.swing.JRadioButton seafloorModeButton;
    private javax.swing.JLabel setpointLabel;
    private javax.swing.JLabel warningLabel1;
    private javax.swing.JLabel warningLabel2;
    private javax.swing.JPanel window;
    private javax.swing.JLabel wingLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run()
    {
        try
        {
            videoImage = ImageIO.read(getClass().getResource("/ntnusubsea/gui/Images/TowedROV.jpg"));
            data.setVideoImage(videoImage);
        } catch (IOException ex)
        {
            Logger.getLogger(NTNUSubseaGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.showImage(videoImage);
        this.setVisible(true); //To change body of generated methods, choose Tools | Templates.

        //
        try
        {
            //  wingImage = ImageIO.read(new File("Images/rovwing.png"));
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        this.showImage(videoImage);

//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(NTNUSubseaGUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        //actuatorDutyCycleBar1.setValue(data.getBarValue());
        //System.out.println(data.getPitchAngle());
        if (data.getVideoImage() != null)
        {
            this.showImage(data.getVideoImage());
        }
        photoModeDelay_FB_Label.setText(String.valueOf(df2.format(data.getPhotoModeDelay_FB())) + " s");
        imageNumberLabel.setText(data.getImageNumber() + " / 1000");
        headingLabel.setText("Heading: " + data.getHeading());
        longitudeLabel.setText("Longitude: " + data.getLongitude());
        latitudeLabel.setText("Latitude: " + data.getLatitude());
        rovDepthLabel.setText("<html>ROV depth:<br/><br/><center/>" + data.getDepth());
        rollLabel.setText("<html>Roll angle:<br/><br/><center/>" + data.getRollAngle());
        pitchLabel.setText("<html>Pitch angle:<br/><br/><center/>" + data.getPitchAngle());
        wingLabel.setText("<html>Wing angle:<br/><br/><center/>" + data.getWingAngle());
        seafloorDepthBoatLabel.setText("<html>Depth beneath boat:<br/><br/><center/>" + data.getDepthBeneathBoat());
        seafloorDepthRovLabel.setText("<html>Depth beneath ROV:<br/><br/><center/>" + data.getDepthBeneathRov());

//        actuatorControlPS.setValue(data.getFb_actuatorPSPos);
//        actuatorControlSB.setValue(data.getFb_actuatorSBPos);
        actuatorPosLabel.setText("<html>PS: " + String.valueOf(actuatorControlPS.getValue()) + "<br/><br/>SB: " + String.valueOf(actuatorControlSB.getValue()));

        if (data.getLeakStatus())
        {
            leakLabel.setText("<html>Leak detection:<br/><br/><center/>Leak detected!");
            try
            {
                leakLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/ntnusubsea/gui/Images/Box.png"))));
            } catch (IOException ex)
            {
                Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else
        {
            leakLabel.setText("<html>Leak detection:<br/><br/><center/>No leak detected");
            /*try {
                leakLabel.setIcon(new ImageIcon(ImageIO.read(new File("src/ntnusubsea/gui/Images/Box.png"))));
            } catch (IOException ex) {
                Logger.getLogger(ROVFrame.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
}
