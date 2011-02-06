/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afro.xmltree.xmlrenderer;

import afro.xmltree.Leaf;
import afro.xmltree.LeafList;

/**
 *
 * @author dmn
 */
public class SimpleRenderer implements XmlRendererInterface {

    @Override
    public String render(Leaf root) {
        return renderImpl(root, "");
    }

    @Override
    public String render(LeafList list) {
        StringBuilder result = new StringBuilder();
        for (Leaf item : list) {
            result.append(render(item));
        }
        return result.toString();
    }

    private String renderImpl(Leaf root, String indent) {
        StringBuilder result = new StringBuilder();
        result.append(indent);
        String s = root.toString();
        result.append(s);
        result.append(CR);
        if (!root.getChildren().isEmpty()) {
            for (Leaf item : root.getChildren()) {
                result.append(renderImpl(item, TAB + indent));
            }
        }
        return result.toString();
    }
}
