package ZPG.MapGenerator;

import java.lang.*;
import java.util.*;

import ZPG.sMap.sPoint;
import ZPG.MapGenerator.Town;

public class TownsGenerator
{
    private int townCenter = 102;
    private int townBlock = 101;
    private int roadBlock = 103;
    private int townWall = 104;

    private sPoint[] towns;
    private int[] towns_r;
    private int maxR = 7;
    private int minR = 1;
    private int minS = 50;
    private int maxS = 1024;
    private int roadR = 1;
    private int maxRoadsForOneTown;

    int[][] map;

    public TownsGenerator(int[][] Map, int numberTowns)
    {
        towns = new sPoint[numberTowns];
        map = Map;
        towns_r = new int[numberTowns];
        maxRoadsForOneTown = 4;
    }
    
    /**
     * Минимальное расстояние между городами, меньше нельзя
     * 
     * @param S - Минимальное расстояние между городами
     */
    public TownsGenerator minS(int S)
    {
        minS = S;
        if(minS < 1)
            throw new IllegalArgumentException();
        return this;
    }

    /**
     * Максимальное расстояние между городами, если больше которого, то дороги не будет
     * 
     * @param S - Максимальное расстояние между городами
     */
    public TownsGenerator maxS(int S)
    {
        maxS = S;
        if(maxS < 1)
            throw new IllegalArgumentException();
        return this;
    }

    /**
     * Минимальный радиус города
     * 
     * @param r - Минимальный радиус города
     * @return
     */
    public TownsGenerator minR(int r)
    {
        minR = r;
        if(minR < 1)
            throw new IllegalArgumentException();
        return this;
    }

    /**
     * Максимальный радиус города
     * 
     * @param r
     * @return
     */
    public TownsGenerator maxR(int r)
    {
        maxR = r;
        if(maxR < 1)
            throw new IllegalArgumentException();
        return this;
    }

    /**
     * Радиус дороги
     * 
     * @param r - Радиус дороги
     * @return
     */
    public TownsGenerator roadR(int r)
    {
        roadR = r;
        return this;
    }

    /**
     * Добавляет города и дороги на карту, тем самым изменяя её
     * 
     */
    public void addTowns()
    {
        Random r = new Random();
        if(maxR < minR || maxS < minS)
            throw new IllegalArgumentException();
        
        //Не забыть, что сначала "рисуются" дороги, потом город, потом центр
        genTownsCoordinates();
        
        boolean[][] adjacencyMatrix = calculateRoads();
        modifyRoads(adjacencyMatrix);

        for(int i = 0; i < towns.length-1; i++)
            for(int j = i+1; j < towns.length; j++)
                if(adjacencyMatrix[i][j] == true)
                    printLine(towns[i], towns[j]);
        
        for(int i = 0; i < towns.length; i++)
        {
            int buffR = r.nextInt(maxR - minR+1) + minR;
            setTown(towns[i], buffR);
            towns_r[i] = buffR;
        }
    }

    private void setBlock(sPoint p, int value)
    {
        if(p.getX() < 0 || p.getY() < 0 || p.getX() >= map.length || p.getY() >= map.length)
        {
            System.out.println("Attempt to set block out of the map");
            return;
        }
        map[p.getY()][p.getX()] = value;
    }

    private void placeRoad(sPoint p)
    {
        sPoint LU = p.subX(roadR).subY(roadR);
        for(int i = LU.getX(); i <= p.getX() + roadR; i++)
            for(int j = LU.getY(); j <= p.getY() + roadR; j++)
                setBlock(new sPoint(i, j), roadBlock);
    }

    private void setTown(sPoint where, int R)
    {
        int j;
        int buffR = R;
        for(j = 1; j < buffR; j++)
        {
            printCircleIn(where, j, townBlock);
        }
        printCircle(where, j, townWall);
        setBlock(where, townCenter);
    }

    private void genTownsCoordinates()
    {
        int i;
        for(i = 0; i < towns.length; i++)
            towns[i] = null;
        for(i = 0; i < towns.length; i++)
        {
            sPoint buffP;
            do
            {
                buffP = sPoint.rndPoint(0, map.length-1);
            }while(!checkLegalTownCoordinates(buffP));
            towns[i] = buffP;
            //System.out.println("towns[" + i + "] = " + towns[i]); //Debug
        }
    }

