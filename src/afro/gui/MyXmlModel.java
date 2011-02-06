/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afro.gui;

import afro.xmltree.Leaf;
import java.util.LinkedList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author dmn
 */
public class MyXmlModel implements TreeModel {

    private LinkedList<TreeModelListener> treeModelListeners = new LinkedList<TreeModelListener>();
    private Leaf rootLeaf;

    public MyXmlModel(Leaf root) {
        rootLeaf = root;
    }

//////////////// Fire events //////////////////////////////////////////////
    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(Leaf oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this,
                new Object[]{oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }

//////////////// TreeModel interface implementation ///////////////////////
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    @Override
    public Object getChild(Object parent, int index) {
        Leaf p = (Leaf) parent;
        return p.getChildren().get(index);
    }

    /**
     * Returns the number of children of parent.
     */
    @Override
    public int getChildCount(Object parent) {
        Leaf p = (Leaf) parent;
        return p.getChildren().size();
    }

    /**
     * Returns the index of child in parent.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Leaf p = (Leaf) parent;
        return p.getChildren().indexOf(child);
    }

    /**
     * Returns the root of the tree.
     */
    @Override
    public Object getRoot() {
        return rootLeaf;
    }

    /**
     * Returns true if node is a leaf.
     */
    @Override
    public boolean isLeaf(Object node) {
        Leaf p = (Leaf) node;
        return p.isDataLeaf() || p.getChildren().size() == 0 || (p.getChildren().size() == 1
                && p.getChildren().get(0).isDataLeaf());
    }

    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    /**
     * Messaged when the user has altered the value for the item
     * identified by path to newValue.  Not used by this model.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }
}
