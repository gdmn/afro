package afro.feeds;

import afro.xmlextractor.ExtractorInterface;
import afro.xmltree.Leaf;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dmn
 */
public class FeedExtractorFactoryTest {


	/**
	 * Test of createRelativeLink method, of class FeedExtractorFactory.
	 */
	@Test
	public void testCreateRelativeLink() {
		testRelativeLink("/mnt/ram/a.html", "x.xml", "/mnt/ram/x.xml");
		testRelativeLink("/mnt/ram/a.html", "/x.xml", "/x.xml");
		testRelativeLink("file:/mnt/ram/a.html", "x.xml", "file:/mnt/ram/x.xml");
		testRelativeLink("file:/mnt/ram/a.html", "/x.xml", "file:/x.xml");
		testRelativeLink("http://devsite.pl/a.html", "x.xml", "http://devsite.pl/x.xml");
		testRelativeLink("http://devsite.pl/a.html", "/x.xml", "http://devsite.pl/x.xml");
	}

	public void testRelativeLink(String baseUrl, String url, String expected) {
		FeedExtractorFactory instance = new FeedExtractorFactory(null, baseUrl);
		String result = instance.createRelativeLink(baseUrl, url);
		assertEquals(expected, result);
	}

}
