package javancss;

// import java.awt.event.WindowAdapter;
// import java.awt.event.WindowEvent;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.io.FileOutputStream;
// import java.io.InputStream;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.OutputStream;
// import java.io.OutputStreamWriter;
// import java.io.PrintWriter;
// import java.io.Reader;
// import java.io.Writer;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;

// import ccl.util.Exitable;
// import ccl.util.FileUtil;
// import ccl.util.Init;
// import ccl.util.Util;

// import javancss.parser.JavaParser;
// import javancss.parser.JavaParserInterface;
// import javancss.parser.JavaParserTokenManager;
// import javancss.parser.TokenMgrError;
// import javancss.parser.debug.JavaParserDebug;

public class Javancss
{
    private JavaParserInterface parser = null;
    private FileMetrics _fileMetrics = new FileMetrics();

    public Javancss(String[] argv) throws IOException
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

    private Reader newReader(File file) throws FileNotFoundException {
        return new InputStreamReader((new FileInputStream(file)));
    }

    private void printFileStats(Writer w) throws IOException {
        FileMetrics fm = _fileMetrics;
        w.write("{\n");
        w.write("  \"filename\": \"" + fm.filename);
        w.write("\",\n  \"num_branches\": " + fm.num_branches);
        w.write(",\n  \"num_dependencies\": " + fm.num_dependencies);
        w.write(",\n  \"num_superclasses\": " + fm.num_superclasses);
        w.write("\n}\n");
    }

}
