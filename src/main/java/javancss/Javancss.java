package javancss;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccl.util.Exitable;
import ccl.util.FileUtil;
import ccl.util.Init;
import ccl.util.Util;

import javancss.parser.JavaParser;
import javancss.parser.JavaParserInterface;
import javancss.parser.JavaParserTokenManager;
import javancss.parser.TokenMgrError;
import javancss.parser.debug.JavaParserDebug;

public class Javancss implements Exitable
{
    private static final String S_INIT__FILE_CONTENT =
        "[Init]\n" +
        "Author=Chr. Clemens Lee\n" +
        "\n" +
        "[Help]\n"+
        "; Please do not edit the Help section\n"+
        "HelpUsage=@srcfiles.txt | *.java | <stdin>\n" +
        "Options=ncss,package,object,function,all,out,recursive,encoding\n" +
        "ncss=b,o,Counts the program NCSS (default).\n" +
        "package=b,o,Assembles a statistic on package level.\n" +
        "object=b,o,Counts the object NCSS.\n" +
        "function=b,o,Counts the function NCSS.\n" +
        "all=b,o,The same as '-function -object -package'.\n" +
        "out=s,o,Output file name. By default output goes to standard out.\n"+
        "recursive=b,o,Recurse to subdirs.\n" +
        "encoding=s,o,Encoding used while reading source files (default: platform encoding).\n" +
        "\n" +
        "[Colors]\n" +
        "UseSystemColors=true\n";
    
    private boolean _bExit = false;

    private List<File> _vJavaSourceFiles = null;

    private String _sErrorMessage = null;
    private Throwable _thrwError = null;

    private JavaParserInterface _pJavaParser = null;
    private int _ncss = 0;
    private int _loc = 0;
    private FileMetrics _fileMetrics = new FileMetrics();
    private List<FunctionMetric> _vFunctionMetrics = new ArrayList<FunctionMetric>();
    private List<ObjectMetric> _vObjectMetrics = new ArrayList<ObjectMetric>();
    private List<PackageMetric> _vPackageMetrics = null;
    private List<Object[]> _vImports = null;
    private Map<String,PackageMetric> _htPackages = null;
    private Object[] _aoPackage = null;

    /**
     * Just used for parseImports.
     */
    private File _sJavaSourceFile = null;

    private Reader createSourceReader( File sSourceFile_ )
    {
        try
        {
            return newReader( sSourceFile_ );
        }
        catch ( IOException pIOException )
        {
            if ( Util.isEmpty( _sErrorMessage ) )
            {
                _sErrorMessage = "";
            }
            else
            {
                _sErrorMessage += "\n";
            }
            _sErrorMessage += "File not found: " + sSourceFile_.getAbsolutePath();
            _thrwError = pIOException;

            return null;
        }
    }

    private void _measureSource( File sSourceFile_ ) throws IOException, Exception, Error
    {
        _fileMetrics.filename = sSourceFile_.getPath();
        Reader reader = null;

        // opens the file
        try
        {
            reader = newReader( sSourceFile_ );
        }
        catch ( IOException pIOException )
        {
            if ( Util.isEmpty( _sErrorMessage ) )
            {
                _sErrorMessage = "";
            }
            else
            {
                _sErrorMessage += "\n";
            }
            _sErrorMessage += "File not found: " + sSourceFile_.getAbsolutePath();
            _thrwError = pIOException;

            throw pIOException;
        }

        String sTempErrorMessage = _sErrorMessage;
        try
        {
            // the same method but with a Reader
            _measureSource( reader );
        }
        catch ( Exception pParseException )
        {
            if ( sTempErrorMessage == null )
            {
                sTempErrorMessage = "";
            }
            sTempErrorMessage += "ParseException in " + sSourceFile_.getAbsolutePath() +
                   "\nLast useful checkpoint: \"" + _pJavaParser.getLastFunction() + "\"\n";
            sTempErrorMessage += pParseException.getMessage() + "\n";

            _sErrorMessage = sTempErrorMessage;
            _thrwError = pParseException;

            throw pParseException;
        }
        catch ( Error pTokenMgrError )
        {
            if ( sTempErrorMessage == null )
            {
                sTempErrorMessage = "";
            }
            sTempErrorMessage += "TokenMgrError in " + sSourceFile_.getAbsolutePath() +
                   "\n" + pTokenMgrError.getMessage() + "\n";
            _sErrorMessage = sTempErrorMessage;
            _thrwError = pTokenMgrError;

            throw pTokenMgrError;
        }
    }

