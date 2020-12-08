package ZPG.MapGenerator;

import java.lang.*;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

import java.awt.Color;
import ZPG.sMap.sPoint;

import ZPG.MapGenerator.Block;

import ZPG.MapGenerator.LandscapeGenerator;
import ZPG.MapGenerator.TownsGenerator;

import ZPG.GameLogic.SetBit;

public class WorldMap
{
    private Block deepWater;
    private Block subtropicalDesert;
    private Block swamp;
    private Block water;
    private Block desert;
    private Block grass;
    private Block beach;
    private Block coastalWater;
    private Block tropicalForest;
    private Block wasteland;
    private Block forest;
    private Block taiga;
    private Block dryGround;
    private Block bushes;
    private Block hills;
    private Block volcanoes;
    private Block dryMountains;
    private Block tundra;
    private Block mountains;
    private Block snow;
    private Block road;
    private Block townCenter;
    private Block townBlock;
    private Block townWall;

    private int maxHeight = 9;
    private int maxWet = 9;
    private int size_pow2 = 10;

    private Block[][] map;

    public WorldMap(int size2)
    {
        //map init
        this.size_pow2 = size2;
        int size = 1;
        for(int i = 0; i < size_pow2; ++i)
            size <<= 1;
        size++; // size = 2^(size2Pow)+1

        map = new Block[size][size];
        for(Block[] item : map) // Заполнить всё null
            for(int li = 0; li < item.length; ++li)
                item[li] = null;
        
        

        //Глубокая вода
        deepWater = new Block(11, new Color(31, 78, 120));
        //Субтропическая пустыня
        subtropicalDesert = new Block(12, new Color(204, 102, 0));
        //Болото
        swamp = new Block(18, new Color(0, 153, 153));
        //Вода
        water = new Block(9, new Color(47, 117, 181));
        //Пустыня
        desert = new Block(14, new Color(255, 217, 102));
        //Трава
        grass = new Block(5, new Color(204, 255, 102));
        //Пляж
        beach = new Block(6, new Color(255, 255, 0));
        //Прибрежная вода
        coastalWater = new Block(10, new Color(189, 215, 238));
        //Тропический лес
        tropicalForest = new Block(13, new Color(102, 255, 102));
        //Пустошь
        wasteland = new Block(8, new Color(118, 114, 86));
        //Лес
        forest = new Block(9, new Color(171, 214, 142));
        //Тайга
        taiga = new Block(16, new Color(0, 176, 80));
        //Сухая земля
        dryGround = new Block(10, new Color(184, 155, 142));
        //Кустарники
        bushes = new Block(7, new Color(227, 236, 110));
        //Холмы
        hills = new Block(17, new Color(51, 102, 0));
        //Вулканы
        volcanoes = new Block(22, new Color(210, 184, 138));
        //Сухие горы
        dryMountains = new Block(25, new Color(104, 100, 100));
        //Тундра
        tundra = new Block(23, new Color(246, 252, 228));
        //Горы
        mountains = new Block(20, new Color(58, 56, 56));
        //Снег
        snow = new Block(28, Color.WHITE);
        //Дорога
        road = new Block(1, new Color(184, 132, 0));
        //Центр города
        townCenter = new Block(1, new Color(255, 183, 0));
        //Блоки города
        townBlock = new Block(2, new Color(26, 24, 22));
        //Стены города
        townWall = new Block(44, new Color(56, 56, 56));
    }

