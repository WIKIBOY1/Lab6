package tests;

import exceptions.IllegalCountOfArgumentsException;
import mainPart.CommandDecoder;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedList;
//
//
//class CommandDecoderBadInputTest {
//
//    CommandDecoder commandDecoder = new CommandDecoder(new LinkedList<>());
//
//    @Test
//    void testNullInput() {
//        Assertions.assertThrows(NullPointerException.class, () -> commandDecoder.decode(null));
//    }
//
//    @Test
//    void testEmptyStringInput() {
//        Assertions.assertThrows(NullPointerException.class, () -> commandDecoder.decode(""));
//    }
//
//    @Test
//    void testValidCommandWithIncorrectArgumentCount() {
//        Assertions.assertThrows(IllegalCountOfArgumentsException.class, () -> commandDecoder.decode("help 123"));
//    }
//
//    @Test
//    void testIncorrectCommand() {
//        Assertions.assertThrows(NullPointerException.class, () -> commandDecoder.decode("sadsa"));
//    }
//
//    @Test
//    void testInvalidTypeOfArgument() {
//        Assertions.assertThrows(NumberFormatException.class, () -> commandDecoder.decode("update sfdsf"));
//    }
//
//    @Test
//    void testTooBigArgument() {
//        Assertions.assertThrows(NumberFormatException.class, () -> commandDecoder.decode("remove_by_id 999999999999999999999"));
//    }
//}