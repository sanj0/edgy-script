package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.script.ArgumentList;

public interface NativeProvider {

    Value function(String function, ArgumentList args);
}
