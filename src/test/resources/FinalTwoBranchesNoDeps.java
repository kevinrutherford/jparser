/**
 * The metrics tool counts `if` statements, but does not include `else` in the branch count
 */
public final class FinalTwoBranchesNoDeps {
	public boolean doSomething() {
		boolean result;
		if(true) {
			result = true;
		} else {
			result = false;
		}
		if(result) {
			return false;
		} else {
			return true;
		}
	}
}