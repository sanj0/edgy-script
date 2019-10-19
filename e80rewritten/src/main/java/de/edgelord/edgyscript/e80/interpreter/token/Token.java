package de.edgelord.edgyscript.e80.interpreter.token;

import de.edgelord.edgyscript.e80.interpreter.Interpreter;
import de.edgelord.edgyscript.e80.interpreter.LinkedValue;
import de.edgelord.edgyscript.e80.interpreter.Value;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.SpecialToken;
import de.edgelord.edgyscript.e80.interpreter.token.tokens.ValueToken;

import java.io.Serializable;

/**
 * Different subtypes of token:
 *
 * [function name] [direct value] [variable name] [operator] [direct value]
 * e.g.
 * myFunction "hello, world!", myVariable + 3,14
 */
public abstract class Token implements Serializable {

    public abstract String getID();
    public abstract String getValue();
    public abstract void setValue(String newValue);
    public abstract Value toValue();

    private ValueType valueType;

    public Token(ValueType valueType) {
        this.valueType = valueType;
    }

    public boolean isBoolean() {
        return getValue().equalsIgnoreCase("true") || getValue().equalsIgnoreCase("false");
    }

    public boolean isNumeric() {
        char[] chars = getValue().toCharArray();

        for (char c : chars) {
            if (!(Character.isDigit(c) || c == '.')) {
                return false;
            }
        }
        return true;
    }

    public ValueType getType() {
        if (valueType == ValueType.AUTO) {

            if (getValue() == null) {
                return ValueType.NUMBER;
            } else if (isBoolean()) {
                return ValueType.BOOLEAN;
            } else if (isNumeric()) {
                return ValueType.NUMBER;
            } else {
                return ValueType.STRING;
            }
        } else {
            return valueType;
        }
    }

    public String getValueForJS() {
        switch (getType()) {
            case NUMBER:
            case BOOLEAN:
                return getValue();
            case STRING:
                return "\"" + getValue() + "\"";
        }

        return null;
    }

    public static Token getToken(String s, Type type, ValueType valueType) {
        switch (type) {
            case SPECIAL:
            case SPLIT:

                if (Interpreter.isKeyWord(s) || Interpreter.isOperator(s)) {
                    return new SpecialToken(s, valueType);
                } else {
                    if (s.contains(":")) {
                        String[] parts = s.split(":");
                        return new LinkedValue(parts[1], ValueType.getType(parts[0]));
                    }
                    return new LinkedValue(s);
                }
            case VALUE:
                return new ValueToken(s, valueType);
        }

        return null;
    }

    public enum ValueType {
        NUMBER,
        STRING,
        BOOLEAN,
        AUTO;

        public static ValueType getType(String typeName) {
            switch (typeName.toLowerCase()) {
                case "number":
                    return NUMBER;
                case "string":
                    return STRING;
                case "boolean":
                case "bool":
                    return BOOLEAN;
            }

            throw new RuntimeException("Variable type" + typeName + " does not exist!");
        }
    }

    public enum Type {
        // The type of token that stores a special value (function name, keyword, operator, ...)
        SPECIAL,
        // the type of token that stores a direct value
        VALUE,
        // the type of token that stores a split char (,)
        SPLIT,
        // the type of token that stores an inline value (z.b. [md5Hash "Hello World!"] -> md5Hash "Hello World!")
        INLINE
    }
}
