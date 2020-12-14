package ZPG.MapGenerator;

import java.lang.*;
import java.util.*;

import java.awt.Color;
import ZPG.sMap.sPoint;

import ZPG.MapGenerator.Block;
import ZPG.MapGenerator.Town;

import ZPG.MapGenerator.LandscapeGenerator;
import ZPG.MapGenerator.TownsGenerator;

import ZPG.GameLogic.SetBit;

public class WorldMap
{
    /*
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
    private Block snow;*/
    private Block road;
    private Block townCenter;
    private Block townBlock;
    private Block townWall;

    private Block[][] blocks; //i - height, j - wet

    private List<Town> towns;

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
        
        blocks = new Block[10][10];
        //w = 0
        blocks[0][0] = new Block(0, new Color(63, 63, 252));
        blocks[1][0] = new Block(0, new Color(245, 243, 175));
        blocks[2][0] = new Block(0, new Color(244, 230, 161));
        blocks[3][0] = new Block(0, new Color(244, 230, 161));
        blocks[4][0] = new Block(0, new Color(210, 199, 138));
        blocks[5][0] = new Block(0, new Color(238, 177, 88));
        blocks[6][0] = new Block(0, new Color(150, 109, 76));
        blocks[7][0] = new Block(0, new Color(184, 155, 142));
        blocks[8][0] = new Block(0, new Color(95, 95, 95));
        blocks[9][0] = new Block(0, new Color(108, 108, 108));

        //w = 1
        blocks[0][1] = new Block(0, new Color(63, 63, 252));
        blocks[1][1] = new Block(0, new Color(245, 243, 175));
        blocks[2][1] = new Block(0, new Color(183, 186, 37));
        blocks[3][1] = new Block(0, new Color(183, 186, 37));
        blocks[4][1] = new Block(0, new Color(183, 186, 37));
        blocks[5][1] = new Block(0, new Color(125, 176, 55));
        blocks[6][1] = new Block(0, new Color(150, 109, 76));
        blocks[7][1] = new Block(0, new Color(184, 155, 142));
        blocks[8][1] = new Block(0, new Color(95, 95, 95));
        blocks[9][1] = new Block(0, new Color(108, 108, 108));

        //w = 2
        blocks[0][2] = new Block(0, new Color(63, 63, 252));
        blocks[1][2] = new Block(0, new Color(245, 243, 175));
        blocks[2][2] = new Block(0, new Color(183, 186, 37));
        blocks[3][2] = new Block(0, new Color(183, 186, 37));
        blocks[4][2] = new Block(0, new Color(183, 186, 37));
        blocks[5][2] = new Block(0, new Color(118, 114, 86));
        blocks[6][2] = new Block(0, new Color(150, 109, 76));
        blocks[7][2] = new Block(0, new Color(184, 155, 142));
        blocks[8][2] = new Block(0, new Color(95, 95, 95));
        blocks[9][2] = new Block(0, new Color(108, 108, 108));
        
        //w = 3
        blocks[0][3] = new Block(0, new Color(63, 63, 252));
        blocks[1][3] = new Block(0, new Color(245, 243, 175));
        blocks[2][3] = new Block(0, new Color(183, 186, 37));
        blocks[3][3] = new Block(0, new Color(183, 186, 37));
        blocks[4][3] = new Block(0, new Color(183, 186, 37));
        blocks[5][3] = new Block(0, new Color(118, 114, 86));
        blocks[6][3] = new Block(0, new Color(150, 109, 76));
        blocks[7][3] = new Block(0, new Color(184, 155, 142));
        blocks[8][3] = new Block(0, new Color(95, 95, 95));
        blocks[9][3] = new Block(0, new Color(108, 108, 108));
        
        //w = 4
        blocks[0][4] = new Block(0, new Color(44, 44, 178));
        blocks[1][4] = new Block(0, new Color(63, 63, 252));
        blocks[2][4] = new Block(0, new Color(125, 176, 55));
        blocks[3][4] = new Block(0, new Color(125, 176, 55));
        blocks[4][4] = new Block(0, new Color(125, 176, 55));
        blocks[5][4] = new Block(0, new Color(118, 114, 86));
        blocks[6][4] = new Block(0, new Color(150, 109, 76));
        blocks[7][4] = new Block(0, new Color(0, 80, 0));
        blocks[8][4] = new Block(0, new Color(95, 95, 95));
        blocks[9][4] = new Block(0, new Color(108, 108, 108));
        
        //w = 5
        blocks[0][5] = new Block(0, new Color(44, 44, 178));
        blocks[1][5] = new Block(0, new Color(63, 63, 252));
        blocks[2][5] = new Block(0, new Color(210, 199, 138));
        blocks[3][5] = new Block(0, new Color(125, 176, 55));
        blocks[4][5] = new Block(0, new Color(0, 123, 0));
        blocks[5][5] = new Block(0, new Color(0, 123, 0));
        blocks[6][5] = new Block(0, new Color(0, 80, 0));
        blocks[7][5] = new Block(0, new Color(0, 80, 0));
        blocks[8][5] = new Block(0, new Color(95, 95, 95));
        blocks[9][5] = new Block(0, new Color(108, 108, 108));
        
