package ZPG;

import java.lang.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.*;
import ZPG.GameLogic.GameHundler;
import ZPG.GameLogic.Bot;

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
    private GameHundler GH;

    private Color botColor;
    private Color wayColor;
    
    public sComponent(int Sizeble)
    {
        GH = new GameHundler( () -> this.repaint() );
        sizeble = Sizeble;
        map = GH.getMap();
        
        DEFAULT_WIDTH = map.getMaxSize()*Sizeble;
        DEFAULT_HEIGHT = map.getMaxSize()*Sizeble;

        botColor = new Color(200, 0, 255);
        wayColor = Color.RED;

        GH.BusinessLogic();
    }

    public void paintComponent(Graphics gOld)
    {
            Graphics2D g = (Graphics2D)gOld;
            
            int n = map.getMaxSize();
            for(int i = 0; i < n; i++)
                for(int j = 0; j < n; j++)
                    paintBlock(i, j, map.getBlock(i, j).getColor(), g);
            
            int x, y;
            List<Bot> bots = GH.getBots();
            for(Bot bot : bots)
            {
                if(true)
                {
                    Deque<sPoint> buffWay = bot.getDeWay();
                    if(buffWay != null)
                    {
                        Object buffLock = bot.getLock4Print();
                        LinkedList<sPoint> way;
                        synchronized(buffLock)
                        {
                            way = new LinkedList<sPoint>((LinkedList<sPoint>)buffWay);
                        }
                        for(sPoint p : way)
                            paintBlock(p.getX(), p.getY(), wayColor, g);
                    }
                }

                /*LinkedList<sPoint> wayyy = ((DijkstraSearcher)bot.searchAlg).get2See();
                if(wayyy != null)
                {
                    for(sPoint p : wayyy)
                    paintBlock(p.getX(), p.getY(), Color.ORANGE, g);
                }*/

                x = bot.getCoords().getX();
                y = bot.getCoords().getY();
                paintBlock(x, y, botColor, g);
            }
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