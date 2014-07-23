package javancss;

import java.io.IOException;
import java.io.Writer;

public interface Formatter
{
    void printFileStats(Writer w) throws IOException;
    void printPackageNcss( Writer w ) throws IOException;

    void printObjectNcss( Writer w )
        throws IOException;

    void printFunctionNcss( Writer w )
        throws IOException;

    void printJavaNcss( Writer w )
        throws IOException;

    void printStart( Writer w )
        throws IOException;

    void printEnd( Writer w )
        throws IOException;
}
