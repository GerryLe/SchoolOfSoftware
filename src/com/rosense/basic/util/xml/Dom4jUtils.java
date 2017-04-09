package com.rosense.basic.util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jUtils {
	public static org.dom4j.Document newXmlDocument(final String rootEleText) {
		final org.dom4j.Document doc = org.dom4j.DocumentHelper.createDocument();
		final org.dom4j.Element root = doc.addElement(rootEleText);
		doc.setRootElement(root);
		return doc;
	}

	public static org.dom4j.Document readDom4jXmlDocument(final InputStream is) throws Exception {
		final SAXReader reader = new SAXReader();
		return reader.read(is);
	}

	public static void saveDom4jXmlDocument(final String filename, final org.dom4j.Document document) throws Exception {
		final XMLWriter write = new XMLWriter(new FileOutputStream(filename));
		write.write(document);
		write.close();
	}

	public static void saveDom4jXmlDocument(final OutputStream os, final org.dom4j.Document document) throws Exception {
		final XMLWriter write = new XMLWriter(os);
		write.write(document);
		write.close();
	}

	public static Document createDocument(final InputStream is) throws DocumentException {
		return createDocument(is, false);
	}

	public static Document createDocument(final InputStream is, final boolean validate) throws DocumentException {
		if (is == null) {
			return createEmptyDocument();
		}
		final SAXReader saxReader = new SAXReader();
		saxReader.setValidation(validate);
		saxReader.setEntityResolver(new MyEntityResolver());
		final Document document = saxReader.read(is);
		if (document == null) {
			return createEmptyDocument();
		}
		return document;
	}

	/**
	 * 通过文件格式创建文档对象
	 */
	public static Document createDocument(final File file) throws DocumentException, MalformedURLException {
		return createDocument(file, false);
	}

	public static Document createDocument(final File file, final boolean validate) throws DocumentException, MalformedURLException {
		if (file == null || !file.exists()) {
			return createEmptyDocument();
		}
		final SAXReader saxReader = new SAXReader();
		saxReader.setValidation(validate);
		final Document document = saxReader.read(file);
		if (document == null) {
			return createEmptyDocument();
		}
		return document;
	}

	public static Document createEmptyDocument() {
		return DocumentHelper.createDocument();
	}

	/**
	 * 获取文档对象的Root元素
	 */
	public static Element getRootElement(final InputStream is) throws DocumentException {
		return getRootElement(is, false);
	}

	public static Element getRootElement(final InputStream is, final boolean validate) throws DocumentException {
		final Document document = createDocument(is, validate);
		return document.getRootElement();
	}

	public static Element getRootElement(final File file) throws DocumentException, MalformedURLException {
		return getRootElement(file, false);
	}

	public static Element getRootElement(final File file, final boolean validate) throws DocumentException, MalformedURLException {
		final Document document = createDocument(file, validate);
		return document.getRootElement();
	}

	/**
	 * 获取一个元素的,指定名字的子元素集合
	 */
	public static Iterator<?> getChildElements(final Element parentElement, final String elementName) {
		return parentElement.elementIterator(elementName);
	}

	public static List<Element> getChildElementList(final Element parentElement, final String elementName) {
		return parentElement.elements(elementName);
	}

	public static Element getSpecialElement(final Element parentElement, final String childElementName, final String childElementAttributeName,
			final String childElementAttributeValue) {
		final List<Element> attrElements = Dom4jUtils.getChildElementList(parentElement, childElementName);
		Attribute attribute;
		for (final Element attrEle : attrElements) {
			attribute = attrEle.attribute(childElementAttributeName);
			if (childElementAttributeValue.equalsIgnoreCase(attribute.getValue())) {
				return attrEle;
			}
		}
		return null;
	}

	/**
	 * 获取一个元素的属性集合
	 */
	@SuppressWarnings("unchecked")
	public static Iterator getElementAttributes(final Element element) {
		return element.attributeIterator();
	}

	/**
	 * 获取一个元素的指定属性名的属性对象
	 */
	@SuppressWarnings("unchecked")
	public static Attribute getAttributeByName(final Element element, final String attributeName) {
		Attribute attribute;
		for (final Iterator iter = getElementAttributes(element); iter.hasNext();) {
			attribute = (Attribute) iter.next();
			if (attribute.getName().equals(attributeName)) {
				return attribute;
			}
		}
		return null;
	}

	/**
	 * 通过属性名获得属性对应的值
	 */
	public static String getAttributeValueByName(final Element element, final String attributeName) {
		final Attribute attribute = getAttributeByName(element, attributeName);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public static String getAttributeValue(final Attribute attribute) {
		return attribute.getValue();
	}

	public static Element getElementByName(final Element tEle, final String name) {
		return tEle.element(name);
	}

	public static String getElementValue(final Element tEle) {
		if (null == tEle)
			return "";
		return tEle.getTextTrim() == null ? "" : tEle.getTextTrim();
	}

	public static String getElementAttributeValue(final Element ele, final String attName) {
		if (null == ele)
			return "";
		return ele.attributeValue(attName) == null ? "" : ele.attributeValue(attName);
	}

	public static Element addRootElement(final Document doc, final String rootEleName) {
		final Element root = doc.addElement(rootEleName);
		doc.setRootElement(root);
		return root;
	}

	public static Element addElement(final Element parentElement, final String eleName) {
		return parentElement.addElement(eleName);
	}

	public static Element addAttribute(final Element element, final String attName, final String attValue) {
		element.addAttribute(attName, attValue);
		return element;
	}

	public static final void writeXml(final Document document, final String outFile) throws Exception {
		writeXml(document, new File(outFile));
	}

	public static final void writeXml(final Document document, final File outFile) throws Exception {
		if (!outFile.getParentFile().exists())
			outFile.getParentFile().mkdirs();
		writeXml(document, new FileOutputStream(outFile));
	}

	public static final void writeXml(final Document document, final OutputStream oStream) throws Exception {
		try {
			final OutputFormat of = OutputFormat.createPrettyPrint();
			of.setIndent(true);
			of.setEncoding("GB2312");
			of.setIndent("\t");
			final XMLWriter out = new XMLWriter(oStream, of);
			out.write(document);
			out.close();
			oStream.close();
		} catch (final Exception e) {

		} finally {
			oStream.close();
		}
	}

	public static final void setElementText(final Element parentElement, final String elementName, final String elementText) {
		if (null == elementText)
			return;
		Element element = parentElement.element(elementName);
		if (null == element)
			element = parentElement.addElement(elementName);
		element.setText(elementText);
	}
}

