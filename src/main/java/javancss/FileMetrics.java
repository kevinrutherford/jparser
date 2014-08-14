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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{\n");
		sb.append("  \"filename\": \"").append(filename).append("\",\n");
		sb.append("  \"num_branches\": ").append(num_branches).append(",\n");
		sb.append("  \"num_dependencies\": ").append(num_dependencies).append(",\n");
		sb.append("  \"num_superclasses\": ").append(num_superclasses).append("\n");
		sb.append("}");
		return sb.toString();
	}
}
