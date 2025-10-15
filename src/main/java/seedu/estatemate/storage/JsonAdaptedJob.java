package seedu.estatemate.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import seedu.estatemate.commons.exceptions.IllegalValueException;
import seedu.estatemate.model.job.Description;
import seedu.estatemate.model.job.Job;

/**
 * Jackson-friendly version of {@link Job}.
 */
class JsonAdaptedJob {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Job's %s field is missing!";

    private final String description;
    private final Integer id;
    private final Boolean isDone;

    /**
     * Constructs a {@code JsonAdaptedJob} with the given job details.
     */
    @JsonCreator
    public JsonAdaptedJob(@JsonProperty("description") String description,
                          @JsonProperty("id") Integer id,
                          @JsonProperty("done") Boolean isDone) {
        this.description = description;
        this.id = id;
        this.isDone = isDone == null ? false
                                     : isDone;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedJob(Job source) {
        this.description = source.getDescription().description;
        this.id = source.getId();
        this.isDone = source.getDone();
    }

    /**
     * Converts this Jackson-friendly adapted Job object into the model's {@code Job} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Job.
     */
    public Job toModelType() throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        if (id == null || id <= 0) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "id"));
        }

        if (isDone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "isDone"));
        }
        Job output = new Job(new Description(description), id);
        output.setDone(isDone);
        return output;
    }
}

