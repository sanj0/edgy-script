package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptLine;

public class Math implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        switch (function.toLowerCase()) {
            case "round":
                return new DirectValue(String.valueOf(java.lang.Math.round(args.get(0).getNumber())));

            case "e":
            case "euler":
                return new DirectValue(String.valueOf(java.lang.Math.E));

            case "pi":
                return new DirectValue(String.valueOf(java.lang.Math.PI));

            case "squareroot":
            case "sqrt":
                return new DirectValue(String.valueOf(java.lang.Math.sqrt(args.get(0).getNumber())));

            case "power":
            case "pow":
                return new DirectValue(String.valueOf(java.lang.Math.pow(args.get(0).getNumber(), args.get(1).getNumber())));

            case "negate":
                return new DirectValue(String.valueOf(-args.get(0).getNumber()));

            case "increment":
                return new DirectValue(String.valueOf(args.get(0).getNumber() + 1));

            case "decrement":
                return new DirectValue(String.valueOf(args.get(0).getNumber() - 1));

        }
        return null;
    }
}
