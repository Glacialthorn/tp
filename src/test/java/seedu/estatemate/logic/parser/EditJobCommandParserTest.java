package seedu.estatemate.logic.parser;

import static seedu.estatemate.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.estatemate.logic.commands.EditJobCommand;
import seedu.estatemate.model.job.Description;

public class EditJobCommandParserTest {

    private final EditJobCommandParser parser = new EditJobCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // basic
        assertParseSuccess(parser,
                "5 d/Repair corridor lights",
                new EditJobCommand(5, new Description("Repair corridor lights")));

        // with surrounding whitespace
        assertParseSuccess(parser,
                "   12   d/Refit door hinge   ",
                new EditJobCommand(12, new Description("Refit door hinge")));
    }

    @Test
    public void parse_missingId_failure() {
        // missing numeric preamble
        assertParseFailure(parser,
                " d/Fix sink leak",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonNumericId_failure() {
        assertParseFailure(parser,
                "abc d/Fix sink leak",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_failure() {
        // id present, but no d/ prefix
        assertParseFailure(parser,
                "3 Fix sink leak",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDescription_failure() {
        // blank description after trimming -> Description.MESSAGE_CONSTRAINTS
        assertParseFailure(parser,
                "3 d/   ",
                Description.MESSAGE_CONSTRAINTS);

        // empty right after prefix
        assertParseFailure(parser,
                "3 d/",
                Description.MESSAGE_CONSTRAINTS);
    }
}
