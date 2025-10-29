package seedu.estatemate.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.estatemate.commons.util.AppUtil.checkArgument;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Represents a Person's lease amount in EstateMate.
 * Guarantees: immutable; is valid as declared in {@link #isValidLeaseAmount(String)}
 */
public class LeaseAmount {

    public static final String MESSAGE_CONSTRAINTS = "Lease amounts must be non-negative, have 1 or more "
            + "integer digits, a decimal point and 2 decimal digits (eg. 1234.56).";

    /*
     * The input is non-negative and has 1 or more integer digits, a decimal point and 2 decimal digits.
     * Leading zeroes such as 0001.30 are accepted as it matches BigDecimal behaviour.
     */
    public static final String VALIDATION_REGEX = "\\d+\\.\\d{2}";

    public final String value;

    private final BigDecimal leaseAmount;

    /**
     * Constructs a {@code LeaseAmount}.
     *
     * @param leaseAmount A valid lease amount.
     */
    public LeaseAmount(String leaseAmount) {
        requireNonNull(leaseAmount);
        checkArgument(isValidLeaseAmount(leaseAmount), MESSAGE_CONSTRAINTS);
        value = leaseAmount;
        this.leaseAmount = new BigDecimal(leaseAmount);
    }

    /**
     * Returns true if a given string is in a valid lease amount format.
     */
    public static boolean isValidLeaseAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Converts the lease amount into a more human-readable string representation, intended for display purposes.
     * @return A display friendly string representing the lease amount.
     */
    public String toDisplayValue() {
        // Solution below adapted from: https://stackoverflow.com/a/3395845
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(leaseAmount);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LeaseAmount)) {
            return false;
        }

        LeaseAmount otherLeaseAmount = (LeaseAmount) other;
        return leaseAmount.equals(otherLeaseAmount.leaseAmount);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
