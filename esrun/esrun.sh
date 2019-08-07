# the first argument is the file
file="$1"
fileDir=$(dirname $file)
startDir=$PWD
e80Path="$HOME/.edgy-script/bin/e80/e80.jar"

cd $fileDir
mkdir -p lib

if [ $# -eq 1 ]
  then
    java  -cp $HOME/.edgy-script/bin/sdk/sdk.jar:$e80Path de.edgelord.edgyscript.e80.main.Main $file
  else
    java -verbose:class -cp $2 -jar $e80Path $file
fi

cd $startDir
