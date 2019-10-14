package befaster.solutions.CHK;

import befaster.solutions.CHK.CheckoutSolution;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CheckoutSolutionTest {
    private CheckoutSolution check;

    @Before
    public void setUp() {
        check = new CheckoutSolution();
    }

    /*
    +------+-------+----------------+
    | Item | Price | Special offers |
    +------+-------+----------------+
    | A    | 50    | 3A for 130     |
    | B    | 30    | 2B for 45      |
    | C    | 20    |                |
    | D    | 15    |                |
    +------+-------+----------------+
     */
    @Test
    public void checkout() {
        assertThat(check.checkout("A"), equalTo(50));
        assertThat(check.checkout("AA"), equalTo(100));

        assertThat(check.checkout("ABCD"), equalTo(115));
        assertThat(check.checkout("ABCCDDD"), equalTo(165));

        assertThat(check.checkout("AAABCD"), equalTo(195));
        assertThat(check.checkout("AAABBCD"), equalTo(210));

        assertThat(check.checkout("AAAAAABCD"), equalTo(325));

        assertThat(check.checkout("ABBBCD"), equalTo(160));

        assertThat(check.checkout(""), equalTo(0));
        assertThat(check.checkout(" "), equalTo(0));
        assertThat(check.checkout("%^&* %$ 2A"), equalTo(-1));
        assertThat(check.checkout("34"), equalTo(-1));
        assertThat(check.checkout("#A"), equalTo(-1));

    }
}