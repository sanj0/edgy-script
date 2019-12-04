package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptLine;

public class Variables implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        switch (function.toLowerCase()) {
            case "isboolean":
                return new DirectValue(String.valueOf(args.get(0).isBoolean()));

            case "isnumber":
            case "isnumeric":
                return new DirectValue(String.valueOf(args.get(0).isNumeric()));
        }
        return null;
    }
}
