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
        JMenuItem viewBotsItem = new JMenuItem("View bots");
        viewBotsItem.addActionListener(
            event ->{
                if(botViewer == null)
                    botViewer = new BotsViewer(this, GH.getBots());
                else
                    botViewer.reset(GH.getBots());
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
        JPanel Gjp;
        LinkedList<JLabel> labels;

        public BotsViewer(JFrame owner, List<Bot> bots)
        {
            super(owner, "View bots", false);
            setPreferredSize(new Dimension(1150, 400));
            GridLayout GLout = new GridLayout();
            GLout.setColumns(1);
            GLout.setRows(bots.size()+1); //+1 для кнопки
            int min, buff;
            Bot buffBot = null;
            List<Bot> sortedBots = new ArrayList<Bot>(bots);
            for(int i = sortedBots.size()-1; i > 0; --i)    //По убыванию (справа налево сортирует). Если надо по возрастанию - поменять на "слева направо"
            {
                min = sortedBots.get(i).getScores();
                for(int j = i-1; j >= 0; --j)
                {
                    buff = sortedBots.get(j).getScores();
                    if(buff < min)
                    {
                        min = buff;
                        buffBot = sortedBots.get(j);
                        sortedBots.set(j, sortedBots.get(i));
                        sortedBots.set(i, buffBot);
                    }
                }
            }
            Gjp = new JPanel();
            Gjp.setLayout(GLout);

            JButton jbutton = new JButton("Refresh");
            jbutton.addActionListener(event -> this.reset(GameHundler.getCurrentGameHundler().getBots()));
            Gjp.add(jbutton);

            labels = new LinkedList<JLabel>();
            for(Bot bot : sortedBots)
            {
                JLabel bufflabel = new JLabel(bot.getInfo());
                JPanel jp = new JPanel();
                labels.add(bufflabel);
                jp.add(bufflabel);
                Gjp.add(jp);
            }

            this.add(Gjp);
            JScrollPane jSP = new JScrollPane(Gjp);
            getContentPane().add(jSP);
            pack();
        }
        
        public void reset(List<Bot> bots)
        {
            int min, buff;
            Bot buffBot = null;
            List<Bot> sortedBots = new ArrayList<Bot>(bots);
            for(int i = sortedBots.size()-1; i > 0; --i)    //По убыванию (справа налево сортирует). Если надо по возрастанию - поменять на "слева направо"
            {
                min = sortedBots.get(i).getScores();
                for(int j = i-1; j >= 0; --j)
                {
                    buff = sortedBots.get(j).getScores();
                    if(buff < min)
                    {
                        min = buff;
                        buffBot = sortedBots.get(j);
                        sortedBots.set(j, sortedBots.get(i));
                        sortedBots.set(i, buffBot);
                    }
                }
            }

            int j = 0;
            for(JLabel label : labels)
                label.setText(sortedBots.get(j++).getInfo());
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
        for(Bot bott : bots)
            paintBlock(bott.getCoords().getX(), bott.getCoords().getY(), botColor, g);

        if(currentBot != null)
        {
            x = currentBot.getCoords().getX();
            y = currentBot.getCoords().getY();
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
            paintBlock(x, y, botColor, g);
            printBotCross(x, y, botColor, g);

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

    private void printBotCross(int x, int y, Color c, Graphics2D g) //Отрисовка бота(он тут)
    {
        g.setPaint(c);
        g.drawOval(x*sizeble-15, y*sizeble-15, 30, 30);
        g.drawOval(x*sizeble-16, y*sizeble-16, 32, 32);
        g.drawOval(x*sizeble-17, y*sizeble-17, 34, 34);
        g.drawLine(x*sizeble-25, y*sizeble, x*sizeble-5, y*sizeble);
        g.drawLine(x*sizeble+25, y*sizeble, x*sizeble+5, y*sizeble);
        g.drawLine(x*sizeble, y*sizeble-25, x*sizeble, y*sizeble-5);
        g.drawLine(x*sizeble, y*sizeble+25, x*sizeble, y*sizeble+5);
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