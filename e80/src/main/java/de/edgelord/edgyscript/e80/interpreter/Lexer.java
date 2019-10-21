package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.interpreter.token.TokenBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class Tokenizes a given String.
 */
public class Lexer {

    private Mode mode = Mode.INIT;
    private Token.ValueType currentValueType = Token.ValueType.AUTO;
    private TokenBuilder tokenBuilder;
    private boolean escapeNextChar = false;
    private int requiredClosedBrackets = 0;

    public static final String lineSeparator = System.getProperty("line.separator");

    public List<Token> tokenize(String s) {
        List<Token> tokens = new ArrayList<>();
        char[] chars = s.toCharArray();
        boolean lastTokenWasSplit = false;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            Token subToken = next(c);
            if (i == chars.length - 1 || mode == Mode.DONE) {
                if (tokenBuilder.length() > 0) {

                    String tokenValue = tokenBuilder.toString();

                    if (tokenValue.equals(",")) {
                        if (lastTokenWasSplit) {
                            continue;
                        } else {
                            lastTokenWasSplit = true;
                        }
                    } else {
                        lastTokenWasSplit = false;
                    }

                    tokens.add(Token.getToken(tokenBuilder.toString(), tokenBuilder.getType(), currentValueType));
                    mode = Mode.INIT;
                }
            }
            if (subToken != null) {
                String tokenValue = subToken.getValue();
                if (tokenValue.equals(",")) {
                    if (lastTokenWasSplit) {
                        continue;
                    } else {
                        lastTokenWasSplit = true;
                    }
                } else {
                    lastTokenWasSplit = false;
                }
                tokens.add(subToken);
            }
        }

        return tokens;
    }

    /**
     * Builds {@link #tokenBuilder} and can return an additional subtoken (e.g. parentheses ()).
     * TODO: make [] be ignored inside ""
     *
     * @param character the next character to process
     * @return an optional subtoken (can be null)
     */
    private Token next(char character) {

        Token subToken = null;

        if (mode != Mode.STRING) {
            if (character == '(' || character == ')') {
                String parenthesis = Character.toString(character);
                character = ' ';
                subToken = Token.getToken(parenthesis, Token.Type.SPECIAL, Token.ValueType.NUMBER);

                if (mode != Mode.INIT && mode != Mode.START) {
                    mode = Mode.DONE;
                }
            }
        }
        switch (mode) {
            // mode init: the string builder has to be initialized.
            // the mode is then set to START and the method calls itself
            case INIT:
                if (character == ' ') {
                    break;
                }

                if (Interpreter.isSplitChar(character)) {
                    tokenBuilder = new TokenBuilder(Token.Type.SPLIT);
                    tokenBuilder.append(character);
                    mode = Mode.DONE;
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
                    currentValueType = Token.ValueType.STRING;
                } else if (character == '.' || Character.isDigit(character)) {
                    mode = Mode.NUMBER;
                    tokenBuilder = new TokenBuilder(Token.Type.VALUE);
                    tokenBuilder.append(character);
                    currentValueType = Token.ValueType.NUMBER;
                } else if (character == '[') {
                    mode = Mode.INLINE;
                    tokenBuilder = new TokenBuilder(Token.Type.INLINE);
                    requiredClosedBrackets = 1;
                    currentValueType = Token.ValueType.AUTO;
                } else {
                    tokenBuilder = new TokenBuilder(Token.Type.SPECIAL);
                    tokenBuilder.append(character);
                    mode = Mode.DIRECT;
                    currentValueType = Token.ValueType.NUMBER;
                }
                break;
            case STRING:
                if (character == '\\' && !escapeNextChar) {
                    escapeNextChar = true;
                }
                if (character != '"' || escapeNextChar) {

                    if (escapeNextChar && character == 'n') {
                        tokenBuilder.append(lineSeparator);
                    } else {
                        tokenBuilder.append(character);
                    }
                } else {
                    mode = Mode.DONE;
                }
                escapeNextChar = false;
                break;
            case NUMBER:
                if (character == '.' || Character.isDigit(character)) {
                    tokenBuilder.append(character);
                } else {
                    mode = Mode.DONE;
                }
                break;
            case DIRECT:
                if (Interpreter.isSplitChar(character) || character == ' ' || Interpreter.isOperator(Character.toString(character))) {
                    mode = Mode.DONE;

                    if (character != ' ') {
                        subToken = Token.getToken(Character.toString(character), Token.Type.SPLIT, Token.ValueType.NUMBER);
                    }
                } else {
                    tokenBuilder.append(character);
                }
                break;
            case INLINE:
                if (character == '[') {
                    requiredClosedBrackets++;
                } else if (character == ']') {
                    requiredClosedBrackets--;
                }

                if (requiredClosedBrackets == 0) {
                    mode = Mode.DONE;
                } else {
                    tokenBuilder.append(character);
                }
        }

        return subToken;
    }

    public enum Mode {
        INIT,
        START,
        STRING,
        INLINE,
        NUMBER,
        DIRECT,
        DONE
    }
}
