package de.edgelord.edgyscript.e80;

public abstract class FunctionProvider {

    /**
     *
     * @param name the name of the function
     * @param args the arguments to the function
     * @param script the script
     * @return true if the function was found
     */
    public abstract Variable function(ScriptLine line, String name, Variable[] args, ScriptFile script);
}
