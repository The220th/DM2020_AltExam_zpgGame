package ZPG.GameLogic;

import java.lang.*;
import java.util.*;

/**
 * Множество. Его использовать, если у каждого элемента есть свой индекс, и известно максимальное возможное кол-во элементов
 * Тогда добавление, удаление и проверка на содержимое будет выполняться за O(1)
 * Пересечение, объединение и дополнение за O(n)
 */
public class SetBit
{
    private int n;
    private int oneCell;

    private int amount;

    private int[] a;

    /*Тесты*/
    public static void main(String[] args)
    {
        int N = 100000;
        int n;
        int i;
        boolean f;
        Random r = new Random();
        SetBit sb = new SetBit(N);
        List<Integer> in = new LinkedList<Integer>();
        List<Integer> out = new LinkedList<Integer>();

        for(i = 0; i < N; ++i)
            if(r.nextBoolean())
            {
                in.add(Integer.valueOf(i));
                sb.add(i);
            }
            else
                out.add(Integer.valueOf(i));
        
        f = true;
        for(Integer item : in)
            if(sb.contains(item.intValue()) == false)
                f = false;

        for(Integer item : out)
            if(sb.contains(item.intValue()) == true)
                f = false;        
        if(sb.size() != in.size())
            f = false;
        if(f)    
            System.out.println("All is OK");
        else
            System.out.println("Error!!!");


        in = new LinkedList<Integer>();
        out = new LinkedList<Integer>();
        for(i = 0; i < N; ++i)
            sb.add(i);

        for(i = 0; i < N; ++i)
            if(r.nextBoolean())
                in.add(Integer.valueOf(i));
            else
            {
                out.add(Integer.valueOf(i));
                sb.remove(i);
            }
        
        f = true;
        for(Integer item : in)
            if(sb.contains(item.intValue()) == false)
                f = false;

        for(Integer item : out)
            if(sb.contains(item.intValue()) == true)
                f = false;
        if(sb.size() != in.size())
            f = false;
        if(f)    
            System.out.println("All is OK");
        else
            System.out.println("Error!!!");

        SetBit sb1 = new SetBit(N);
        SetBit sb2 = new SetBit(N);
        
        //union
        in = new LinkedList<Integer>();
        out = new LinkedList<Integer>();
        
        for(i = 0; i < N; ++i)
        {
            if(r.nextBoolean())
            {
                in.add(Integer.valueOf(i));
                sb1.add(i);

            }
            if(r.nextBoolean())
            {
                out.add(Integer.valueOf(i));
                sb2.add(i);
            }
        }
        n = 0;
        for(i = 0; i < N; ++i)
            if(in.contains(Integer.valueOf(i)) || out.contains(Integer.valueOf(i)))
                ++n;
        sb1.union(sb2);
        f = true;
        for(i = 0; i < N; ++i)
            if(in.contains(Integer.valueOf(i)) || out.contains(Integer.valueOf(i)))
            {
                if(sb1.contains(i) == false)
                    f = false;
            }
            else
                if(sb1.contains(i) == true)
                    f = false;
        if(sb1.size() != n)
            f = false;
        if(f)    
            System.out.println("All is OK");
        else
            System.out.println("Error!!!");


        //intersection
        in = new LinkedList<Integer>();
        out = new LinkedList<Integer>();
        sb1.clear();
        sb2.clear();
        
        for(i = 0; i < N; ++i)
        {
            if(r.nextBoolean())
            {
                in.add(Integer.valueOf(i));
                sb1.add(i);

            }
            if(r.nextBoolean())
            {
                out.add(Integer.valueOf(i));
                sb2.add(i);
            }
        }
        n = 0;
        for(i = 0; i < N; ++i)
            if(in.contains(Integer.valueOf(i)) && out.contains(Integer.valueOf(i)))
                ++n;
        sb1.intersection(sb2);
        f = true;
        for(i = 0; i < N; ++i)
            if(in.contains(Integer.valueOf(i)) && out.contains(Integer.valueOf(i)))
            {
                if(sb1.contains(i) == false)
                    f = false;
            }
            else
                if(sb1.contains(i) == true)
                    f = false;
        if(sb1.size() != n)
            f = false;
        if(f)    
            System.out.println("All is OK");
        else
            System.out.println("Error!!!");

        //complement
        in = new LinkedList<Integer>();
        sb.clear();
        for(i = 0; i < N; ++i)
            if(r.nextBoolean())
            {
                in.add(Integer.valueOf(i));
                sb.add(i);
            }
        
        sb.complement();
        f = true;
        n = 0;
        for(i = 0; i < N; ++i)
            if(in.contains(Integer.valueOf(i)) == false)
            {
                ++n;
                if(sb.contains(i) == false)
                    f = false;
            }
        if(sb.size() != n)
            f = false;

        if(f)    
            System.out.println("All is OK");
        else
            System.out.println("Error!!!");
    }

