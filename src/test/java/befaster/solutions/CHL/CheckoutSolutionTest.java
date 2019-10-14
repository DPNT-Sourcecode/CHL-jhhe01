package befaster.solutions.CHL;

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
        assertThat(check.checkout("1A"), equalTo(50));

        assertThat(check.checkout("1A 1B 1C 1D"), equalTo(115));
        assertThat(check.checkout("A B C D"), equalTo(115));
        assertThat(check.checkout("A, B, C, D"), equalTo(115));
        assertThat(check.checkout("A B 2C 3D"), equalTo(165));

        assertThat(check.checkout("3A B C D"), equalTo(195));
        assertThat(check.checkout("3A 2B C D"), equalTo(210));

        assertThat(check.checkout("6A B C D"), equalTo(325));

        assertThat(check.checkout("A 3B C D"), equalTo(160));

        assertThat(check.checkout(" "), equalTo(-1));
        assertThat(check.checkout("%^&* %$ 2A"), equalTo(-1));
        assertThat(check.checkout("34"), equalTo(-1));
        assertThat(check.checkout("#A"), equalTo(-1));

    }
}

