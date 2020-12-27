package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class DiagonalSearcher implements IDeWaySearcher
{
    private WorldMap map;
	private Deque<sPoint> deque;
    private HashMap<sPoint, sPoint> parent;

    public DiagonalSearcher(WorldMap worldMap)
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
        sPoint buffPoint;
		boolean ENDED;
		boolean xAxis;
		List<sPoint> buff = new LinkedList<sPoint>();
		deque = new LinkedList<sPoint>();
        parent = new HashMap<sPoint, sPoint>();

        deque.addLast(start);
        parent.put(start, null);

		ENDED = false;
		xAxis = false;
		v = start;
		/*System.out.println("[Axis] I have to get to " + end + ". My position is " + start);
		long startTime = System.currentTimeMillis();*/
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
				buffPoint = null;
				if(Math.abs(u.getX()-end.getX()) == 0)
					xAxis = false;
				else if(Math.abs(u.getY()-end.getY()) == 0)
					xAxis = true;
				for(sPoint b : buff)
				{
					if(xAxis)
					{
						if(buffPoint == null || Math.abs(end.getX()-b.getX()) < Math.abs(end.getX()-buffPoint.getX()))
							buffPoint = b;
					}
					else
					{
						if(buffPoint == null || Math.abs(end.getY()-b.getY()) < Math.abs(end.getY()-buffPoint.getY()))
							buffPoint = b;
					}
				}
				parent.put(buffPoint, u);
				deque.addLast(buffPoint);
				xAxis = !xAxis;
			}
        }
		v = end;
		do
		{
			res.addFirst(v);
			v = parent.get(v);
		}while(!v.equals(start));

		/*long endTime = System.currentTimeMillis();
		System.out.println("[Axis] Moving out! It took " + (endTime-startTime) + " ms.");*/
		
		parent.clear();
		parent = null;
		deque = null;
		
		return res;
	}
	
	@Override
    public String toString()
    {
        return "Diagonal search algorithm";
    }
}