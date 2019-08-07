package de.edgelord.edgyscript.sdk;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;
import de.edgelord.edgyscript.sdk.exceptions.SDKException;

import javax.script.ScriptException;

public class Math extends FunctionProvider {
    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equals("math")) {
            StringBuilder mathExpr = new StringBuilder();

            for (Variable var : variables) {
                if (var.isNumber() || var.isMathOperator()) {
                    mathExpr.append(var.getString());
                } else {
                    throw new SDKException("Variable " + var.getName() + " : " + var.getString() + " is not a number!", scriptFile);
                }
            }

            try {
                return new Variable(scriptFile.nextTempvar(), NativeExec.getJavaScriptEngine().eval(mathExpr.toString()).toString());
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
