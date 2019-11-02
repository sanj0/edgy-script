# Edgy Script

Edgy Script: a modern, slow, barely-documented programming language for no purpose.

This projects contains three modules:
- e80, the interpreter and native provider API
- esrun, the script that runs a given ES-Script
- esdk, which is a collection of java classes that provide a sdk for Edgy Scripts

### Examples
1. The classical Hello World:

 ```python
 use stdio

 print("Hello World!\n")
 ```

2. A stupidly long version of the Hello World:

 ```python
 use stdio
 
 var hello = "Hello"
 var world = "World"
 
 print(hello + " " + world + "!\n")
 # better: printf "%s %s!\n", hello, world
 // should not make a real performance difference though
 ```
 
3. An enhanced Hello World with more features:

 ```python
 use stdio
 
 print("What is your name? ")
 var name = $string[input()]
 
 print("Hello World, " + name + " here!\n")
 ```

4. Enhanced Hello World with Easteregg (a bit clunky because `if` is not builtin yet [21.10.2019])

```python
use stdio
use structures as ctrl
 
printf("What is your name? ")
var name = [input()]
 
ctrl.if(name == "Stormtrooper", $[printf("These are not the droids you are looking for!")])
print("Hello World, " + name + " here!\n")
```

Code inside `[]` is evaluated at runtime as Edgy Script code, `input` is a function of `stdio` that returns user input upon pressing `ENTER`. <br>
Comments are either made by putting `//` or `#` in front of a line or `/*` and `*/` at the beginning of lines to have everythign in between (inclusively) commented. <br>
A `;` at the end of a line is allowed but not needed.

### How to install Edgy Script
On Linux and MacOS, you can use one of the install scripts, on Windows (not supported yet), follow these steps:

1. Make sure that git, maven and Java are installed properly on your system
2. Clone the repo
    - type `git clone https://www.github.com/edgelord314/edgy-script` into a CLI
3. Build e80 and the esdk using maven
    - cd into the repo (`cd edgy-script`)
    - cd into the e80 project (`cd e80`)
    - build it using maven (`mvn clean install`)
    - cd into the sdk project (`cd ../esdk`)
    - build it using maven (still `mvn clean install`)
4. Run a script using esrun/esrun.sh
    - cd into the esrun project (`cd ../esrun`)
    - run the script using the esrun script (`./esrun.sh path-to-file.et`)
