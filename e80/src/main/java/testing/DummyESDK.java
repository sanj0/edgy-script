package testing;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;

import java.util.Scanner;

public class DummyESDK implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args) {

        System.out.println("native call to " + function);
        if (function.equalsIgnoreCase("println")) {
            System.out.println(args.get(0).getValue());
        }

        if (function.equalsIgnoreCase("input")) {
            return new DirectValue(new Scanner(System.in).next());
        }
        return new DirectValue("dummy-value");
    }
}
