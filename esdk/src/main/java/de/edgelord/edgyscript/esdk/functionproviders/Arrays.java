package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides functions:
 *
 * - array (alias createarray)
 *     - creates a new, empty array with the given name and the given number of entries. returns the name of the array
 *
 * - name of an array (w/o "")
 *     - applies a given command to the array with the name (and in some cases to the given entry number)
 *         - commands: set, gte, length, clone
 *
 * Example usage:
 * // in the following lines, the simpleStringMode hasn't been changed to false
 * // for simplification, it is convention to use "" in some places anyways.
 * array countries 5
 * countries set 0 "Germany"
 * countries set 1 "USA"
 * countries set 2 "France"
 * countries set 3 "Brazil"
 * countries set 4 "England"
 *
 * # clone "countries" and name the new array "countriesCopy"
 * countries clone countriesCopy
 *
 * countries get 2 and writeLine
 * countries clear
 * # should now be 0
 * countries length and writeLine
 *
 */
public class Arrays extends FunctionProvider {

    private static Map<String, Variable[]> arrays = new HashMap<>();

    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("array") || name.equalsIgnoreCase("createarray")) {
            arrays.put(variables[0].getString(), new Variable[variables[1].getInt()]);
            return new Variable(scriptFile.nextTempvar(), variables[0].getString());
        }

        if (arrays.containsKey(name)) {
            String command = variables[0].getString().toLowerCase();
            Variable[] array = arrays.get(name);

            switch (command) {
                case "set":
                    array[variables[1].getInt()] = variables[2];
                    return variables[2];
                case "get":
                    return array[variables[1].getInt()];
                case "length":
                    return new Variable(scriptFile.nextTempvar(), String.valueOf(array.length));
                case "clone":
                    arrays.put(variables[1].getString(), array.clone());
                    return variables[1];
            }
        }

        return null;
    }
}
