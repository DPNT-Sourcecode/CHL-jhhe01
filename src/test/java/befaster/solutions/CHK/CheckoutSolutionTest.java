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
+------+-------+------------------------+
| Item | Price | Special offers         |
+------+-------+------------------------+
| A    | 50    | 3A for 130, 5A for 200 |
| B    | 30    | 2B for 45              |
| C    | 20    |                        |
| D    | 15    |                        |
| E    | 40    | 2E get one B free      |
| F    | 10    | 2F get one F free      |
| G    | 20    |                        |
| H    | 10    | 5H for 45, 10H for 80  |
| I    | 35    |                        |
| J    | 60    |                        |
| K    | 80    | 2K for 150             |
| L    | 90    |                        |
| M    | 15    |                        |
| N    | 40    | 3N get one M free      |
| O    | 10    |                        |
| P    | 50    | 5P for 200             |
| Q    | 30    | 3Q for 80              |
| R    | 50    | 3R get one Q free      |
| S    | 30    |                        |
| T    | 20    |                        |
| U    | 40    | 3U get one U free      |
| V    | 50    | 2V for 90, 3V for 130  |
| W    | 20    |                        |
| X    | 90    |                        |
| Y    | 10    |                        |
| Z    | 50    |                        |
+------+-------+------------------------+
     */
    @Test
    public void checkout() {
        assertThat(check.checkout("A"), equalTo(50));
        assertThat(check.checkout("AA"), equalTo(100));

        assertThat(check.checkout("ABCD"), equalTo(115));
        assertThat(check.checkout("ABCCDDD"), equalTo(165));

        assertThat(check.checkout("AAABCD"), equalTo(195));
        assertThat(check.checkout("AAABBCD"), equalTo(210));

        assertThat(check.checkout("AAAAAABCD"), equalTo(315));

        assertThat(check.checkout("ABBBCD"), equalTo(160));


        assertThat(check.checkout(""), equalTo(0));
        assertThat(check.checkout(" "), equalTo(0));
        assertThat(check.checkout("%^&* %$ 2A"), equalTo(-1));
        assertThat(check.checkout("34"), equalTo(-1));
        assertThat(check.checkout("#A"), equalTo(-1));

        assertThat(check.checkout("BEE"), equalTo(80));
        assertThat(check.checkout("BEEEE"), equalTo(160));
        assertThat(check.checkout("BBEEEE"), equalTo(160));
        assertThat(check.checkout("EEBB"), equalTo(110));

        assertThat(check.checkout("E"), equalTo(40));
        assertThat(check.checkout("ABCDE"), equalTo(155));
        assertThat(check.checkout("AAAAA"), equalTo(200));

        assertThat(check.checkout("AAAAAAAA"), equalTo(330));
        assertThat(check.checkout("AAAAAAAAA"), equalTo(380));
        assertThat(check.checkout("AAAAAEEBAAABB"), equalTo(455));

        assertThat(check.checkout("F"), equalTo(10));
        assertThat(check.checkout("FF"), equalTo(20));
        assertThat(check.checkout("FFF"), equalTo(20));
        assertThat(check.checkout("FFFF"), equalTo(30));
        assertThat(check.checkout("FFFFF"), equalTo(40));
        assertThat(check.checkout("FFFFFF"), equalTo(40));

        assertThat(check.checkout("AAAAAEEBAAABBF"), equalTo(465));
        assertThat(check.checkout("AAAAAFEEBAAABBF"), equalTo(475));
        assertThat(check.checkout("AAAFAAFEEBAAABBF"), equalTo(475));

    }
}



