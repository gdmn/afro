package afro;

import afro.feeds.Entry;
import afro.feeds.EntryHandler;
import afro.feeds.FeedExtractorFactory;
import afro.feeds.builder.BuildAtom;
import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;
import afro.xmltree.xmlparser.DefaultXmlParser;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author dmn
 */
public class MyChannel {

    private static final String[] FEEDS_URL = new String[]{
//        "http://twitter.com/statuses/user_timeline/48274520.rss",
//        "http://identi.ca/api/statuses/user_timeline/dmn.atom",
//        "http://dmn.jogger.pl/atom/short/100/",
//        "http://www.google.com/reader/public/atom/user%2F12890149063310591021%2Fstate%2Fcom.google%2Fstarred",
//        "http://dmn.posterous.com/rss.xml",
//		"http://devsite.pl/rss.xml",
//		"http://devsite.pl/index.html",
//		"/home/dmn/Pulpit/netvibes-short.htm",
		"/home/dmn/delicious-short.html",

//		"http://www.ppe.pl/index.php",
//		" http://monkeyisland.pl/feed/",
//		"/media/Home/dmn/Dropbox/Projects/src.dmn.bitbucket.org/output/rss.xml"
//		"/media/Home/dmn/Dropbox/Projects/src.dmn.bitbucket.org/output/index.html"
//		"/mnt/ram/c.xml",
	};

    public MyChannel() {
    }

    public void printResults() {
        for (Entry entry : myHandler.entries) {
            System.out.println("" + entry);
        }
    }

    public void makeAtom() {
        BuildAtom atom = new BuildAtom(myHandler.entries);
        System.out.println(atom.build());
    }

    public void initialize() {
        for (String feedUrl : FEEDS_URL) {
            try {
                Leaf root = new DefaultXmlParser(feedUrl).parse();
                FeedExtractorFactory factory = new FeedExtractorFactory(myHandler, feedUrl);
                ExtractorInterface extractor = factory.getExtractor(root);
                extractor.extract();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(MyChannel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(MyChannel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MyChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
		System.err.println("MyChannel.java:68");
        Collections.sort(myHandler.entries);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        MyChannel f = new MyChannel();
        f.initialize();
        // f.printResults();
        f.makeAtom();
    }

    private static class MyEntryHandler implements EntryHandler {

        LinkedList<Entry> entries;

        public MyEntryHandler() {
            entries = new LinkedList<Entry>();
        }

        @Override
        public void newEntry(Entry entry) {
            entries.add(entry);
        }
    }
    private MyEntryHandler myHandler = new MyEntryHandler();
}
