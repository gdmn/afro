package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;
import afro.xmltree.xmlparser.DefaultXmlParser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author dmn
 */
class FeedOpmlExtractor implements ExtractorInterface {

	private String url, feedName;
	private EntryHandler entryHandler;

	FeedOpmlExtractor(String feedUrl, EntryHandler entryHandler, String feedName) {
		this.url = feedUrl;
		this.entryHandler = entryHandler;
		this.feedName = feedName;
	}

	@Override
	public void extract() {
		try {
			Leaf root = new DefaultXmlParser(url).parse();
			ParseOPML opmlParser = new ParseOPML(root);
			for (String feed : opmlParser.getFeeds()) {
				try {
					FeedExtractorFactory factory = new FeedExtractorFactory(entryHandler, feed);
					Leaf feedRoot = new DefaultXmlParser(feed).parse();
					ExtractorInterface extractor = factory.getExtractor(feedRoot);
					if (extractor == null) {
						Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, "No extractor for " + feed);
					} else {
						extractor.extract();
					}
				} catch (ParserConfigurationException ex) {
					Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
				} catch (SAXException ex) {
					Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SAXException ex) {
			Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FeedOpmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
