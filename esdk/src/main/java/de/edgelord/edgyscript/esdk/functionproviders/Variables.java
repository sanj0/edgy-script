package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.Interpreter;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import javax.script.ScriptException;

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

            if (variables.length == 3 && variables[1].getString().equals("=")) {
                var.setValue(variables[2].getString());
            }

            scriptFile.getVarPool().add(var);
            return var;
        }

        if (name.equals("set")) {
            Variable var = scriptFile.getVar(variables[0].getName());
            var.setValue(variables[1].getString());

            if (NativeExec.jsFedVars.contains(var)) {
                try {
                    NativeExec.getJavaScriptEngine().eval(var.getName() + " = " + var.getValueForJS());
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
            return var;
        }

        if (name.equals("createset")) {
            Variable var = new Variable(variables[0].getString(), variables[1].getString());
            scriptFile.getVarPool().add(var);
            try {
                NativeExec.getJavaScriptEngine().eval("var " + var.getName() + " = " + var.getValueForJS());
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            return var;
        }

        if (name.equals("evalset") || name.equals("exprset")) {
            Variable var = Interpreter.execLine(variables[1].getString(), scriptFile, false);
            variables[0].setValue(var.getString());

            if (NativeExec.jsFedVars.contains(var)) {
                try {
                    NativeExec.getJavaScriptEngine().eval(var.getName() + " = " + var.getValueForJS());
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
            return var;
        }

        return null;
    }
}
