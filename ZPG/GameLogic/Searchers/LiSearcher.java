package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class LiSearcher implements IDeWaySearcher
{
    WorldMap map;
    SetBit visited;
    Deque<sPoint> deque;
    HashMap<sPoint, sPoint> parent;
	HashMap<sPoint, Integer> distance;
	Integer time;

    public LiSearcher(WorldMap worldMap)
    {
        this.map = worldMap;
    }

    public Deque<sPoint> search(sPoint start, sPoint end) throws IllegalArgumentException
    {
        if(map.checkCoordsLegal(start) == false || map.checkCoordsLegal(end) == false)
            throw new IllegalArgumentException("start = " + start + ", end = " + end);
        
        Deque<sPoint> res = new LinkedList<sPoint>();
        if(start.equals(end))
            return res;

		time = 0;
        sPoint v;
        sPoint u;
        boolean ENDED;
        List<sPoint> buff = new LinkedList<sPoint>();
        deque = new LinkedList<sPoint>();
        Deque<sPoint> buffDeque = new LinkedList<sPoint>();
        visited = new SetBit(map.getMaxLineNum()+1);
        parent = new HashMap<sPoint, sPoint>();
        distance = new HashMap<sPoint, Integer>();

        deque.addLast(start);
        parent.put(start, null);
		distance.put(start, 0);

        ENDED = false;
        v = start;
		visited.add(map.toLineNum(v));
        while(!ENDED)
        {
			time++;
			while(!deque.isEmpty())
			{
				u = deque.pollFirst();
				
				if(u.equals(end))
				{
					ENDED = true;
					v = u;
					break;
				}
				buff = map.getListOfAdjacentVertices(u, true, visited);
				for(sPoint b : buff)
				{
					visited.add(map.toLineNum(b));
					parent.put(b, u);
					distance.put(b, time);
					buffDeque.addLast(b);
				}
			}
			while(!ENDED && !buffDeque.isEmpty())
			{
				u = buffDeque.pollFirst();
				if(!deque.contains(u)) 
					deque.addLast(u);
			}
			
        }
		System.out.println(distance.get(end));	//time финиша
        if(v.equals(end))
        {
            v = end;
            do
            {
                res.addFirst(v);
                v = parent.get(v);
            }while(!v.equals(start));
            return res;
        }
        else
            return null;
    }
}