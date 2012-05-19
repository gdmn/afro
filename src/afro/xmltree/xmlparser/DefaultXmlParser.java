package afro.xmltree.xmlparser;

import afro.xmltree.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class DefaultXmlParser extends DefaultHandler {

	private String sourceUrl;
	private Leaf root;
	private Leaf current;
	private InputStream inputStream;

	public DefaultXmlParser(String url) {
		this.sourceUrl = url;
	}

	public DefaultXmlParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public DefaultXmlParser(InputSource in) {
		this.inputStream = in.getByteStream();
	}

	public static String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

	public static InputStream convertStringToStream(String string) {
		return new ByteArrayInputStream(string.getBytes());
	}

	@SuppressWarnings(value = "CallToThreadDumpStack")
	public Leaf parse() throws ParserConfigurationException, SAXException, IOException {
//        root = new Leaf(null);
//        current = root;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);

		SAXParser reader = factory.newSAXParser();
		reader.setProperty("http://xml.org/sax/properties/lexical-handler", myLexicalHandler);

		if (sourceUrl != null && sourceUrl.indexOf(":/") < 1) {
			//jeśli nie jest adresem internetowym
			System.err.println("Parsing file " + sourceUrl);
			reader.parse(new File(sourceUrl), this);
		} else {
			if (inputStream == null) {
				//w przypadku gdy http://....
				System.err.println("Parsing url " + sourceUrl);
				URL adres = new URL(sourceUrl);
				URLConnection connection = adres.openConnection();
				inputStream = connection.getInputStream();
				String buf = convertStreamToString(inputStream);
				inputStream = convertStringToStream(buf);
				reader.parse(inputStream, this);
			} else {
				reader.parse(inputStream, this);
			}
		}
		return root;
//        } catch (SAXParseException spe) {
//            System.out.println("** Parsing error" + ", line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
//            System.out.println("   " + spe.getMessage());
//            Exception x = spe;
//            if (spe.getException() != null) {
//                x = spe.getException();
//            }
//            x.printStackTrace();
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
//        } finally {
//        }
	}

	/////////////////////////////////////////////////////////////
	// SAX
	@Override
	public void setDocumentLocator(Locator l) {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}
	StringBuilder charactersBuffer;

	@Override
	public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
		flushCharacters();
		String eName = lName;
		if ("".equals(eName)) {
			eName = qName;
		}
		Leaf newElement = new Leaf(eName);
		if (root == null) {
			current = newElement;
			root = current;
		} else {
			current.addChild(newElement);
			current = newElement;
		}
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getLocalName(i);
				if ("".equals(aName)) {
					aName = attrs.getQName(i);
				}
				Attribute attribute = new Attribute(aName, attrs.getValue(i));
				current.addAttribute(attribute);
			}
		}
	}

	@Override
	public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
		flushCharacters();
		current = current.getParent();
	}

	void flushCharacters() {
		if (charactersBuffer != null && charactersBuffer.length() > 0 && !charactersBuffer.toString().trim().isEmpty()) {
			Leaf text = new Leaf(null);
			text.setValue(charactersBuffer.toString().trim());
			current.addChild(text);
		}
		charactersBuffer = null;
	}

	void appendCharacters(String s) {
		if (s != null && !s.equals("")) {
			if (charactersBuffer == null) {
				charactersBuffer = new StringBuilder();
			}
			charactersBuffer.append(s);
//            String currentValue = current.getValue();
//            if (currentValue == null) {
//                current.setValue(s);
//            } else {
//                current.setValue(currentValue + s);
//            }
		}
	}

	@Override
	public void characters(char[] buf, int offset, int len) throws SAXException {
		String s = new String(buf, offset, len);
		appendCharacters(s);
	}

	@Override
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}

	@Override
	public void warning(SAXParseException err) throws SAXParseException {
		System.err.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
		System.err.println("   " + err.getMessage());
	}

	@Override
	public org.xml.sax.InputSource resolveEntity(String publicId, String systemId)
			throws org.xml.sax.SAXException, java.io.IOException {
		//System.err.println("Ignoring: " + publicId + ", " + systemId);
		return new org.xml.sax.InputSource(new java.io.StringReader(""));
	}
	LexicalHandler myLexicalHandler = new LexicalHandler() {

		@Override
		public void startDTD(String name, String publicId, String systemId) throws SAXException {
		}

		@Override
		public void endDTD() throws SAXException {
		}

		@Override
		public void startEntity(String name) throws SAXException {
		}

		@Override
		public void endEntity(String name) throws SAXException {
		}

		@Override
		public void startCDATA() throws SAXException {
			appendCharacters("<![CDATA[");
		}

		@Override
		public void endCDATA() throws SAXException {
			appendCharacters("]]>");
		}

		@Override
		public void comment(char[] ch, int start, int length) throws SAXException {
		}
	};
}
