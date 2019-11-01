package de.edgelord.edgyscript.esdk;

import de.edgelord.edgyscript.e80.interpreter.DirectValue;
import de.edgelord.edgyscript.e80.interpreter.NativeProvider;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.script.ArgumentList;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Files implements NativeProvider {
    @Override
    public Value function(String function, ArgumentList args) {

        File file = new File(args.get(0).getValue());
        switch (function.toLowerCase()) {
            case "createfile":
            case "newfile":
                DirectValue returnVal = new DirectValue("null");
                try {
                    returnVal = new DirectValue(String.valueOf(file.createNewFile()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return returnVal;

            case "mkdir":
                return new DirectValue(String.valueOf(file.mkdir()));

            case "mkdirs":
                return new DirectValue(String.valueOf(file.mkdirs()));

            case "fileexists":
            case "exists":
                return new DirectValue(String.valueOf(file.exists()));

            case "isdir":
            case "isdirectory":
                return new DirectValue(String.valueOf(file.isDirectory()));

            case "delete":
                return new DirectValue(String.valueOf(file.delete()));

            case "executable":
                return new DirectValue(String.valueOf(file.canExecute()));

            case "readable":
                return new DirectValue(String.valueOf(file.canRead()));

            case "writeable":
                return new DirectValue(String.valueOf(file.canWrite()));

            case "filename":
                return new DirectValue(file.getName());

            case "tourl":
                try {
                    return new DirectValue(String.valueOf(file.toURI().toURL()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return new DirectValue("null");
        }
        return null;
    }
}
