package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.*;

public class ESDK implements NativeProvider {

    private Map<String, NativeProvider> usedNativeProviders = new HashMap<>();

    public ESDK() {
        usedNativeProviders.put("", new Essentials());
    }

    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        // use a dependency e.g. use stdio
        if (function.equalsIgnoreCase("use") || function.equalsIgnoreCase("import")) {
            String value = args.get(0).getValue();
            String usedName = value;

            if (args.size() >= 3) {
                if (args.get(1).getValue().equalsIgnoreCase("as")) {
                    usedName = args.get(2).getValue();
                    if (usedName.equals("")) {
                        throw new ScriptException("cannot use " + usedName + " as " + "\"\"");
                    }
                }
            }
            try {
                usedNativeProviders.put(usedName, getDependency(value));
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return new DirectValue(value);
        } else {
            Value returnVal = null;

            if (function.contains(".")) {
                String[] parts = function.split("\\.", 2);
                return usedNativeProviders.get(parts[0]).function(parts[1], args, line);
            }

            for (Map.Entry<String, NativeProvider> stringNativeProviderEntry : usedNativeProviders.entrySet()) {
                NativeProvider np = (NativeProvider) ((Map.Entry) stringNativeProviderEntry).getValue();
                returnVal = np.function(function, args, line);

                if (returnVal != null) {
                    break;
                }
            }
            return returnVal;
        }
    }

    public String getUsedProviders() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, NativeProvider> stringNativeProviderEntry : usedNativeProviders.entrySet()) {
            builder.append(stringNativeProviderEntry.getValue().getClass().getSimpleName());
            builder.append(", ");
        }

        return builder.toString();
    }

    private NativeProvider getDependency(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        switch (name.toLowerCase()) {
            case "stdio":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.StdIO").asSubclass(NativeProvider.class).newInstance();

            case "strings":
            case "string":
            case "stringutils":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Strings").asSubclass(NativeProvider.class).newInstance();

            case "fileio":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.FileIO").asSubclass(NativeProvider.class).newInstance();

            case "files":
            case "file":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Files").asSubclass(NativeProvider.class).newInstance();

            case "structures":
            case "controls":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Structures").asSubclass(NativeProvider.class).newInstance();

            case "math":
            case "maths":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Math").asSubclass(NativeProvider.class).newInstance();

            case "system":
            case "os":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.System").asSubclass(NativeProvider.class).newInstance();

            case "variables":
            case "vars":
            case "varutils":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Variables").asSubclass(NativeProvider.class).newInstance();

            case "random":
            case "rng":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Random").asSubclass(NativeProvider.class).newInstance();

            case "arrays":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Arrays").asSubclass(NativeProvider.class).newInstance();

            case "thread":
                return Class.forName("de.edgelord.edgyscript.esdk.functionproviders.Thread").asSubclass(NativeProvider.class).newInstance();

            default:
                return Class.forName(name).asSubclass(NativeProvider.class).newInstance();
        }
    }
}
