package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;

import java.util.ArrayList;
import java.util.List;

public class ESDK implements NativeProvider {

    private List<NativeProvider> usedNativeProviders = new ArrayList<>();

    @Override
    public Value function(String function, List<Value> args) {

        // use a dependency e.g. use stdio
        if (function.equalsIgnoreCase("use")) {
            String value = args.get(0).getValue();
            try {
                usedNativeProviders.add(getDependency(value));
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return new DirectValue(value);
        } else {
            Value returnVal = null;

            for (int i = 0; i < usedNativeProviders.size() && returnVal == null; i++) {
                returnVal = usedNativeProviders.get(i).function(function, args);
            }
            return returnVal;
        }
    }

    private NativeProvider getDependency(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        switch (name.toLowerCase()) {
            case "stdio":
                return Class.forName("de.edgelord.edgyscript.esdk.StdIO").asSubclass(NativeProvider.class).newInstance();
            default:
                return Class.forName(name).asSubclass(NativeProvider.class).newInstance();
        }
    }
}
