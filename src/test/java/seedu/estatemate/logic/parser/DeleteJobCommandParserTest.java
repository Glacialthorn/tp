package seedu.estatemate.logic.parser;

import static seedu.estatemate.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.estatemate.logic.commands.DeleteJobCommand;

public class DeleteJobCommandParserTest {

    private final DeleteJobCommandParser parser = new DeleteJobCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "1", new DeleteJobCommand(1));
        assertParseSuccess(parser, "   12  ", new DeleteJobCommand(12));
    }

    @Test
    public void parse_invalidArgs_failure() {
        // non-numeric
        assertParseFailure(parser, "abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE));

        // empty
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobCommand.MESSAGE_USAGE));
    }
}
