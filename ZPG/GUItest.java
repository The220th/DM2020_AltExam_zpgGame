package ZPG;

import java.lang.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private BotChooser botChooser = null;
    private BotsViewer botViewer = null;
    private sComponent printedMap;
    private GameHundler GH;
    private JComboBox<Bot> botsCombo;

    public sFrame()
    {
        printedMap = new sComponent(2);
        GH = printedMap.getGameHundler();
        List<Bot> buffBotsList = GH.getBots();
        botsCombo = new JComboBox<Bot>();
        for(Bot bot : buffBotsList)
            botsCombo.addItem(bot);
        botsCombo.addActionListener(event -> 
                printedMap.setCurrentBot(botsCombo.getItemAt(botsCombo.getSelectedIndex())));

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new TestAction("Open"));
        fileMenu.add(new TestAction("Save"));

        JMenu BotMenu = new JMenu("BotHundler");
        JMenuItem chooseBotItem = new JMenuItem("Choose bot");
        chooseBotItem.addActionListener(
            event -> {
                if(botChooser == null)
                    botChooser = new BotChooser(this, botsCombo);
                botChooser.setVisible(true);
            }
        );
        JMenuItem viewBotsItem = new JMenuItem("View score");
        viewBotsItem.addActionListener(
            event ->{
                botViewer = new BotsViewer(this, GH.getBots());
                botViewer.setVisible(true);
            }
        );
        BotMenu.add(chooseBotItem);
        BotMenu.add(viewBotsItem);


        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);
        menubar.add(fileMenu);
        menubar.add(BotMenu);

        this.add(new JScrollPane(printedMap));
        this.pack();
    }

    class TestAction extends AbstractAction
    {
        public TestAction(String name)
        {
            super(name);
        }

        public void actionPerformed(ActionEvent event)
        {
            System.out.println(this.getValue(Action.NAME) + " choosed. *Saved or Opened... Coming Soon...* Beep Boop");
        }
    }

    class BotChooser extends JDialog
    {
        private JComboBox<Bot> botsCombo;
        public BotChooser(JFrame owner, JComboBox<Bot> botscombo)
        {
            super(owner, "Choose bot", false);

            botsCombo = botscombo;

            JPanel jp = new JPanel();
            jp.add(new JLabel("Bots: "));
            jp.add(botsCombo);
            /*this.add(
                new JLabel("Bots: "), BorderLayout.CENTER
            );
            this.add(botsCombo, BorderLayout.SOUTH);*/
            this.add(jp);
            pack();
        }
    }

    class BotsViewer extends JDialog
    {
        public BotsViewer(JFrame owner, List<Bot> bots)
        {
            //Выблядит сейчас очень недружелюбно. Это можно пофиксить, если использовать диспетчер сеточно-контейнерной компоновки (GridBagLayout). Если не лень, то изучи эту штуку и сделай нормально=)
            super(owner, "View bot`s score", false);

            List<Bot> sortedBots = new ArrayList<Bot>(bots);
            //ТУТ СОРТИРОВКА ПО ОЧКАМ sortedBots
            JPanel Gjp = new JPanel();
            for(Bot bot : sortedBots)
            {
                JPanel jp = new JPanel();
                jp.add(new JLabel(bot.toString()));
                jp.add(new JLabel(bot.getInfo()));
                Gjp.add(jp);
            }
            this.add(Gjp);
            pack();
        }
    }
}

class sComponent extends JComponent
{

    private int DEFAULT_WIDTH;
    private int DEFAULT_HEIGHT;
    private int sizeble;
    private WorldMap map;
    private GameHundler GH;

    private Bot currentBot;

    private Color botColor;
    private Color wayColor;
    
    public sComponent(int Sizeble)
    {
        currentBot = null;

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
        //System.out.println(currentBot);
        Graphics2D g = (Graphics2D)gOld;
        
        int n = map.getMaxSize();
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                paintBlock(i, j, map.getBlock(i, j).getColor(), g);
        
        int x, y;
        List<Bot> bots = GH.getBots();
        if(currentBot != null)
        {
            Deque<sPoint> buffWay = currentBot.getDeWay();
            if(buffWay != null)
            {
                Object buffLock = currentBot.getLock4Print();
                LinkedList<sPoint> way;
                synchronized(buffLock)
                {
                    way = new LinkedList<sPoint>((LinkedList<sPoint>)buffWay);
                }
                for(sPoint p : way)
                    paintBlock(p.getX(), p.getY(), wayColor, g);
            }
            x = currentBot.getCoords().getX();
            y = currentBot.getCoords().getY();
            paintBlock(x, y, botColor, g);
        }

            /*for(Bot bot : bots)
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
                }//

                x = bot.getCoords().getX();
                y = bot.getCoords().getY();
                paintBlock(x, y, botColor, g);
            }*/
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

    public GameHundler getGameHundler()
    {
        return GH;
    }

    public void setCurrentBot(Bot bot)
    {
        this.currentBot = bot;
    }
}