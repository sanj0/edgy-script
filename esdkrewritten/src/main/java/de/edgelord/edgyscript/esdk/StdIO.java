package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.*;

import java.util.List;
import java.util.function.Consumer;

public class StdIO implements NativeProvider {

    @Override
    public Value function(String function, List<Value> args) {

        switch (function.toLowerCase()) {
            case "print":
                String value = args.get(0).getValue();
                value = value.replace("\\n", Lexer.lineSeparator);
                print(value);
                return new DirectValue(value);

            case "printf":
                String text = args.get(0).getValue();
                String[] arguments = new String[args.size() - 1];
                args.subList(1, args.size()).forEach(new Consumer<Value>() {
                    int i = 0;
                    @Override
                    public void accept(Value value) {
                        arguments[i] = value.getValue();
                        i++;
                    }
                });
                String formattedString = String.format(text, arguments);
                print(formattedString);
                return new DirectValue(formattedString);

            default:
                return null;
        }
    }

    public static void print(String text) {
        System.out.print(text.replace("\\n", Lexer.lineSeparator));
    }
}
