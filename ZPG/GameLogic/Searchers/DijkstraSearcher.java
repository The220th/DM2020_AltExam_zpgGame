package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.Bot;
import ZPG.GameLogic.SetBit;

public class DijkstraSearcher implements IDeWaySearcher
{
	private WorldMap map;
	private SetBit inGraph;
	private LinkedList<sPoint> toSee;
	private HashMap<sPoint, sPoint> parent;
	private HashMap<sPoint, Double> dist;
	
	private double min;
	private sPoint nmin;

    public DijkstraSearcher(WorldMap worldMap)
    {
        this.map = worldMap;
    }

    public Deque<sPoint> search(sPoint start, sPoint end) throws IllegalArgumentException
    {
		//System.out.println(start + " -> " + end);


        if(map.checkCoordsLegal(start) == false || map.checkCoordsLegal(end) == false)
            throw new IllegalArgumentException("start = " + start + ", end = " + end);
        
        Deque<sPoint> res = new LinkedList<sPoint>();
        if(start.equals(end))
            return res;

		List<sPoint> buff = null;
		ListIterator<sPoint> iter = null;
		sPoint v;
		
		inGraph = new SetBit(map.getMaxLineNum()+1);
		toSee = new LinkedList<sPoint>();
		parent = new HashMap<sPoint, sPoint>(map.getMaxLineNum()/2);
		dist = new HashMap<sPoint, Double>(map.getMaxLineNum()/2);
		
		inGraph.add(map.toLineNum(start));
		toSee.add(start);
		dist.put(start, Double.valueOf(0.0));
		while(true)
		{
			synchronized(this)
			{
			iter = toSee.listIterator();
			min = Double.POSITIVE_INFINITY;
			nmin = null;
			while(iter.hasNext())
			{
				v = iter.next();
				buff = map.getListOfAdjacentVertices(v, true, inGraph);
				if(!buff.isEmpty())
				{
					for(sPoint u : buff)
					{
						double buffDist1 = dist.getOrDefault(u, Double.valueOf(Double.POSITIVE_INFINITY)).doubleValue();
						double buffDist2 = dist.get(v) + map.getCost(v, u);
						if(buffDist1 > buffDist2)
						{
							dist.put(u, Double.valueOf(buffDist2));
							parent.put(u, v);
						}
						if(min > dist.get(u).doubleValue())
						{
							min = dist.get(u).doubleValue();
							nmin = u;
						}
					}
				}
				else
					iter.remove();
			}

			toSee.add(nmin);
			inGraph.add(map.toLineNum(nmin));
			if(nmin.equals(end))
				break;
			}
		}
		
		v = end;
		do
		{
			res.addFirst(v);
			v = parent.get(v);
		}while(!v.equals(start));
		
		inGraph = null;
		toSee = null;
		parent.clear();
		parent = null;
		dist.clear();
		dist = null;

		//System.out.println("Ended: \n" + res);
		return res;
	}

	public LinkedList<sPoint> get2See()
	{
		LinkedList<sPoint> res;
		synchronized(this)
		{
			if(toSee == null)
				return null;
			res = new LinkedList<sPoint>(toSee);
		}
		return res;
	}
	
	@Override
    public String toString()
    {
        return "Dijkstra Search algorithm";
    }
}