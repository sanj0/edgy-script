package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.interpreter.*;
import de.edgelord.edgyscript.e80.script.ArgumentList;

import java.util.Scanner;
import java.util.function.Consumer;

public class StdIO implements NativeProvider {

    public static final Scanner STDINPUT = new Scanner(java.lang.System.in);

    @Override
    public Value function(String function, ArgumentList args) {

        switch (function.toLowerCase()) {
            case "print":
                String value = args.get(0).getValue();
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

            case "input":
            case "read":
            case "scan":

                if (args.size() >= 1) {
                    print(args.get(0).getValue());
                }
                String input = STDINPUT.nextLine();
                return new DirectValue(input);
            default:
                return null;
        }
    }

    public static void print(String text) {
        java.lang.System.out.print(text.replace("\\n", Lexer.lineSeparator));
    }
}
