package de.edgelord.edgyscript.e80.interpreter;

import de.edgelord.edgyscript.e80.interpreter.token.Token;
import de.edgelord.edgyscript.e80.interpreter.token.TokenBuilder;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.SpecialToken;

import java.util.ArrayList;
import java.util.List;

/**
 * This class Tokenizes a given String.
 */
public class Lexer {

    /*
    TODO: Add colon operator, which swaps primary argument and function name, e.g.
    toLowerCase "Hello, WoRlD" -> "Hello, WoRlD":toLowerCase // or
    contains myArray "hello world!" -> myArray:contains "hello world!"
     */

    private Mode mode = Mode.INIT;
    private Token.ValueType currentValueType = Token.ValueType.AUTO;
    private TokenBuilder tokenBuilder;
    private boolean escapeNextChar = false;
    private int requiredClosedBrackets = 0;
    private boolean inlineCastMode = false;

    public static final String lineSeparator = System.getProperty("line.separator");

    public List<Token> tokenize(String s) {

        List<Token> tokens = new ArrayList<>();
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            Token subToken = next(c);
            if ((i == chars.length - 1 || mode == Mode.DONE) && mode != Mode.INIT && mode != Mode.START) {
                if (tokenBuilder.length() > 0) {
                    tokens.add(Token.getToken(tokenBuilder.toString(), tokenBuilder.getType(), currentValueType));
                    mode = Mode.INIT;
                }
            }
            if (subToken != null) {
                tokens.add(subToken);
            }
        }

        if (tokens.size() > 1) {
            if (tokens.get(1).isBracket()) {
                for (int i = tokens.size() - 1; i > 0; i--) {
                    if (tokens.get(i).isBracket()) {
                        tokens.remove(i);
                        tokens.remove(1);
                        break;
                    }
                }
            }
        }

        // check for colon operator here
        if (tokens.size() > 1) {
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);

                if (token instanceof SpecialToken && token.getValue().equals(":")) {
                    Token previousToken = tokens.get(i - 1);
                    tokens.set(i - 1, tokens.get(i + 1));
                    tokens.set(i + 1, previousToken);
                    tokens.add(i + 2, new SpecialToken(",", Token.ValueType.NUMBER));
                    tokens.remove(i);
                    ++i;
                }
            }
        }

        return tokens;
    }

    /**
     * Builds the {@link #tokenBuilder} and can return an additional subtoken (e.g. parentheses ()).
     * TODO: make [] be ignored inside ""
     *
     * @param character the next character to process
     * @return an optional subtoken (can be null)
     */
    private Token next(char character) {

        Token subToken = null;

        if (mode != Mode.STRING && mode != Mode.INLINE) {
            String characterString = Character.toString(character);
            if (character == '(' || character == ')') {
                character = ' ';
                subToken = Token.getToken(characterString, Token.Type.SPECIAL, Token.ValueType.NUMBER);

                if (mode != Mode.INIT && mode != Mode.START) {
                    mode = Mode.DONE;
                }
            } else if (Interpreter.isOperator(Character.toString(character))) {
                subToken = Token.getToken(characterString, Token.Type.SPECIAL, Token.ValueType.NUMBER);
                if (mode != Mode.INIT && mode != Mode.START) {
                    mode = Mode.DONE;
                }
            } else if (Interpreter.isSplitChar(character)) {
                if (mode != Mode.INIT && mode != Mode.START) {
                    mode = Mode.DONE;
                }
                subToken = Token.getToken(characterString, Token.Type.SPLIT, Token.ValueType.NUMBER);
            }
        }
        switch (mode) {
            // mode init: the string builder has to be initialized.
            // the mode is then set to START and the method calls itself
            case INIT:
                if (character == ' ' | character == ',') {
                    break;
                }

               if (Interpreter.isOperator(Character.toString(character))) {
                    mode = Mode.INIT;
                    return subToken;
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
                } else if (character == '$') {
                    mode = Mode.INLINE;
                    tokenBuilder = new TokenBuilder(Token.Type.INLINE);
                    requiredClosedBrackets = 0;
                    inlineCastMode = true;
                    currentValueType = Token.ValueType.AUTO;
                } else {
                    tokenBuilder = new TokenBuilder(Token.Type.SPECIAL);
                    tokenBuilder.append(character);
                    mode = Mode.DIRECT;
                    currentValueType = Token.ValueType.NUMBER;
                }
                break;
            case STRING:
                if (character == '"') {
                    if (escapeNextChar) {
                        tokenBuilder.append(character);
                    } else {
                        mode = Mode.DONE;
                    }
                } else if (character == '\\') {
                    if (!escapeNextChar) {
                        escapeNextChar = true;
                    }
                    tokenBuilder.append(character);
                } else {
                    escapeNextChar = false;
                    tokenBuilder.append(character);
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
                if (character == ' ' || Interpreter.isOperator(Character.toString(character))) {
                    mode = Mode.DONE;

                } else {
                    tokenBuilder.append(character);
                }
                break;
            case INLINE:
                if (character == '[') {
                    requiredClosedBrackets++;

                    if (inlineCastMode) {
                        inlineCastMode = false;
                        tokenBuilder.append(":");
                    } else {
                        tokenBuilder.append(character);
                    }
                } else if (character == ']') {
                    requiredClosedBrackets--;

                    if (requiredClosedBrackets > 0) {
                        tokenBuilder.append(character);
                    }
                } else {
                    tokenBuilder.append(character);
                }

                if (requiredClosedBrackets == 0 && !inlineCastMode) {
                    mode = Mode.DONE;
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
