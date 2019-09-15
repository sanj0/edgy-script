package de.edgelord.edgyscript.e80.interpreter.token;

import de.edgelord.edgyscript.e80.interpreter.token.tokens.KeywordToken;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.LinkedToken;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.ValueToken;

public interface Token {

    String getID();
    String getValue();
    void setValue(String newValue);

    static Token getToken(String s, Type type) {
        switch (type) {
            case LINK:
                return new LinkedToken(s, null);
            case VALUE:
                return new ValueToken(s);
             case KEYWORD:
                return new KeywordToken(s);
        }

        return null;
    }

    enum Type {
        // The type of token that stores a variable
        LINK,
        // the type of token that stores a direct value, e.g. {} or keywords
        VALUE,
        // the type of token that stores a keyword
        KEYWORD
    }
}