    public Block whatBlockLandscape(int height, int wet) throws IllegalArgumentException
    {
        Block b = null;
        if(height != 101 && height != 102 && height != 103 && height != 104 && (wet < 0 || wet > maxWet || height < 0 || height > maxHeight))
            throw new IllegalArgumentException("height = " + height + ", wet = " + wet + ". Max height = " + maxHeight + ", max wet = " + maxWet);
        if(height == 0)
        {
            //Глубокая вода
            b = deepWater;
        }
        else if(height == 1)
        {
            if(0 <= wet && wet <= 3)
            {
                //Субтропическая пустыня
                b = subtropicalDesert;
            }
            else if(4 <= wet && wet <= 6)
            {
                //Болото
                b = swamp;
            }
            else if(7 <= wet && wet <= 9)
            {
                //Вода
                b = water;
            }
        }
        else if(height == 2)
        {
            if(0 <= wet && wet <= 3)
            {
                //Пустыня
                b = desert;
            }
            else if(4 <= wet && wet <= 5)
            {
                //Трава
                b = grass;
            }
            else if(6 <= wet && wet <= 7)
            {
                //Пляж
                b = beach;
            }
            else if(8 <= wet && wet <= 9)
            {
                //Прибрежная вода
                b = coastalWater;
            }
        }
        else if(height == 3)
        {
            if(0 <= wet && wet <= 3)
            {
                //Пустыня
                b = desert;
            }
            else if(4 <= wet && wet <= 6)
            {
                //Трава
                b = grass;
            }
            else if(7 <= wet && wet <= 8)
            {
                //Пляж
                b = beach;
            }
            else if(9 <= wet && wet <= 9)
            {
                //Тропический лес
                b = tropicalForest;
            }
        }
        else if(height == 4)
        {
            if(0 <= wet && wet <= 2)
            {
                //Пустыня
                b = desert;
            }
            else if(2 <= wet && wet <= 3)
            {
                //Пустошь
                b = wasteland;
            }
            else if(4 <= wet && wet <= 4)
            {
                //Трава
                b = grass;
            }
            else if(5 <= wet && wet <= 7)
            {
                //Лес
                b = forest;
            }
            else if(8 <= wet && wet <= 9)
            {
                //Тропический лес
                b = tropicalForest;
            }
        }
        else if(height == 5)
        {
            if(0 <= wet && wet <= 0)
            {
                //Пустыня
                b = desert;
            }
            else if(1 <= wet && wet <= 3)
            {
                //Пустошь
                b = wasteland;
            }
            else if(4 <= wet && wet <= 7)
            {
                //Лес
                b = forest;
            }
            else if(8 <= wet && wet <= 8)
            {
                //Тропический лес
                b = tropicalForest;
            }
            else if(9 <= wet && wet <= 9)
            {
                //Тайга
                b = taiga;
            }
        }
        else if(height == 6)
        {
            if(0 <= wet && wet <= 2)
            {
                //Сухая земля
                b = dryGround;
            }
            else if(3 <= wet && wet <= 3)
            {
                //Пустошь
                b = wasteland;
            }
            else if(4 <= wet && wet <= 6)
            {
                //Кустарники
                b = bushes;
            }
            else if(7 <= wet && wet <= 7)
            {
                //Лес
                b = forest;
            }
            else if(8 <= wet && wet <= 9)
            {
                //Тайга
                b = taiga;
            }
        }
        else if(height == 7)
        {
            if(0 <= wet && wet <= 3)
            {
                //Сухая земля
                b = dryGround;
            }
            else if(4 <= wet && wet <= 5)
            {
                //Кустарники
                b = bushes;
            }
            else if(6 <= wet && wet <= 9)
            {
                //Холмы
                b = hills;
            }
        }
        else if(height == 8)
        {
            if(0 <= wet && wet <= 1)
            {
                //Вулканы
                b = volcanoes;
            }
            else if(1 <= wet && wet <= 1)
            {
                //Сухая земля
                b = dryGround;
            }
            else if(2 <= wet && wet <= 3)
            {
                //Сухие горы
                b = dryMountains;
            }
            else if(4 <= wet && wet <= 4)
            {
                //Кустарники
                b = bushes;
            }
            else if(5 <= wet && wet <= 6)
            {
                //Тундра
                b = tundra;
            }
            else if(7 <= wet && wet <= 9)
            {
                //Горы
                b = mountains;
            }
        }
        else if(height == 9)
        {
            if(0 <= wet && wet <= 1)
            {
                //Вулканы
                b = volcanoes;
            }
            else if(2 <= wet && wet <= 5)
            {
                //Сухие горы
                b = dryMountains;
            }
            else if(6 <= wet && wet <= 7)
            {
                //Тундра
                b = tundra;
            }
            else if(8 <= wet && wet <= 9)
            {
                //Снег
                b = snow;
            }
        }
        else if(height == 101)
            b = townBlock;
        else if(height == 102)
            b = townCenter;
        else if(height == 103)
            b = road;
        else if(height == 104)
            b = townWall;
        else
            System.out.println("Failed succussfully: height = " + height + ", wet = " + wet);
        return b;
    }

