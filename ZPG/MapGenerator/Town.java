package ZPG.MapGenerator;

import ZPG.sMap.sPoint;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Bot;
import ZPG.GameLogic.Quests.*;
import ZPG.MapGenerator.WorldMap;

public class Town
{
    private static int numbers = 1;

    private String name;
    private sPoint coords;
    private int size; //Радиус города

    public Town(sPoint p, int r)
    {
        this.coords = p;
        this.size = r;
        this.name = "Town " + numbers;
        ++numbers;
    }

    public sPoint getCoords()
    {
        return this.coords;
    }

    public String getName()
    {
        return this.name;
    }

    public int getSize()
    {
        return this.size;
    }

    public IQuest getQuest(WorldMap map, Bot bot)
    {
        Random r = new Random();
        int what = r.nextInt(3);
        //int what = 2;
        IQuest res = null;
        switch(what)
        {
            case 0:
                res = new OneItemQuest(bot, map, this);
                break;
            case 1:
                res = new TownTravelQuest(bot, map, this);
                break;
            case 2:
                res = new TownDelivery(bot, map, this);
                break;
            case 3:
                res = null;
                break;
            case 4:
                res = null;
                break;
            default:
                System.out.println("Failed successfully in getQuest");
                break;
        }
        return res;
    }

    public void getQuestToAll(WorldMap map, List<Bot> bots)
    {
        IQuest res = new TownTravelQuest(bots.get(0), map, this);
        for(Bot bot : bots)
            bot.currentQuest = res;
    }

    @Override
    public String toString()
    {
        return this.name + " = {size is " + size + ", coordinates = " + this.coords + "}";
    }
}