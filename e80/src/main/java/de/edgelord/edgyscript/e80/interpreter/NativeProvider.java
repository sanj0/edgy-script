package de.edgelord.edgyscript.e80.interpreter;

import java.util.List;

public interface NativeProvider {

    Value function(String function, List<Value> args);
}
