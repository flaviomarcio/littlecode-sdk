#!/bin/bash

function repositoryClone(){
  cd ${1}
  if [[ ${__quiet} == true ]]; then
    git clone ${3} -q
    cd ${2}
    git checkout ${4} -q
  else
    git clone ${3}
    cd ${2}
    git checkout ${4}
  fi
}

function repositoryBuild(){
  cd ${1}
  rm -rf ${1}/.git
  if [[ ${__quiet} == true ]]; then
    mvn install -DskipTests -q
  else
    mvn install -DskipTests
  fi
}

function gitClone(){
  local cloneDir=${1}
  mkdir -p ${cloneDir}
  local buildDir="${cloneDir}/${2}"
  rm -rf ${buildDir}
  repositoryClone "${cloneDir}" ${buildDir} "git@github.com:flaviomarcio/${2}.git" "${3}"
}

function mvnBuild(){
  local cloneDir=${1}
  mkdir -p ${cloneDir}
  local buildDir="${cloneDir}/${2}"
  repositoryBuild "${buildDir}"
}

function build(){
  local arg=${1}
  local cloneDir=${2}
  local gitName=${3}
  local gitBranch=${4}

  if [[ ${arg} == 1 ]]; then
    gitClone "${cloneDir}" "${gitName}" "${gitBranch}"
  fi
  mvnBuild "${cloneDir}" "${gitName}" "${gitBranch}"
}

function cloneQuest(){
  export __selector=0
  clear
  options=(Yes No)
  echo $'\n'"Update repositories?"$'\n'
  PS3=$'\n'"Choose option: "
  select opt in "${options[@]}"
  do
    if [[ ${opt} == "Yes" ]]; then
      export __selector=1;
      break
    elif [[ ${opt} == "No" ]]; then
      export __selector=2;
      break
    fi
  done
  return 0
}

function main(){
  if [[ $(which u-env) != "" ]]; then
    echo $(source gitGo)&>/dev/null
  fi
  if [[ ${__quiet} == true ]]; then
    local __clone=1
  else
    cloneQuest
    if [ ${__selector} -eq 0 ]; then
      exit 0
    elif [ ${__selector} -eq 1 ]; then
      local __clone=1
    else
      local __clone=0
    fi
    echo ${__selector}
  fi

  local cloneDir="${PWD}/targets"
  local RELEASE_0_0_1=release/0.0.1
  build ${__clone} "${cloneDir}" "littlecode-dependencies-java" ${RELEASE_0_0_1}
  build ${__clone} "${cloneDir}" "littlecode-utils-core" ${RELEASE_0_0_1}
  build ${__clone} "${cloneDir}" "littlecode-setup" ${RELEASE_0_0_1}
  build ${__clone} "${cloneDir}" "littlecode-mq" ${RELEASE_0_0_1}
  build ${__clone} "${cloneDir}" "littlecode-business" ${RELEASE_0_0_1}
  build ${__clone} "${cloneDir}" "littlecode-scheduler" ${RELEASE_0_0_1}
  build ${__clone} "${cloneDir}" "littlecode-cloud-s3" ${RELEASE_0_0_1}
}


if [[ ${1} == "--quiet" || ${1} == "-q" ]]; then
  export __quiet=true
fi

main