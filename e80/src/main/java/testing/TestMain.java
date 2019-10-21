package testing;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.Tokenizer;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.script.Script;

import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.util.List;

public class TestMain {

    public static void main(String[] args) throws ScriptException, FileNotFoundException {
        Interpreter.ESDK = new DummyESDK();
        Lexer lexer = new Lexer();

        System.out.println("\n\nTest 1: args\n");
        // the input is "\"hello, world\" or \"Hello World\"", 3.14, \"9.81\", .05
        testTokenize("\"\\\"hello, world!\\\" or \\\"Hello World!\\\"?\", 3.14, \"9.81\", .05", lexer);

        System.out.println("\n\nTest 2: an entire line\n");
        testTokenize("var myVar = 314 and increment and print \"myVar is \" + myVar", lexer);

        System.out.println("\n\nTest 3: Tokens to Values\n");
        testValuenize("3.14 * (2 + 3)", lexer);

        System.out.println("\n\n\nFinal Test: A script");
        Script testScript = new Script("/Users/edgelord/git/edgy-script/e80rewritten/testScript.et");
        testScript.compile();
        testScript.run();
    }

    private static void testTokenize(String s, Lexer lexer) {
        List<Token> tokens = lexer.tokenize(s);

        for (Token token : tokens) {
            System.out.println(token.getClass().getSimpleName() + ":" +  token.getValue());
        }
    }

    private static void testValuenize(String s, Lexer lexer) throws ScriptException {
        List<Value> values = Tokenizer.evaluateTokens(lexer.tokenize(s));

        for (Value value : values) {
            System.out.println(value.getClass().getSimpleName() + ":" + value.getValue());
        }
    }
}
