package tests;
import commands.*;
import mainPart.CommandDecoder;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.LinkedList;
//
//public class CommandDecoderGoodInputTest {
//
//    CommandDecoder commandDecoder = new CommandDecoder(new LinkedList<>());
//
//    @Test
//    void testCommandDecoderCommandWithoutAdditionalArgument1() {
//        assertEquals(HelpCommand.class, commandDecoder.decode("help").getClass());
//    }
//
//    @Test
//    void testCommandDecoderCommandWithoutAdditionalArgument2() {
//        assertEquals(ShowCommand.class, commandDecoder.decode("show").getClass());
//    }
//
//    @Test
//    void testCommandDecoderCommandWithAdditionalArgument1() {
//        assertEquals(UpdateCommand.class, commandDecoder.decode("update 1").getClass());
//    }
//
//    @Test
//    void testCommandDecoderCommandWithAdditionalArgument2() {
//        assertEquals(FilterGreaterThanPriceCommand.class, commandDecoder.decode("filter_greater_than_price 1").getClass());
//    }
//
//    @Test
//    void testCommandDecoderCapsLockCommand() {
//        assertEquals(HelpCommand.class, commandDecoder.decode("HELP").getClass());
//    }
//
//    @Test
//    void testCommandDecoderCommandWithWhiteSpaces() {
//        assertEquals(HelpCommand.class, commandDecoder.decode("            help     ").getClass());
//    }
//}
