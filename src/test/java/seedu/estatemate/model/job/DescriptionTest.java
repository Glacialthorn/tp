package seedu.estatemate.model.job;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.estatemate.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalid = "";
        assertThrows(IllegalArgumentException.class, () -> new Description(invalid));
    }

    @Test
    public void isValidDescription() {
        // null
        assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid (fails VALIDATION_REGEX "\\S.*")
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // single space
        assertFalse(Description.isValidDescription("   ")); // spaces only
        assertFalse(Description.isValidDescription(" leading")); // first char whitespace

        // valid (first char non-whitespace)
        assertTrue(Description.isValidDescription("a"));
        assertTrue(Description.isValidDescription("Fix sink leak"));
        assertTrue(Description.isValidDescription("123"));
        assertTrue(Description.isValidDescription("#42 drill? ok"));
        assertTrue(Description.isValidDescription("🛠️ maintenance"));
    }

    @Test
    public void equals() {
        Description d1 = new Description("Fix sink");
        Description d2 = new Description("Fix sink");
        Description d3 = new Description("Replace light");

        // same values -> true
        assertTrue(d1.equals(d2));

        // same object -> true
        assertTrue(d1.equals(d1));

        // null -> false
        assertFalse(d1.equals(null));

        // different types -> false
        assertFalse(d1.equals(5.0f));

        // different values -> false
        assertFalse(d1.equals(d3));
    }
}
