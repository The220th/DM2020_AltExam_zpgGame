package ZPG.GameLogic.Searchers;

import java.util.Deque;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;

public interface IDeWaySearcher
{
    /**
     * Вернёт очередь вершин для посещения от start до end, включая end и исключая start
     * 
     * @return вначале в очереди - следующая точка для посещения, в конце будет точка end
     */
    public abstract Deque<sPoint> search(sPoint start, sPoint end);
}