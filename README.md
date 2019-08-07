# Edgy Script
Edgy Script, a modern, slow, barely-documentated programming language for no purpose.

This projects contains three modules:
- e80, the interpreter and library API
- esrun, the script that runs a given ES-Script
- the sdk, which is a collection of java classes that provide a (not really) solid sdk for ES-Script

Edgy Script examples:

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
