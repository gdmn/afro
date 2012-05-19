package afro;

import afro.feeds.ParseOPML;
import afro.xmltree.*;
import afro.xmltree.xmlparser.*;
import afro.xmltree.xmlrenderer.XmlRenderer;
import afro.xmltree.xmlrenderer.SimpleRenderer;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author dmn
 */
public class Tests {

    BufferedWriter out;

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        // TODO code application logic here
        new Tests();
    }
    private static final String DIVIDER = "\n\n\n-------------------------------------\n\n\n\n";
    private static final String FILE_RSS = "file:///home/dmn/Pulpit/joggerrss.xml";
    private static final String FILE_ATOM = "file:///home/dmn/Pulpit/joggeratom.xml";
    private static final String FILE_OPML = "file:///home/dmn/Pulpit/opml.xml";

    class TestFeedData {

        String url;
        String xml;
        String compare;
        String before;
        String after;
        String simple;
    }

    private static boolean testFeedImpl(TestFeedData data) throws ParserConfigurationException, SAXException, IOException {
        String renderedSimple, renderedXml;
        DefaultXmlParser parser;
        Leaf rootXmlElement = null;
        parser = new DefaultXmlParser(data.url);
//        try {
        rootXmlElement = parser.parse();
//        } catch (Exception e) {
//        }
        if (rootXmlElement == null) {
            return false;
        }

        renderedSimple = new SimpleRenderer().render(rootXmlElement);
        data.before = renderedSimple;
        data.simple = renderedSimple;

        renderedXml = new XmlRenderer().render(rootXmlElement);
        data.xml = renderedXml;
        data.before = renderedXml;
        // COMPARE
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(renderedXml.getBytes("UTF-8"));
            StringReader reader = new StringReader(renderedXml);
            InputSource in = new InputSource(reader);
            parser = new DefaultXmlParser(is);
            rootXmlElement = parser.parse();
            String simpleFromXml = new SimpleRenderer().render(rootXmlElement);
            data.after = simpleFromXml;
            String xmlFromXml = new XmlRenderer().render(rootXmlElement);
            data.after = xmlFromXml;
            if (data.before.equals(data.after)) {
//                println("COMPARE OK");
                return true;
            } else {
                String[] simpleXmlSplitted = data.before.split("\n");
                String[] simpleFromXmlSplitted = data.after.split("\n");
                data.compare = new String();
                if (simpleXmlSplitted.length == simpleFromXmlSplitted.length) {
                    for (int i = 0; i < simpleFromXmlSplitted.length; i++) {
                        if (!simpleFromXmlSplitted[i].trim().equals(simpleXmlSplitted[i].trim())) {
                            data.compare = data.compare + "\n"
                                    + i + "#1: " + simpleXmlSplitted[i] + "\n"
                                    + i + "#2: " + simpleFromXmlSplitted[i];
                        }
                    }
                    data.compare = data.compare + "END OF LIST OF CHANGES\n";
                }
                data.compare = data.compare + "\n" + "simpleXmlSplitted.length = " + simpleXmlSplitted.length + "\n"
                        + "simpleFromXmlSplitted.length = " + simpleFromXmlSplitted.length + "\n"
                        + "COMPARE ERROR";
                return false;
            }
        } catch (UnsupportedEncodingException ex) {
        }
        return false;
    }

    void println(String s) {
        synchronized (out) {
            try {
                out.append(s);
                out.newLine();
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void testFeed(String url, boolean fullOutput) {

        StringBuilder out = new StringBuilder();
        out.append("-------------------------- ");
        out.append(url).append(": ").append('\n');
        boolean finished = false;
        boolean testResult = false;
        Exception exception = null;
        TestFeedData data = new TestFeedData();
        data.url = url;
        try {
            testResult = testFeedImpl(data);
            out.append(testResult).append('\n');
            finished = true;
        } catch (SAXParseException spe) {
            exception = spe;
            out.append("** Parsing error" + ", line ").append(spe.getLineNumber()).append(", uri ").append(spe.getSystemId());
            out.append('\n').append("   ").append(spe.getMessage()).append('\n');
            Exception x = spe;
            if (spe.getException() != null) {
                x = spe.getException();
            }
            //x.printStackTrace();
//        } catch (SAXException sxe) {
//            Exception x = sxe;
//            if (sxe.getException() != null) {
//                x = sxe.getException();
//            }
//            x.printStackTrace();
//        } catch (ParserConfigurationException pce) {
//            pce.printStackTrace();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
        } catch (Exception ex) {
            out.append(ex.toString()).append('\n');
        } finally {
            if (!testResult || !finished) {
                //if (exception != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                if (data.compare != null) {
                    out.append(data.compare).append('\n');
                }
                if (data.xml != null) {
//                    out.append(data.xml).append('\n');
                }
                if (fullOutput) {
                    out.append(DIVIDER).append("before:\n").append(data.before).append('\n');
                    out.append(DIVIDER).append("after:\n").append(data.after).append('\n');
                }

            } else {
//                    out.append(data.xml).append('\n');
                out.append(data.simple).append('\n');
            }
            println(out.toString());
        }
    }

    public Tests() {
        try {
            out = new BufferedWriter(new FileWriter("/home/dmn/Pulpit/characteroutput.txt"));
        } catch (IOException ex) {
        }
        DefaultXmlParser parser;
        Leaf rootXmlElement;
        String atomSimple, atomXml, rssSimple, rssXml, simpleFromXml;
        final List<String> urls;
        Leaf opmlRoot = null;
        try {
            opmlRoot = new DefaultXmlParser(FILE_OPML).parse();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
//            println(new RenderToXml().render(opmlRoot.getChildren()));
//            println(DIVIDER);
//            println(new SimpleRenderer().render(opmlRoot.getChildren()));
//            println(DIVIDER);
//            println(new SimpleRenderer().render(opmlRoot.getChildren("body")));
//            println(DIVIDER);
//            println(new RenderToXml().render(opmlRoot.getChildren("body")));
//            println(DIVIDER);
        ParseOPML opmlParser = new ParseOPML(opmlRoot);
        urls = opmlParser.getFeeds();
//        for (String url : urls) {
//            println("> " + url);
//        }
        for (String feed1 : new String[]{FILE_OPML, FILE_ATOM, FILE_RSS}) {
            testFeed(feed1, true);
        }
System.exit(0);
        //testFeed("http://consoleo.pl/index.php/magazyn.feed?type=rss", true);
//        testFeed("/home/dmn/Pulpit/meat.xml", true); System.exit(0);
        int threadCount = 24;
        int delta = (urls.size() / threadCount) + 1;
        for (int t = 0; t < threadCount; t++) {
            int imin = t * delta;
            int imax = imin + delta;
            if (imax > urls.size()) {
                imax = urls.size();
            }
            Runnable k = new MyRunnable(urls, imin, imax);
            Thread thread = new Thread(k);
            thread.start();
        }

    }
    static final Object syncObject = new Object();
    static int feedprocessedcounter = 0;

    class MyRunnable implements Runnable {

        int imin, imax;
        List<String> urls;

        public MyRunnable(List<String> urls, int imin, int imax) {
            this.urls = urls;
            this.imin = imin;
            this.imax = imax;
        }

        public void run() {
            for (int i = imin; i < imax; i++) {
                String feed2 = urls.get(i);
                synchronized (syncObject) {
                    feedprocessedcounter++;
                }
                System.out.println((feedprocessedcounter) + "/" + urls.size() + " " + feed2);
                testFeed(feed2, false);
            }
        }
    }
}
