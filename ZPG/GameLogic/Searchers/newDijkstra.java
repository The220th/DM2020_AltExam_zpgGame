package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class newDijkstra implements IDeWaySearcher
{
    private WorldMap map;
    private SetBit visited;
    private SetBit verticesBit;
    private LinkedList<sPoint> vertices;
    private HashMap<sPoint, sPoint> parent;
	private HashMap<sPoint, Double> distance;

    public newDijkstra(WorldMap worldMap)
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
		boolean ENDED;
		Double buffDouble;
        List<sPoint> buff = new LinkedList<sPoint>();
        vertices = new LinkedList<sPoint>();
        visited = new SetBit(map.getMaxLineNum()+1);
        verticesBit = new SetBit(map.getMaxLineNum()+1);
        parent = new HashMap<sPoint, sPoint>();
        distance = new HashMap<sPoint, Double>();

		ListIterator<sPoint> it;

		vertices.addLast(start);
		verticesBit.add(map.toLineNum(start));
        parent.put(start, null);
		distance.put(start, 0.0);

        ENDED = false;
		v = start;
		/*System.out.println("[FastAF] I have to get to " + end + ". My position is " + start);
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
					if(distance.get(buffPoint) < buffDouble)
					{
						buffDouble = distance.get(buffPoint);
						u = buffPoint;
						toDel = bIndex;
					}
				}
				/*for(int buffIndex = 0; buffIndex < vectices.size(); ++buffIndex) 
				{
					buffPoint = vertices.get(buffIndex);
					if(distance.get(buffPoint) < buffDouble)
					{
						buffDouble = distance.get(buffPoint);
						u = buffPoint;
						toDel = buffIndex;
					}
				}*/

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
		}while(v != null && !v.equals(start));
		/*long endTime = System.currentTimeMillis();
		System.out.println("[FastAF] Moving out! It took " + (endTime-startTime) + " ms.");*/
		//Clean Day
		parent.clear();
		distance.clear();
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
        return "Dijkstra 2.0 Search algorithm";
    }
}