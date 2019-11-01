package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

public class FileIO implements NativeProvider {
    @Override
    public Value function(String function, List<Value> args) {

        switch (function.toLowerCase()) {
            case "readfile":
            case "read":
                String fileContent = "null";
                try {
                    fileContent = new String(Files.readAllBytes(Paths.get(args.get(0).getValue())), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return new DirectValue(fileContent);
            case "writefile":
            case "write":
                PrintStream stream = null;
                String value = args.get(1).getValue();
                try {
                    stream = new PrintStream(args.get(0).getValue());
                    stream.println(value);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
                return new DirectValue(value);
        }
        return null;
    }
}
