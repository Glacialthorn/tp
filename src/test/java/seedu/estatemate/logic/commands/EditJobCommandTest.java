package seedu.estatemate.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.estatemate.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.estatemate.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.estatemate.testutil.TypicalPersons.getTypicalEstateMate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.estatemate.logic.Messages;
import seedu.estatemate.model.Model;
import seedu.estatemate.model.ModelManager;
import seedu.estatemate.model.UserPrefs;
import seedu.estatemate.model.job.Description;
import seedu.estatemate.model.job.Job;

/**
 * Integration tests for {@link EditJobCommand} with ModelManager.
 */
public class EditJobCommandTest {

    private Model model;
    private Model expectedModel;

    private int id1;
    private int id2;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalEstateMate(), new UserPrefs());
        expectedModel = new ModelManager(model.getEstateMate(), new UserPrefs());

        // Seed two jobs with deterministic ids
        id1 = model.nextJobId();
        model.addJob(new Job(new Description("Fix sink leak"), id1));
        id2 = model.nextJobId();
        model.addJob(new Job(new Description("Replace corridor lights"), id2));

        expectedModel.addJob(new Job(new Description("Fix sink leak"), id1));
        expectedModel.addJob(new Job(new Description("Replace corridor lights"), id2));
    }

    @Test
    public void execute_validIdNewDescription_success() {
        Description newDesc = new Description("Repair lobby door");

        EditJobCommand cmd = new EditJobCommand(id1, newDesc);

        // expected state after edit
        expectedModel.editJobById(id1, newDesc);

        assertCommandSuccess(
                cmd,
                model,
                String.format(EditJobCommand.MESSAGE_SUCCESS, id1),
                expectedModel
        );
    }

    @Test
    public void execute_invalidId_throwsCommandException() {
        int nonExisting = 999999;
        EditJobCommand cmd = new EditJobCommand(nonExisting, new Description("Anything"));
        assertCommandFailure(cmd, model, Messages.MESSAGE_INVALID_JOB_ID);
    }

    @Test
    public void execute_duplicateDescription_throwsCommandException() {
        // Attempt to edit id1 to have the same description as id2
        EditJobCommand cmd = new EditJobCommand(id1, new Description("Replace corridor lights"));
        assertCommandFailure(cmd, model, EditJobCommand.MESSAGE_DUPLICATE_JOB);
    }

    @Test
    public void equals() {
        EditJobCommand a = new EditJobCommand(3, new Description("A"));
        EditJobCommand b = new EditJobCommand(3, new Description("A"));
        EditJobCommand c = new EditJobCommand(4, new Description("A"));
        EditJobCommand d = new EditJobCommand(3, new Description("B"));

        // same values -> true
        assertTrue(a.equals(b));

        // same object -> true
        assertTrue(a.equals(a));

        // different id -> false
        assertFalse(a.equals(c));

        // different description -> false
        assertFalse(a.equals(d));

        // null -> false
        assertFalse(a.equals(null));

        // different type -> false
        assertFalse(a.equals("not a command"));
    }

    @Test
    public void toStringMethod() {
        EditJobCommand cmd = new EditJobCommand(42, new Description("Z"));
        String expected = EditJobCommand.class.getCanonicalName()
                + "{targetId=42, newDescription=Z}";
        assertEquals(expected, cmd.toString());
    }
}
