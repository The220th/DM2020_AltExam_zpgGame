package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Quests.*;

/**
 * Гарантируется, что нельзя изменять
 */
public class QuestPoint
{
    /**
     * if QUEST_ENDED = k = -1, то value = null
     * if NEXT_PLACE_TO_VISIT = k = 1, то value = sPoint
     * if REWARD = k = 2, то value = Integer
     */
    private Object value;
    private int key;
    public QuestPoint(int key, Object value)
    {
        this.key = key;
        this.value = value;
    }

    public int getKey()
    {
        return this.key;
    }

    public Object getValue()
    {
        return this.value;
    }
}