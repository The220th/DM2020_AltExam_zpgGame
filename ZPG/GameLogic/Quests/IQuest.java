package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

import ZPG.GameLogic.Quests.QuestPoint;

public interface IQuest
{
    public static final int QUEST_ENDED = -1;
    public static final int NEXT_PLACE_TO_VISIT = 1;
    public static final int REWARD = 2;

    /**
     * if QUEST_ENDED = k = -1, то value = null
     * if NEXT_PLACE_TO_VISIT = k = 1, то value = sPoint
     * if REWARD = k = 2, то value = Integer
     */
    public abstract QuestPoint getNextQuestPoint();
    public abstract QuestPoint peekNextQuestPoint();
}