package afro.xmltree.xmlrenderer;

import afro.xmltree.Leaf;
import afro.xmltree.LeafList;

/**
 *
 * @author dmn
 */
public interface XmlRendererInterface {

    static final String CR = "\n";
    static final String OPEN = "<";
    static final String CLOSE = ">";
    static final String SLASH = "/";
    static final String EQ = "=";
    static final String SP = " ";
    static final String TAB = "\t";
    static final String QUOT = "\"";
    static final String CDATAOPEN = "<![CDATA[";
    static final String CDATACLOSE = "]]>";
//    static final String[] RAWOPEN= new String[] {CDATAOPEN, "<code>", "<pre>"};
//    static final String[] RAWCLOSE= new String[] {CDATACLOSE, "</code>", "</pre>"};
    static final String[] RAWOPEN= new String[] {CDATAOPEN};
    static final String[] RAWCLOSE= new String[] {CDATACLOSE};

    String render(Leaf root);
    String render(LeafList list);
}
