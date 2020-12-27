package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class LeeSearcher implements IDeWaySearcher
{
    private WorldMap map;
    private Deque<sPoint> deque;
    private HashMap<sPoint, sPoint> parent;
	private HashMap<sPoint, Double> distance;

    public LeeSearcher(WorldMap worldMap)
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
        deque = new LinkedList<sPoint>();
        Deque<sPoint> buffDeque = new LinkedList<sPoint>();
        parent = new HashMap<sPoint, sPoint>();
        distance = new HashMap<sPoint, Double>();

        deque.addLast(start);
        parent.put(start, null);
		distance.put(start, 0.0);

        ENDED = false;
        v = start;
        while(!ENDED)
        {
			while(!deque.isEmpty())
			{
				u = deque.pollFirst();
				
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
						buffDeque.addLast(b);
					}
				}
			}
			while(!ENDED && !buffDeque.isEmpty())
			{
				u = buffDeque.pollFirst();
				if(!deque.contains(u)) 
					deque.addLast(u);
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
		
		return res;
	}
	
	@Override
    public String toString()
    {
        return "Wave search algorithm (Lee's algorithm)";
    }
}