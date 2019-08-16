# Edgy Script
Edgy Script: a modern, slow, barely-documented programming language for no purpose.

This projects contains three modules:
- e80, the interpreter and library API
- esrun, the script that runs a given ES-Script
- the sdk, which is a collection of java classes that provide a (not really) solid sdk for Edgy SCripts

### Edgy Script examples:

- The Hello World! Example:
    ```
    write "Hello World!"
    ```

- An unnecessarily long version of the Hello World! Example:
    ```
    create greet
    set greet "Hello World!"
    write greet
    ```
  Which is equal to:
    ```
    var greet
    set greet, "Hello World!"
    print greet
    ```
  (Yes, the comma between arguments is not necessary)

- A little bit longer example:
    ```
    create name
    input "What is your name? ", name
    write "Hello World, here is "
    println name
    ```
  Which is equivalent to:
    ```
    var name
    read "What is your name" name
    print "Hello World, here is "
    outln name
    ```

### How to install Edgy Script
On Linux and MacOS, you can use install.sh, on Windows, follow these steps:

1. Make sure that git, maven and Java are installed properly on your system
2. Clone the repo
    - type `git clone https://www.github.com/edgelord314/edgy-script` into a CLI
3. Build e80 and the sdk using maven
    - cd into the repo (`cd edgy-script`)
    - cd into the e80 project (`cd e80`)
    - build it using maven (`mvn clean install`)
    - cd into the sdk project (`cd ../sdk`)
    - build it using maven (still `mvn clean install`)
4. Run a ES Script using esrun/esrun.sh
    - cd into the esrun project (`cd ../esrun`)
    - run the script using the esrun script (`./esrun.sh path-to-file.e8t`)
5. Enjoys the interactive mode (similar to the one from python)
    - run esrun/esrun.sh (`./esrun.sh`)
