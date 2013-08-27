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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author dmn
 */
public class Mixer {

	LinkedList<Entry> entries = new LinkedList<Entry>();

	void makeAtom() {
		BuildAtom atom = new BuildAtom(entries);
		System.out.println(atom.build());
	}

	void parse(List<String> urls, EntryHandler handler) {
		for (String feedUrl : urls) {
			try {
				Leaf root = new DefaultXmlParser(feedUrl).parse();
				FeedExtractorFactory factory = new FeedExtractorFactory(handler, feedUrl);
				ExtractorInterface extractor = factory.getExtractor(root);
				extractor.extract();
			} catch (ParserConfigurationException ex) {
				Logger.getLogger(Mixer.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SAXException ex) {
				Logger.getLogger(Mixer.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(Mixer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(entries);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		if (args.length == 0) {

			System.out.println("Create one channel from list of feeds.\n"
					+ "Usage:\n"
					+ "\t" + Mixer.class.getSimpleName() + " href1 href2...");

			System.exit(1);
		}

		LinkedList<String> urls = new LinkedList<String>();
		for (String arg : args) {
			if (!arg.startsWith("-")) {
				urls.add(arg);
			}
		}
		final Mixer f = new Mixer();
		f.parse(urls, new EntryHandler() {
			@Override
			public void newEntry(Entry entry) {
				f.entries.add(entry);
			}
		});
		f.makeAtom();
	}
}
