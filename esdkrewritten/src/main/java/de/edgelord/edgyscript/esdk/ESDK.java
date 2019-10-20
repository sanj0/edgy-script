package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;

import java.util.*;

public class ESDK implements NativeProvider {

    private Map<String, NativeProvider> usedNativeProviders = new HashMap<>();

    @Override
    public Value function(String function, List<Value> args) {

        // use a dependency e.g. use stdio
        if (function.equalsIgnoreCase("use")) {
            String value = args.get(0).getValue();
            try {
                usedNativeProviders.put(value, getDependency(value));
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return new DirectValue(value);
        } else {
            Value returnVal = null;

            if (function.contains(".")) {
                String[] parts = function.split("\\.", 2);
                return usedNativeProviders.get(parts[0]).function(parts[1], args);
            }

            Iterator it = usedNativeProviders.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                NativeProvider np = (NativeProvider) pair.getValue();
                returnVal = np.function(function, args);

                if (returnVal != null) {
                    break;
                }
                it.remove();
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
