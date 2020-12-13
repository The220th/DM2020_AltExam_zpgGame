package ZPG.GameLogic;

import java.util.*;
import java.lang.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.MapGenerator.Town;
import ZPG.GameLogic.Searchers.*;

public class GameHundler
{
    private static GameHundler GL;

    private WorldMap map;
    private List<Town> towns;

    public GameHundler()
    {
        map = new WorldMap(10);
        map.genWorld();
        towns = map.getTowns();
        GL = this;
    }

    public static GameHundler getCurrentGameHundler()
    {
        return GL;
    }

    public Town getNearestTown(sPoint from)
    {
        Town res = null;
        double minR = Double.POSITIVE_INFINITY;
        double buff;

        for(Town t : towns)
        {
            if(from.equals(t.getCoords()))
                return t;
            
            buff = t.getCoords().getDistance(from);
            if(minR > buff)
            {
                minR = buff;
                res = t;
            }
        }
        return res;
    }
}