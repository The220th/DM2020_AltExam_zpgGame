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
        GH = new GameHundler( () -> {System.out.println(GH.getStats());} );
        GH.delayToPrint = 50000;
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
        for(int i = 0; i < 25; ++i)
            bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), chooseSearchAlg()));

        //sPoint buff = new sPoint(0, 0);
        /*bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new newDijkstra(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new LeeSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new UnweightedLeeSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new BreadthFirstSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new AStarSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new DiagonalSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new CornerSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new LeekstraSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new LeeStarSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new BellmanFordSearcher(this.map)));
        bots.add(new Bot(sPoint.rndPoint(0, map.getMaxSize()-1), new LeeBellFordSearcher(this.map)));*/

        this.print = print;
    }

    private IDeWaySearcher chooseSearchAlg()
    {
        Random r = new Random();
        int what = r.nextInt(11);
        //int what = 6;
        IDeWaySearcher res = null;
        switch(what)
        {
            case 0:
                res = new BreadthFirstSearcher(this.map);
                break;
            case 1:
                /*if(DepthFirstSearcher.getNums() == -1)
                    res = new DepthFirstSearcher(this.map);
                else
                    res = new BreadthFirstSearcher(this.map);*/
                res = new AStarSearcher(this.map);
                break;
            case 2:
                res = new LeeSearcher(this.map);
                break;
            case 3:
                res = new newDijkstra(this.map);
                break;
            case 4:
                res = new DiagonalSearcher(this.map);
                break;
            case 5:
                res = new CornerSearcher(this.map);
                break;
            case 6:
                res = new LeekstraSearcher(this.map);
                break;
            case 7:
                res = new LeeStarSearcher(this.map);
                break;
            case 8:
                res = new UnweightedLeeSearcher(this.map);
                break;
            case 9:
                res = new BellmanFordSearcher(this.map);
                break;
            case 10:
                res = new LeeBellFordSearcher(this.map);
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
				try
				{
					++currentTick;
					printTick += delayBetweenTick;
					if(currentTick % 500 == 0)
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
				catch(Throwable t)
				{
					System.out.println(t);
					System.out.println(t.printStackTrace());
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
        try
        {
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
        } 
        catch(Throwable t)
        {
            System.out.println("NullPointer in getNearestTown function");
        }
        return res;
    }

    public String getStats()
    {
        ArrayList<Bot> sortedBots = new ArrayList<Bot>(bots);
        Collections.sort(sortedBots);
        Collections.reverse(sortedBots);
        StringBuilder res = new StringBuilder();
        res.append("\n==============================\n");
        res.append("Tick: " + currentTick + " \n");
        for(Bot bot : sortedBots)    
            res.append(bot.getInfo() + "\n");
        res.append("==============================\n");
        return res.toString();
    }

    public int getCurrentTick()
    {
        return currentTick;
    }
}