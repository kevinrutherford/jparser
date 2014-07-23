package javancss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import javancss.parser.JavaParser;
import javancss.parser.JavaParserInterface;

public class Javancss
{
    private JavaParserInterface parser = null;
    private FileMetrics _fileMetrics = new FileMetrics();

    public Javancss(String[] argv) throws Exception
    {
        try {
            _measureRoot(argv[0]);
        } catch (Throwable pThrowable) {
            pThrowable.printStackTrace(System.err);
            return;
        }

        final PrintWriter pw = new PrintWriter(System.out);
        try {
            printFileStats(pw);
        } finally {
            pw.flush();
        }
    }

    private void _measureRoot(String path) throws Exception
    {
        Reader reader = newReader(new File(path));
        parser = new JavaParser(reader);
        parser.parse();
        parser.collectFileMetrics(_fileMetrics);
        _fileMetrics.filename = path;
    }

    private Reader newReader(File file) throws Exception {
        return new InputStreamReader((new FileInputStream(file)));
    }

    private void printFileStats(Writer w) throws Exception {
        FileMetrics fm = _fileMetrics;
        w.write("{\n");
        w.write("  \"filename\": \"" + fm.filename);
        w.write("\",\n  \"num_branches\": " + fm.num_branches);
        w.write(",\n  \"num_dependencies\": " + fm.num_dependencies);
        w.write(",\n  \"num_superclasses\": " + fm.num_superclasses);
        w.write("\n}\n");
    }

}
