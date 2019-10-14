package befaster.solutions.CHL;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CheckliteSolutionTest {
    private CheckliteSolution check;

    @Before
    public void setUp() {

        check = new CheckliteSolution();
    }

    @Test
    public void say_hello() {
        assertThat(check.checklite("Saman"), equalTo("Hello, Saman!"));
    }
}
