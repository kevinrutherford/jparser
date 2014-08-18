package javancss;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JavancssTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				// base case
				{"/FinalBranchlessNoDeps.java", new FileMetrics() {{
					num_branches = 0;
					num_dependencies = 0;
					num_superclasses = 0;
				}}
				},
				// non final class base case
				{"/NonFinalBranchlessNoDeps.java", new FileMetrics() {{
					num_branches = 0;
					num_dependencies = 0;
					num_superclasses = 1;
				}}
				},
				// single branch
				{"/FinalSingleBranchNoDeps.java", new FileMetrics() {{
					num_branches = 1;
					num_dependencies = 0;
					num_superclasses = 0;
				}}
				},
				// 2 branches
				{"/FinalTwoBranchesNoDeps.java", new FileMetrics() {{
					num_branches = 2;
					num_dependencies = 0;
					num_superclasses = 0;
				}}
				},
				// derived class
				{"/DerrivedOnceFinalBranchlessNoDeps.java", new FileMetrics() {{
					num_branches = 0;
					num_dependencies = 0;
					num_superclasses = 1;
				}}
				},
				// 3-case switch statement
				{"/FinalThreeBranchSwitchNoDeps.java", new FileMetrics() {{
					num_branches = 2;
					num_dependencies = 0;
					num_superclasses = 0;
				}}
				},
				// simple class with one import statement
				{"/FinalBranchlessSingleDep.java", new FileMetrics() {{
					num_branches = 0;
					num_dependencies = 1;
					num_superclasses = 0;
				}}
				}
		});
	}

	private final String path;
	private final FileMetrics expected;

	public JavancssTest(final String path, final FileMetrics expected) {
		this.path = path;
		this.expected = expected;
	}

	@Test
	public void testBranchlessFileWithNoDeps() {
		final String file = getClass().getResource(path).getFile();
		Javancss javancss = new Javancss();
		FileMetrics metrics = javancss.measure(file);
		assertEquals("unexpected num_branches", expected.num_branches, metrics.num_branches);
		assertEquals("unexpected num_dependencies", expected.num_dependencies, metrics.num_dependencies);
		assertEquals("unexpected num_superclasses", expected.num_superclasses, metrics.num_superclasses);
	}
}