    private void _measureSource( Reader reader ) throws IOException, Exception, Error
    {
        try
        {
            _pJavaParser = new JavaParser(reader);
            _pJavaParser.parse();
            _pJavaParser.collectFileMetrics(_fileMetrics);
        } catch ( Exception pParseException ) {
            if ( _sErrorMessage == null )
            {
                _sErrorMessage = "";
            }
            _sErrorMessage += "ParseException in STDIN";
            if ( _pJavaParser != null )
            {
                _sErrorMessage += "\nLast useful checkpoint: \"" + _pJavaParser.getLastFunction() + "\"\n";
            }
            _sErrorMessage += pParseException.getMessage() + "\n";
            _thrwError = pParseException;

            throw pParseException;
        }
        catch ( Error pTokenMgrError )
        {
            if ( _sErrorMessage == null )
            {
                _sErrorMessage = "";
            }
            _sErrorMessage += "TokenMgrError in STDIN\n";
            _sErrorMessage += pTokenMgrError.getMessage() + "\n";
            _thrwError = pTokenMgrError;

            throw pTokenMgrError;
        }
    }

    private void _measureFiles( List<File> vJavaSourceFiles_ ) throws TokenMgrError
    {
        for ( File file : vJavaSourceFiles_ )
        {
            try
            {
                _measureSource( file );
            }
            catch ( Throwable pThrowable )
            {
                // hmm, do nothing? Use getLastError() or so to check for details.
                // error details have been written into lastError
            }
        }
    }

    /**
     * If arguments were provided, they are used, otherwise
     * the input stream is used.
     */
    private void _measureRoot(Reader reader) throws IOException, Exception, Error
    {
        _htPackages = new HashMap<String, PackageMetric>();
        _measureFiles( _vJavaSourceFiles );

        _vPackageMetrics = new ArrayList<PackageMetric>();
        for ( PackageMetric pkm : _htPackages.values() )
        {
            _vPackageMetrics.add( pkm );
        }
        Collections.sort( _vPackageMetrics );
    }

    private List<File> findFiles( List<String> filenames, boolean recursive ) throws IOException {
        if ( filenames.size() == 0 )
            return null;

        Set<String> _processedAtFiles = new HashSet<String>();
        List<File> newFiles = new ArrayList<File>();
        for ( String filename : filenames )
        {
            filename = FileUtil.normalizeFileName( filename );
            File file = new File( filename );
            newFiles.add( file );
        }
        return newFiles;
    }

    private Init _pInit = null;

    /**
     * This is the constructor used in the main routine in
     * javancss.Main.
     * Other constructors might be helpful to use Javancss out
     * of other programs.
     */
    public Javancss(String[] asArgs_) throws IOException
    {
        _pInit = new Init( this, asArgs_, Main.S_RCS_HEADER, S_INIT__FILE_CONTENT );
        if ( _bExit )
        {
            return;
        }
        Map<String, String> htOptions = _pInit.getOptions();

        // the arguments (the files) to be processed
        _vJavaSourceFiles = findFiles( _pInit.getArguments(), htOptions.get( "recursive" ) != null );

        // this initiates the measurement
        try
        {
            _measureRoot( newReader( System.in ) );
        }
        catch ( Throwable pThrowable ) {
            pThrowable.printStackTrace(System.err);
        }
        if ( getLastErrorMessage() != null ) {
            Util.printlnErr( getLastErrorMessage() + "\n" );
            if ( getNcss() <= 0 )
                return;
        }

        final PrintWriter pw = new PrintWriter(System.out);
        try {
            printFileStats(pw);
        } finally {
            pw.flush();
        }
    }

    public int getNcss()
    {
        return _ncss;
    }

    public String getLastErrorMessage()
    {
        return _sErrorMessage;
    }

    public Throwable getLastError()
    {
        return _thrwError;
    }

    public void setExit()
    {
        _bExit = true;
    }

    private Reader newReader( InputStream stream )
        throws UnsupportedEncodingException
    {
        return new InputStreamReader(stream);
    }

    private Reader newReader( File file )
        throws FileNotFoundException, UnsupportedEncodingException
    {
        return newReader( new FileInputStream( file ) );
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
