package seedu.estatemate.logic.commands;

import static seedu.estatemate.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.estatemate.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.estatemate.testutil.TypicalPersons.getTypicalEstateMate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.estatemate.model.Model;
import seedu.estatemate.model.ModelManager;
import seedu.estatemate.model.UserPrefs;
import seedu.estatemate.model.job.Description;
import seedu.estatemate.model.job.Job;

/**
 * Integration tests for {@link AddJobCommand} with the ModelManager.
 */
public class AddJobCommandIntegrationTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalEstateMate(), new UserPrefs());
        expectedModel = new ModelManager(model.getEstateMate(), new UserPrefs());
    }

    @Test
    public void execute_newJob_success() {
        Description description = new Description("Fix sink leak");
        int id = expectedModel.nextJobId();
        Job toAdd = new Job(description, id);

        // expected state
        expectedModel.addJob(toAdd);

        assertCommandSuccess(
                new AddJobCommand(description),
                model,
                String.format(AddJobCommand.MESSAGE_SUCCESS, id, description.toString()),
                expectedModel
        );
    }

    @Test
    public void execute_duplicateDescription_throwsCommandException() {
        Description description = new Description("Replace light bulb");

        // seed an existing job with the same description
        int seededId = model.nextJobId();
        model.addJob(new Job(description, seededId));

        assertCommandFailure(
                new AddJobCommand(description),
                model,
                AddJobCommand.MESSAGE_DUPLICATE_JOB
        );
    }
}
