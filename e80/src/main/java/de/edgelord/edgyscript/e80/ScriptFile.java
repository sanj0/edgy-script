package de.edgelord.edgyscript.e80;

import de.edgelord.edgyscript.e80.exceptions.VarNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a script-file
 */
public class ScriptFile {

    /**
     * The actual file
     */
    private File file;

    private int tempvarCount = 0;

    private List<Variable> varPool = new ArrayList<>();

    public ScriptFile(File file) {
        this.file = file;
    }

    public ScriptFile(String path) {
        this(new File(path));
    }

    /**
     * Returns an array that contains all the lines of this {@link #file script file}
     *
     * @return an array that contains all the lines of this script file
     * @throws FileNotFoundException when the file does not exist
     */
    public String[] getLines() throws FileNotFoundException {
        Scanner fileScanner = new Scanner(file);

        List<String> lines = new LinkedList<>();

        while (fileScanner.hasNextLine()) {
            lines.add(fileScanner.nextLine());
        }

        return lines.toArray(new String[0]);
    }

    /**
     * Gets {@link #varPool}.
     *
     * @return the value of {@link #varPool}
     */
    public List<Variable> getVarPool() {
        return varPool;
    }

    public boolean varExists(String name) {
        for (Variable var : varPool) {
            if (var.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public Variable getVar(String name) {
        for (Variable var : varPool) {
            if (var.getName().equals(name)) {
                return var;
            }
        }

        throw new VarNotFoundException(name, this);
    }

    public String nextTempvar() {
        return "tmpvar" + tempvarCount++;
    }

    public String getPath() {
        return file.getAbsolutePath();
    }
}
