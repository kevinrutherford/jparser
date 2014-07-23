package javancss.test;

import java.io.File;
import java.io.StringWriter;

import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;
import ccl.xml.XMLUtil;
import javancss.Javancss;

/**
 * This test class checks that the xml output feature is
 * working properly.
 *
 * @version $Id: XmlFormatterTest.java 295 2014-07-12 00:54:50Z pkofler $
 * @author  Chr. Clemens Lee
 */
public class XmlFormatterTest
    extends AbstractTest
{
    /**
     * Is it at least possible to properly parse generated xml code?
     */
    public void testParsing()
        throws Exception
    {
    }

    /**
     * Is the transformed XML output identical to the standard ASCI output?
     */
    public void testXML2Text()
        throws Exception
    {
    }

    public XmlFormatterTest()
    {
        super();
    }

    public XmlFormatterTest( Test pTest_ )
    {
        super( pTest_ );
    }

    private File getXslFile( String filename )
    {
        return new File( getTestDir(), "../../../xslt/" + filename );
    }

    /**
     * Test code goes here.
     */
    @Override
    protected void _doIt()
        throws Exception
    {
        testParsing();
        testXML2Text();
    }

    public static void main( String[] asArg_ )
    {
        new XmlFormatterTest().main();
    }
}
