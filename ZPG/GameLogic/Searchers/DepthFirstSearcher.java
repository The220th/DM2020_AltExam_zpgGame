package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.SetBit;

public class DepthFirstSearcher implements IDeWaySearcher
{
    WorldMap map;
    SetBit visited;
    Stack<sPoint> stack;
    HashMap<sPoint, sPoint> parent;

    public DepthFirstSearcher(WorldMap worldMap)
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
        stack = new Stack<sPoint>();
        visited = new SetBit(map.getMaxLineNum()+1);
        parent = new HashMap<sPoint, sPoint>();

        stack.push(start);
        visited.add(map.toLineNum(start));
        parent.put(start, null);

        ENDED = false;
        v = start;
        while(!stack.empty() && !ENDED)
        {
            v = stack.pop();
            buff = map.getListOfAdjacentVertices(v, true, visited);
            for(sPoint u : buff)
            {
                stack.push(u);
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
            return res;
        }
        else
            return null;
    }
}