package seedu.estatemate.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.estatemate.commons.util.ToStringBuilder;
import seedu.estatemate.logic.Messages;
import seedu.estatemate.logic.commands.exceptions.CommandException;
import seedu.estatemate.model.Model;

/**
 * Marks a job as complete
 */
public class UnmarkJobCommand extends Command {
    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the job (identified by the id number used in the displayed job) as incomplete.\n"
            + "Parameters: id (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked job as incomplete: #%d";

    private final Integer targetId;

    public UnmarkJobCommand(int targetId) {
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.getUnfilteredJobList().stream().anyMatch(j -> j.getId() == targetId)) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_ID);
        }
        if (model.getUnfilteredJobList().stream().anyMatch(j -> (j.getId() == targetId) && !(j.getDone()))) {
            throw new CommandException("Job is already marked as incomplete!");
        }

        model.unmarkJobById(targetId);
        return new CommandResult(String.format(MESSAGE_SUCCESS, targetId));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkJobCommand)) {
            return false;
        }

        UnmarkJobCommand otherUnmarkJobCommand = (UnmarkJobCommand) other;
        return targetId.equals(otherUnmarkJobCommand.targetId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetId", targetId)
                .toString();
    }
}
