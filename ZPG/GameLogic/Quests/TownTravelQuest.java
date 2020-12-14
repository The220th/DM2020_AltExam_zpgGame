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

public class TownTravelQuest implements IQuest
{
    private int totalReward;
    private sPoint where;
    private int number;

    private Bot bot;
    private WorldMap map;
    private Town town;
    private Deque<QuestPoint> q;
    private LinkedList<Town> townsToVisit;

    public TownTravelQuest(Bot bot, WorldMap map, Town town)
    {
        if(!town.getCoords().equals(bot.getCoords()))
            throw new IllegalArgumentException("Boot coordinates != town coordinates (bot = " + bot.getCoords() + ", town = " + town.getCoords() + ")");
        this.town = town;
        this.map = map;
        this.bot = bot;
        totalReward = 0;
        q = new LinkedList<QuestPoint>();
        townsToVisit = new LinkedList<Town>();

        Random r = new Random();
        List<Town> towns = map.getTowns();
        number = r.nextInt(towns.size()) + 1;
        int rTown;

        number = 2;
        do
        {
            rTown = r.nextInt(towns.size());
            calcQuest(towns.get(rTown));

            townsToVisit.addFirst(towns.get(rTown));
            --number;
        } while(number != 0);
        totalReward = (int)(totalReward/1.5 + 0.5);
        q.addFirst(new QuestPoint(IQuest.REWARD, Integer.valueOf((this.totalReward))));
        for(int i = 0; i < townsToVisit.size(); ++i)
            q.addFirst(new QuestPoint(IQuest.NEXT_PLACE_TO_VISIT, townsToVisit.get(i).getCoords()));
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

    private void calcQuest(Town rTown)
    {
        int x,y;
        x = rTown.getCoords().getX();
        y = rTown.getCoords().getY();

        where = new sPoint(x, y);
        int reward;
        if(townsToVisit.size() != 0)
            reward = (int)(where.getDistance(townsToVisit.getFirst().getCoords()) + 0.5);
        else
            reward = (int)(where.getDistance(town.getCoords()) + 0.5);
        totalReward += reward;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        String str;
        for(Town town : townsToVisit)
            builder.insert(0, town.getName() + " " + town.getCoords() + ", ");
        str = builder.toString();
        str = str.substring(0, str.length()-2) + ". Reward is " + totalReward;
        return "Need to visit these towns: " + str;
    }
}