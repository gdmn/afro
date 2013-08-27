package afro.feeds.builder;

import afro.xmltree.Leaf;
import afro.xmltree.xmlrenderer.XmlRenderer;
import java.util.List;
import afro.feeds.Entry;
import java.util.Date;

/**
 *
 * @author dmn
 */
public class BuildAtom {

    private List<Entry> entries;
    private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public BuildAtom(List<Entry> entries) {
        this.entries = entries;
    }

    public String build() {
        Leaf root = new Leaf("feed");
        root.addAttribute("xmlns", "http://www.w3.org/2005/Atom");
        Leaf dataLeaf = new Leaf("generator");
        dataLeaf.setValue("afro 0.2");
        root.addChild(dataLeaf);
        dataLeaf = new Leaf("title");
        dataLeaf.setValue("afro feed");
        root.addChild(dataLeaf);
        dataLeaf = new Leaf("updated");
        dataLeaf.setValue(Entry.dateToString(DATE_FORMAT, new Date()));
        root.addChild(dataLeaf);
        for (Entry entry : entries) {
            Leaf entryLeaf = new Leaf("entry");
            root.addChild(entryLeaf);
            Leaf titleLeaf = new Leaf("title");
            Leaf linkLeaf = new Leaf("link");
            Leaf updatedLeaf = new Leaf("updated");
            Leaf contentLeaf = new Leaf("content");
            contentLeaf.addAttribute("type", "html");
            entryLeaf.addChild(titleLeaf);
            entryLeaf.addChild(updatedLeaf);
            entryLeaf.addChild(linkLeaf);
            entryLeaf.addChild(contentLeaf);

            titleLeaf.setValue(entry.getTitle());
            linkLeaf.addAttribute("href", entry.getHref());
            updatedLeaf.setValue(Entry.dateToString(DATE_FORMAT, entry.getUpdated()));
            contentLeaf.setValue(entry.getContent());
        }
        XmlRenderer renderer = new XmlRenderer();
        return renderer.render(root);
    }
}
