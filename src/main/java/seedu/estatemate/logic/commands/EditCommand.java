package seedu.estatemate.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_JOB;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_LEASE;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_LEASE_AMOUNT;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_PAYDATE;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.estatemate.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.estatemate.model.Model.PREDICATE_SHOW_ALL_TENANTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.estatemate.commons.core.index.Index;
import seedu.estatemate.commons.util.CollectionUtil;
import seedu.estatemate.commons.util.ToStringBuilder;
import seedu.estatemate.logic.Messages;
import seedu.estatemate.logic.commands.exceptions.CommandException;
import seedu.estatemate.model.Model;
import seedu.estatemate.model.person.Address;
import seedu.estatemate.model.person.Email;
import seedu.estatemate.model.person.Lease;
import seedu.estatemate.model.person.LeaseAmount;
import seedu.estatemate.model.person.Name;
import seedu.estatemate.model.person.PayDate;
import seedu.estatemate.model.person.Person;
import seedu.estatemate.model.person.Phone;
import seedu.estatemate.model.tag.Tag;

/**
 * Edits the details of an existing tenant in EstateMate.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the tenant identified "
            + "by the index number used in the displayed tenant list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer between 1 - 2147483647) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_LEASE + "START END] "
            + "[" + PREFIX_LEASE_AMOUNT + "AMOUNT] "
            + "[" + PREFIX_PAYDATE + "PAYDATE] "
            + "[" + PREFIX_TAG + "TAG]..."
            + "[" + PREFIX_JOB + "JOB]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_TENANT_SUCCESS = "Edited tenant: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TENANT = "This tenant already exists in EstateMate.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TENANT_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_TENANT);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_TENANTS);
        return new CommandResult(String.format(MESSAGE_EDIT_TENANT_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Lease updatedLease = editPersonDescriptor.getLease().orElse(personToEdit.getLease());
        LeaseAmount updatedLeaseAmount = editPersonDescriptor.getLeaseAmount()
                .orElse(personToEdit.getLeaseAmount());
        PayDate updatedPayDate = editPersonDescriptor.getPayDate().orElse(personToEdit.getPayDate());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        List<Integer> updatedJobs = editPersonDescriptor.getJobs().orElse(personToEdit.getJobs());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedLease,
                updatedLeaseAmount, updatedPayDate, updatedTags, updatedJobs);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the corresponding field value
     * of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Lease lease;
        private LeaseAmount leaseAmount;
        private PayDate payDate;
        private Set<Tag> tags;
        private List<Integer> jobs;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor. A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setLease(toCopy.lease);
            setLeaseAmount(toCopy.leaseAmount);
            setPayDate(toCopy.payDate);
            setTags(toCopy.tags);
            setJobs(toCopy.jobs);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, lease, leaseAmount, payDate,
                    tags, jobs);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setLease(Lease lease) {
            this.lease = lease;
        }

        public Optional<Lease> getLease() {
            return Optional.ofNullable(lease);
        }

        public void setLeaseAmount(LeaseAmount leaseAmount) {
            this.leaseAmount = leaseAmount;
        }

        public Optional<LeaseAmount> getLeaseAmount() {
            return Optional.ofNullable(leaseAmount);
        }

        public void setPayDate(PayDate payDate) {
            this.payDate = payDate;
        }

        public Optional<PayDate> getPayDate() {
            return Optional.ofNullable(payDate);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public void setJobs(List<Integer> jobs) {
            this.jobs = (jobs != null) ? new ArrayList<>(jobs) : null;
        }

        public Optional<List<Integer>> getJobs() {
            return (jobs != null) ? Optional.of(Collections.unmodifiableList(jobs)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(lease, otherEditPersonDescriptor.lease)
                    && Objects.equals(leaseAmount, otherEditPersonDescriptor.leaseAmount)
                    && Objects.equals(payDate, otherEditPersonDescriptor.payDate)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(jobs, otherEditPersonDescriptor.jobs);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("lease", lease)
                    .add("lease amount", leaseAmount)
                    .add("pay date", payDate)
                    .add("tags", tags)
                    .add("jobs", jobs)
                    .toString();
        }
    }
}
