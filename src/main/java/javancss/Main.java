package javancss;

import java.io.File;

public class Main
{
    public static void main(String[] asArgs) throws Exception
    {
		Javancss pJavancss = new Javancss();
		for(String filename : asArgs) {
			File file = new File(filename);
			if(file.exists()) {
				FileMetrics metrics = pJavancss.measure(filename);
				System.out.println(metrics.toString());
			}
		}
        System.exit(0);
    }
}
