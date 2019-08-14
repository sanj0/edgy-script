package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.Variable;

/**
 * provides functions:
 *
 * - systemctrl (alias systemctl, syscontrol)
 *     several options via first given arg:
 *       - "shutdown" (alias "exit"): shuts the jvm down
 *       - "vars" (alias "variables"): shows a list of all variables and their values (expect temporary ones)
 *
 * - systemprop (alias sysprop, systemProperty)
 *     returns the value of the given system property (e.g. "user.home", "user.name", "os.name", "os.version"...)
 *
 * - string
 */
public class SystemProvider extends FunctionProvider {
    @Override
    public Variable function(String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("systemctrl") || name.equalsIgnoreCase("systemctl") || name.equalsIgnoreCase("syscontrol")) {
            String command = variables[0].getString();

            if (command.equalsIgnoreCase("shutdown") || command.equalsIgnoreCase("exit")) {
                System.exit(0);
            } else if (command.equalsIgnoreCase("vars") || command.equalsIgnoreCase("variables")) {
                for (Variable var : scriptFile.getVarPool()) {
                    System.out.println(var.getName() + " : " + var.getString());
                }
            }
            return Variable.empty();
        }

        if (name.equalsIgnoreCase("systemprop") || name.equalsIgnoreCase("sysprop") || name.equalsIgnoreCase("systemproperty")) {
            return new Variable(scriptFile.nextTempvar(), System.getProperty(variables[0].getString()));
        }

        return null;
    }
}
