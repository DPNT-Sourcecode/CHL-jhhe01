package befaster.solutions.HLO;

import befaster.solutions.SUM.SumSolution;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HelloSolutionTest {
    private HelloSolution hello;

    @Before
    public void setUp() {

        hello = new HelloSolution();
    }

    @Test
    public void say_hello() {
        assertThat(hello.hello("Saman"), equalTo("Hello, Saman!"));
    }
}


