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
    private static final int limit = 4;

    private Town town;
    private Deque<QuestPoint> q;
    private LinkedList<Town> townsToVisit;

    public TownTravelQuest(Bot bot, WorldMap map, Town town)
    {
        if(!town.getCoords().equals(bot.getCoords()))
            throw new IllegalArgumentException("Boot coordinates != town coordinates (bot = " + bot.getCoords() + ", town = " + town.getCoords() + ")");
        this.town = town;
        totalReward = 0;
        q = new LinkedList<QuestPoint>();
        townsToVisit = new LinkedList<Town>();

        Random r = new Random();
        List<Town> towns = map.getTowns();
        for(int i = 0; i < towns.size(); ++i)
        {
            if(towns.get(i).getCoords().equals(town.getCoords()));
            {
                towns.remove(i);
                break;
            }
        }
        number = r.nextInt(towns.size()) % limit + 1;
        int rTown;
        
        do
        {
            rTown = r.nextInt(towns.size());
            calcQuest(towns.get(rTown));

            townsToVisit.addFirst(towns.get(rTown));
            towns.remove(rTown);
            --number;
        } while(number > 0);
        totalReward = (int)(totalReward/1.5 + 0.5);
        q.addFirst(new QuestPoint(IQuest.REWARD, Integer.valueOf((this.totalReward))));
        for(int i = 0; i < townsToVisit.size(); ++i)
            q.addFirst(new QuestPoint(IQuest.NEXT_PLACE_TO_VISIT, townsToVisit.get(i).getCoords()));
		
		
		townsToVisit = null;
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
        return "visiting " + (q.size()-1) + " towns. The next one is at " + q.getFirst().getValue() + ". Reward is " + totalReward;
    }
}