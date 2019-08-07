package de.edgelord.edgyscript.sdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

/**
 * Provides functions:
 *
 * - create (alias var)
 *     creates a new empty {@link Variable} with the name being the first arg and adds it to the pool. returns the created var
 *
 * - set
 *     sets the value of the variable with the name of the first arg equal to the second arg. returns the var
 *
 * - evalset (alias exprset)
 *     - sets the value of the first given variable to the returning value of the given second variable holding an expression. returns the assigned value
 *
 * - createset
 *     a combination of create and set, the first argument is the name and the second is the value
 *
 * Example usage:
 *
 * create greet
 * set greet "hello, world!"
 *
 * createset greet2 "Hello World!"
 */
public class Variables extends FunctionProvider {

    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {
        if (name.equals("create") || name.equals("var")) {
            Variable var = new Variable(variables[0].getString(), "");
            scriptFile.getVarPool().add(var);
            return var;
        }

        if (name.equals("set")) {
            Variable var = scriptFile.getVar(variables[0].getString());
            var.setValue(variables[1].getString());
            return var;
        }

        if (name.equals("createset")) {
            Variable var = new Variable(variables[0].getString(), variables[1].getString());
            scriptFile.getVarPool().add(var);
            return var;
        }

        if (name.equals("evalset") || name.equals("exprset")) {
            Variable var = Interpreter.eval(variables[1].getString(), scriptFile);
            variables[0].setValue(var.getString());
            return var;
        }

        return null;
    }
}
