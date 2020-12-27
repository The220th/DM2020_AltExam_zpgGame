//Дейкстра, но рассматривает точки до maxR

package ZPG.GameLogic.Searchers;

import java.util.*;

import ZPG.MapGenerator.WorldMap;
import ZPG.sMap.sPoint;
import ZPG.GameLogic.Searchers.IDeWaySearcher;
import ZPG.GameLogic.Searchers.newDijkstra;
import ZPG.GameLogic.SetBit;


public class LikstraSearcher implements IDeWaySearcher
{
    private WorldMap map;
    private int maxR;
    private IDeWaySearcher searcher;

    public LikstraSearcher(WorldMap worldMap)
    {
        this.map = worldMap;
        maxR = 250;
        searcher = new newDijkstra(this.map); 
    }

    public Deque<sPoint> search(sPoint start, sPoint end) throws IllegalArgumentException
    {
        //System.out.println(start + " -> " + end);
        if(map.checkCoordsLegal(start) == false || map.checkCoordsLegal(end) == false)
            throw new IllegalArgumentException("start = " + start + ", end = " + end);
        
        Deque<sPoint> res = new LinkedList<sPoint>();
        if(start.equals(end))
            return res;
        
        sPoint buff;
        double end_start = end.getDistance(start);
        int R = maxR;
        sPoint cur = start;
        while(end_start > R)
        {
            buff = getRightPoint_Crossing_CircleAndLine(start, end, R);
            //System.out.println(buff);
            res.addAll( searcher.search(cur, buff) );
            cur = buff;
            R += maxR;
        }
        res.addAll( searcher.search(cur, end) );
        return res;
    }

    private sPoint getRightPoint_Crossing_CircleAndLine(sPoint start, sPoint end, int R)
    {
        //https://imgur.com/BkFuPih - Line
        //https://imgur.com/gwBwQjo - Circle
        //https://imgur.com/EYDTDn9 - Crossing Line and Circle

        int x0 = start.getX();
        int x1 = end.getX();
        int y0 = start.getY();
        int y1 = end.getY();

        //https://imgur.com/BkFuPih - Line
        double k = ((double)(y1 - y0))/(x1 - x0);
        double b = y0 - (((double)(y1 - y0))/(x1 - x0))*x0;

        //https://imgur.com/gwBwQjo - Circle
        double A = -2 * x0;
        double B = -2 * y0;
        double C = x0*x0 + y0*y0 - R*R;

        //https://imgur.com/EYDTDn9 - Crossing Line and Circle
        double Da = 1 + k*k;
        double Db = 2*k*b + A + B*k;
        double Dc = b*b+B*b+C;
        double D = Db*Db - 4 * Da * Dc;
        double _x1 = (-Db - Math.sqrt(D)) / (2*Da);
        double _y1 = k*_x1 + b;
        double _x2 = (-Db + Math.sqrt(D)) / (2*Da);
        double _y2 = k*_x2 + b;

        sPoint dot_a = new sPoint((int)(_x1+0.5), (int)(_y1+0.5));
        sPoint dot_b = new sPoint((int)(_x2+0.5), (int)(_y2+0.5));
        if(dot_a.getDistance(end) < dot_b.getDistance(end))
            return dot_a;
        else
            return dot_b;
    }
	
	@Override
    public String toString()
    {
        return "Likstra";
    }
}