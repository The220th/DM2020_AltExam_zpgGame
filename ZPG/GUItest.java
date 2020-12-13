package ZPG;

import java.lang.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.*;
import ZPG.GameLogic.GameHundler;

public class GUItest
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater( () ->
        {
            sFrame frame = new sFrame();
            frame.setTitle("Landscape");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setSize(1280, 720);
        }
        );
    }
}

class sFrame extends JFrame
{
    public sFrame()
    {
        this.add(new JScrollPane(new sComponent(2)));
        this.pack();
    }
}

class sComponent extends JComponent
{
    private int DEFAULT_WIDTH;
    private int DEFAULT_HEIGHT;
    private int sizeble;
    private WorldMap map;
    LinkedList<sPoint> ll;
    
    public sComponent(int Sizeble)
    {
        GameHundler buffH = new GameHundler();
        buffH.getNearestTown(new sPoint(123, 123));
        sizeble = Sizeble;
        map = new WorldMap(10);
        map.genWorld();
        
        DEFAULT_WIDTH = map.getMaxSize()*Sizeble;
        DEFAULT_HEIGHT = map.getMaxSize()*Sizeble;

        //delete========
        //IDeWaySearcher test = new DepthFirstSearcher(map);
        //IDeWaySearcher test = new BreadthFirstSearcher(map);
        IDeWaySearcher test = new LiSearcher(map);
        ll = (LinkedList<sPoint>)test.search(new sPoint(7, 10), new sPoint(637, 781));
        //System.out.println("\n\n" + ll + "\n");
        //==============
    }

    public void paintComponent(Graphics gOld)
    {
        Graphics2D g = (Graphics2D)gOld;
        
        int n = map.getMaxSize();
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                paintBlock(j, i, map.getBlock(i, j).getColor(), g);
        
        for(sPoint next : ll)
            paintBlock(next.getX(), next.getY(), Color.RED, g);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void paintBlock(int X, int Y, Color c, Graphics2D g)
    {
        //Color buff;
        //buff = g.getColor();
        g.setPaint(c);
        g.fillRect(X*sizeble, Y*sizeble, sizeble, sizeble);
        //g.setPaint(buff);
    }
}