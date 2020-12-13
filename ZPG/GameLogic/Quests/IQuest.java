package ZPG.GameLogic.Quests;

import java.util.*;
import java.lang.*;

public interface IQuest
{
    public static final int QUEST_ENDED = -1;
    public static final int NEXT_PLACE_TO_VISIT = 1;
    public static final int REWARD = 2;

    public abstract Map.Entry<Integer, Object> getNextQuestPoint();
    public abstract Map.Entry<Integer, Object> peekNextQuestPoint();
}