    public void genWorld()
    {
        int[][] rawMap_heights;
        int[][] rawMap_wet;
        
        //высоты
        LandscapeGenerator heights = new LandscapeGenerator(size_pow2, maxHeight, 2, 0.7f);
        rawMap_heights = heights.genMap();
        heights = null;
        
        TownsGenerator tg = (new TownsGenerator(rawMap_heights, 75)).roadR(1).minR(2);
        tg.addTowns();

        //влажность
        LandscapeGenerator wets = new LandscapeGenerator(size_pow2, maxWet, 4, 0.07f);
        rawMap_wet = wets.genMap();
        wets = null;

        for(int i = 0; i < map.length; ++i)
            for(int j = 0; j < map[i].length; ++j)
                map[i][j] = whatBlockLandscape(rawMap_heights[i][j], rawMap_wet[i][j]);
    }

    /**
     * Отсчёт с верхнего левого угла
     * |------>X
     * |
     * v
     * Y
     */
    public Block getBlock(int x, int y)
    {
        if(checkCoordsLegal(x, y))
            return map[x][y];
        else
            return null;
    }

    /**
     * Отсчёт с верхнего левого угла
     * |------>X
     * |
     * v
     * Y
     */
    public Block getBlock(sPoint p)
    {
        if(checkCoordsLegal(p))
            return map[p.getX()][p.getY()];
        else
            return null;
    }

    public boolean checkCoordsLegal(sPoint p)
    {
        int x = p.getX();
        int y = p.getY();
        if(0 <= x && x < map.length && 0 <= y && y < map.length)
            return true;
        else
            return false;

    }

    public boolean checkCoordsLegal(int x, int y)
    {
        if(0 <= x && x < map.length && 0 <= y && y < map.length)
            return true;
        else
            return false;
    }

    public int getMaxSize()
    {
        return map.length;
    }

    public int toLineNum(sPoint p) throws IllegalArgumentException
    {
        int x = p.getX();
        int y = p.getY();
        if(checkCoordsLegal(x, y))
            return y*map.length + x;
        else
            throw new IllegalArgumentException("max x is " + (map.length-1) + ", max y is " + (map.length-1) + ". Your x = " + p.getX() + ", your y = " + p.getY());
    }

    public int getMaxLineNum()
    {
        return map.length*map.length - 1;
    }

    /**
     * Вернёр список смежности вершин (sPoint), смежных с вершиной v, кроме тех, что содержатся в excluding
     * Если excluding == null, то вернёт просто список смежности вершин, смежных с v
     * Если shuffle == true, то список смежности перемешается, тем самым порядок элементов в нём будет рандомным
     * Если shuffle == false, то вершрины в списке будут в следующем порядке: верхняя, правая, нижняя, левая. Если какой-то вершины нет, то она пропускается
     * 
     * @return LinkedList<sPoint> - список смежности
     */
    public List<sPoint> getListOfAdjacentVertices(sPoint v, boolean shuffle, SetBit excluding)
    {
        List<sPoint> res = new LinkedList<sPoint>();
        sPoint buff;

        //up
        buff = v.subY(1);
        if(checkCoordsLegal(buff) && (excluding == null || excluding.contains(this.toLineNum(buff)) == false))
            res.add(buff);
        
        //right
        buff = v.addX(1);
        if(checkCoordsLegal(buff) && (excluding == null || excluding.contains(this.toLineNum(buff)) == false))
            res.add(buff);

        //down
        buff = v.addY(1);
        if(checkCoordsLegal(buff) && (excluding == null || excluding.contains(this.toLineNum(buff)) == false))
            res.add(buff);

        //left
        buff = v.subX(1);
        if(checkCoordsLegal(buff) && (excluding == null || excluding.contains(this.toLineNum(buff)) == false))
            res.add(buff);
        
        if(shuffle)
            Collections.shuffle(res);
        return res;
    }

    public boolean adjacencyMatrix(sPoint i, sPoint j)
    {
        if(checkCoordsLegal(i) && checkCoordsLegal(j))
        {
            if(i.equals(j))
                return false;
            else
            {
                int dx = i.getX() - j.getX();
                dx  = dx > 0?dx:-dx;
                int dy = i.getY() - j.getY();
                dy = dy > 0?dy:-dy;
                if(dy > 1 || dx > 1)
                    return false;
                if((dx ^ dy) != 0)
                    return true;
                else
                    return false;
            }
        }
        else
            return false;
    }
}