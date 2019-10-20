package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.Lexer;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;

import java.util.List;

public class StdIO implements NativeProvider {

    @Override
    public Value function(String function, List<Value> args) {
        switch (function.toLowerCase()) {
            case "print":
                String value = args.get(0).getValue();
                value = value.replace("\\n", Lexer.lineSeparator);
                System.out.print(value);
                return new DirectValue(value);

            default:
                return null;
        }
    }
}
