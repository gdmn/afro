package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Attribute;
import afro.xmltree.Leaf;

/**
 *
 * @author dmn
 */
public class FeedAtomExtractor implements ExtractorInterface {

    protected Leaf xml;
    protected EntryHandler entryHandler;
    protected final String feedName;

    public FeedAtomExtractor(Leaf rootElement, EntryHandler entryHandler, String feedName) {
        this.xml = rootElement;
        this.entryHandler = entryHandler;
        this.feedName = feedName;
    }

	@Override
	public void extract() {
        for (Leaf entryLeaf : xml.getAll("entry")) {
            afro.feeds.Entry entry = new Entry();
            entry.setFeedName(feedName);

            Leaf titleLeaf = entryLeaf.getFirst("title");
            entry.setTitle(titleLeaf == null ? "" : titleLeaf.getContent());

            Leaf linkLeaf = entryLeaf.getFirst("link");
            Attribute linkHref = linkLeaf == null ? null : linkLeaf.getAttribute("href");
            entry.setHref(linkHref == null ? "" : linkHref.getValue());

            Leaf updatedLeaf = entryLeaf.getFirst("updated");
            if (updatedLeaf != null) {
                if (updatedLeaf.getContent().endsWith("Z")) {
                    entry.setUpdated(Entry.parseDate(updatedLeaf.getContent()));
                } else {
                    String d = updatedLeaf.getContent();
                    int p = d.lastIndexOf(':');
                    if (p == d.length()-3) {
                        d = d.substring(0, p)+d.substring(p+1);
                    }
                    entry.setUpdated(Entry.parseDate(d));
                }
            }

            Leaf contentLeaf = entryLeaf.getFirst("content");
            entry.setContent(contentLeaf == null ? null : contentLeaf.getContent());

            if (contentLeaf == null) {
                Leaf summaryLeaf = entryLeaf.getFirst("summary");
                entry.setContent(summaryLeaf == null ? null : summaryLeaf.getContent());
            }

            entryHandler.newEntry(entry);
        }
    }
}
