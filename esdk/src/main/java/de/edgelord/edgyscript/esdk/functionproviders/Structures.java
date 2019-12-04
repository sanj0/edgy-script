package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptLine;

public class Structures implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        switch (function.toLowerCase()) {
            case "if":
                if (args.get(0).getBoolean()) {
                     return new DirectValue(args.get(1).getValue());
                } else {
                    return new DirectValue("");
                }

            case "ifelse":
                if (args.get(0).getBoolean()) {
                    return new DirectValue(args.get(1).getValue());
                } else if (args.get(2).getBoolean()) {
                    return new DirectValue(args.get(3).getValue());
                }

            case "while":
                Value currValue = new DirectValue("");
                while (args.get(0).getBoolean()) {
                    currValue.setValue(args.get(1).getValue());
                }
                return currValue;
        }
        return null;
    }
}
