package seedu.estatemate.logic.parser;

import static seedu.estatemate.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.estatemate.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.estatemate.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.estatemate.logic.commands.AddJobCommand;
import seedu.estatemate.model.job.Description;

public class AddJobCommandParserTest {

    private final AddJobCommandParser parser = new AddJobCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // with preamble whitespace
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + " d/Fix sink leak",
                new AddJobCommand(new Description("Fix sink leak")));

        // minimal valid description
        assertParseSuccess(parser,
                " d/a",
                new AddJobCommand(new Description("a")));

        // emojis / punctuation allowed
        assertParseSuccess(parser,
                " d/🛠️ replace lights #2",
                new AddJobCommand(new Description("🛠️ replace lights #2")));
    }

    @Test
    public void parse_missingPrefix_failure() {
        // no d/ prefix
        assertParseFailure(parser,
                "Fix sink leak",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + " d/Fix sink leak",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDescription_failure() {
        // blank
        assertParseFailure(
                parser,
                " d/",
                seedu.estatemate.model.job.Description.MESSAGE_CONSTRAINTS
        );

        // blank (after trim)
        assertParseFailure(
                parser,
                " d/   ",
                seedu.estatemate.model.job.Description.MESSAGE_CONSTRAINTS
        );
    }
}
