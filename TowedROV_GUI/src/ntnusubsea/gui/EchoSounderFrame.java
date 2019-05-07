/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Frame to display a graph panel
 *
 */
public class EchoSounderFrame extends javax.swing.JFrame implements Runnable, Observer
{

    private Data data;
    private GraphPanel graph;
    private XYPlot plot;

    /**
     * Creates new form SonarFrame
     *
     * @param data Data containing shared variables
     */
    public EchoSounderFrame(Data data)
    {
        super("XY Line Chart Example with JFreechart");
        initComponents();
        this.data = data;
        JPanel chartPanel = createChartPanel();

        //setSize(640, 480);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        chartPanel.setSize(jPanel1.getWidth(), jPanel1.getHeight());
        chartPanel.setVisible(true);
        jPanel1.add(chartPanel, BorderLayout.CENTER);
        this.add(jPanel1);
        this.pack();

//        initComponents();
//        this.data = data;
//        graph = new GraphPanel();
//        graph.setSize(jPanel1.getWidth(), jPanel1.getHeight());
//        graph.setBackground(Color.BLACK);
//        graph.setForeground(Color.WHITE);
//        graph.setVisible(true);
//        jPanel1.add(graph);
//        this.add(jPanel1);
//        this.pack();
    }

    private JPanel createChartPanel()
    {
        String chartTitle = "ROV Depth Chart";
        String xAxisLabel = "Time";
        String yAxisLabel = "Depth";
        XYDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset);
        plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        return new ChartPanel(chart);
    }

    private XYDataset createDataset()
    {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("ROV Depth");
        XYSeries series2 = new XYSeries("Seafloor");
        XYSeries series3 = new XYSeries("Surface");

        series1.add(0.0, 0.0);
        series1.add(5.0, 0.2);
        series1.add(9.0, 0.5);
        series1.add(12.0, 0.1);
        series1.add(16.0, -0.1);
        series1.add(20.0, 0.2);

        series2.add(0.0, 0.0);
        series2.add(30.0, 0.0);

        series3.add(0.0, -20.0);
        series3.add(5.0, -18.0);
        series3.add(9.0, -15.0);
        series3.add(12.0, -19.0);
        series3.add(16.0, -22.0);
        series3.add(20.0, -17.0);

//        dataset.addSeries(series1);
        dataset.addSeries(series2);
//        dataset.addSeries(series3);

        return dataset;
    }

    private XYDataset createDataset_2()
    {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("ROV Depth");
        XYSeries series2 = new XYSeries("Seafloor");
        XYSeries series3 = new XYSeries("Surface");

        Iterator it = data.rovDepthDataList.iterator();
        while (it.hasNext())
        {
            String s = it.next().toString();
            String[] data = s.split(":");
            series1.add(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
        }

        Iterator it2 = data.depthBeneathBoatDataList.iterator();
        while (it2.hasNext())
        {
            String s = it2.next().toString();
            String[] data = s.split(":");
            series2.add(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
        }

//        series1.add(0.0, 0.0);
//        series1.add(5.0, 0.2);
//        series1.add(9.0, 0.5);
//        series1.add(12.0, 0.1);
//        series1.add(16.0, -0.1);
//        series1.add(20.0, 0.2);
//
//        series2.add(0.0, -1.5);
//        series2.add(4.0, -8.0);
//        series2.add(8.0, -13.0);
//        series2.add(12.0, -17.0);
//        series2.add(15.0, -19.25);
//        series2.add(17.0, -18.55);
//        series2.add(20.0, -15.0);
//        series3.add(0.0, -20.0);
//        series3.add(5.0, -18.0);
//        series3.add(9.0, -15.0);
//        series3.add(12.0, -19.0);
//        series3.add(16.0, -22.0);
//        series3.add(20.0, -17.0);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
//        dataset.addSeries(series3);

        return dataset;
    }

    /**
     * Draws and displays the graphs
     */
    public void showGraph()
    {
        graph.createAndShowGraph();
    }

    /**
     * |- Not finished -| Refreshes the graphs with the given depth and position
     * of the 2nd graph in the x axis x-axis
     *
     * @param position X position of second graph
     * @param depth Depth of the graphs
     */
    public void refreshGraph(int position, int depth)
    {
        graph.drawGraph(position, depth);
        graph.repaint();
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

        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Echo sounder");
        setBackground(new java.awt.Color(39, 44, 50));
        setMinimumSize(new java.awt.Dimension(1200, 600));
        setPreferredSize(new java.awt.Dimension(1200, 600));
        setSize(new java.awt.Dimension(1200, 600));

        jPanel1.setBackground(new java.awt.Color(39, 44, 50));
        jPanel1.setForeground(new java.awt.Color(39, 44, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 600));
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tools");

        jMenuItem2.setText("Calibrate");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String cableLength = (String) JOptionPane.showInputDialog(this, "Enten current cable length (Meters)", "Calibration", JOptionPane.PLAIN_MESSAGE, null, null, "100.000");
        try
        {
            System.out.println(Float.valueOf(cableLength));
        } catch (NumberFormatException | NullPointerException ex)
        {
            System.out.println("Invalid or no input");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    @Override
    public void run()
    {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        double time = 0.0;
        double time2 = time + data.getTimeBetweenBoatAndRov();
        String rovDepthValue = "0.0";
        String depthBeneathBoatValue = "0.0";
        double counter = 0;
        double counter2 = -25.0;
        double amount = -1.0;
        double amount2 = -0.1;

        while (true)
        {
//            for (int i = 1; i < 10; i++)
//            {
//                
            try
            {

                data.updateRovDepthDataList(String.valueOf(df.format(time)), rovDepthValue);
                data.updateDepthBeneathBoatDataList(String.valueOf(df.format(time2)), depthBeneathBoatValue);
                Thread.sleep(100);
                time = time + 0.1;
                time2 = time + data.getTimeBetweenBoatAndRov();
                
                rovDepthValue = String.valueOf(df.format(data.getRovDepth()));
                depthBeneathBoatValue = String.valueOf(df.format(data.getDepthBeneathBoat()));

//                //-------------------------------------------------------------------------
//                // COMMENT OUT THIS SECTION AND UNCOMMENT THE TWO LINES ABOVE TO GET REAL DATA FROM THE ROV
//                counter = (counter + amount) * 1.05;
//                counter2 = counter2 + amount2;
//                rovDepthValue = String.valueOf(df.format(counter));
//                depthBeneathBoatValue = String.valueOf(df.format(counter2));
//
//                if (counter <= -22.0)
//                {
//                    counter = -20.0;
//                }
//                if (counter <= -20.0)
//                {
//                    amount = +1.0;
//                } else if (counter >= 0.0)
//                {
//                    amount = -1.0;
//                }
//
//                if (counter2 <= -27.0)
//                {
//                    counter2 = -25.0;
//                }
//                if (counter2 <= -26.0)
//                {
//                    amount2 = 0.1;
//                } else if (counter2 >= -24.0)
//                {
//                    amount2 = -0.1;
//                }
//                // -----------------------------------------------------------------------
                this.plot.setDataset(createDataset_2());

            } catch (InterruptedException ex)
            {
                Logger.getLogger(EchoSounderFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
//            }

        }

//        showGraph();
//        int depth = 0;
//        int xpos = 0;
//
//        while (true)
//        {
//            for (int i = 0; i < 10; i++)
//            {
//                depth = depth + 1;
//                xpos = xpos + 3;
//                this.refreshGraph(xpos, depth);
//                System.out.println("Refresed graph: " + String.valueOf(xpos) + ":" + String.valueOf(depth));
//                try
//                {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex)
//                {
//                    Logger.getLogger(EchoSounderFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            for (int o = 10; o > 0; o--)
//            {
//                depth = depth - 1;
//                xpos = xpos + 3;
//                this.refreshGraph(xpos, depth);
//                System.out.println("Refresed graph: " + String.valueOf(xpos) + ":" + String.valueOf(depth));
//                try
//                {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex)
//                {
//                    Logger.getLogger(EchoSounderFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg)
    {
        //int depth = Math.round(data.getDepth() * 100);
        // refreshGraph(0, depth);
    }
}