    private boolean checkLegalTownCoordinates(sPoint p)
    {
        if(p.getX() < maxR+1 || p.getY() < maxR+1 || p.getX() > map.length-maxR-2 || p.getY() > map.length-maxR-2)
            return false;
        for(int i = 0; i < towns.length; i++)
        {
            //if(towns[i] != null) System.out.println(towns[i].getX() + " " + towns[i].getY() + " vs " +  p.getX() + " " + p.getY()); //Debug
            if(towns[i] != null && ((int)(towns[i].getX() - p.getX()))*((int)(towns[i].getX() - p.getX())) + ((int)(towns[i].getY() - p.getY()))*((int)(towns[i].getY() - p.getY())) < (int)minS*(int)minS)
                return false;
        }
        return true;
    }

    private boolean[][] calculateRoads()
    {
        boolean[][] res = new boolean[towns.length][towns.length];
        //Заполнить false`ами
        for(int i = 0; i < res.length; i++)
            for(int j = 0; j < res[i].length; j++)
                res[i][j] = false;
        
        List<Integer> visited = new LinkedList<Integer>();
        sPoint mins;
        visited.add(Integer.valueOf(0));
        do
        {
            mins = primAlg(-1, 0, visited, res);
            res[mins.getX()][mins.getY()] = true;
            res[mins.getY()][mins.getX()] = true;
            visited.add(Integer.valueOf(mins.getY()));
            //System.out.println(visited); //Debug
        }while(!stopPrimAlg(visited));
        return res;
    }

    /**
     * 
     * @param where - .из какой точки (города) вызвана функция. -1 = из никакого (начало)
     * @param target - для какой точки (города) вызвана функия
     * @param visited - список "посещенных" точек (городов)
     * @return sPoint в данном слумае - это sPoint.x = первый город, а sPoint.y = второй город, которые нужно соединить
     */
    private sPoint primAlg(int where, int target, List<Integer> visited, boolean[][] adjacencyMatrix)
    {
        sPoint buff;
        sPoint res = new sPoint(target, findMinWay(target, visited));
        for(int i = 0; i < towns.length; i++)
            if(i != where && i != target && adjacencyMatrix[target][i] == true/* && visited.contains(Integer.valueOf(i))*/)
            {
                buff = primAlg(target, i, visited, adjacencyMatrix);
                if(countS(res.getX(), res.getY()) > countS(buff.getX(), buff.getY()))
                    res = buff;
            }
        return res;
    }

    private int findMinWay(int town, List<Integer> visited)
    {
        double min = Integer.MAX_VALUE; //=(
        int res = -1;
        for(int i = 0; i < towns.length; i++)
        {
            if(i != town && !visited.contains(Integer.valueOf(i)) && min > countS(town, i))
            {
                min = countS(town, i);
                res = i;
            }
        }
        //System.out.println("For towns[" + town + "] --- towns[" + res + "]"); // Debug
        return res;
    }
    private boolean stopPrimAlg(/*boolean[][] a*/List<Integer> visited)
    {
        if(visited.size() == towns.length)
            return true;
        else
            return false;
        /*
        int i, j;
        boolean f;
        for(i = 0; i < a.length; i++)
        {
            for(j = 0, f = false; j < a[i].length && !f; j++)
                if(i != j && a[i][j] == true)
                    f = true;
            if(f == false)
                return false;
        }
        return true;*/
    }

    private double countS(int town1, int town2)
    {
        sPoint p1 = towns[town1];
        sPoint p2 = towns[town2];
        return Math.sqrt( (p1.getX() - p2.getX())*(p1.getX() - p2.getX())+(p1.getY() - p2.getY())*(p1.getY() - p2.getY()) );
    }

    private void printCircle(sPoint center, int r, int block)
    {
        // r - радиус, X1, Y1 - координаты центра
        //System.out.println("Circle: " + center + ", r = " + r); // Debug
        int x = 0;
        int y = r;
        int X1 = center.getX();
        int Y1 = center.getY();
        int delta = 1 - 2 * r;
        int error = 0;
        while (y >= 0)
        {
            setBlock(new sPoint((X1 + x), (Y1 + y)), block);
            setBlock(new sPoint((X1 + x), (Y1 - y)), block);
            setBlock(new sPoint((X1 - x), (Y1 + y)), block);
            setBlock(new sPoint((X1 - x), (Y1 - y)), block);
            error = 2 * (delta + y) - 1;
            if ((delta < 0) && (error <= 0))
            {
                delta += 2 * ++x + 1;
                continue;
            }
            if ((delta > 0) && (error > 0))
            {
                delta -= 2 * --y + 1;
                continue;
            }
            delta += 2 * (++x - --y);
        }
    }

