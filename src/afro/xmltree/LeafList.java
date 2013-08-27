package afro.xmltree;

import java.util.LinkedList;

/**
 *
 * @author dmn
 */
public class LeafList extends LinkedList<Leaf> {

    /**
     * @param name tag names, slash separated
     * @return all children from path identified by name
     */
    public LeafList filterByNames(String name) {
        return filterByNames(this, name);
    }

    /**
     * @param name tag name, slash is not allowed
     * @return all elements with given name
     */
    public LeafList selectByName(String name) {
        return selectByName(this, name);
    }

    private static LeafList filterByNames(LeafList list, String name) {
        if (list.isEmpty()) {
            return list;
        }
        LeafList result = new LeafList();
        String rest = null;
        int slashPos = name.indexOf('/', 1);
        if (slashPos > 0) {
            rest = name.substring(slashPos + 1);
            name = name.substring(0, slashPos);
        }
        for (Leaf item : list) {
            if (name.equals(item.getName())) {
                result.addAll(item.getChildren());
            }
        }
        if (rest == null) {
            return result;
        }
        return filterByNames(result, rest);
    }

    private static LeafList selectByName(LeafList list, String name) {
        if (list.isEmpty() || name.indexOf('/') >= 0) {
            return list;
        }
        LeafList result = new LeafList();
        for (Leaf item : list) {
            if (name.equals(item.getName())) {
                result.add(item);
            }
        }
        return result;
    }
}
