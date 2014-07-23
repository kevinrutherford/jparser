package javancss;

public class FileMetrics {
    
    public int num_dependencies = 0;
    public int num_branches = 0;
    public int num_superclasses = 0;
    public String filename = "";

    public void clear()
    {
        filename = "";
        num_dependencies = 0;
        num_branches = 0;
        num_superclasses = 0;
    }
}
