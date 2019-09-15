package de.edgelord.edgyscript.e80.interpreter.token;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class Tokenizes a given String.
 */
public class Tokenizer {

    private Mode mode = Mode.INIT;
    private TokenBuilder tokenBuilder;
    private boolean escapeNextChar = false;

    public List<Token> tokenize(String s) {
        List<Token> tokens = new ArrayList<>();
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            next(c);
            if (i == chars.length - 1 || mode == Mode.DONE) {
                if (tokenBuilder.length() > 0) {
                    switch (tokenBuilder.getType()) {
                        case LINK:
                            tokens.add(Token.getToken(tokenBuilder.toString(), Token.Type.LINK));
                            break;
                        case VALUE:
                            String token = tokenBuilder.toString();

                            if (Interpreter.isKeyWord(token)) {
                                tokens.add(Token.getToken(token, Token.Type.KEYWORD));
                            } else {
                                tokens.add(Token.getToken(token, Token.Type.VALUE));
                            }
                            break;
                    }
                    mode = Mode.INIT;
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
                    tokenBuilder = new TokenBuilder(Token.Type.VALUE);
                } else if (character == '.' || Character.isDigit(character)) {
                    mode = Mode.NUMBER;
                    tokenBuilder = new TokenBuilder(Token.Type.VALUE);
                    tokenBuilder.append(character);
                } else {
                    tokenBuilder = new TokenBuilder(Token.Type.LINK);
                }
                break;
            case STRING:
                if (character == '\\') {
                    escapeNextChar = true;
                } else {
                    if (character != '"' || escapeNextChar) {
                        tokenBuilder.append(character);
                    } else {
                        mode = Mode.DONE;
                    }
                    escapeNextChar = false;
                }
                break;
            case NUMBER:
                if (character == '.' || Character.isDigit(character)) {
                    tokenBuilder.append(character);
                } else {
                    mode = Mode.DONE;
                }
                break;
            case DIRECT:
                if (isSplitChar(character)) {
                    mode = Mode.DONE;
                } else {
                    tokenBuilder.append(character);
                }
                break;
        }
    }

    public static boolean isSplitChar(char c) {
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
