package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author dmn
 */
public class FeedRssExtractor extends FeedAtomExtractor implements ExtractorInterface {

    private final static String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    public FeedRssExtractor(Leaf rootElement, EntryHandler entryHandler, String feedName) {
        super(rootElement, entryHandler, feedName);
    }

    @Override
	public void extract() {
        Leaf channelLeaf = xml.getFirst("channel");
        for (Leaf itemLeaf : channelLeaf.getAll("item")) {
            afro.feeds.Entry entry = new Entry();
            entry.setFeedName(feedName);

            Leaf titleLeaf = itemLeaf.getFirst("title");
            entry.setTitle(titleLeaf == null ? "" : titleLeaf.getContent());

            Leaf linkLeaf = itemLeaf.getFirst("link");
            entry.setHref(linkLeaf == null ? "" : linkLeaf.getContent());

            Leaf pubDateLeaf = itemLeaf.getFirst("pubDate");
            entry.setUpdated(pubDateLeaf == null ? null : Entry.parseDate(pubDateLeaf.getContent()));

            Leaf descriptionLeaf = itemLeaf.getFirst("description");
            entry.setContent(descriptionLeaf == null ? null : descriptionLeaf.getContent());

            entryHandler.newEntry(entry);
        }
    }
}
