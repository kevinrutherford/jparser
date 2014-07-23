package javancss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javancss.parser.JavaParser;
import javancss.parser.JavaParserInterface;

public class Javancss
{
    private FileMetrics _fileMetrics = new FileMetrics();

    public Javancss(String[] argv) throws Exception
    {
        try {
            measure(argv[0]);
            printFileStats();
        } catch (Throwable pThrowable) {
            pThrowable.printStackTrace(System.err);
            return;
        }
    }

    private void measure(String path) throws Exception
    {
        Reader reader = newReader(new File(path));
        JavaParserInterface parser = new JavaParser(reader);
        parser.parse();
        parser.collectFileMetrics(_fileMetrics);
        _fileMetrics.filename = path;
    }

    private Reader newReader(File file) throws Exception {
        return new InputStreamReader((new FileInputStream(file)));
    }

    private void printFileStats() throws Exception {
        FileMetrics fm = _fileMetrics;
        System.out.println("{");
        System.out.println("  \"filename\": \"" + fm.filename + "\",");
        System.out.println("  \"num_branches\": " + fm.num_branches + ",");
        System.out.println("  \"num_dependencies\": " + fm.num_dependencies + ",");
        System.out.println("  \"num_superclasses\": " + fm.num_superclasses);
        System.out.println("}");
        System.out.flush();
    }

}
