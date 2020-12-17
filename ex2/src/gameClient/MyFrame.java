package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 */
public class MyFrame extends JFrame {

    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;

    MyFrame(String a) {
        super(a);

        int _ind = 0;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(30, this.getWidth() - 30);
        Range ry = new Range(this.getHeight() - 30, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        Image img = this.createImage(w, h);
        Graphics graph = img.getGraphics();
        paintComponents(graph);
        g.drawImage(img, 0, 0, this);
//        g.clearRect(0, 0, w, h);
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        updateFrame();
    }

    @Override
    public void paintComponents(Graphics g) {
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        updateFrame();
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }
        int time = (int) _ar.getTime_left() / 1000;
        g.drawString("time left : " + time + "s", this.getWidth() - 150, 50);
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        for (node_data n : gg.getV()) {
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            for (edge_data e : gg.getE(n.getKey())) {
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            for (CL_Pokemon f : fs) {
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    //g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        Color[] colors = new Color[]{Color.red, Color.BLACK, Color.BLUE, Color.CYAN};
        int i = 0;
        String a = "";
        Font font = new Font("Ariel", Font.PLAIN, 15);
        g.setFont(font);
        g.drawString("Agents : ", 50, 50);
        while (rs != null && i < rs.size()) {
            g.setColor(colors[i % 4]);
            a = "id: " + rs.get(i).getID() + "  score: " + rs.get(i).getValue();
            font = new Font("Ariel", Font.PLAIN, 15);
            g.setFont(font);
            g.drawString(a, 50, 70 + i * 15);
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }
}
