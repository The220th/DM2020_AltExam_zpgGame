package ZPG.GameLogic;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.GameHundler;
import ZPG.GameLogic.Searchers.*;
import ZPG.MapGenerator.Town;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Quests.*;

public class Bot
{
    private static int numbers = 1;

    private sPoint coords;
    private String name;
    private int scores;

    private Deque<sPoint> deWay;
    public IQuest currentQuest; //Вернуть private
    
    private boolean BUSY;
    private int ticksDelay;

    private IDeWaySearcher searchAlg;

    private static GameHundler GH = null;

    private Object Lock4Print;

    public Bot(sPoint coordinates, IDeWaySearcher searchAlgorithm)
    {
        if(GH == null)
            GH = GameHundler.getCurrentGameHundler();
        
        this.coords = coordinates;
        this.searchAlg = searchAlgorithm;
        this.name = "Bot " + numbers;
        this.scores = 0;
        this.BUSY = false;
        this.deWay = null;
        this.ticksDelay = 0;
        this.currentQuest = null;
        ++numbers;
        
        Lock4Print = new Object();
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

    public Deque<sPoint> getDeWay()
    {
        return this.deWay;
    }

    private void goTo(sPoint p)
    {
        ticksDelay += (int)(GH.getMap().getCost(this.coords, p) + 0.5);
        this.coords = p;
    }

    /*
        Если нет пути, то надо:
            Путь до ближайшего города, если нет квеста
            Иначе Взять следующую точку для квеста
    */
    public void live()
    {
        decrementTicksDelay();
        if(ticksDelay == 0)
        {
            if(!BUSY)
            {
                if(deWay == null || deWay.isEmpty())
                {
                    if(currentQuest != null)
                    {
                        QuestPoint buff = currentQuest.peekNextQuestPoint();
                        if(buff.getKey() == IQuest.QUEST_ENDED)
                            currentQuest = null;
                        else if(buff.getKey() == IQuest.REWARD)
                        {
                            this.scores += ((Integer)buff.getValue()).intValue();
                            currentQuest.getNextQuestPoint();
                            System.out.println(this);
                        }
                        else if(buff.getKey() == IQuest.NEXT_PLACE_TO_VISIT)
                        {
                            sPoint target = (sPoint)buff.getValue();
                            if(this.getCoords().equals(target))
                                currentQuest.getNextQuestPoint();
                            else
                                findDeWay(target);
                        }
                    }
                    else
                    {
                        Town town = GH.getNearestTown(this.coords);
                        if(this.coords.equals(town.getCoords()))
                        {
                            currentQuest = town.getQuest(GH.getMap(), this);
                        }
                        else
                        {
                            findDeWay(town.getCoords());
                        }
                    }
                }
                else
                {
                    synchronized(Lock4Print)
                    {
                        this.goTo(deWay.pollFirst());
                    }
                }
            }
        }
    }

    private void findDeWay(sPoint where)
    {
        BUSY = true;
        Runnable task = () ->
        {
            deWay = searchAlg.search(this.coords, where);
            BUSY = false;
        };
        new Thread(task).start();
    }

    private void decrementTicksDelay()
    {
        if(ticksDelay > 0)
            --ticksDelay;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public String getInfo()
    {
        return this.name + " uses " + searchAlg + ", his scores " + scores + " and its coordinates = " + coords;
    }

    public Object getLock4Print()
    {
        return Lock4Print;
    }
}