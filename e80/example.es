# import FunctionProviders in either ./lib/ or /Users/edgelord/.edgy-script/lib
import VarUtils

# hello world example
write "hello, world!"

# fancy hello world example
create greet
set greet, "hello, world!"
write greet

# fancier hello world
createset greet, "hello, world!"
write greet

# even fancier hello world example
# with the "and" operator, you can pipe variables (even temporary ones) to another function
# it is good practice to create a doc-like comment for every line of code that contains more than one "and"
# also, a line should never have more than 5 "and"; 3 "and" are considered many

# with this line, the variable greet is global, which means that you can use it in any other code after this one line
createset greet, "hello, world!" and write

# a little game
# if the function you pipe to via "and" has multiple arguments, you have to specify the place of the piped one with the dollar-sign ($), otherwise 
# the piped var will be appended last
getinput "What's your name? " and createset name, $
stringutils "join", name, ", what a beautiful name, welcome!" and write

getinput "Do you want to start your journey? " and createset answer_start_journey, $
equal answer_start_journey, "no" and if $, "systemctrl exit"

getinput "you see a dragon, do you want to fight it? " and createset dragon_fight, $
createset dragon_fight_won, false
equal dragon_fight, "yes"   and   if $, "rng 0, 1   and   equal $, 0   and   if $, "set dragon_fight_won, true""

if dragon_fight_won, "write "You run at the dragon and decapitate the dragon with your sword"", "write "The dragon runs at you and takes a bite...""
