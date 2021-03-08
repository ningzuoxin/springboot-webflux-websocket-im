#!/bin/bash
# ======================================================================
# 项目启动shell脚本
# lib目录: spring boot  jar包--合并打包
# config目录: 配置文件目录
# logs目录: 项目运行日志目录
# logs/*_startup.log: 记录启动日志
# logs/back: 项目运行日志备份目录
# nohup后台运行
#
# ======================================================================
# 设置环境变量，主要为了防止 init版本执行service时，环境变量不生效的问题
source /etc/profile
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

BASE_APP_LOG="/opt/logs/tomcat/"${APPLICATION}

# 项目启动jar包名称
APPLICATION_JAR="${APPLICATION}*.jar"

CONFIG_DIR=${BASE_PATH}"/conf/"
#echo ${CONFIG_DIR}

CONFIG_PARAM="--spring.config.location="${CONFIG_DIR}

# 项目日志输出绝对路径
LOG_DIR=${BASE_APP_LOG}
LOG_FILE="${APPLICATION}.log"
LOG_PATH="${LOG_DIR}/${LOG_FILE}"
# 日志备份目录
LOG_BACK_DIR="${LOG_DIR}/back/"

# 项目启动日志输出绝对路径
LOG_STARTUP_PATH="${LOG_DIR}/${APPLICATION}_startup.log"

# 当前时间
NOW=`date +'%Y-%m-%m-%H-%M-%S'`
NOW_PRETTY=`date +'%Y-%m-%m %H:%M:%S'`

# 启动日志
STARTUP_LOG="================================================ ${NOW_PRETTY} ================================================\n"

# 如果logs文件夹不存在,则创建文件夹
if [[ ! -d "${LOG_DIR}" ]]; then
  mkdir -p "${LOG_DIR}"
fi

# 如果logs/back文件夹不存在,则创建文件夹
if [[ ! -d "${LOG_BACK_DIR}" ]]; then
  mkdir -p "${LOG_BACK_DIR}"
fi

# 如果项目运行日志存在,则重命名备份
if [[ -f "${LOG_PATH}" ]]; then
	mv ${LOG_PATH} "${LOG_BACK_DIR}/${APPLICATION}_back_${NOW}.log"
fi

# 创建新的项目运行日志
echo "" > ${LOG_PATH}

# 如果项目启动日志不存在,则创建,否则追加
echo "${STARTUP_LOG}" >> ${LOG_STARTUP_PATH}

PIDS=`ps -ef|grep java|grep ${APPLICATION}|grep -v grep|awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $APPLICATION already started!\n"
    echo "ERROR: The $APPLICATION already started!\n" >> ${LOG_STARTUP_PATH}
    echo "PID: $PIDS\n"
    echo "PID: $PIDS\n" >> ${LOG_STARTUP_PATH}
    exit 1
fi


#==========================================================================================
# JVM Configuration
# -Xmx1024m:设置JVM最大可用内存为1024m,根据项目实际情况而定，建议最小和最大设置成一样。
# -Xms1024m:设置JVM初始内存。此值可以设置与-Xmx相同,以避免每次垃圾回收完成后JVM重新分配内存
# -Xmn384m:设置年轻代大小为384m。整个JVM内存大小=年轻代大小 + 年老代大小 + 持久代大小。
#          持久代一般固定大小为64m,所以增大年轻代,将会减小年老代大小。此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8
# -XX:MetaspaceSize=128m:存储class的内存大小,该值越大触发Metaspace GC的时机就越晚
# -XX:MaxMetaspaceSize=320m:限制Metaspace增长的上限，防止因为某些情况导致Metaspace无限的使用本地内存，影响到其他程序
# -XX:-OmitStackTraceInFastThrow:解决重复异常不打印堆栈信息问题
#==========================================================================================
JAVA_OPT="-server -Xms1024m -Xmx1024m -Xmn384m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"

#=======================================================
# 将命令启动相关日志追加到日志文件
#=======================================================

# 输出项目名称
STARTUP_LOG="${STARTUP_LOG}application name: ${APPLICATION}\n"
# 输出jar包名称
STARTUP_LOG="${STARTUP_LOG}application jar name: ${APPLICATION_JAR}\n"
# 输出项目bin路径
STARTUP_LOG="${STARTUP_LOG}application bin  path: ${BIN_PATH}\n"
# 输出项目根目录
STARTUP_LOG="${STARTUP_LOG}application root path: ${BASE_PATH}\n"
# 打印日志路径
STARTUP_LOG="${STARTUP_LOG}application log  path: ${LOG_PATH}\n"
# 打印JVM配置
STARTUP_LOG="${STARTUP_LOG}application JAVA_OPT : ${JAVA_OPT}\n"

# 打印启动命令
STARTUP_LOG="${STARTUP_LOG}application background startup command: nohup java ${JAVA_OPT} -jar
${BASE_PATH}/lib/${APPLICATION_JAR} ${CONFIG_PARAM}  > ${LOG_PATH} 2>&1 &\n"
#echo ${STARTUP_LOG}
#exit 1
#======================================================================
# 执行启动命令：后台启动项目,并将日志输出到项目根目录下的logs文件夹下
#======================================================================
nohup java ${JAVA_OPT} -jar ${BASE_PATH}/lib/${APPLICATION_JAR} ${CONFIG_PARAM}  > ${LOG_PATH} 2>&1 &
# 进程ID
PID=$(ps -ef |grep java|grep "${APPLICATION}" | grep -v grep | awk '{ print $2 }')
STARTUP_LOG="${STARTUP_LOG}application pid: ${PID}\n"
#因为 按照目前一个端口号只给一个服务用，所以目前不存在一个服务在同一机器上起多个实例的问题。就不要pid文件的形式停止服务了
# echo PID > "${BIN_PATH}/${APPLICATION}.pid"
#echo $! > "${BIN_PATH}/${APPLICATION}.pid"
# 启动日志追加到启动日志文件中
echo -e ${STARTUP_LOG} >> ${LOG_STARTUP_PATH}
# 打印启动日志
echo -e ${STARTUP_LOG}

# 打印项目日志
#tail -f ${LOG_PATH}
