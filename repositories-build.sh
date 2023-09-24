#!/bin/bash

function repositoryClone(){
  cd ${1}
  git clone ${3}
  cd ${2}
  git checkout ${4}
}

function repositoryBuild(){
  cd ${1}
  mvn install
}

function build(){
  local cloneDir=${1}
  mkdir -p ${cloneDir}
  local buildDir="${cloneDir}/${2}"
  rm -rf ${buildDir}
  repositoryClone "${cloneDir}" ${buildDir} "git@github.com:flaviomarcio/${2}.git" "${3}"
  repositoryBuild "${buildDir}"
}

function main(){
  source gitGo
  local cloneDir="${PWD}/targets"
  build "${cloneDir}" "littlecode-utils-core" "release/0.0.1"
  build "${cloneDir}" "littlecode-setup" "release/0.0.1"
  build "${cloneDir}" "littlecode-mq" "release/0.0.1"
}


main