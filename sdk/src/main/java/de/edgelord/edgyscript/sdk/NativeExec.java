package de.edgelord.edgyscript.sdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;

public class NativeExec extends FunctionProvider {

    private static ScriptEngine javaScriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
    private static ScriptContext javaScriptContext = javaScriptEngine.getContext();
    private static StringWriter javaScriptWriter = new StringWriter();
    static {
        javaScriptContext.setWriter(javaScriptWriter);
    }

    @Override
    public Variable function(String s, Variable[] variables, ScriptFile scriptFile) {
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
