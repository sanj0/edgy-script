package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;

/**
 * Provides functions:
 *
 * - js (alisa javascript) builds a js script out of the given arguments and evaluates it, returning its output/value
 *
 * Example usage:
 * # do a simple math calculation, which could also be done using {@code math}
 * createset number1 2
 * createset number2 4
 * js number1 + number2
 * # in this case, the "+"-sign is a string
 */
public class NativeExec extends FunctionProvider {

    private static ScriptEngine javaScriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
    private static ScriptContext javaScriptContext = javaScriptEngine.getContext();
    private static StringWriter javaScriptWriter = new StringWriter();
    static {
        javaScriptContext.setWriter(javaScriptWriter);
    }

    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equals("js") || name.equals("javascript")) {

            StringBuilder command = new StringBuilder();
            for (Variable var : variables) {
                command.append(var.getString());
            }

            try {
                return new Variable(scriptFile.nextTempvar(), getJavaScriptEngine().eval(command.toString()).toString());
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Gets {@link #javaScriptEngine}.
     *
     * @return the value of {@link #javaScriptEngine}
     */
    public static ScriptEngine getJavaScriptEngine() {
        return javaScriptEngine;
    }

    public static String getJavaScriptOutput() {
        String output = javaScriptWriter.toString();
        javaScriptWriter.getBuffer().setLength(0);
        javaScriptWriter.getBuffer().trimToSize();

        return output;
    }
}
