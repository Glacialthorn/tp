package seedu.estatemate.logic.parser;

import static seedu.estatemate.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.estatemate.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.estatemate.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.estatemate.logic.commands.DeleteTenantCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations outside of the DeleteCommand
 * code. For example, inputs "1" and "1 abc" take the same path through the DeleteCommand, and therefore we test only
 * one of them. The path variation for those two cases occur inside the ParserUtil, and therefore should be covered by
 * the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteTenantCommandParser parser = new DeleteTenantCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteTenantCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteTenantCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_outOfRangeIndex_failure() {
        assertParseFailure(parser, "2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
