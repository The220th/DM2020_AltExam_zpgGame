package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class AStarSearcher implements IDeWaySearcher
{
    private WorldMap map;
	private SetBit visited;
	private SetBit verticesBit;
	private LinkedList<sPoint> vertices;
	private HashMap<sPoint, sPoint> parent;
	private HashMap<sPoint, Double> f_func;
	private HashMap<sPoint, Double> distance;
	/*
	h(v) = getDistance(v, end);
	g(v) = parent's distance + distance to v
	f(v) = g(v) + h(v)
	*/
    public AStarSearcher(WorldMap worldMap)
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

		//end = new sPoint(250,250);

		sPoint v, u, buffPoint;
		int toDel, bIndex;
		Double buffDouble;
		boolean ENDED;
		List<sPoint> buff = new LinkedList<sPoint>();
        vertices = new LinkedList<sPoint>();
		visited = new SetBit(map.getMaxLineNum()+1);
		verticesBit = new SetBit(map.getMaxLineNum()+1);
        parent = new HashMap<sPoint, sPoint>();
		f_func = new HashMap<sPoint, Double>();
		distance = new HashMap<sPoint, Double>();
		
		ListIterator<sPoint> it;

		vertices.addLast(start);
		verticesBit.add(map.toLineNum(start));
        parent.put(start, null);
		f_func.put(start, 0.0);
		distance.put(start, 0.0);

		ENDED = false;
		v = start;
		/*System.out.println("[A*] I have to get to " + end + ". My position is " + start);
		long startTime = System.currentTimeMillis();*/
		while(!ENDED)
		{
			while(!vertices.isEmpty())
			{
				u = null;
				buffDouble = Double.MAX_VALUE;
				it = vertices.listIterator();
				bIndex = -1;
				toDel = -1;
				while(it.hasNext())
				{
					++bIndex;
					buffPoint = it.next();
					if(f_func.get(buffPoint) < buffDouble)
					{
						buffDouble = f_func.get(buffPoint);
						u = buffPoint;
						toDel = bIndex;
					}
				}
				vertices.remove(toDel);
				verticesBit.remove(map.toLineNum(u));
				visited.add(map.toLineNum(u));
				if(u.equals(end))
				{
					ENDED = true;
					v = u;
					break;
				}
				buff = map.getListOfAdjacentVertices(u, true, visited);
				for(sPoint b : buff)
				{
					if(!verticesBit.contains(map.toLineNum(b)))
					{
						vertices.addLast(b);
						verticesBit.add(map.toLineNum(b));
					}
					buffDouble = f_func.get(u) + map.getCost(b, u) + b.getDistance(end);
					if(f_func.get(b) == null || f_func.get(b) > buffDouble)
					{
						parent.put(b, u);
						distance.put(b, distance.get(u) + map.getCost(b, u));
						f_func.put(b, buffDouble);
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
		/*long endTime = System.currentTimeMillis();
		System.out.println("[A*] Moving out! It took " + (endTime-startTime) + " ms.");*/
		//Clean Day
		parent.clear();
		distance.clear();
		vertices.clear();
		f_func.clear();
		distance.clear();
		parent = null;
		distance = null;
		vertices = null;
        visited = null;
		verticesBit = null;
		f_func = null;
		return res;
	}
	
	@Override
    public String toString()
    {
        return "A* Search algorithm";
    }
}