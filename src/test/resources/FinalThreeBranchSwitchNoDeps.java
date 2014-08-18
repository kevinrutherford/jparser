/**
 * The default case is not counted by the metrics tool.
 */
public final class FinalThreeBranchSwitchNoDeps {
	public int doSomething(int input) {
		switch(input) {
			case 1:
				return 1 + input;
			case 2:
				return 2 + input;
			default:
				return -1;
		}
	}
}
