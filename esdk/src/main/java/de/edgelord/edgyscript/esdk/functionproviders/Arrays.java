package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.Script;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.HashMap;
import java.util.Map;

public class Arrays implements NativeProvider {

    public static Map<String, String[]> arrays = new HashMap<>();

    static {
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("foreach");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("set");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("get");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("contains");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("indexof");

        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("array");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("create");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("createarray");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("new");
        Interpreter.SPECIAL_NATIVE_FUNCTIONS.add("newarray");
    }

    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        String arrayName = args.get(0, "array name").getValue();

        try {

            switch (function.toLowerCase()) {
                case "array":
                case "create":
                case "createarray":
                case "new":
                case "newarray":
                    arrays.put(arrayName, new String[args.get(1, "array length").getInt()]);
                    return new DirectValue(arrayName);

                case "length":
                case "len":
                    checkArray(arrayName);
                    return new DirectValue(String.valueOf(arrays.get(arrayName).length));

                case "set":
                    checkArray(arrayName);
                    String value = args.get(2, "new value for array entry").getValue();
                    arrays.get(arrayName)[args.get(1).getInt()] = value;
                    return new DirectValue(value);

                case "get":
                    checkArray(arrayName);
                    return new DirectValue(arrays.get(arrayName)[args.get(1, "index of desired element").getInt()]);

                case "clone":
                case "copy":
                    checkArray(arrayName);
                    String targetArrayName = args.get(1).getValue();
                    String[] originArray = arrays.get(arrayName);
                    int startIndex = 0;
                    int endIndex = originArray.length;

                    if (args.size() == 3) {
                        endIndex = args.get(2).getInt();
                    } else if (args.size() == 4) {
                        startIndex = args.get(2).getInt();
                        endIndex = args.get(3).getInt();
                    }
                    if (args.size() > 4) {
                        throw new ScriptException("too many arguments for " + function);
                    }

                    arrays.put(targetArrayName, java.util.Arrays.copyOfRange(originArray, startIndex, endIndex));
                    return new DirectValue(targetArrayName);

                case "tostring":
                    checkArray(arrayName);
                    String delimiter = "";
                    StringBuilder builder = new StringBuilder();

                    if (args.size() == 2) {
                        delimiter = args.get(1).getValue();
                    } else if (args.size() > 2) {
                        throw new ScriptException("too many arguments for " + function);
                    }
                    String[] array = arrays.get(arrayName);

                    for (int i = 0; i < array.length; i++) {
                        builder.append(array[i]);

                        // don't append the delimiter after the last element
                        if (i != array.length - 1) {
                            builder.append(delimiter);
                        }
                    }
                    return new DirectValue(builder.toString());

                case "foreach":
                    // foreach name in names
                    if (!args.get(1).getValue().equalsIgnoreCase("in")) {
                        throw new ScriptException("foreach syntax: \"foreach element in array:\"");
                    }

                    String varName = arrayName;
                    arrayName = args.get(2).getValue();
                    checkArray(arrayName);
                    String[] subject = arrays.get(arrayName);
                    int index = 0;
                    String indexName = "iteration";

                    for (String s : subject) {
                        Interpreter.SCOPE.put(varName, s);
                        Interpreter.SCOPE.put(indexName, index);
                        line.runSubLines();
                        index++;
                    }
                    Interpreter.SCOPE.remove(varName);
                    Interpreter.SCOPE.remove(indexName);
                    return new DirectValue("null");

                case "contains":
                    checkArray(arrayName);
                    String[] arr = arrays.get(arrayName);
                    String val = args.get(1).getValue();

                    for (String s : arr) {
                        if (s.equals(val)) {
                            return new DirectValue(String.valueOf(true));
                        }
                    }
                    return new DirectValue(String.valueOf(false));

                case "indexof":
                    checkArray(arrayName);
                    String element = args.get(1).getValue();

                    return new DirectValue(String.valueOf(java.util.Arrays.asList(arrays.get(arrayName)).indexOf(element)));
            }
        } catch (Exception e) {
            throw new ScriptException(e.getMessage());
        }
        return null;
    }

    private static void checkArray(String name) {
        if (!arrays.containsKey(name)) {
            throw new ScriptException("array " + name + " does not exist");
        }
    }
}
