//By programmer The Great (false)

package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Bot;
import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Quests.IQuest;
import ZPG.GameLogic.Quests.QuestPoint;
import ZPG.MapGenerator.Town;

public class TownDelivery implements IQuest
{
    private int reward;
    private Deque<QuestPoint> q;
    private Town town;

    public TownDelivery(Bot bot, WorldMap map, Town town)
    {
        if(!town.getCoords().equals(bot.getCoords()))
            throw new IllegalArgumentException("Boot coordinates != town coordinates (bot = " + bot.getCoords() + ", town = " + town.getCoords() + ")");
        reward = 0;
        this.town = town;
        q = new LinkedList<QuestPoint>();

        Random r = new Random();
        List<Town> towns = map.getTowns();
        int rTown;
        Town buffTown = null;
        double radius = getDistanceToNearestTown(towns);
        do
        {
            rTown = r.nextInt(towns.size());
            buffTown = towns.get(rTown);
        } while(buffTown.getCoords().equals(town.getCoords()) || buffTown.getCoords().getDistance(town.getCoords()) > radius*2.5);
        this.town = buffTown;
        reward = (int)(buffTown.getCoords().getDistance(town.getCoords())*0.7 + 0.5);
        q.addFirst(new QuestPoint(IQuest.REWARD, Integer.valueOf((this.reward))));
        q.addFirst(new QuestPoint(IQuest.NEXT_PLACE_TO_VISIT, buffTown.getCoords()));
    }

    public QuestPoint getNextQuestPoint()
    {
        if(q.isEmpty())
            return new QuestPoint(IQuest.QUEST_ENDED, null);
        else
            return q.pollFirst();
    }

    public QuestPoint peekNextQuestPoint()
    {
        if(q.isEmpty())
            return new QuestPoint(IQuest.QUEST_ENDED, null);
        else
            return q.peekFirst();
    }

    private double getDistanceToNearestTown(List<Town> towns)
    {
        double minR = Double.POSITIVE_INFINITY;
        double buff;
        for(Town t : towns)
        {
            if(!t.equals(town))
            {
                buff = t.getCoords().getDistance(town.getCoords());
                if(buff < minR)
                    minR = buff;
            }
        }
        return minR;
    }

    @Override
    public String toString()
    {
        return "delivering something to town at " + town.getCoords() + ". Reward is " + reward;
    }
}