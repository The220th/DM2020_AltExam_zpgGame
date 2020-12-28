package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class BellmanFordSearcher implements IDeWaySearcher
{
	private WorldMap map;
	private SetBit visited;
    private Deque<sPoint> deque;
    private HashMap<sPoint, sPoint> parent;
	private HashMap<sPoint, Double> distance;

    public BellmanFordSearcher(WorldMap worldMap)
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

        sPoint v;
		sPoint u;
		Double dist;
        boolean ENDED;
		List<sPoint> buff = new LinkedList<sPoint>();
		visited = new SetBit(map.getMaxLineNum()+1);
        deque = new LinkedList<sPoint>();
        parent = new HashMap<sPoint, sPoint>();
        distance = new HashMap<sPoint, Double>();

        deque.addLast(start);
        parent.put(start, null);
		distance.put(start, 0.0);
		visited.add(map.toLineNum(start));

        ENDED = false;
        v = start;
        while(!ENDED)
        {
			while(!deque.isEmpty())
			{
				u = deque.pollFirst();
				visited.add(map.toLineNum(u));
				if(u.equals(end))
				{
					ENDED = true;
					v = u;
					break;
				}
				buff = map.getListOfAdjacentVertices(u, true, null);
				for(sPoint b : buff)
				{
					dist = distance.get(u) + map.getCost(u, b);
					if(distance.get(b) == null || distance.get(b) > dist)
					{
						parent.put(b, u);
						distance.put(b, dist);
						if(visited.contains(map.toLineNum(b)))
							deque.addFirst(b);
						else
							deque.addLast(b);
					}
				}
			}
        }

		v = end;
		do
		{
			res.addFirst(v);
			v = parent.get(v);
		}while(v != null && !v.equals(start));
		
		parent.clear();
		parent = null;
		distance.clear();
		distance = null;
		deque = null;
		visited.clear();
		visited = null;
		
		return res;
	}
	
	@Override
    public String toString()
    {
        return "Bellman-Ford search algorithm";
    }
}