package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.script.ArgumentList;
import de.edgelord.edgyscript.e80.script.ScriptLine;

public interface NativeProvider {

    Value function(String function, ArgumentList args, ScriptLine line);
}
