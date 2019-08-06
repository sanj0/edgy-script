package testing;

import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.Lexer;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestingMain {

    public static void main(String[] args) throws IOException {

        // first test: var parsing
        System.err.println("First test");
        ScriptFile scriptFile = new ScriptFile(File.createTempFile("foo", "bar"));
        scriptFile.getVarPool().add(new Variable("myVar", "foo bar"));

        List<Variable> vars = Lexer.parseVariables("314 \"hello, world!\", myVar, 3.14", scriptFile, false);

        for (Variable var : vars) {
            System.out.println(var.getName() + " : " + var.getString());
        }

        System.out.println();
        System.out.println();
        System.err.println("-----");
        System.out.println();
        System.out.println();

        // second test: test script
        System.err.println("Second test");
        ScriptFile testScript = new ScriptFile("/Users/edgelord/git/edgy-script/e80/testScript.es");
        Interpreter.interpretRun(testScript);
    }
}
