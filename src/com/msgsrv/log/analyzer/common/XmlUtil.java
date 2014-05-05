package com.msgsrv.log.analyzer.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtil {

	public static String toXml(Object model) throws JAXBException, IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
		marshal(model, output);
		output.flush();
		return new String(output.toByteArray(), "UTF-8");
	}

	public static void marshal(Object model, OutputStream output) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(model.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(model, output);
	}

	public static <T> T parseXml(Class<T> clazz, String xml) throws JAXBException, IOException {
		byte[] buf = xml.getBytes("UTF-8");
		ByteArrayInputStream input = new ByteArrayInputStream(buf, 0, buf.length);
		return unmarshal(clazz, input);
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> clazz, InputStream input) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (T) jaxbUnmarshaller.unmarshal(input);
	}

	public static void saveXmlToFile(Object model, String filename) throws FileNotFoundException, JAXBException {
		FileOutputStream fos = new FileOutputStream(filename);
		marshal(model, fos);
	}

	public static void saveXmlToFile(Object model, File file) throws FileNotFoundException, JAXBException {
		FileOutputStream fos = new FileOutputStream(file);
		marshal(model, fos);
	}

	public static <T> T loadXmlFromFile(Class<T> clazz, String filename) throws FileNotFoundException, JAXBException {
		return unmarshal(clazz, new FileInputStream(filename));
	}

	public static <T> T loadXmlFromFile(Class<T> clazz, File file) throws FileNotFoundException, JAXBException {
		return unmarshal(clazz, new FileInputStream(file));
	}

	public static <T> T loadXmlFromFile(Class<T> clazz, InputStream is) throws JAXBException {
		return unmarshal(clazz, is);
	}
}
