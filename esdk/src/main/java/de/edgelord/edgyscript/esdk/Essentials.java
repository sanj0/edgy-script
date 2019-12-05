package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.*;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

public class Essentials implements NativeProvider {

    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        switch (function.toLowerCase()) {
            case "ev":
            case "eval":
            case "evaluate":
                return new DirectValue(args.get(0).getValue());

            case "deletescopevar":
            case "purgescopevar":
            case "delscopevar":
            case "rmscopevar":
            case "removescopevar":
                String varName = args.get(0).getValue();
                Interpreter.SCOPE.remove(varName);
                return new DirectValue(varName);

            case "deletevar":
            case "purgevar":
            case "delvar":
            case "vardel":
            case "removevar":
            case "rmvar":
                if (!(args.get(0) instanceof LinkedValue)) {
                    throw new ScriptException(function + "expects a variable!");
                }
                String var = args.get(0).getID();
                Interpreter.MEMORY.remove(var);
                return new DirectValue(var);
        }
        return null;
    }
}
