package seedu.estatemate.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.estatemate.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.estatemate.commons.core.GuiSettings;
import seedu.estatemate.logic.commands.exceptions.CommandException;
import seedu.estatemate.model.EstateMate;
import seedu.estatemate.model.Model;
import seedu.estatemate.model.ReadOnlyEstateMate;
import seedu.estatemate.model.ReadOnlyUserPrefs;
import seedu.estatemate.model.job.Description;
import seedu.estatemate.model.job.Job;
import seedu.estatemate.model.person.Person;

/**
 * Unit tests for {@link AddJobCommand} using Model stubs.
 */
public class AddJobCommandTest {

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddJobCommand(null));
    }

    @Test
    public void execute_jobAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingJobAdded modelStub = new ModelStubAcceptingJobAdded(/*nextId*/ 1);
        Description validDesc = new Description("Fix sink leak");

        CommandResult commandResult = new AddJobCommand(validDesc).execute(modelStub);

        assertEquals(
                String.format(AddJobCommand.MESSAGE_SUCCESS, 1, "Fix sink leak"),
                commandResult.getFeedbackToUser()
        );
        assertEquals(1, modelStub.jobsAdded.size());
        assertEquals("Fix sink leak", modelStub.jobsAdded.get(0).getDescription().toString());
        assertEquals(1, modelStub.jobsAdded.get(0).getId());
    }

    @Test
    public void execute_duplicateJob_throwsCommandException() {
        Description desc = new Description("Replace light bulb");
        ModelStub modelStub = new ModelStubWithExistingDescription(desc);

        AddJobCommand command = new AddJobCommand(desc);
        assertThrows(CommandException.class, AddJobCommand.MESSAGE_DUPLICATE_JOB, () -> command.execute(modelStub));
    }

    @Test
    public void equals() {
        Description d1 = new Description("Paint living room");
        Description d2 = new Description("Repair window");
        AddJobCommand addD1 = new AddJobCommand(d1);
        AddJobCommand addD2 = new AddJobCommand(d2);

        // same object -> returns true
        assertTrue(addD1.equals(addD1));

        // same description -> returns true
        assertTrue(addD1.equals(new AddJobCommand(d1)));

        // same values -> returns true
        assertTrue(addD1.equals(new AddJobCommand(new Description("Paint living room"))));

        // different types -> returns false
        assertFalse(addD1.equals(1));

        // null -> returns false
        assertFalse(addD1.equals(null));

        // different description -> returns false
        assertFalse(addD1.equals(addD2));
    }

    @Test
    public void toStringMethod() {
        Description d = new Description("Clean corridor");
        AddJobCommand command = new AddJobCommand(d);
        String expected = AddJobCommand.class.getCanonicalName() + "{description=" + d + "}";
        assertEquals(expected, command.toString());
    }

    private static class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getEstateMateFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEstateMateFilePath(Path estateMateFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyEstateMate getEstateMate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEstateMate(ReadOnlyEstateMate estateMate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasJobWithDescription(Description description) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addJob(Job job) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteJobById(int id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void markJobById(int id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void unmarkJobById(int id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int nextJobId() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getJobDescriptionById(int jobId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Boolean isJobCompleted(int jobId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Integer> getJobIdsForPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void editJobById(int id, Description newDescription) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Job> getFilteredJobList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Job> getUnfilteredJobList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredJobList(Predicate<Job> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    private static class ModelStubAcceptingJobAdded extends ModelStub {
        private final List<Job> jobsAdded = new ArrayList<>();
        private final int nextId;

        ModelStubAcceptingJobAdded(int nextId) {
            this.nextId = nextId;
        }

        @Override
        public boolean hasJobWithDescription(Description description) {
            requireNonNull(description);
            return jobsAdded.stream().anyMatch(j -> j.getDescription().equals(description));
        }

        @Override
        public void addJob(Job job) {
            requireNonNull(job);
            jobsAdded.add(job);
        }

        @Override
        public int nextJobId() {
            return nextId; // deterministic for the test
        }

        @Override
        public ReadOnlyEstateMate getEstateMate() {
            return new EstateMate();
        }

        @Override
        public ObservableList<Job> getFilteredJobList() {
            return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(jobsAdded));
        }

        @Override
        public void updateFilteredJobList(Predicate<Job> predicate) {
            // nothing for stub
        }
    }

    private static class ModelStubWithExistingDescription extends ModelStub {
        private final Description existing;

        ModelStubWithExistingDescription(Description existing) {
            this.existing = existing;
        }

        @Override
        public boolean hasJobWithDescription(Description description) {
            return existing.equals(description);
        }

        @Override
        public int nextJobId() {
            return 1;
        }

        @Override
        public ReadOnlyEstateMate getEstateMate() {
            return new EstateMate();
        }
    }
}
