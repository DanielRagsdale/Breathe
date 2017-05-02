package io.studiodan.breathe.util.multiselector;

public abstract class MultiSelectorAction
{
    public abstract void action(int actionID, Object obj);

    public abstract void clear(Object obj);

    public boolean allowUndo(int actionID)
    {
        return false;
    }

    public void undo(int actionID, Object obj)
    {}
}
