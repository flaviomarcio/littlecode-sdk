#!/bin/bash

export COLOR_OFF='\e[0m'
export COLOR_BACK='\e[0;30m'
export COLOR_BACK_B='\e[1;30m'
export COLOR_RED='\e[0;31m'
export COLOR_RED_B='\e[0;31m'
export COLOR_GREEN='\e[0;32m'
export COLOR_GREEN_B='\e[1;32m'
export COLOR_YELLOW='\e[0;33m'
export COLOR_YELLOW_B='\e[2;33m'
export COLOR_BLUE='\e[0;34m'
export COLOR_BLUE_B='\e[1;34m'
export COLOR_MAGENTA='\e[0;35m'
export COLOR_MAGENTA_B='\e[1;35m'
export COLOR_CIANO='\e[0;36m'
export COLOR_CIANO_B='\e[1;36m'
export COLOR_WHITE='\e[0;37m'
export COLOR_WHITE_B='\e[1;37m'

function echoColor()
{
  echo -e "${1}${2}\e[0m"
}

function echR()
{
  echoColor ${COLOR_RED} "$@"
}

function echG()
{
  echoColor ${COLOR_GREEN} "$@"
}

function echY()
{
  echoColor ${COLOR_YELLOW} "$@"
}

function echB()
{
  echoColor ${COLOR_BLUE} "$@"
}

function echM()
{
  echoColor ${COLOR_MAGENTA} "$@"
}

function echC()
{
  echoColor ${COLOR_CIANO} "$@"
}
