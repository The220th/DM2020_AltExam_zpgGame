package ZPG.sMap;

import ZPG.sMap.sPoint;
/**
 * Должно гарантироваться, что изменить ромб нельзя
 */
public class sDiamond
{
    private sPoint center;
    private int halfDiagonal;

    /**
     * Конструирует ромб
     * 
     * @param center - центр ромба
     * @param l - полудиагональ - 1
     */
    public sDiamond(sPoint Center, int l)
    {
        this.center = Center;
        halfDiagonal = l;
    }

    public sPoint getC()
    {
        return center;
    }

    public sPoint getU()
    {
        return this.center.subY(halfDiagonal);
    }

    public sPoint getD()
    {
        return this.center.addY(halfDiagonal);
    }

    public sPoint getL()
    {
        return this.center.subX(halfDiagonal);
    }

    public sPoint getR()
    {
        return this.center.addX(halfDiagonal);
    }

    public String toString()
    {
        return "Diamond: c = " + this.center.toString() + ", l = " + halfDiagonal;
    }
}