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
    public void checklite() {
        assertThat(check.checklite("1A 1B 1C 1D"), equalTo(115));
        assertThat(check.checklite("A B C D"), equalTo(115));
        assertThat(check.checklite("A, B, C, D"), equalTo(115));
        assertThat(check.checklite("A B 2C 3D"), equalTo(165));

        assertThat(check.checklite("3A B C D"), equalTo(195));
        assertThat(check.checklite("3A 2B C D"), equalTo(210));

        assertThat(check.checklite("6A B C D"), equalTo(325));

        assertThat(check.checklite("A 3B C D"), equalTo(160));

        assertThat(check.checklite(""), equalTo(160));

    }
}



