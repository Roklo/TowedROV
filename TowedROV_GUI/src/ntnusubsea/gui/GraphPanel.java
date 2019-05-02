/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * A class that represents a panel where graphs can we drawn to display data
 * to the user in a visual manner.
 */
@SuppressWarnings("serial")
public class GraphPanel extends JPanel {

    private final int MAX_SCORE = 65536;
    private final int BORDER_GAP = 30;
    private final Stroke GRAPH_STROKE = new BasicStroke(2);
    private final int GRAPH_START_POINT = 12;
    private final int HATCH_CNT = 10;
    private ArrayList<Integer> scores;
    private ArrayList<Integer> scores2;
    private int maxDataPoints = 5000;
    private int maxScore = 5;

    /**
     * Creates an instance of GraphPanel
     */
    public GraphPanel() {
        this.scores = new ArrayList<Integer>();
        this.scores2 = new ArrayList<Integer>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores2.size() - 1);
        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

        List<Point> graphPoints = new ArrayList<Point>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
        }

        List<Point> graphPoints2 = new ArrayList<Point>();
        for (int i = 0; i < scores2.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scores2.get(i)) * yScale + BORDER_GAP);
            graphPoints2.add(new Point(x1, y1));
        }

        // create x and y axes 
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        // create hatch marks for y axis. 
        for (int i = 0; i < HATCH_CNT; i++) {
            int x0 = BORDER_GAP;
            int x1 = GRAPH_START_POINT + BORDER_GAP;
            int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / HATCH_CNT + BORDER_GAP);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < HATCH_CNT; i++) {
            int x0 = getWidth() - (((i + 1) * (getWidth() - BORDER_GAP * 2)) / HATCH_CNT + BORDER_GAP);
            int x1 = x0;
            int y0 = getHeight() - GRAPH_START_POINT - BORDER_GAP;
            int y1 = getHeight() - BORDER_GAP;
            g2.drawLine(x0, y0, x1, y1);

        }

        Stroke oldStroke = g2.getStroke();
        g2.setColor(Color.GREEN);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setColor(Color.YELLOW);
        for (int i = 0; i < graphPoints2.size() - 1; i++) {
            int x1 = graphPoints2.get(i).x;
            int y1 = graphPoints2.get(i).y;
            int x2 = graphPoints2.get(i + 1).x;
            int y2 = graphPoints2.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setStroke(oldStroke);

    }

    /**
     * Creates and displays the graph
     */
    public void createAndShowGraph() {
        Random random = new Random();
        for (int i = 0; i < maxDataPoints - 2000; i++) {
            scores.add(random.nextInt(maxScore) + 11);
        }
        for (int i = 0; i < maxDataPoints; i++) {
            scores2.add(random.nextInt(maxScore) + 1);
        }
        revalidate();
        repaint();

    }

    /**
     * Draws one new point in the graph and moves the old data 1 step further 
     * back.
     * @param position Position of the ROV compared to the vessel
     * @param depth Depth of the ROV
     */
    public void drawGraph(int position, int depth) {
        // int lastNumber = scores.set(maxDataPoints-200 - 1, depth + 11);
        int lastNumber = scores.set(maxDataPoints - 2001, scores2.get(maxDataPoints - 2000)+10000);
        for (int i = maxDataPoints - 2000 - 2; i >= 0; i--) {
            lastNumber = scores.set(i, lastNumber);
        }
        int lastNumber2 = scores2.set(maxDataPoints - 1, depth + 1);
        for (int i = maxDataPoints - 2; i >= 0; i--) {
            lastNumber2 = scores2.set(i, lastNumber2);
        }
        repaint();
    }
}
