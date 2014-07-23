package javancss.parser;

import java.util.*;
import javancss.FunctionMetric;
import javancss.ObjectMetric;
import javancss.PackageMetric;
import javancss.FileMetrics;

public interface JavaParserInterface
{
    public void parse() throws Exception;
    public void parseImportUnit() throws Exception;
    public int getNcss();
    public int getLOC();
    public int getJvdc();
    public List<FunctionMetric> getFunction();
    public List<ObjectMetric> getObject();
    public Map<String, PackageMetric> getPackage();
    public List<Object[]> getImports();
    public Object[] getPackageObjects();
    public String getLastFunction();

    public void collectFileMetrics(FileMetrics fm);
}
