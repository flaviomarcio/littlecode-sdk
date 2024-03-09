#!/bin/bash

export MAVEN_ARGS="$@"
export ROOT_DIR=${PWD}
export LITTLECODE_PATH=${PWD}

function projectList(){

  unset __return
  local __list=$(ls ${LITTLECODE_PATH} | sort)

  local __list="littlecode-utils-core $(echo ${__list} | sed 's/littlecode-utils-core//g')"
  local __list="$(echo ${__list} | sed 's/littlecode-dependencies-java//g' | sort) littlecode-dependencies-java"
  local __list=(${__list})

  for __item in "${__list[@]}"
  do
    local __dir=${LITTLECODE_PATH}/${__item}
    if [[ -d ${__dir} ]]; then
      local __return="${__return} ${__dir}"
    fi
  done

  echo ${__return}
}

function main(){

  local __list=($(projectList))

  for __item in "${__list[@]}"
  do
    cd ${ROOT_DIR}
    cd ${__item}
    local __pom_file=${__item}/pom.xml
    if [[ -f ${__pom_file}  ]]; then
      mvn ${MAVEN_ARGS}
    fi
  done

}

main 
