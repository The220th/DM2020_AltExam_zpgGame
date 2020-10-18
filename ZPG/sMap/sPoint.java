package ZPG.sMap;

import java.util.Random;

/**
 * Должно гарантироваться, что изменить точку нельзя
 */
public class sPoint
{
    private int x;
    private int y;
    public sPoint(int X, int Y)
    {
        this.x = X;
        this.y = Y;
    }

    public int getX()
    {
        return this.x;
    }
    public int getY()
    {
        return this.y;
    }

    public sPoint addX(int addedX)
    {
        return new sPoint(this.x + addedX, this.y);
    }

    public sPoint addY(int addedY)
    {
        return new sPoint(this.x, this.y + addedY);
    }

    public sPoint subX(int addedX)
    {
        return new sPoint(this.x - addedX, this.y);
    }

    public sPoint subY(int addedY)
    {
        return new sPoint(this.x, this.y - addedY);
    }

    public sPoint addXY(int addedX, int addedY)
    {
        return new sPoint(this.x + addedX, this.y + addedY);
    }

    static sPoint make(int X, int Y)
    {
        return new sPoint(X, Y);
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     * Включительно всё
     */
    public static sPoint rndPoint(int min, int max)
    {
        Random r = new Random();
        int x = r.nextInt(max-min+1) + min;
        int y = r.nextInt(max-min+1) + min;
        sPoint res = new sPoint(x, y);
        return res;
    }
}