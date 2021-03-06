package afro;

import afro.feeds.Entry;
import afro.feeds.EntryHandler;
import afro.feeds.FeedExtractorFactory;
import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;
import afro.xmltree.xmlparser.DefaultXmlParser;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author dmn
 */
public class HrefLister {

    public HrefLister() {
    }

    public void parse(List<String> urls, EntryHandler handler) {
        for (String feedUrl : urls) {
            try {
                Leaf root = new DefaultXmlParser(feedUrl).parse();
                FeedExtractorFactory factory = new FeedExtractorFactory(handler, feedUrl);
                ExtractorInterface extractor = factory.getExtractor(root);
                extractor.extract();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(HrefLister.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(HrefLister.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(HrefLister.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		args = new String[] {"/mnt/ram/index.html"};
		if (args.length == 0) {
/*TODO: format: https://feeds.pinboard.in/rss/u:dmn/
Exception in thread "main" java.lang.StringIndexOutOfBoundsException: String index out of range: -3
	at java.lang.String.substring(String.java:1911)
	at afro.feeds.FeedExtractorFactory.getSimpleFeedName(FeedExtractorFactory.java:109)
	at afro.feeds.FeedExtractorFactory.<init>(FeedExtractorFactory.java:24)
	at afro.HrefLister.parse(HrefLister.java:30)
	at afro.HrefLister.main(HrefLister.java:60)*/

			System.out.println("Lists hrefs from feeds.\n"
					+ "Usage:\n"
					+ "\t"+HrefLister.class.getSimpleName()+" href1 href2...");

			System.exit(1);
		}

		LinkedList<String> urls = new LinkedList<String>();
		for (String arg : args) {
			if (!arg.startsWith("-")) {
				urls.add(arg);
			}
		}
        HrefLister f = new HrefLister();
        f.parse(urls, new MyEntryHandler());
    }

    private static class MyEntryHandler implements EntryHandler {
        public MyEntryHandler() {
        }

        @Override
        public void newEntry(Entry entry) {
            System.out.println(entry.getHref());
        }
    }
}
