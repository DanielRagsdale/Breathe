package io.studiodan.breathe.util.multiselector;

public interface IMultiSelectorAction
{
    void action(int actionID, Object obj);
    void clear(Object obj);
}
