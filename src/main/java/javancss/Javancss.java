package javancss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javancss.parser.JavaParser;
import javancss.parser.JavaParserInterface;

public class Javancss
{
	/**
	 * Measures metrics for a file and returns a metrics object
	 * @param path The file to measure
	 * @return The measured FileMetrics or null if an exception occurred
	 */
    public FileMetrics measure(String path) {
		try {
			Reader reader = newReader(new File(path));
			JavaParserInterface parser = new JavaParser(reader);
			FileMetrics metrics = new FileMetrics();
			metrics.filename = path;
        	parser.parse();
        	parser.collectFileMetrics(metrics);
			return metrics;
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			return null;
		}
    }

    private Reader newReader(File file) throws Exception {
        return new InputStreamReader((new FileInputStream(file)));
    }

}