        //w = 6
        blocks[0][6] = new Block(0, new Color(44, 44, 178));
        blocks[1][6] = new Block(0, new Color(63, 63, 252));
        blocks[2][6] = new Block(0, new Color(210, 199, 138));
        blocks[3][6] = new Block(0, new Color(125, 176, 55));
        blocks[4][6] = new Block(0, new Color(0, 123, 0));
        blocks[5][6] = new Block(0, new Color(0, 123, 0));
        blocks[6][6] = new Block(0, new Color(0, 80, 0));
        blocks[7][6] = new Block(0, new Color(0, 80, 0));
        blocks[8][6] = new Block(0, new Color(95, 95, 95));
        blocks[9][6] = new Block(0, new Color(108, 108, 108));
        
        //w = 7
        blocks[0][7] = new Block(0, new Color(44, 44, 178));
        blocks[1][7] = new Block(0, new Color(63, 63, 252));
        blocks[2][7] = new Block(0, new Color(210, 199, 138));
        blocks[3][7] = new Block(0, new Color(125, 176, 55));
        blocks[4][7] = new Block(0, new Color(0, 123, 0));
        blocks[5][7] = new Block(0, new Color(0, 123, 0));
        blocks[6][7] = new Block(0, new Color(50, 79, 50));
        blocks[7][7] = new Block(0, new Color(50, 79, 50));
        blocks[8][7] = new Block(0, new Color(95, 95, 95));
        blocks[9][7] = new Block(0, new Color(108, 108, 108));
        
        //w = 8
        blocks[0][8] = new Block(0, new Color(44, 44, 178));
        blocks[1][8] = new Block(0, new Color(63, 63, 252));
        blocks[2][8] = new Block(0, new Color(210, 199, 138));
        blocks[3][8] = new Block(0, new Color(125, 176, 55));
        blocks[4][8] = new Block(0, new Color(0, 145, 62));
        blocks[5][8] = new Block(0, new Color(0, 145, 62));
        blocks[6][8] = new Block(0, new Color(50, 79, 50));
        blocks[7][8] = new Block(0, new Color(50, 79, 50));
        blocks[8][8] = new Block(0, new Color(95, 95, 95));
        blocks[9][8] = new Block(0, new Color(108, 108, 108));
        
        //w = 9
        blocks[0][9] = new Block(0, new Color(44, 44, 178));
        blocks[1][9] = new Block(0, new Color(63, 63, 252));
        blocks[2][9] = new Block(0, new Color(210, 199, 138));
        blocks[3][9] = new Block(0, new Color(0, 145, 62));
        blocks[4][9] = new Block(0, new Color(0, 145, 62));
        blocks[5][9] = new Block(0, new Color(0, 145, 62));
        blocks[6][9] = new Block(0, new Color(50, 79, 50));
        blocks[7][9] = new Block(0, new Color(50, 79, 50));
        blocks[8][9] = new Block(0, new Color(95, 95, 95));
        blocks[9][9] = new Block(0, new Color(239, 249, 255));
        
        /*
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
        snow = new Block(28, Color.WHITE);*/
        //Дорога
        road = new Block(1, new Color(184, 132, 0));
        //Центр города
        townCenter = new Block(1, new Color(255, 183, 0));
        //Блоки города
        townBlock = new Block(2, new Color(26, 24, 22));
        //Стены города
        townWall = new Block(44, new Color(255, 0, 0));
    }

    public Block whatBlockLandscape(int height, int wet) throws IllegalArgumentException
    {
        Block b = null;
        if(height != 101 && height != 102 && height != 103 && height != 104 && (wet < 0 || wet > maxWet || height < 0 || height > maxHeight))
            throw new IllegalArgumentException("height = " + height + ", wet = " + wet + ". Max height = " + maxHeight + ", max wet = " + maxWet);
        
        if(height <= 9)
        {
            b = blocks[height][wet];
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
        /*
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
        */
    }

    public void genWorld()
    {
        int[][] rawMap_heights;
        int[][] rawMap_wet;
        
        //высоты
        LandscapeGenerator heights = new LandscapeGenerator(size_pow2, maxHeight, 2, 0.5f);
        rawMap_heights = heights.genMap();
        heights = null;
        
        TownsGenerator tg = (new TownsGenerator(rawMap_heights, 75)).roadR(1).minR(2);
        tg.addTowns();
        towns = tg.getTowns();
        /*for(Town t : this.towns)
            System.out.println(t);*/

        //влажность
        LandscapeGenerator wets = new LandscapeGenerator(size_pow2, maxWet, 4, 0.07f);
        rawMap_wet = wets.genMap();
        wets = null;

        for(int i = 0; i < map.length; ++i)
            for(int j = 0; j < map[i].length; ++j)
                map[i][j] = whatBlockLandscape(rawMap_heights[j][i], rawMap_wet[i][j]);
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
     * Вернёт список смежности вершин (sPoint), смежных с вершиной v, кроме тех, что содержатся в excluding
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
            if(i.equals(j))	//here LiSearcher brakes
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
	
	/**
	 * Вернёт стоимость перехода между вершинами
	 *
	 * Стоимость перехода равна среднему арифметическому стоимости текущей точки и стоимости другой точки
	 *
	 * @return double - стоиомсть перехода
	 */
	public double getCost(sPoint i, sPoint j)
	{
		if(!adjacencyMatrix(i, j))
			return Double.POSITIVE_INFINITY;
		return ((getBlock(i).getCost() + getBlock(j).getCost()) / 2.0);
    }
    
    public List<Town> getTowns()
    {
        return towns;
    }
}