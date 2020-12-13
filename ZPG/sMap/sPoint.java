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

    public double getDistance(sPoint other)
    {
        return Math.sqrt( (this.x - other.x)*(this.x - other.x) + (this.y-other.y)*(this.y-other.y) );
    }

    @Override
    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(other == null)
            return false;
        if(this.getClass() != other.getClass())
            return false;
        sPoint otherPoint = (sPoint)other;
        return this.x == otherPoint.x && this.y == otherPoint.y;
    }

    @Override
    public int hashCode()
    {
        int ez = 32771;
        return this.x + this.y*ez;
    }
}