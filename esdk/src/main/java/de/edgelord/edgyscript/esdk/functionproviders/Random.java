package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;

public class Random implements NativeProvider {

    private static java.util.Random rng = new java.util.Random();
    @Override
    public Value function(String function, ArgumentList args) {

        switch (function.toLowerCase()) {
            case "rng":
            case "nextint":
            case "randomint":
                int number = -1;

                if (args.size() == 0) {
                    number = rng.nextInt();
                } else if (args.size() == 1) {
                    number = rng.nextInt(args.get(0).getInt());
                } else if (args.size() == 2) {
                    int lowBound = args.get(0).getInt();
                    int highBound = args.get(1).getInt();
                    number = lowBound + rng.nextInt((highBound - lowBound) + 1);
                }

                return new DirectValue(String.valueOf(number));
        }
        return null;
    }
}
