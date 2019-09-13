package testing;

import de.edgelord.edgyscript.e80.interpreter.Token;
import de.edgelord.edgyscript.e80.interpreter.Tokenizer;

import java.util.List;

public class TestMain {

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();

        List<Token> tokens = tokenizer.tokenize("\"hello, world!\", 3.14, \"Hello World!\", .05");

        for (Token token : tokens) {
            System.out.println(token.getValue());
        }
    }
}
