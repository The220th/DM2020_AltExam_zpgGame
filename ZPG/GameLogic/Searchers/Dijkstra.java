package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class Dijkstra implements IDeWaySearcher
{
    private WorldMap map;
    private SetBit visited;
    private SetBit verticesBit;
    private LinkedList<sPoint> vertices;
    private HashMap<sPoint, sPoint> parent;
	private HashMap<sPoint, Double> distance;

    public Dijkstra(WorldMap worldMap)
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
		boolean ENDED;
		Double buffDouble;
        List<sPoint> buff = new LinkedList<sPoint>();
        vertices = new LinkedList<sPoint>();
        visited = new SetBit(map.getMaxLineNum()+1);
        verticesBit = new SetBit(map.getMaxLineNum()+1);
        parent = new HashMap<sPoint, sPoint>();
        distance = new HashMap<sPoint, Double>();

		vertices.addLast(start);
		verticesBit.add(map.toLineNum(start));
        parent.put(start, null);
		distance.put(start, 0.0);

        ENDED = false;
		v = start;
        while(!ENDED)
        {
			while(!vertices.isEmpty())
			{
				u = vertices.get(0);
				buffDouble = Double.MAX_VALUE;
				for (sPoint point : vertices) 
				{
					if(distance.get(point) < buffDouble)
					{
						buffDouble = distance.get(point);
						u = point;
					}
				}
				vertices.remove(vertices.indexOf(u));
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
					buffDouble = distance.get(u) + map.getCost(b, u);
					if(distance.get(b) == null)
					{
						parent.put(b, u);
						distance.put(b, buffDouble);
					}
					else if(distance.get(b) > buffDouble)
					{
						parent.put(b, u);
						distance.put(b, buffDouble);
					}
				}
			}
			
        }

		v = end;
		do
		{
			res.addFirst(v);
			v = parent.get(v);
		}while(!v.equals(start));

		//Clean Day
		parent = null;
		distance = null;
		vertices = null;
        visited = null;
        verticesBit = null;
		return res;
	}
	
	@Override
    public String toString()
    {
        return "Dijkstra Search algorithm";
    }
}