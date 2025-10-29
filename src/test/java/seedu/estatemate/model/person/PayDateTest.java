package seedu.estatemate.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.estatemate.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PayDateTest {

    @Test
    public void construct_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PayDate(null));
    }

    @Test
    public void construct_invalidPayDate_throwsIllegalArgumentException() {
        // invalid format
        assertThrows(IllegalArgumentException.class, () -> new PayDate(""));
        assertThrows(IllegalArgumentException.class, () -> new PayDate(" "));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("14/10/2025"));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("2025/09/12"));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("2025-9-1"));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("12-09-2025"));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("2025.09.12"));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("2025-03-32"));
        assertThrows(IllegalArgumentException.class, () -> new PayDate("2025-02-29"));
    }

    @Test
    public void isValidPayDate_invalidDates() {
        // null pay date
        assertFalse(PayDate.isValidPayDate(null));

        // incorrect formats
        assertFalse(PayDate.isValidPayDate(""));
        assertFalse(PayDate.isValidPayDate(" "));
        assertFalse(PayDate.isValidPayDate("14/10/2025")); // incorrect format
        assertFalse(PayDate.isValidPayDate("2025/09/12")); // use of '/' instead of '-'
        assertFalse(PayDate.isValidPayDate("2025-9-1")); // format: YYYY-M-D
        assertFalse(PayDate.isValidPayDate("2025.09.12")); // use of '.' instead of '-'
        assertFalse(PayDate.isValidPayDate("12-09-2025")); // format: DD-MM-YYYY

        // incorrect calendar dates
        assertFalse(PayDate.isValidPayDate("2025-03-32")); // 32 does not exist
        assertFalse(PayDate.isValidPayDate("2025-02-29")); // non-leap year
    }

    @Test
    public void isValidPayDate_validDates() {
        // valid leap years
        assertTrue(PayDate.isValidPayDate("2024-02-29")); // current leap year
        assertTrue(PayDate.isValidPayDate("2104-02-29")); // future valid leap year

        // valid calendar dates
        assertTrue(PayDate.isValidPayDate("2025-01-31")); // month-end
        assertTrue(PayDate.isValidPayDate("2025-10-10")); // normal date
        assertTrue(PayDate.isValidPayDate("2100-12-31")); // future date
    }

    @Test
    public void equals() {
        PayDate payDate = new PayDate("2025-10-10");

        // same value -> returns true
        assertTrue(payDate.equals(new PayDate("2025-10-10")));

        // same object -> returns true
        assertTrue(payDate.equals(payDate));

        // null -> returns false
        assertFalse(payDate.equals(null));

        // different types -> returns false
        assertFalse(payDate.equals(5.0f));

        // different values -> returns false
        assertFalse(payDate.equals(new PayDate("2025-10-12")));
    }
}
