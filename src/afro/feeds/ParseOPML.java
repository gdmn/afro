/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afro.feeds;

import afro.xmltree.Attribute;
import afro.xmltree.Leaf;
import afro.xmltree.LeafList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dmn
 */
public class ParseOPML  {

    private Leaf xml;

    public ParseOPML(Leaf rootElement) {
        this.xml = rootElement;
    }

    private void getUrls(LeafList list, LinkedList<String> result) {
        for (Leaf item : list) {
            Attribute attr = item.getAttribute("xmlUrl");
            if (attr != null) {
                result.add(attr.getValue());
            }
            if (!item.getChildren().isEmpty()) {
                getUrls(item.getChildren(), result);
            }
        }
    }

    public List<String> getFeeds() {
        LinkedList<String> result = new LinkedList<String>();
        if ("opml".equals(xml.getName())) {
            LeafList list = xml.getChildren("body");
            getUrls(list, result);
        }
        //String s = new SimpleRenderer().render(list);
        //System.out.println(s);
        return result;
    }
}
