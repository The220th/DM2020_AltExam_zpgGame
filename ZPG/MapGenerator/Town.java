package ZPG.MapGenerator;

import ZPG.sMap.sPoint;

import java.util.*;
import java.lang.*;

public class Town
{
    private static int numbers = 1;

    private String name;
    private sPoint coords;
    private int size; //Радиус города

    public Town(sPoint p, int r)
    {
        this.coords = p;
        this.size = r;
        this.name = "Town " + numbers;
        ++numbers;
    }

    public sPoint getCoords()
    {
        return this.coords;
    }

    public String getName()
    {
        return this.name;
    }

    public int getSize()
    {
        return this.size;
    }

    @Override
    public String toString()
    {
        return this.name + " = {size is " + size + ", coordinates = " + this.coords + "}";
    }
}