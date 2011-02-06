/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;

/**
 *
 * @author dmn
 */
public class FeedExtractorFactory {
    private EntryHandler entryHandler;
    private String feedName;

    public FeedExtractorFactory(EntryHandler entryHandler, String feedName) {
        this.entryHandler = entryHandler;
        this.feedName = feedName;
    }

    public FeedExtractorFactory(EntryHandler entryHandler) {
        this(entryHandler, null);
    }

    public ExtractorInterface getExtractor(Leaf feedRoot) {
        String feedRootName = feedRoot.getName();
        if ("rss".equals(feedRootName.toLowerCase())) {
            return new FeedRssExtractor(feedRoot, entryHandler, feedName);
        }
        if (feedRoot.getFirst("entry") != null) {
            return new FeedAtomExtractor(feedRoot, entryHandler, feedName);
        }
        return null;
    }
}