    /**
     * @param elementsQuantity - максимальное количество элементов в множестве
     */
    public SetBit(int elementsQuantity)
    {
        this.n = elementsQuantity;

        this.oneCell = Integer.BYTES * 8;

        this.amount = 0;

        this.a = new int[this.n/this.oneCell+1];
        for(int i = 0; i < this.a.length; ++i)
            this.a[i] = 0;
    }

    /**
     * Добавить элемент в множество
     */
    public void add(int element) throws IllegalArgumentException
    {
        if(element >= n)
            throw new IllegalArgumentException("Max element is " + n + ". Your element is " + element);
        
        if(!this.contains(element))
        {
            int bit = element % oneCell;
            int cell = element / oneCell;
            a[cell] = a[cell] | (1 << bit);
            ++amount;
        }
    }

    /**
     * Удалить элемент из множества
     */
    public void remove(int element)
    {
        if(element >= n)
            throw new IllegalArgumentException("Max element is " + n + ". Your element is " + element);

        if(this.contains(element))
        {
            int bit = element % oneCell;
            int cell = element / oneCell;
            a[cell] = a[cell] & (~(1 << bit));
            --amount;
        }
    }

    /**
     * Содержится ли элемент в множестве
     */
    public boolean contains(int element)
    {
        if(element >= n)
            throw new IllegalArgumentException("Max element is " + n + ". Your element is " + element);
        
        int bit = element % oneCell;
        int cell = element / oneCell;
        return (a[cell] & (1 << bit)) != 0?true:false;
    }

    /**
     * @return количество элементов в множестве
     */
    public int size()
    {
        return amount;
    }

    /**
     * @return если множество пустое, то true
     */
    public boolean isEmpty()
    {
        return amount == 0?true:false;
    }

    /**
     * @return Максимальное количество элементов в множестве
     */
    public int maxSize()
    {
        return n;
    }

    /**
     * Очистить всё множество (всё сбросить в false)
     */
    public void clear()
    {
        this.amount = 0;

        for(int i = 0; i < this.a.length; ++i)
            this.a[i] = 0; 
    }

    /**
     * Пересечение (операция И)
     */
    public void intersection(SetBit other)
    {
        if(other.maxSize() != this.maxSize())
            throw new IllegalArgumentException("Size of other set is not equals this set. Other size is " + other.size() + ", this size is " + this.size());
        for(int i = 0; i < a.length; ++i)
            a[i] &= other.a[i];
        
        amount = 0;
        for(int i = 0; i < maxSize(); ++i)
            if( (  a[i / oneCell] & (1 << (i%oneCell)) ) != 0)
                ++amount;
    }

    /**
     * Объединение (операция ИЛИ)
     */
    public void union(SetBit other)
    {
        if(other.maxSize() != this.maxSize())
            throw new IllegalArgumentException("Size of other set is not equals this set. Other size is " + other.size() + ", this size is " + this.size());
        for(int i = 0; i < a.length; ++i)
            a[i] |= other.a[i];
        
        amount = 0;
        for(int i = 0; i < maxSize(); ++i)
            if( (  a[i / oneCell] & (1 << (i%oneCell)) ) != 0)
                ++amount;
    }

    /**
     * Дополнение (операция НЕ)
     */
    public void complement()
    {
        for(int i = 0; i < a.length; ++i)
            a[i] = ~a[i];
        amount = this.maxSize() - amount;
    }
}