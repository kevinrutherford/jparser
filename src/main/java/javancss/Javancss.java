package javancss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javancss.parser.JavaParser;
import javancss.parser.JavaParserInterface;

public class Javancss
{
    public FileMetrics measure(String path) throws Exception {
        Reader reader = newReader(new File(path));
        JavaParserInterface parser = new JavaParser(reader);
		FileMetrics metrics = new FileMetrics();
		metrics.filename = path;
		try {
        	parser.parse();
        	parser.collectFileMetrics(metrics);
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
		return metrics;
    }

    private Reader newReader(File file) throws Exception {
        return new InputStreamReader((new FileInputStream(file)));
    }

}
