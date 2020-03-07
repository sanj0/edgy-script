package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptException;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.concurrent.TimeUnit;

public class System implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        switch (function.toLowerCase()) {
            case "exit":
            case "shutdown":
            case "quit":
                java.lang.System.exit(args.get(0).getInt());
                return new DirectValue("");

            case "throw":
            case "error":
            case "exception":
                String msg = args.size() > 0 ? args.get(0).getValue() : "";
                throw new ScriptException(msg);

            case "property":
            case "getproperty":
                return new DirectValue(java.lang.System.getProperty(args.get(0).getValue()));

            case "readpw":
            case "readpassowrd":
                return new DirectValue(new String(java.lang.System.console().readPassword()));

            case "currenttimemillis":
            case "millis":
            case "milliseconds":
            case "unixtime":
                return new DirectValue(String.valueOf(java.lang.System.currentTimeMillis()));

            case "nanotime":
            case "nanos":
            case "nanoseconds":
                return new DirectValue(String.valueOf(java.lang.System.nanoTime()));

            case "executeall":
                for (Value value : args) {
                    value.getValue();
                }
                return new DirectValue("");

            case "wait":
                long delay = args.get(0).getInt();
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return new DirectValue(String.valueOf(delay));
        }
        return null;
    }
}
