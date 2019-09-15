package de.edgelord.edgyscript.e80.interpreter.token;

public class TokenBuilder {

    private Token.Type type;
    private StringBuilder builder;

    public TokenBuilder(Token.Type type) {
        this.type = type;
        builder = new StringBuilder();
    }

    /**
     * Gets {@link #type}.
     *
     * @return the value of {@link #type}
     */
    public Token.Type getType() {
        return type;
    }

    public void append(char c) {
        builder.append(c);
    }

    public int length() {
        return builder.length();
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
