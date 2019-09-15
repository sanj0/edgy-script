package de.edgelord.edgyscript.e80.interpreter.token.tokens;

public class KeywordToken extends ValueToken {

    public KeywordToken(String keyword) {
        super(keyword);
        setID("keyword" + keyword.hashCode());
    }
}
