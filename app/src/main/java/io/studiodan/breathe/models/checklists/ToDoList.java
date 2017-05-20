package io.studiodan.breathe.models.checklists;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Object representing a to-do list and all sub lists.
 */
public class ToDoList implements Comparable<ToDoList>
{
    /**
     * Set of child lists sorted alphabetically
     */
    SortedSet<ToDoList> childLists = new ConcurrentSkipListSet<>();

    /**
     * Set of all items in the list sorted first by due date, then alphabetically
     */
    SortedSet<ToDoItem> items = new ConcurrentSkipListSet<>();

    public String localName;
    public String fullName;

    public ToDoList(String localName)
    {
        this.localName = localName;
        this.fullName = localName;
    }

    /**
     * Add given ToDoItem to this list
     *
     * @param item Item to be added
     */
    public void add(ToDoItem item)
    {
        items.add(item);
    }

    /**
     * Add given ToDoList as child list
     *
     * @param list List to be added
     */
    public void add(ToDoList list)
    {
        childLists.add(list);
    }

    /**
     * Remove list from children
     *
     * @return was list successfully removed
     */
    public boolean removeFromChildren(ToDoList list)
    {
        //Log.d("Breathe", this.localName + this.toString());
        //Log.d("Breathe", list.localName + list.toString());

        for(ToDoList l : childLists)
        {
            if(list == l)
            {
                childLists.remove(l);
                return true;
            }
            if(l.removeFromChildren(list))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove item from children or this list
     *
     * @return was item successfully removed
     */
    public boolean removeFromChildren(ToDoItem item)
    {

        if(removeItem(item))
        {
            return true;
        }
        else
        {
            for (ToDoList l : childLists)
            {
                if(l.removeFromChildren(item))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Remove item from this list
     *
     * @param item to be removed
     * @return was item successfully removed
     */
    public boolean removeItem(ToDoItem item)
    {
        if(items.contains(item))
        {
            items.remove(item);
            return true;
        }

        return false;
    }

    public SortedSet<ToDoItem> getItems()
    {
        return items;
    }

    /**
     * Get the position of targetList within child lists
     *
     * @param targetList list to be found
     * @return position of targetList. Negative if not found
     */
    public int getPositionOfList(ToDoList targetList)
    {
        if(targetList == this)
        {
            return 0;
        }
        int pos = 1;
        for(ToDoList childList : childLists)
        {
            int relPos = childList.getPositionOfList(targetList);

            if(relPos < 0)
            {
                pos -= relPos;
            }
            else
            {
                return pos + relPos;
            }
        }

        return -pos;
    }

    /**
     * Get the childList at given position
     *
     * @param pos position of list
     * @return ToDoList
     */
    public ToDoList getListAtPos(int pos)
    {
        return getListAtPos(new ReferenceInt(pos));
    }

    /**
     * Get count of this list and all child lists
     *
     * @return
     */
    public int getTotalListCount()
    {
        ReferenceInt count = new ReferenceInt();

        addTotalLength(count);

        return count.val;
    }

    protected ToDoList getListAtPos(ReferenceInt input)
    {
        if(input.val == 0)
        {
            this.fullName = localName;
            return this;
        }

        ToDoList output = null;
        for(ToDoList list : childLists)
        {
            input.dec();
            output = list.getListAtPos(input);

            if(output != null)
            {
                //Log.d("Breathe", output.localName);
                output.fullName = localName + "/" + output.fullName;
                break;
            }
        }

        return output;
    }

    protected ReferenceInt addTotalLength(ReferenceInt count)
    {
        count.inc();

        for(ToDoList list : childLists)
        {
            list.addTotalLength(count);
        }

        return count;
    }

    protected class ReferenceInt
    {
        protected int val;

        protected ReferenceInt()
        {
            this(0);
        }

        protected ReferenceInt(int p)
        {
            this.val = p;
        }

        protected void dec()
        {
            val--;
        }

        protected void inc()
        {
            val++;
        }
    }

    @Override
    public int compareTo(ToDoList o)
    {
        return localName.compareTo(o.localName);
    }

    @Override
    public String toString()
    {
        Iterator<ToDoList> listIterator = childLists.iterator();
        Iterator<ToDoItem> itemIterator = items.iterator();

        String output = localName + ": {";

        while(listIterator.hasNext())
        {
            ToDoList i = listIterator.next();

            output += i.toString() + ", ";
        }

        output += "| ";

        while(itemIterator.hasNext())
        {
            ToDoItem i = itemIterator.next();

            output += i.title + ", ";
        }

        output += "}";

        return output;
    }

}
