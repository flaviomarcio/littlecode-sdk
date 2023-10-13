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
  local RELEASE_0_0_1=release/0.0.1
  build "${cloneDir}" "littlecode-dependencies-java" ${RELEASE_0_0_1}
  build "${cloneDir}" "littlecode-utils-core" ${RELEASE_0_0_1}
  build "${cloneDir}" "littlecode-setup" ${RELEASE_0_0_1}
  build "${cloneDir}" "littlecode-mq" ${RELEASE_0_0_1}
  build "${cloneDir}" "littlecode-business" ${RELEASE_0_0_1}
  build "${cloneDir}" "littlecode-scheduler" ${RELEASE_0_0_1}

}


main