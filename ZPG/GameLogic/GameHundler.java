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
    private static GameHundler GL;

    private int currentTick;
    private Runnable print;

    private WorldMap map;
    private List<Town> towns;
    private List<Bot> bots;

    public GameHundler(Runnable print)
    {
        this.map = new WorldMap(10);
        this.map.genWorld();
        this.towns = map.getTowns();
        GL = this;
        this.currentTick = 0;
        bots = new ArrayList<Bot>();
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()), new BreadthFirstSearcher(map)));

        this.print = print;
    }

    public void BusinessLogic()
    {
        Runnable task = () ->
        {
            while(true)
            {
                ++currentTick;
                System.out.println("Tick = " + currentTick);
                for(Bot curBot : bots)
                {
                    curBot.live();
                }
            
                try
                {
                    Thread.sleep(300);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
                print.run();
            }
        };
        new Thread(task).start();
    }

    public static GameHundler getCurrentGameHundler()
    {
        return GL;
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