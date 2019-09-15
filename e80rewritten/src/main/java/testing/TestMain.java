package testing;

import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.interpreter.token.Tokenizer;

import java.util.List;

public class TestMain {

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();

        // the input is "\"hello, world\" or \"Hello World\"", 3.14, \"9.81\", .05
        List<Token> tokens = tokenizer.tokenize("\"\\\"hello, world!\\\" or \\\"Hello World!\\\"?\", 3.14, \"9.81\", .05");

        for (Token token : tokens) {
            System.out.println(token.getValue());
        }
    }
}
