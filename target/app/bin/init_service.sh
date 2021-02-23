#!/bin/bash
#======================================================================
# 为项目生成服务的脚本--initd类型 service app-name start
# add by onewan 2019-09-26
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

#获得项目的名称，根据目录名获得
# 所以应用服务一定得按照规范放在 /opt/service/applicatio-name
APPLICATION=${BASE_PATH##*/} #拿掉最后一条/及其左边的字串
#服务程序存放的路径
INIT_SERVICE_PATH='/etc/init.d'
#服务程序的路径包含文件名
SERVICE_DES_APTH="${INIT_SERVICE_PATH}/${APPLICATION}"

BASE_APP_LOG="/opt/logs/"${APPLICATION}
# 项目日志输出绝对路径
LOG_DIR=${BASE_APP_LOG}
LOG_FILE="${APPLICATION}.log"
LOG_PATH="${LOG_DIR}/${LOG_FILE}"
# 创建新的项目运行日志
echo "" >> ${LOG_PATH}


#判断服务是否存在，如果存在则不需要服务初始化，直接重启服务
#if [[ -f "${SERVICE_DES_APTH}" ]]; then
#	echo "==== the service ${APPLICATION} has exists in ${SERVICE_APTH} ====\n" >> ${LOG_PATH}
	#重启服务
#	service "${APPLICATION}" restart
#	exit 1
#fi

#生成服务代码
#服务代码本地路径
SERVICE_FILE="${BIN_PATH}/${APPLICATION}"
echo "" > ${SERVICE_FILE}
#抬头部分 注意 chkconfig 开机关机优先级 是100以内，数字越大，优先级越低
echo "#! /bin/bash" >> ${SERVICE_FILE}
echo "# chkconfig: 2345 80 20" >> ${SERVICE_FILE}
echo "# Description: Startup script for service ${APPLICATION} on Debian. Place in /etc/init.d and" >> ${SERVICE_FILE}
echo "# For CentOS/Redhat run: 'chkconfig --add ${APPLICATION}'" >> ${SERVICE_FILE}
echo "### BEGIN INIT INFO " >> ${SERVICE_FILE}
echo "# Provides:          ${APPLICATION}" >> ${SERVICE_FILE}
echo "# Required-Start:    $all" >> ${SERVICE_FILE}
echo "# Required-Stop:     $all" >> ${SERVICE_FILE}
echo "# Default-Start:     2 3 4 5" >> ${SERVICE_FILE}
echo "# Default-Stop:      0 1 6" >> ${SERVICE_FILE}
echo "# Short-Description: starts the service ${APPLICATION}  server" >> ${SERVICE_FILE}
echo "# Description:       starts nginx using start-stop-daemon" >> ${SERVICE_FILE}
echo "### END INIT INFO" >> ${SERVICE_FILE}
echo "# Author:   onewan" >> ${SERVICE_FILE}

#执行脚本部分
echo "#开始脚本路径" >> ${SERVICE_FILE}
START_BIN="${BIN_PATH}/start.sh"
echo "START_BIN=${START_BIN}" >> ${SERVICE_FILE}
echo "#停止脚本路径" >> ${SERVICE_FILE}
STOP_BIN="${BIN_PATH}/stop.sh"
echo "STOP_BIN=${STOP_BIN}" >> ${SERVICE_FILE}
echo "#重启脚本路径" >> ${SERVICE_FILE}
RESTART_BIN="${BIN_PATH}/restart.sh"
echo "RESTART_BIN=${RESTART_BIN}" >> ${SERVICE_FILE}
echo "#状态脚本路径" >> ${SERVICE_FILE}
STATUS_BIN="${BIN_PATH}/status.sh"
echo "STATUS_BIN=${STATUS_BIN}" >> ${SERVICE_FILE}

#执行正文

echo "case \"\$1\" in" >> ${SERVICE_FILE}
echo "    start)" >> ${SERVICE_FILE}
echo "        echo  \"Starting ${APPLICATION}... \"" >> ${SERVICE_FILE}
echo "" >> ${SERVICE_FILE}
echo "        sh \"\${START_BIN}\"" >> ${SERVICE_FILE}
echo "        ;;" >> ${SERVICE_FILE}
echo "    stop)" >> ${SERVICE_FILE}
echo "        echo  \"Stoping ${APPLICATION}... \"" >> ${SERVICE_FILE}
echo "" >> ${SERVICE_FILE}
echo "        sh \"\${STOP_BIN}\"" >> ${SERVICE_FILE}
echo "        ;;" >> ${SERVICE_FILE}
echo "    status)" >> ${SERVICE_FILE}
echo "        sh \"\${STATUS_BIN}\"" >> ${SERVICE_FILE}
echo "        ;;" >> ${SERVICE_FILE}
echo "" >> ${SERVICE_FILE}
echo "    restart)" >> ${SERVICE_FILE}
echo "        echo  \"Restart ${APPLICATION}... \"" >> ${SERVICE_FILE}
echo "" >> ${SERVICE_FILE}
echo "        sh \"\${RESTART_BIN}\"" >> ${SERVICE_FILE}
echo "        ;;" >> ${SERVICE_FILE}
echo "    *)" >> ${SERVICE_FILE}
echo "        echo \"Usage: \$0 {start|stop|restart|status}\"" >> ${SERVICE_FILE}
echo "        exit 1" >> ${SERVICE_FILE}
echo "        ;;" >> ${SERVICE_FILE}
echo "" >> ${SERVICE_FILE}
echo "esac" >> ${SERVICE_FILE}
#修改文件的权限
chmod 755 "${SERVICE_FILE}"
#复制文件到 /etc/init.d
#cp -rf "${SERVICE_FILE}" "${SERVICE_DES_APTH}"
#删除软连接
rm -rf "${SERVICE_DES_APTH}"
#建立软连接 ln  -s  [源文件或目录]  [目标文件或目录]
ln  -s "${SERVICE_FILE}" "${SERVICE_DES_APTH}"
#加入开机启动
chkconfig --add "${APPLICATION}"
${SERVICE_DES_APTH} restart