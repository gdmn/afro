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
class FeedFromHtmlExtractor implements ExtractorInterface {

	private String url, feedName;
	private EntryHandler entryHandler;

	public FeedFromHtmlExtractor(String url, EntryHandler entryHandler, String feedName) {
		this.url = url;
		this.entryHandler = entryHandler;
		this.feedName = feedName;
	}

	@Override
	public void extract() {
		try {
			Leaf root = new DefaultXmlParser(url).parse();
			FeedExtractorFactory factory = new FeedExtractorFactory(entryHandler, url);
			ExtractorInterface extractor = factory.getExtractor(root);
			if (extractor == null) {
				Logger.getLogger(FeedFromHtmlExtractor.class.getName()).log(Level.SEVERE, "No extractor for " + url);
			} else {
				extractor.extract();
			}
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(FeedFromHtmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SAXException ex) {
			Logger.getLogger(FeedFromHtmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FeedFromHtmlExtractor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