    private void printCircleIn(sPoint center, int r, int block)
    {
        // r - радиус, X1, Y1 - координаты центра
        //System.out.println("Circle: " + center + ", r = " + r); // Debug
        int x = 0;
        int y = r;
        int X1 = center.getX();
        int Y1 = center.getY();
        int delta = 1 - 2 * r;
        int error = 0;
        while (y >= 0)
        {
            setBlock(new sPoint((X1 + x), (Y1 + y)), block);
            setBlock(new sPoint((X1 + x), (Y1 + y)).addX(1), block);
            setBlock(new sPoint((X1 + x), (Y1 + y)).addY(1), block);
            setBlock(new sPoint((X1 + x), (Y1 + y)).subX(1), block);
            setBlock(new sPoint((X1 + x), (Y1 + y)).subY(1), block);

            setBlock(new sPoint((X1 + x), (Y1 - y)), block);
            setBlock(new sPoint((X1 + x), (Y1 - y)).addX(1), block);
            setBlock(new sPoint((X1 + x), (Y1 - y)).addY(1), block);
            setBlock(new sPoint((X1 + x), (Y1 - y)).subX(1), block);
            setBlock(new sPoint((X1 + x), (Y1 - y)).subY(1), block);

            setBlock(new sPoint((X1 - x), (Y1 + y)), block);
            setBlock(new sPoint((X1 - x), (Y1 + y)).addX(1), block);
            setBlock(new sPoint((X1 - x), (Y1 + y)).addY(1), block);
            setBlock(new sPoint((X1 - x), (Y1 + y)).subX(1), block);
            setBlock(new sPoint((X1 - x), (Y1 + y)).subY(1), block);

            setBlock(new sPoint((X1 - x), (Y1 - y)), block);
            setBlock(new sPoint((X1 - x), (Y1 - y)).addX(1), block);
            setBlock(new sPoint((X1 - x), (Y1 - y)).addY(1), block);
            setBlock(new sPoint((X1 - x), (Y1 - y)).subX(1), block);
            setBlock(new sPoint((X1 - x), (Y1 - y)).subY(1), block);
            error = 2 * (delta + y) - 1;
            if ((delta < 0) && (error <= 0))
            {
                delta += 2 * ++x + 1;
                continue;
            }
            if ((delta > 0) && (error > 0))
            {
                delta -= 2 * --y + 1;
                continue;
            }
            delta += 2 * (++x - --y);
        }
    }

    private void printLine(sPoint p0, sPoint p1)
    {
        int x1 = p0.getX();
        int x2 = p1.getX();
        int y1 = p0.getY();
        int y2 = p1.getY();

        int deltaX = Math.abs(x2 - x1);
        int deltaY = Math.abs(y2 - y1);
        int signX = x1 < x2 ? 1 : -1;
        int signY = y1 < y2 ? 1 : -1;
        //
        int error = deltaX - deltaY;
        //
        placeRoad(new sPoint(x2, y2));
        while(x1 != x2 || y1 != y2) 
        {
            placeRoad(new sPoint(x1, y1));
            int error2 = error * 2;
            //
            if(error2 > -deltaY) 
            {
                error -= deltaY;
                x1 += signX;
            }
            if(error2 < deltaX) 
            {
                error += deltaX;
                y1 += signY;
            }
        }
    }

    public List<Town> getTowns()
    {
        List<Town> res = new ArrayList<Town>();
        for(int i = 0; i < towns.length; ++i)
        {
            res.add(new Town(towns[i], towns_r[i]));
        }
        return res;
    }

    private void modifyRoads(boolean[][] adjacencyMatrix)
    {
        Random r = new Random();

        int roadsNum, buff;

        for(int i = 0; i < towns.length; ++i)
        {
            roadsNum = r.nextInt(maxRoadsForOneTown)+1;
            buff = countRoads(i, adjacencyMatrix);
            if(buff < roadsNum)
            {
                buff = roadsNum-buff;
                double min;
                int nmin;
                int j;
                for(int gi = 0; gi < buff; ++gi)
                {
                    nmin = -1;
                    min = Double.POSITIVE_INFINITY;
                    for(j = 0; j < towns.length; ++j)
                    {
                        if(i != j && adjacencyMatrix[i][j] == false && min > towns[i].getDistance(towns[j]))
                        {
                            min = towns[i].getDistance(towns[j]);
                            nmin = j;
                        }
                    }
                    adjacencyMatrix[i][nmin] = true;
                    adjacencyMatrix[nmin][i] = true;
                }

            }
        }
    }

    private int countRoads(int town, boolean[][] adjacencyMatrix)
    {
        int res = 0;

        for(int i = 0; i < towns.length; ++i)
            if(adjacencyMatrix[town][i] == true)
                ++res;
        return res;
    }
}