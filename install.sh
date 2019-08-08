#!/bin/bash

# this script downloads, builds and installs Edgy Script to your system as a standalone script (meaning this works from outside the repo)

echo make sure that git, maven and java are installed properly!

git clone https://www.github.com/edgelord314/edgy-script
cd edgy-script
cd e80
mvn clean install
cd ../sdk
mvn clean install
cd ../esrun
yes | cp -rf esrun.sh /usr/local/bin/esrun
chmod +x /usr/local/bin/esrun


