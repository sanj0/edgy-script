package de.edgelord.edgyscript.e80.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class Tokenizes a given String.
 */
public class Tokenizer {

    private Mode mode = Mode.INIT;
    private StringBuilder currentTokenValue;
    private boolean escapeNextChar = false;

    public List<Token> tokenize(String line) {
        List<Token> tokens = new ArrayList<>();
        for (char c : line.toCharArray()) {
            next(c);
            if (mode == Mode.DONE && isSplitChar(c)) {
                if (currentTokenValue.length() > 0) {
                    tokens.add(Token.getToken(null, currentTokenValue.toString()));
                    mode = Mode.INIT;
                    currentTokenValue = new StringBuilder();
                }
            }
        }

        return tokens;
    }

    private void next(char character) {
        switch (mode) {
            // mode init: the string builder ha to be initialized.
            // the mode is then set to START and the method calls itself
            case INIT:
                currentTokenValue = new StringBuilder();

                if (isSplitChar(character)) {
                    return;
                }
                mode = Mode.START;
                next(character);
                break;
            // the first char of the token
            case START:

                // first case: " -> begin of a string
                if (character == '"') {
                    mode = Mode.STRING;
                } else if (character == '.' || Character.isDigit(character)) {
                    mode = Mode.NUMBER;
                    currentTokenValue.append(character);
                }
                break;
            case STRING:
                if (character == '\\') {
                    escapeNextChar = true;
                } else {
                    if (character != '"' || escapeNextChar) {
                        currentTokenValue.append(character);
                    } else {
                        mode = Mode.DONE;
                    }
                    escapeNextChar = false;
                }
                break;
            case NUMBER:
                if (character == '.' || Character.isDigit(character)) {
                    currentTokenValue.append(character);
                } else {
                    mode = Mode.DONE;
                }
                break;
            case DIRECT:
                if (isSplitChar(character)) {
                    mode = Mode.DONE;
                } else {
                    currentTokenValue.append(character);
                }
                break;
        }
    }

    public boolean isSplitChar(char c) {
        return c == ' ' || c == ',';
    }

    public enum Mode {
        INIT,
        START,
        STRING,
        NUMBER,
        DIRECT,
        DONE
    }
}
