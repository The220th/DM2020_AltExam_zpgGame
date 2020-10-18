package ZPG.MapGenerator;

import java.lang.*;
import java.util.*;

import ZPG.sMap.sPoint;
import ZPG.sMap.sSquare;
import ZPG.sMap.sDiamond;

public class LandscapeGenerator
{
    private int[][] worldMap;
    private int maxH;
    private int outMap;
    private float defect;
    public static void main(String[] args)
    {
        LandscapeGenerator mg = new LandscapeGenerator(8, 10, 0, 0.333333333f);
        int[][] a = mg.genMap();
        PrintMap(a);
    }

    /**
     * Конструрирует объект
     * 
     * @param size2Pow - размер карты будет 2^(size2Pow) + 1
     * @param MaxH - максимальная высота на карте
     * @param OutMap - высота за картой. Например, если нужно побольше воды, то OutMap = 0
     * @param Defect - Дописать!!!!!!!!!!!!
     */
    public LandscapeGenerator(int size2Pow, int MaxH, int OutMap, float Defect)
    {
        if(size2Pow < 2 || MaxH < 0 ||  OutMap < 0 || OutMap > MaxH /*|| Float.compare(Defect, 1) > 0 || Float.compare(Defect, 0) < 0*/)
            throw new IllegalArgumentException();
        
        int size = 1;
        for(int i = 0; i < size2Pow; i++)
            size <<= 1;
        size++; // size = 2^(size2Pow)+1

        worldMap = new int[size][size];
        for(int[] item : worldMap) // Заполнить всё -1
            for(int li = 0; li < item.length; li++)
                item[li] = -1;

        this.maxH = MaxH;
        this.outMap = OutMap;
        this.defect = Defect;
    }
    
    /**
     * x
     * |
     * |
     * v  y---->
     *   0 1 2 3 4 5 ...
     * 0 * * * * * * ...
     * 1 * * * * * * ...
     * 2 * * * * * * ...
     * 3 * * * * * * ...
     * 4 * * * * * * ...
     * 5 * * * * * * ...
     *  ... ... ...  ...
     * genMap[y][x]
     * @return карта
     */
    public int[][] genMap()
    {
        int l = worldMap.length - 1;
        sSquare s;
        sDiamond d;

        Random r = new Random();
        set(new sPoint(0, 0), r.nextInt(maxH + 1));
        set(new sPoint(l, 0), r.nextInt(maxH + 1));
        set(new sPoint(0, l), r.nextInt(maxH + 1));
        set(new sPoint(l, l), r.nextInt(maxH + 1));

        do
        {
            s = new sSquare(new sPoint(0, 0), l);
            d = new sDiamond(new sPoint(l/2, 0), l/2);
            //PrintMap(worldMap); //Debug
            do
            {
                //System.out.println(s); //Debug
                squarePhase(s, l);
                s = nextSquare(s, l);
                //PrintMap(worldMap); //Debug
            }while(s != null);

            do
            {
                //System.out.println(d); //Debug
                diamondPhase(d, l);
                d = nextDiamond(d, l/2);
                //PrintMap(worldMap); //Debug
            }while(d != null);
            l = l/2;
        }while(l > 1);
        return worldMap;
    }

    private void squarePhase(sSquare s, int l)
    {
        set(s.getC(), genCenter( get(s.getLU()), get(s.getLD()), get(s.getRU()), get(s.getRD()), l) );
    }

    private void diamondPhase(sDiamond d, int l)
    {
        set(d.getC(), genCenter( get(d.getU()), get(d.getD()), get(d.getL()), get(d.getR()), l) );
    }

    private int get(sPoint p)
    {
        if(checkOutSide(p))
            return outMap;
        return worldMap[p.getY()][p.getX()];
    }

    private void set(sPoint p, int value)
    {
        if(value > maxH) value = maxH;
        if(value < 0) value = 0;
        worldMap[p.getY()][p.getX()] = value;
    }

    private int getSize()
    {
        return worldMap.length;
    }

    private sSquare nextSquare(sSquare current, int l)
    {
        sSquare res;
        int l1 = l+1;
        sPoint buff = current.getLU();
        if(checkOutSide(buff.addX(l1)))
            if(checkOutSide(buff.addY(l1)))
                res = null;
            else
                res = new sSquare(new sPoint(0, buff.getY() + l), l);
        else
            res = new sSquare(buff.addX(l), l);
        return res;
    }

    private sDiamond nextDiamond(sDiamond current, int l)
    {   //IwantToGoNah$i
        sDiamond res;
        int l2 = l*2;
        sPoint buff = current.getC();
        if(checkOutSide(buff.addX(l2))) // Вылетело за карту направо
            if( !checkOutSide( buff.addX(l2).subX(l) ) ) // Это была верхняя или нижняя сторона
                if(!checkOutSide(buff.addY(1))) //Ещё есть место
                    res = new sDiamond(new sPoint(0, buff.getY() + l), l);
                else
                    res = null;
            else // Это был ромб на второнах слева или справа
                res = new sDiamond(new sPoint(l, buff.getY() + l), l);
        else // Ещё есть место
            res = new sDiamond(buff.addX(l2), l);
        return res;
    }

    /**
     * Проверка выхода за карту
     * @param p
     * @return вернёт true, если выйдет за карту, false - если внутри карты
     */
    private boolean checkOutSide(sPoint p)
    {
        boolean res = false;
        if(p.getX() < 0 || p.getX() >= worldMap.length || p.getY() < 0 || p.getY() >= worldMap.length)
            res = true;
        return res;
    }

    private int genCenter(int x1, int x2, int x3, int x4, int l) throws IllegalArgumentException
    {
        if(x1 < 0 || x2 < 0 || x3 < 0 || x4 < 0)
            throw new IllegalArgumentException("x1 = " + x1 + ", x2 = " + x2 + ", x3 = " + x3 + ", x4 = " + x4 + " must be more than zero or equals");
        int res, buff;
        Random r = new Random();
        res = (x1+x2+x3+x4) / 4;
        buff = (int)((r.nextInt() % l)*defect * (r.nextBoolean()?1:-1));
        res += buff;
        return res;
    }

    public static void PrintMap(int[][] a)
    {
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j < a[i].length; j++)
            if(a[i][j] != -1)
                System.out.printf("%2d ", a[i][j]);
            else
                System.out.printf("%2c ", 'o');
            System.out.println();
        }
        System.out.println();
    }
}