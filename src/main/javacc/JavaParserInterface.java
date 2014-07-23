package javancss.parser;

import javancss.FileMetrics;

public interface JavaParserInterface
{
    public void parse() throws Exception;
    public void collectFileMetrics(FileMetrics fm);
}
