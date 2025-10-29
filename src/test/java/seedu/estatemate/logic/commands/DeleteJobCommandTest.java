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
 * Integration tests for {@link DeleteJobCommand} with ModelManager.
 */
public class DeleteJobCommandTest {

    private Model model;
    private Model expectedModel;

    private int id1;
    private int id2;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalEstateMate(), new UserPrefs());
        expectedModel = new ModelManager(model.getEstateMate(), new UserPrefs());

        // seed a couple of jobs with deterministic ids
        id1 = model.nextJobId();
        model.addJob(new Job(new Description("Fix sink leak"), id1));
        id2 = model.nextJobId();
        model.addJob(new Job(new Description("Replace corridor lights"), id2));

        // keep expectedModel in sync
        expectedModel.addJob(new Job(new Description("Fix sink leak"), id1));
        expectedModel.addJob(new Job(new Description("Replace corridor lights"), id2));
    }

    @Test
    public void execute_validId_success() {
        DeleteJobCommand deleteCommand = new DeleteJobCommand(id1);

        // expected state after deletion
        expectedModel.deleteJobById(id1);

        assertCommandSuccess(
                deleteCommand,
                model,
                String.format(DeleteJobCommand.MESSAGE_DELETE_JOB_SUCCESS, id1),
                expectedModel
        );
    }

    @Test
    public void execute_invalidId_throwsCommandException() {
        int nonExisting = 999999;
        DeleteJobCommand deleteCommand = new DeleteJobCommand(nonExisting);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_JOB_ID);
    }

    @Test
    public void equals() {
        DeleteJobCommand deleteFirst = new DeleteJobCommand(1);
        DeleteJobCommand deleteSecond = new DeleteJobCommand(2);

        // same object -> true
        assertTrue(deleteFirst.equals(deleteFirst));

        // same values -> true
        assertTrue(deleteFirst.equals(new DeleteJobCommand(1)));

        // different values -> false
        assertFalse(deleteFirst.equals(deleteSecond));

        // null -> false
        assertFalse(deleteFirst.equals(null));

        // different type -> false
        assertFalse(deleteFirst.equals(5));
    }

    @Test
    public void toStringMethod() {
        DeleteJobCommand command = new DeleteJobCommand(42);
        String expected = DeleteJobCommand.class.getCanonicalName() + "{targetId=42}";
        assertEquals(expected, command.toString());
    }
}
