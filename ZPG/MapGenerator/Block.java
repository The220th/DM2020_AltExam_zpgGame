package ZPG.MapGenerator;

import java.lang.*;
import java.util.*;

import ZPG.sMap.sPoint;

import java.awt.Color;

public class Block
{
    private int cost;

    private Color color;

    public Block(int cost, Color color)
    {
        this.cost = cost;
        this.color = color;
    }

    public Color getColor()
    {
        return this.color;
    }

    public int getCost()
    {
        return this.cost;
    }

}