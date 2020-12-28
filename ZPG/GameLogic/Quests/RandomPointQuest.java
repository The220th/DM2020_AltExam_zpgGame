package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Bot;
import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Quests.IQuest;
import ZPG.GameLogic.Quests.QuestPoint;
import ZPG.MapGenerator.Town;

public class RandomPointQuest implements IQuest
{
    private int reward;
    private sPoint where;

    private Bot bot;
    private WorldMap map;
    private Deque<QuestPoint> q;

    public RandomPointQuest(Bot bot, WorldMap map, Town town)
    {
        if(!town.getCoords().equals(bot.getCoords()))
            throw new IllegalArgumentException("Boot coordinates != town coordinates (bot = " + bot.getCoords() + ", town = " + town.getCoords() + ")");
        this.map = map;
        this.bot = bot;
        q = new LinkedList<QuestPoint>();

        where = sPoint.rndPoint(0, map.getMaxSize()-1);
        reward = (int)(where.getDistance(town.getCoords()) + 0.5);

        q.addFirst(new QuestPoint(IQuest.REWARD, Integer.valueOf(this.reward)));
        q.addFirst(new QuestPoint(IQuest.NEXT_PLACE_TO_VISIT, where));
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

    @Override
    public String toString()
    {
        return "getting to " + where + " to set beacon. Reward: " + reward;
    }
}