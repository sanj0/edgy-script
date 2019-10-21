# Edgy Script

**TODO:** Make variables a `SimpleBinding` for nashorn

Edgy Script: a modern, slow, barely-documented programming language for no purpose.

This projects contains three modules:
- e80, the interpreter and native provider API
- esrun, the script that runs a given ES-Script
- esdk, which is a collection of java classes that provide a sdk for Edgy Scripts

### Examples
1. The classical Hello World:

 ```python
 use stdio

 print "Hello World!\n"
 ```

2. A stupidly long version of the Hello World:

 ```python
 use stdio
 
 var hello = "Hello"
 var world = "World"
 
 print hello + " " + world + "!\n"
 ```
 
3. An enhanced Hello World with more features:

 ```python
 use stdio
 
 print "What is your name? "
 var name = [input]
 
 print "Hello World, " + name + " here!\n"
 ```

4. Enhanced Hello World with Easeteregg (not yet working due to lack of structures like `if` [21.10.2019])

```python
use stdio
 
printf "What is your name? "
var name = [input]
 
if name == "Stormtrooper":
    printf "These are not the droids you are looking for!"
print "Hello World, " + name + " here!\n"
```

Code inside `[]` is evaluated at runtime as Edgy Script code, `input` is a function of `stdio` that return user input upon pressing `ENTER`.

### How to install Edgy Script
On Linux and MacOS, you can use one of the install scripts, on Windows, follow these steps:

1. Make sure that git, maven and Java are installed properly on your system
2. Clone the repo
    - type `git clone https://www.github.com/edgelord314/edgy-script` into a CLI
3. Build e80 and the esdk using maven
    - cd into the repo (`cd edgy-script`)
    - cd into the e80 project (`cd e80`)
    - build it using maven (`mvn clean install`)
    - cd into the sdk project (`cd ../esdk`)
    - build it using maven (still `mvn clean install`)
4. Run a ES Script using esrun/esrun.sh
    - cd into the esrun project (`cd ../esrun`)
    - run the script using the esrun script (`./esrun.sh path-to-file.et`)
5. Enjoys the interactive mode (similar to the one from python)
    - run esrun/esrun.sh (`./esrun.sh`)
