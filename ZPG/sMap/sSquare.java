package ZPG.sMap;

import ZPG.sMap.sPoint;
/**
 * Должно гарантироваться, что изменить квадрат нельзя
 */
public class sSquare
{
    private sPoint left_up;
    private int a;

    /**
     * Конструирует квадрат
     * 
     * @param Left_up - верхний левый угол
     * @param l - сторона квадрата - 1
     */
    public sSquare(sPoint Left_up, int l)
    {
        this.left_up = Left_up;
        a = l;
    }

    public sPoint getLU()
    {
        return left_up;
    }

    public sPoint getLD()
    {
        return new sPoint(this.left_up.getX(), this.left_up.getY()+a);
    }

    public sPoint getRU()
    {
        return new sPoint(this.left_up.getX()+a, this.left_up.getY());
    }

    public sPoint getRD()
    {
        return new sPoint(this.left_up.getX()+a, this.left_up.getY()+a);
    }

    public sPoint getC()
    {
        return this.left_up.addXY(a/2, a/2);
    }

    public String toString()
    {
        return "Square: lu = " + this.left_up.toString() + ", l = " + a;
    }
}