package io.studiodan.breathe.util.multiselector;

public abstract class ActionMultiSelector<T>
{
    MultiSelector<T> mSelector;

    public abstract void action(int actionID, Object obj);

    public void setMultiSelector(MultiSelector<T> selector)
    {
        mSelector = selector;
    }

    public void clearOnce(){}

    public  void clear(Object obj){}

    public boolean allowUndo(int actionID)
    {
        return false;
    }

    public void undo(int actionID, Object obj)
    {}
}
