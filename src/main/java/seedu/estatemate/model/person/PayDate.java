package seedu.estatemate.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.estatemate.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents a Person's pay date in EstateMate.
 * Guarantees: immutable; is valid as declared in {@link #isValidPayDate(String)}
 */
public class PayDate {

    /**
     * The first character of the pay date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\d{4}-\\d{2}-\\d{2}";

    public static final String MESSAGE_CONSTRAINTS = "Pay date must be real calendar date: yyyy-MM-dd";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    public final String value;

    private final LocalDate date;

    /**
     * Constructs a {@code PayDate}.
     *
     * @param payDate A valid pay date.
     */
    public PayDate(String payDate) {
        requireNonNull(payDate);
        checkArgument(isValidPayDate(payDate), MESSAGE_CONSTRAINTS);
        this.value = payDate;
        this.date = LocalDate.parse(value, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date in the correct format.
     */
    public static boolean isValidPayDate(String test) {
        if (test == null) {
            return false;
        }
        return test.matches(VALIDATION_REGEX) && isValidDates(test);
    }

    /**
     * Returns true if pay date is a date that exist, assuming that it has already pass the VALIDATION_REGEX format.
     */
    private static boolean isValidDates(String test) {
        try {
            LocalDate.parse(test, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return date.format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PayDate otherPayDate)) {
            return false;
        }

        return date.equals(otherPayDate.date);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
