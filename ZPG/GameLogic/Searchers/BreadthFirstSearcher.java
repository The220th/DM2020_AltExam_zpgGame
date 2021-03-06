package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class BreadthFirstSearcher implements IDeWaySearcher
{
    private WorldMap map;
    private SetBit visited;
    private Deque<sPoint> q;
    private HashMap<sPoint, sPoint> parent;

    public BreadthFirstSearcher(WorldMap worldMap)
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
        boolean ENDED;
        List<sPoint> buff;
        q = new LinkedList<sPoint>();
        visited = new SetBit(map.getMaxLineNum()+1);
        parent = new HashMap<sPoint, sPoint>();

        q.addLast(start);
        visited.add(map.toLineNum(start));
        parent.put(start, null);

        ENDED = false;
        v = start;
        while(q.peekFirst() != null && !ENDED)
        {
            v = q.pollFirst();
            buff = map.getListOfAdjacentVertices(v, true, visited);
            for(sPoint u : buff)
            {
                q.addLast(u);
                visited.add(map.toLineNum(u));
                parent.put(u, v);
                if(u.equals(end))
                {
                    ENDED = true;
					v = u;
                    break;
                }
            }
        }
        if(v.equals(end))
        {
            v = end;
            do
            {
                res.addFirst(v);
                v = parent.get(v);
            }while(!v.equals(start));
			parent.clear();
			parent = null;
			visited = null;
			q = null;
            return res;
        }
        else
		{
			parent.clear();
			parent = null;
			visited = null;
			q = null;
            return null;
		}
    }
    
	
	@Override
    public String toString()
    {
        return "Breadth-first search algorithm (BFS)";
    }
}