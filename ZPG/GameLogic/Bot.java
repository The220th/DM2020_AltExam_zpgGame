package ZPG.GameLogic;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.GameHundler;
import ZPG.GameLogic.Searchers.*;
import ZPG.sMap.sPoint;

public class Bot implements Runnable
{
    private static int numbers = 1;

    private sPoint coords;
    private String name;
    private int scores;
    private Deque<sPoint> deWay;
    
    private boolean BUSY;

    private IDeWaySearcher searchAlg;

    private static GameHundler GH = null;

    public Bot(sPoint coordinates, IDeWaySearcher searchAlgorithm)
    {
        if(GH == null)
            GH = GameHundler.getCurrentGameHundler();
        
        this.coords = coordinates;
        this.searchAlg = searchAlgorithm;
        this.name = "Bot " + numbers;
        this.scores = 0;
        this.BUSY = false;
        deWay = new LinkedList<sPoint>();
    }

    public sPoint getCoords()
    {
        return this.coords;
    }

    public String getName()
    {
        return this.name;
    }

    public int getScores()
    {
        return this.scores;
    }

    public String getSearchAlg()
    {
        return "" + this.searchAlg;
    }

    private void goTo(sPoint p)
    {
        this.coords = p;
    }

    public void live()
    {
        if(!BUSY)
        {
            if(deWay.isEmpty())
            {
                ...
            }
            else
            {
                this.goTo(deWay.getFirst());
            }
        }
    }

    public void findDeWay(sPoint where)
    {
        ...
    }

    @Override
    public String toString()
    {
        return this.name + " uses " + searchAlg + ", his scores " + scores + " and its coordinates = " + coords;
    }
}