package seedu.estatemate.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.estatemate.commons.core.GuiSettings;
import seedu.estatemate.model.job.Description;
import seedu.estatemate.model.job.Job;
import seedu.estatemate.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * {@code Predicate} that always evaluate to true
     */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    /**
     * Always-true predicate for listing all jobs.
     */
    Predicate<Job> PREDICATE_SHOW_ALL_JOBS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getEstateMateFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setEstateMateFilePath(Path estateMateFilePath);

    /**
     * Returns the EstateMate object
     */
    ReadOnlyEstateMate getEstateMate();

    /**
     * Replaces address book data with the data in {@code EstateMate}.
     */
    void setEstateMate(ReadOnlyEstateMate estateMate);

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person. The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person. {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}. {@code target} must exist in the address
     * book. The person identity of {@code editedPerson} must not be the same as another existing person in the address
     * book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Returns an unmodifiable view of the filtered person list
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    ObservableList<Job> getFilteredJobList();

    void updateFilteredJobList(Predicate<Job> predicate);

    ObservableList<Job> getUnfilteredJobList();

    void addJob(Job job);

    void deleteJobById(int id);

    void markJobById(int id);

    void unmarkJobById(int id);

    int nextJobId();

    String getJobDescriptionById(int jobId);

    Boolean isJobCompleted(int jobId);

    List<Integer> getJobIdsForPerson(Person person);

    boolean hasJobWithDescription(Description description);

    void editJobById(int id, Description newDescription);

}
