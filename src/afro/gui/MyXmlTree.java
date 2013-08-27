package afro.gui;

import afro.xmltree.Leaf;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author dmn
 */
public class MyXmlTree extends JTree implements TreeSelectionListener {

    MyXmlModel model;
    private JTextComponent text;

    public MyXmlTree(Leaf root) {
        super();
        this.setModel(new MyXmlModel(root));
        getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Icon personIcon = null;
        renderer.setLeafIcon(personIcon);
        renderer.setClosedIcon(personIcon);
        renderer.setOpenIcon(personIcon);
        setCellRenderer(renderer);
        this.addTreeSelectionListener(this);
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                MyXmlTree tree = MyXmlTree.this;
                TreePath path = tree.getSelectionPath();
                boolean collapsed = tree.isCollapsed(path);
                System.out.println("collapsed " + collapsed);
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    expandLevel(tree, MyXmlTree.this.getSelectionPath(), true, true, true);
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    expandLevel(MyXmlTree.this, MyXmlTree.this.getSelectionPath(), false, true, true);
                } else if (e.getKeyCode() == KeyEvent.VK_4) {
                    expandLevel(tree, MyXmlTree.this.getSelectionPath(), true, true, false);
                } else if (e.getKeyCode() == KeyEvent.VK_3) {
                    expandLevel(MyXmlTree.this, MyXmlTree.this.getSelectionPath(), false, true, false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    expandLevel(MyXmlTree.this, MyXmlTree.this.getSelectionPath(), collapsed, false, true);
                } else {
                    super.keyReleased(e);
                }
            }
        });
    }

    public MyXmlTree(Leaf root, JTextComponent text) {
        this(root);
        this.text = text;
    }

    public void setRootLeaf(Leaf root) {
        this.setModel(new MyXmlModel(root));
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof Leaf) {
            Leaf l = (Leaf) value;
            if (l.getChildren().size() == 1 && l.getChildren().get(0).isDataLeaf()) {
                return l.toString() + l.getChildren().get(0).toString();
            } else {
                return l.toString();
            }
        } else {
            return value.toString();
        }

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        //System.out.println(""+e.getSource());
        Object selected = getLastSelectedPathComponent();
        if (text != null) {
            if (selected == null) {
                text.setText("");
            } else if (selected instanceof Leaf) {
                Leaf l = (Leaf) getLastSelectedPathComponent();
                String s = l.getContent();
                text.setText(s == null ? "" : s);
                text.setSelectionStart(0);
                text.setSelectionEnd(0);
            }
        }
    }

    private void expandLevel(JTree tree, TreePath parent, boolean expand, boolean alsoChildren, boolean alsoParent) {
        Leaf node = (Leaf) parent.getLastPathComponent();
        if (alsoChildren) {
            if (node.getChildren().size() >= 0) {
                for (Leaf e : node.getChildren()) {
                    TreePath path = parent.pathByAddingChild(e);
                    if (expand) {
                        tree.expandPath(path);
                    } else {
                        tree.collapsePath(path);
                    }
                }
            }
        }
        if (alsoParent) {
            if (expand) {
                tree.expandPath(parent);
            } else {
                tree.collapsePath(parent);
            }
        }
    }
}
