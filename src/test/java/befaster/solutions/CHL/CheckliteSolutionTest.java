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
 +------+-------+------------------------+
| Item | Price | Special offers         |
+------+-------+------------------------+
| A    | 50    | 3A for 130, 5A for 200 |
| B    | 30    | 2B for 45              |
| C    | 20    |                        |
| D    | 15    |                        |
| E    | 40    | 2E get one B free      |
+------+-------+------------------------+
     */
    @Test
    public void checklite() {
        assertThat(check.checklite("A"), equalTo(50));
        assertThat(check.checklite("AA"), equalTo(100));

        assertThat(check.checklite("ABCD"), equalTo(115));
        assertThat(check.checklite("ABCCDDD"), equalTo(165));

        assertThat(check.checklite("AAABCD"), equalTo(195));
        assertThat(check.checklite("AAABBCD"), equalTo(210));

        assertThat(check.checklite("AAAAAABCD"), equalTo(315));

        assertThat(check.checklite("ABBBCD"), equalTo(160));


        assertThat(check.checklite(""), equalTo(0));
        assertThat(check.checklite(" "), equalTo(0));
        assertThat(check.checklite("%^&* %$ 2A"), equalTo(-1));
        assertThat(check.checklite("34"), equalTo(-1));
        assertThat(check.checklite("#A"), equalTo(-1));

        assertThat(check.checklite("BEE"), equalTo(80));
        assertThat(check.checklite("BEEEE"), equalTo(160));
        assertThat(check.checklite("BBEEEE"), equalTo(160));
        assertThat(check.checklite("EEBB"), equalTo(110));

        assertThat(check.checklite("E"), equalTo(40));
        assertThat(check.checklite("ABCDE"), equalTo(155));
        assertThat(check.checklite("AAAAA"), equalTo(200));
    }
}




