package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptLine;

import java.util.HashMap;
import java.util.Map;

public class Thread implements NativeProvider {

    private static Map<String, java.lang.Thread> THREADS = new HashMap<>();

    @Override
    public Value function(String function, ArgumentList args, ScriptLine line) {

        String threadName = args.get(0).getValue();
        switch (function.toLowerCase()) {
            case "thread":
                Value methodCall = args.get(1);
                THREADS.put(threadName, new java.lang.Thread(methodCall::getValue));
                return new DirectValue(threadName);

            case "run":
                THREADS.get(threadName).start();
                return new DirectValue(threadName);

            case "loop":
                Value condition = args.get(1, "condition to avoid an infinite loop within thread");
                java.lang.Thread thread = THREADS.get(threadName);
                new java.lang.Thread(() -> {
                    while (condition.getBoolean()) {
                        thread.run();
                    }
                }).start();
                return new DirectValue(threadName);
        }
        return null;
    }
}
