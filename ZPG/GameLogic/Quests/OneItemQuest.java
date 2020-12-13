package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Bot;
import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Quests.IQuest;
import ZPG.GameLogic.Quests.QuestPoint;
import ZPG.MapGenerator.Town;

public class OneItemQuest
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
        this.town = town;
        this.map = map;
        this.bot = bot;
        q = new LinkedList<QuestPoint>();

        r = (new Random()).nextInt(75);

        calcQuest();

        q.addFirst(new QuestPoint(IQuest.REWARD, Integer.valueOf(this.reward)));
        q.addFirst(new QuestPoint(IQuest.NEXT_PLACE_TO_VISIT, where));
    }

    public Map.Entry<Integer, Object> getNextQuestPoint()
    {
        return null;
    }

    public Map.Entry<Integer, Object> peekNextQuestPoint()
    {
        return null;
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
        reward = where.getDistance(town.getCoords());
    }
}