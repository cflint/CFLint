
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.junit.Test;

import com.cflint.CFLint;

public class Scratch {

	@Test
	public void test() throws Exception {
//		final PrintStream ps = System.out;
//		System.setOut(new PrintStream(new OutputStream() {
//
//			@Override
//			public void write(final int arg0) throws IOException {
//				ps.write("WRITE".getBytes());
//				new NullPointerException().printStackTrace(ps);
//				throw new IOException("NOT HERE");
//			}
//		}));
		final CFLint cflint = new CFLint();
		cflint.scan("C:\\source\\cfmx\\extensions\\components\\goodville\\objects\\quote");
	}
}
