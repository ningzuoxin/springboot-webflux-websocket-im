#!/bin/bash
#======================================================================
# 项目状态查看脚本
#
#======================================================================

# bin目录绝对路径
BIN_PATH=$(cd `dirname $0`; pwd)
# 进入bin目录
cd `dirname $0`
# 返回到上一级项目根目录路径
cd ..
# 打印项目根目录绝对路径
# `pwd` 执行系统命令并获得结果
BASE_PATH=`pwd`

# 获得项目的名称，根据目录名获得
# 所以应用服务一定得按照规范放在 /opt/service/applicatio-name
APPLICATION=${BASE_PATH##*/} #拿掉最后一条/及其左边的字串

PID=$(ps -ef |grep java|grep "${APPLICATION}" | grep -v grep | awk '{ print $2 }')
if [[ -z "$PID" ]]
then
    echo ${APPLICATION} is already stopped
else
    echo ${APPLICATION} is started, pid is ${PID}
fi

#kill `cat service.pid`
