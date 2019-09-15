package de.edgelord.edgyscript.e80.interpreter;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private static final List<String> keywords = new ArrayList<>();

    static {
        keywords.add("and");
        keywords.add("{");
        keywords.add("}");
        keywords.add("if");
        keywords.add("else");
        keywords.add("while");
        keywords.add("for");
        keywords.add("goto");
        keywords.add("label");
    }

    public static boolean isKeyWord(String s) {
        return keywords.contains(s);
    }
}
