package de.edgelord.edgyscript.e80;

public class Variable {

    private String name;
    private String value;

    public Variable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean isNumber() {
        for (char character : value.toCharArray()) {
            if (!(Character.isDigit(character) || character == '.')) {
                return false;
            }
        }

        return true;
    }

    public boolean isBoolean() {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public boolean isMathOperator() {
        return getString().equals("+") || getString().equals("-") || getString().equals("*") || getString().equals("**")
               || getString().equals("/") || getString().equals("%");
    }

    public String getValueForJS() {

        if (isBoolean() || isNumber()) {
            return getString();
        } else {
            return "\"" + getString() + "\"";
        }
    }

    public void setValue(Object value) {
        this.value = value.toString();
    }

    public float getFloat() {
        return Float.parseFloat(value);
    }

    public int getInt() {
        return Integer.valueOf(value);
    }

    public String getString() {
        return value;
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Variable empty() {
        return new Variable("", "");
    }
}
