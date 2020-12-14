package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Bot;
import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Quests.IQuest;
import ZPG.GameLogic.Quests.QuestPoint;
import ZPG.MapGenerator.Town;

public class OneItemQuest implements IQuest
{
    private int reward;
    private sPoint where;
    private int r;

    private Bot bot;
    private WorldMap map;
    private Town town;
    private Deque<QuestPoint> q;

    public OneItemQuest(Bot bot, WorldMap map, Town town)
    {
        if(!town.getCoords().equals(bot.getCoords()))
            throw new IllegalArgumentException("Boot coordinates != town coordinates (bot = " + bot.getCoords() + ", town = " + town.getCoords() + ")");
        this.town = town;
        this.map = map;
        this.bot = bot;
        q = new LinkedList<QuestPoint>();

        r = (new Random()).nextInt(150)+1;

        calcQuest();

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

    private void calcQuest()
    {
        Random r = new Random();
        int minX = town.getCoords().getX() - this.r;
        int maxX = town.getCoords().getX() + this.r;
        int minY = town.getCoords().getY() - this.r;
        int maxY = town.getCoords().getY() + this.r;
        int x, y;

        do
        {
            x = r.nextInt(maxX-minX) + minX;
            y = r.nextInt(maxY-minY) + minY;
        }while(!map.checkCoordsLegal(x, y));

        where = new sPoint(x, y);
        reward = (int)(where.getDistance(town.getCoords()) + 0.5);
    }

    @Override
    public String toString()
    {
        return "Need to get to " + where + ". Reward: " + reward;
    }
}