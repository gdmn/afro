package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;

/**
 *
 * @author dmn
 */
public class FeedRdfExtractor extends FeedAtomExtractor implements ExtractorInterface {

    public FeedRdfExtractor(Leaf rootElement, EntryHandler entryHandler, String feedName) {
        super(rootElement, entryHandler, feedName);
    }

    @Override
	public void extract() {
        for (Leaf itemLeaf : xml.getAll("item")) {
            afro.feeds.Entry entry = new Entry();
            entry.setFeedName(feedName);

            Leaf titleLeaf = itemLeaf.getFirst("title");
            entry.setTitle(titleLeaf == null ? "" : titleLeaf.getContent());

            Leaf linkLeaf = itemLeaf.getFirst("link");
            entry.setHref(linkLeaf == null ? "" : linkLeaf.getContent());

            Leaf pubDateLeaf = itemLeaf.getFirst("dc:date");
            entry.setUpdated(pubDateLeaf == null ? null : Entry.parseDate(pubDateLeaf.getContent()));

            Leaf descriptionLeaf = itemLeaf.getFirst("description");
            entry.setContent(descriptionLeaf == null ? null : descriptionLeaf.getContent());

            entryHandler.newEntry(entry);
        }
    }
}
