#!/bin/bash

# this script builds and installs Edgy Script on your system from inside the already cloned repo
echo make sure that git, maven and java are installed properly!

cd e80
mvn clean install
cd ../esdk
mvn clean install
cd ../esrun
yes | cp -rf esrun.sh /usr/local/bin/esrun
chmod +x /usr/local/bin/esrun


