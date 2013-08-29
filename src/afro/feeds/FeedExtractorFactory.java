package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Attribute;
import afro.xmltree.Leaf;
import afro.xmltree.LeafList;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dmn
 */
public class FeedExtractorFactory {

	private EntryHandler entryHandler;
	private String feedName, feedUrl;

	public FeedExtractorFactory(EntryHandler entryHandler, String feedUrl) {
		this.entryHandler = entryHandler;
		this.feedUrl = feedUrl;
		this.feedName = getSimpleFeedName(feedUrl);
	}

	public FeedExtractorFactory(EntryHandler entryHandler) {
		this(entryHandler, null);
	}

	public ExtractorInterface getExtractor(Leaf feedRoot) throws MalformedURLException {
		String feedRootName = feedRoot.getName();
		if ("rss".equals(feedRootName.toLowerCase())) {
			return new FeedRssExtractor(feedRoot, entryHandler, feedName);
		}
		if ("rdf:rdf".equals(feedRootName.toLowerCase())) {
			return new FeedRdfExtractor(feedRoot, entryHandler, feedName);
		}
		if (feedRoot.getFirst("entry") != null) {
			return new FeedAtomExtractor(feedRoot, entryHandler, feedName);
		}
		if ("html".equals(feedRootName.toLowerCase())) {
			String url = findRssLink(feedRoot);
			if (url == null) {
				return null;
			}
			url = createRelativeLink(feedUrl, url);
			return new FeedFromHtmlExtractor(url, entryHandler, feedName);
		}
		if ("opml".equals(feedRootName.toLowerCase())) {
			FeedOpmlExtractor opmlExtractor = new FeedOpmlExtractor(feedUrl, entryHandler, feedName);
			return opmlExtractor;
		}
		return null;
	}

	protected String createRelativeLink(String baseUrl, String url) {
		if (url.contains(":/")) {
			return url;
		}
		if (baseUrl.contains(":/")) {
			URL u1, u2;
			try {
				u1 = new URL(baseUrl);
				u2 = new URL(u1, url);
				return u2.toString();
			} catch (MalformedURLException ex) {
				Logger.getLogger(FeedExtractorFactory.class.getName()).log(Level.SEVERE, null, ex);
				return null;
			}
		}
		if (url.startsWith("/")) {
			return url;

		}
		int lastIndex = baseUrl.lastIndexOf('/');
		if (lastIndex > 0) {
			return baseUrl.substring(0, lastIndex + 1) + url;
		}
		return null;
	}

	private String findRssLink(Leaf feedRoot) {
		Attribute attr;
		LeafList leaves;
		leaves = feedRoot.getChildren("head");
		leaves = leaves.selectByName("link");
		for (Leaf l : leaves) {
			attr = l.getAttribute("rel");
			if (attr != null && "alternate".equals(attr.getValue())) {
				attr = l.getAttribute("type");
				if (attr != null) {
					if ("application/atom+xml".equals(attr.getValue()) || "application/rss+xml".equals(attr.getValue())) {
						attr = l.getAttribute("href");
						if (attr != null) {
							String url = attr.getValue();
							if (feedUrl.equals(url)) {
								return null;
							}
							return url;
						}
					}
				}
			}
		}
		return null;
	}

	public static String getSimpleFeedName(String url) {
		int p1 = url.indexOf("://");
		int p2 = url.indexOf('/', p1 + 3);
		return url.substring(p1 + 3, p2);
	}
}
