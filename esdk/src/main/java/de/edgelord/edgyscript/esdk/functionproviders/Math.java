package de.edgelord.edgyscript.esdk.functionproviders;

import de.edgelord.edgyscript.e80.FunctionProvider;
import de.edgelord.edgyscript.e80.ScriptFile;
import de.edgelord.edgyscript.e80.ScriptLine;
import de.edgelord.edgyscript.e80.Variable;
import de.edgelord.edgyscript.esdk.exceptions.SDKException;

import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Provides functions:
 *
 * - math
 *     calculates and returns the given math expression (+ - * /)
 * - euler (alias generateEuler, generateE)
 *     generates and returns euler's number (e) with the given amount of digits
 * - prime (alias primeNumber, isPrime, isPrimeNumber)
 *     returns true if the given number is a prime number and false if not
 * - smallerThan (alias smaller, isSmaller)
 *     returns true if the first given number is smaller than the second given variable and vice versa
 * - greaterThan (alias greater, isGreater)
 *     returns true if the first given number is greater than the second given variable and vice versa
 * - increment
 *     increments the first given variable and returns it
 * - decrement
 *     decrements the first given variable and returns it
 */
public class Math extends FunctionProvider {
    @Override
    public Variable function(ScriptLine line, String name, Variable[] variables, ScriptFile scriptFile) {

        if (name.equalsIgnoreCase("math")) {
            StringBuilder mathExpr = new StringBuilder();

            for (Variable var : variables) {
                if (var.isNumber() || var.isMathOperator()) {
                    mathExpr.append(var.getString());
                } else {
                    throw new SDKException("Variable " + var.getName() + " : " + var.getString() + " is not a number!", scriptFile);
                }
            }

            try {
                return new Variable(scriptFile.nextTempvar(), NativeExec.getJavaScriptEngine().eval(mathExpr.toString()).toString());
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        if (name.equalsIgnoreCase("euler") || name.equalsIgnoreCase("generateEuler") || name.equalsIgnoreCase("generateE")) {
            return new Variable(scriptFile.nextTempvar(), generateEuler(variables[0].getInteger()).toPlainString());
        }

        if (name.equalsIgnoreCase("prime") || name.equalsIgnoreCase("primeNumber") || name.equalsIgnoreCase("isPrime") || name.equalsIgnoreCase("isPrimeNumber")) {
            return new Variable(scriptFile.nextTempvar(), String.valueOf(isPrime(variables[0].getInteger())));
        }

        if (name.equalsIgnoreCase("smallerThan") || name.equalsIgnoreCase("smaller") || name.equalsIgnoreCase("isSmaller")) {
            return new Variable(scriptFile.nextTempvar(), String.valueOf(variables[0].getFloat() < variables[1].getFloat()));
        }

        if (name.equalsIgnoreCase("greaterThan") || name.equalsIgnoreCase("greater") || name.equalsIgnoreCase("isGreater")) {
            return new Variable(scriptFile.nextTempvar(), String.valueOf(variables[0].getFloat() > variables[1].getFloat()));
        }

        if (name.equalsIgnoreCase("increment")) {
            variables[0].setValue(variables[0].getInteger() + 1);
            return variables[0];
        }

        if (name.equalsIgnoreCase("decrement")) {
            variables[0].setValue(variables[0].getInteger() - 1);
            return variables[0];
        }
        return null;
    }

    private static boolean isPrime(int n) {
        if (n % 2 == 0) {
            return false;
        }
        long highestPossibleDivisor = java.lang.Math.round(java.lang.Math.sqrt(n)) + 1;
        for(long i = 3; i < highestPossibleDivisor; i += 2) {
            if(n % i == 0)
                return false;
        }
        return true;
    }

    private static BigDecimal generateEuler(int digits) {
        BigDecimal e = BigDecimal.ONE;
        BigDecimal factorial = BigDecimal.ONE;
        for(long i = 1; i < digits; i++) {
            factorial = factorial.multiply(new BigDecimal(i * 1.0 + ""));
            e = e.add(new BigDecimal(1.0 + "").divide(factorial, new MathContext(10000)));
        }
        return e;
    }
}
