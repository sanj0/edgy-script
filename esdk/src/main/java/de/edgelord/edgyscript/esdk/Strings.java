package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;

public class Strings implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args) {

        switch (function.toLowerCase()) {

            case "join":
            case "merge":
                StringBuilder value = new StringBuilder();

                for (Value v : args) {
                    value.append(v.getValue());
                }
                return new DirectValue(value.toString());

            case "lowercase":
                return new DirectValue(args.get(0).getValue().toLowerCase());

            case "uppercase":
                return new DirectValue(args.get(0).getValue().toUpperCase());

            case "equals":
            case "equal":
            case "eq":
                return new DirectValue(String.valueOf(args.get(0).getValue().equals(args.get(1).getValue())));

            case "charat":
                return new DirectValue(Character.toString(args.get(0).getValue().charAt(args.get(1).getInt())));

            case "substring":
                return new DirectValue(args.get(0).getValue().substring(args.get(1).getInt(), args.get(2).getInt()));

            case "trim":
                return new DirectValue(args.get(0).getValue().trim());

            case "replace":
                return new DirectValue(args.get(0).getValue().replace(args.get(1).getValue(), args.get(2).getValue()));

            case "replaceFirst":
                return new DirectValue(args.get(0).getValue().replaceFirst(args.get(1).getValue(), args.get(2).getValue()));

            case "len":
            case "length":
                return new DirectValue(String.valueOf(args.get(0).getValue().length()));

            case "empty":
            case "isempty":
                return new DirectValue(String.valueOf(args.get(0).getValue().isEmpty()));

            case "startswith":
                return new DirectValue(String.valueOf(args.get(0).getValue().startsWith(args.get(1).getValue())));

            case "endswith":
                return new DirectValue(String.valueOf(args.get(0).getValue().endsWith(args.get(1).getValue())));

            case "hash":
            case "hashcode":
                return new DirectValue(String.valueOf(args.get(0).getValue().hashCode()));

            case "indexof":
                return new DirectValue(String.valueOf(args.get(0).getValue().indexOf(args.get(1).getValue())));

            default:
                return null;
        }
    }
}
