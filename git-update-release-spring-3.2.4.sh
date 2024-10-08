#!/bin/bash
echo -e "\e[0;34mSending release/spring-3.2.4\e[0m"
echo -e "\e[0;33m  Release steps\e[0m"
echo -e "\e[0;35m   Commit current branch: \e[0;33m[release/spring-3.2.4]\e[0m"
echo -e "\e[0;33m      - git add .\e[0m"
git add .
echo -e "\e[0;33m      - git commit -am '[release/spring-3.2.4] news changes' -q\e[0m"
git commit -am '[release/spring-3.2.4] news changes' -q
echo -e "\e[0;33m      - git push origin release/spring-3.2.4 -q\e[0m"
git push origin release/spring-3.2.4 -q
echo -e "\e[0;35m   Merge release branch \e[0;33m[release/spring-3.2.4]\e[0;35m to \e[0;33m[master]\e[0m"
echo -e "\e[0;33m      - git pull origin master -q\e[0m"
git pull origin master -q
echo -e "\e[0;33m      - git checkout master -q\e[0m"
git checkout master -q
echo -e "\e[0;33m      - git merge release/spring-3.2.4 -q\e[0m"
git merge release/spring-3.2.4 -q
echo -e "\e[0;33m      - git push origin master -q\e[0m"
git push origin master -q
echo -e "\e[0;35m   Checkout to current branch \e[0;33m[release/spring-3.2.4]\e[0m"
echo -e "\e[0;33m      - git checkout -B release/spring-3.2.4 -q\e[0m"
git checkout -B release/spring-3.2.4 -q
