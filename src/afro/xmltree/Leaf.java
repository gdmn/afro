/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afro.xmltree;

import java.util.LinkedList;

/**
 *
 * @author dmn
 */
public class Leaf {

    private LeafList children = new LeafList();
    private LinkedList<Attribute> attributes = new LinkedList<Attribute>();
    private Leaf parent;
    private String name, value;

    public Leaf(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Leaf getParent() {
        if (parent == null) {
            return this;
        } else {
            return parent;
        }
    }

    public boolean isDataLeaf() {
        return getName() == null && getChildren().isEmpty();
    }

    public void setParent(Leaf parent) {
        this.parent = parent;
    }

    public void addChild(Leaf child) {
        child.parent = this;
        children.add(child);
    }

    public void addAttribute(Attribute attribute) {
        attribute.parent = this;
        attributes.add(attribute);
    }

    public void addAttribute(String key, String value) {
        addAttribute(new Attribute(key, value));
    }

    public LinkedList<Attribute> getAttributes() {
        return attributes;
    }

    public LeafList getChildren() {
        return children;
    }

    public LeafList getChildren(String name) {
        if (children.isEmpty() || name == null || name.isEmpty()) {
            return new LeafList();
        }
        return getChildren().filterByNames(name);
    }

    public LeafList getAll(String name) {
        if (children.isEmpty() || name == null || name.isEmpty()) {
            return new LeafList();
        }
        return getChildren().selectByName(name);
    }

    public Leaf getFirst(String name) {
        if (children.isEmpty() || name == null || name.isEmpty()) {
            return null;
        }
        LeafList result = getChildren().selectByName(name);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public Attribute getAttribute(String name) {
        if (attributes.isEmpty() || name == null || name.isEmpty()) {
            return null;
        }
        for (Attribute item : attributes) {
            if (name.equals(item.getKey())) {
                return item;
            }
        }
        return null;
    }

    public String getContent() {
        StringBuilder result = new StringBuilder();
        if (getChildren().size() > 0) {
            for (Leaf leaf : getChildren()) {
                if (leaf.isDataLeaf()) {
                    result.append(leaf.getValue());
                }
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        if (name != null) {
            return name + (attributes.isEmpty() ? "" : " " + attributes.toString()) + (value == null ? "" : "=" + value);
        } else {
            return "=" + value;
        }
    }
}
