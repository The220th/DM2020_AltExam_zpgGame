package ZPG.GameLogic;

import java.util.*;

import java.lang.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.MapGenerator.Town;
import ZPG.GameLogic.Searchers.*;
import ZPG.GameLogic.Bot;


public class GameHundler
{
    private static GameHundler GH;

    private int currentTick;
    private int delayBetweenTick;
    private int delayToPrint;
    private Runnable print;

    private WorldMap map;
    private List<Town> towns;
    private List<Bot> bots;

    public static void main(String[] args)
    {
        GH = new GameHundler( () -> {} );
        GH.BusinessLogic();
    }

    public GameHundler(Runnable print)
    {
        this.map = new WorldMap(10);
        this.map.genWorld();
        this.towns = map.getTowns();
        GH = this;
        this.currentTick = 0;
        this.delayBetweenTick = 30;
        this.delayToPrint = 1000;

        this.bots = new ArrayList<Bot>();
        /*for(int i = 0; i < 1; ++i)
            bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), chooseSearchAlg()));*/

        sPoint buff = new sPoint(0, 0); //delete below
        bots.add(new Bot(buff, new DijkstraSearcher(this.map)));
        bots.add(new Bot(buff, new Dijkstra(this.map)));
        bots.add(new Bot(buff, new newDijkstra(this.map)));
        bots.add(new Bot(buff, new LiSearcher(this.map)));
        bots.add(new Bot(buff, new BreadthFirstSearcher(this.map)));
        bots.add(new Bot(buff, new AStarSearcher(this.map)));

        this.print = print;
    }

    private IDeWaySearcher chooseSearchAlg()
    {
        Random r = new Random();
        //int what = r.nextInt(4);
        int what = 3;
        IDeWaySearcher res = null;
        switch(what)
        {
            case 0:
                res = new BreadthFirstSearcher(this.map);
                break;
            case 1:
                if(DepthFirstSearcher.getNums() == -1)
                    res = new DepthFirstSearcher(this.map);
                else
                    res = new BreadthFirstSearcher(this.map);
                break;
            case 2:
                res = new LiSearcher(this.map);
                break;
            case 3:
                res = new /*Dijkstra(this.map);*/ DijkstraSearcher(this.map);
                break;
            case 4:
                res = null;
                break;
            default:
                System.out.println("Failed successfully in chooseSearchAlg");
                break;
        }
        return res;
    }

    public void BusinessLogic()
    {
        Runnable task = () ->
        {
            int printTick = 0;
            while(true)
            {
                ++currentTick;
                printTick+=delayBetweenTick;
                if(currentTick % 100 == 0)
                    System.out.println("Tick = " + currentTick);
                for(Bot curBot : bots)
                {
                    curBot.live();
                }
            
                try
                {
                    Thread.sleep(delayBetweenTick);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
                if(printTick >= delayToPrint)
                {
                    print.run();
                    printTick = 0;
                }
            }
        };
        new Thread(task).start();
    }

    public static GameHundler getCurrentGameHundler()
    {
        return GH;
    }

    public WorldMap getMap()
    {
        return this.map;
    }

    public List<Bot> getBots()
    {
        return bots;
    }

    public Town getNearestTown(sPoint from)
    {
        Town res = null;
        double minR = Double.POSITIVE_INFINITY;
        double buff;

        for(Town t : towns)
        {
            if(from.equals(t.getCoords()))
                return t;
            
            buff = t.getCoords().getDistance(from);
            if(minR > buff)
            {
                minR = buff;
                res = t;
            }
        }
        return res;
    }
}