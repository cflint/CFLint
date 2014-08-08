import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;

import com.cflint.BugInfo;
import com.cflint.CFLint;

public class Scratch {

	@Test
	public void test() throws Exception {
		// final PrintStream ps = System.out;
		// System.setOut(new PrintStream(new OutputStream() {
		//
		// @Override
		// public void write(final int arg0) throws IOException {
		// ps.write("WRITE".getBytes());
		// new NullPointerException().printStackTrace(ps);
		// throw new IOException("NOT HERE");
		// }
		// }));
		final CFLint cflint = new CFLint();
//		cflint.getBugs().getFilter().includeCode("GLOBAL_VAR");
		cflint.scan("C:\\source\\cfmx\\extensions\\components\\goodville\\access\\projects");
		for (List<BugInfo> list : cflint.getBugs().getBugList().values()) {
			for (BugInfo bug : list) {
				System.out.println(bug);
			}
		}
	}
}
