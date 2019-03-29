package com.state.util.office;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * @author Yegor Kozlov
 */
public class XWPFTestDataSamples {

	private static final class NonSeekableInputStream extends InputStream {

        private final InputStream _is;

        public NonSeekableInputStream(InputStream is) {
            _is = is;
        }

        @Override
        public int read() throws IOException {
            return _is.read();
        }
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return _is.read(b, off, len);
        }
        @Override
        public boolean markSupported() {
            return false;
        }
        @Override
        public void close() throws IOException {
            _is.close();
        }
    }
	
    public static XWPFDocument openSampleDocument(String sampleName) throws IOException {
        //InputStream is = POIDataSamples.getDocumentInstance().openResourceAsStream(sampleName);
    	InputStream is = new NonSeekableInputStream(new FileInputStream(sampleName));
        return new XWPFDocument(is);
    }

    public static XWPFDocument writeOutAndReadBack(XWPFDocument doc) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        doc.write(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return new XWPFDocument(bais);
    }

    public static byte[] getImage(String filename) throws IOException {
        InputStream is = POIDataSamples.getDocumentInstance().openResourceAsStream(filename);
        try {
            return IOUtils.toByteArray(is);
        } finally {
            is.close();
        }
    }
}
