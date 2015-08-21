package com.example.xmlReader;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {
	private SAXParser parser = null;
	private XmlHandler handler = null;

	public XmlElement parse(String uri) throws ParserConfigurationException,
			SAXException, IOException {
		InputSource source = new InputSource(uri);
		return parse(source, "UTF-8");
	}

	/*
	 * public XmlElement parse(String uri, String encoding) throws
	 * ParserConfigurationException, SAXException, IOException { InputSource
	 * source = new InputSource(uri); return parse(source, encoding); }
	 */
	public XmlElement parse(InputSource source, String encoding)
			throws ParserConfigurationException, SAXException, IOException {
		if (parser == null) {
			parser = SAXParserFactory.newInstance().newSAXParser();
		}
		if (handler == null) {
			handler = new XmlHandler();
		}

		source.setEncoding(encoding);
		System.out.println("Encoding is done.................");

		parser.parse(source, handler);
		System.out.println("Parsing is done..................");
		return handler.getOutput();
	}
}
