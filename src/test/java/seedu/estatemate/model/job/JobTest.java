package seedu.estatemate.model.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.estatemate.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class JobTest {

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Job(null, 1));
    }

    @Test
    public void constructor_valid_setsFields() {
        Description desc = new Description("Fix sink leak");
        Job job = new Job(desc, 7);

        assertEquals(desc, job.getDescription());
        assertEquals(7, job.getId());
    }

    @Test
    public void defaultDone_falseByDefault() {
        Job job = new Job(new Description("Fix sink leak"), 7);
        assertFalse(job.getDone());
    }

    @Test
    public void setDone_togglesState() {
        Job job = new Job(new Description("Replace corridor lights"), 2);
        assertFalse(job.getDone());

        job.setDone(true);
        assertTrue(job.getDone());

        job.setDone(false);
        assertFalse(job.getDone());
    }

    @Test
    public void isSameJob_basedOnDescription() {
        Job a = new Job(new Description("Paint wall"), 1);
        Job b = new Job(new Description("Paint wall"), 99); // different id, same description
        Job c = new Job(new Description("Repair window"), 1);

        // same description -> true
        assertTrue(a.isSameJob(b));

        // different description -> false
        assertFalse(a.isSameJob(c));

        // same reference -> true
        assertTrue(a.isSameJob(a));

        // null -> false
        assertFalse(a.isSameJob(null));
    }

    @Test
    public void equals_basedOnIdOnly() {
        Job withId1DescA = new Job(new Description("A"), 1);
        Job withId1DescB = new Job(new Description("B"), 1);
        Job withId2DescA = new Job(new Description("A"), 2);

        // same id, different description -> equal
        assertEquals(withId1DescA, withId1DescB);

        // same object -> true
        assertEquals(withId1DescA, withId1DescA);

        // null -> false
        assertNotEquals(null, withId1DescA);

        // different types -> false
        assertNotEquals("not a job", withId1DescA);

        // different id -> false (even if description equal)
        assertNotEquals(withId1DescA, withId2DescA);
    }

    @Test
    public void hashCode_consistentWithEqualsOnId() {
        Job j1 = new Job(new Description("Alpha"), 42);
        Job j2 = new Job(new Description("Beta"), 42); // same id, different desc

        assertEquals(j1.hashCode(), j2.hashCode());
    }

    @Test
    public void toString_containsIdAndDescription() {
        Job j = new Job(new Description("Fix door hinge"), 13);
        String s = j.toString();

        assertTrue(s.contains("id"));
        assertTrue(s.contains("13"));
        assertTrue(s.contains("description"));
        assertTrue(s.contains("Fix door hinge"));
    }
}
