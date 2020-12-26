package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Bot;
import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Quests.IQuest;
import ZPG.GameLogic.Quests.QuestPoint;
import ZPG.MapGenerator.Town;
import ZPG.GameLogic.SetBit;

public class ResourceGettingQuest implements IQuest
{
    private int reward;
    private Deque<QuestPoint> q;
    private int target;    //40 (сухая гора) - руда, 12 (лес) - дерево
    private sPoint targetPlacement;

    public ResourceGettingQuest(Bot bot, WorldMap map, Town town)
    {
        if(!town.getCoords().equals(bot.getCoords()))
            throw new IllegalArgumentException("Boot coordinates != town coordinates (bot = " + bot.getCoords() + ", town = " + town.getCoords() + ")");

        if(new Random().nextInt(2) > 0)
            target = 40;
        else
            target = 12;
        reward = 0;
        q = new LinkedList<QuestPoint>();
        targetPlacement = getTreePlacement(town, map);
        reward = (int)(targetPlacement.getDistance(town.getCoords()) + 0.5);
        q.addFirst(new QuestPoint(IQuest.REWARD, Integer.valueOf((this.reward))));
        q.addFirst(new QuestPoint(IQuest.NEXT_PLACE_TO_VISIT, targetPlacement));
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

    private sPoint getTreePlacement(Town town, WorldMap map)
    {
        Deque<sPoint> buffDeque;
        List<sPoint> buff;
        sPoint v;
        boolean ENDED = false;
        SetBit visited;

        visited = new SetBit(map.getMaxLineNum()+1);
        buffDeque = new LinkedList<sPoint>();

        v = town.getCoords();
        visited.add(map.toLineNum(v));
        buffDeque.addFirst(v);
        while(buffDeque.peekFirst() != null && !ENDED)
        {
            v = buffDeque.pollFirst();
            buff = map.getListOfAdjacentVertices(v, true, visited);
            for(sPoint u : buff)
            {
                buffDeque.addLast(u);
                visited.add(map.toLineNum(u));
                if(map.getBlock(u).getCost() == target)
                {
                    ENDED = true;
					v = u;
                    break;
                }
            }
        }
        visited.clear();
        visited = null;
        buffDeque.clear();
        buffDeque = null;
        buff = null;
        return v;
    }

    @Override
    public String toString()
    {
        return "Need to get some resources at " + targetPlacement + ". Reward is" + reward;
    }
}