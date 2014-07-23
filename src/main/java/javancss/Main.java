package javancss;

import java.io.IOException;
import java.util.Locale;

public class Main
{
    private static final String IMPL_VERSION = Main.class.getPackage().getImplementationVersion();

    public static final String S_RCS_HEADER = "$Header: /javancss/Main.java,v "
        + ( ( IMPL_VERSION == null ) ? "0.0 2001/01/01 00:00:00" : IMPL_VERSION ) + " clemens Exp clemens $";

    public static void main(String[] asArgs) throws IOException
    {
        Locale.setDefault(Locale.US);
        Javancss pJavancss = new Javancss(asArgs);
        System.exit( 0 );
    }
}
