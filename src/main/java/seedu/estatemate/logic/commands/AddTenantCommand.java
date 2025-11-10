package seedu.estatemate.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_LEASE;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_LEASE_AMOUNT;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_PAYDATE;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.estatemate.commons.util.ToStringBuilder;
import seedu.estatemate.logic.Messages;
import seedu.estatemate.logic.commands.exceptions.CommandException;
import seedu.estatemate.model.Model;
import seedu.estatemate.model.person.Person;

/**
 * Adds a tenant to EstateMate.
 */
public class AddTenantCommand extends Command {

    public static final String COMMAND_WORD = "tenant";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tenant to EstateMate. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_LEASE + "START END "
            + PREFIX_LEASE_AMOUNT + "AMOUNT "
            + PREFIX_PAYDATE + "PAYDATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_LEASE + "2025-12-01 2027-02-01 "
            + PREFIX_LEASE_AMOUNT + "1100.00 "
            + PREFIX_PAYDATE + "2026-01-01 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New tenant added: %1$s";
    public static final String MESSAGE_DUPLICATE_TENANT = "This tenant already exists in EstateMate";

    private final Person toAdd;

    /**
     * Creates an AddTenantCommand to add the specified {@code Person}
     */
    public AddTenantCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TENANT);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTenantCommand)) {
            return false;
        }

        AddTenantCommand otherAddTenantCommand = (AddTenantCommand) other;
        return toAdd.equals(otherAddTenantCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
