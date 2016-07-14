#!/bin/bash
#========================
# create by WangJun
# date 2014-11-06
# email wangjuntytl@163.com
#
# =============================
#
# 描述：构建脚本
#
# 如果你的开发平台是window，需要手动执行以下步骤
#   1. git clone https://github.com/WangJunTYTL/peaceful-basic-platform.git
#   2. 进入peaceful-basic-platform 目录 ，先执行 mvn install f peaceful-parent/pom.xml -Dmaven.test.skip=true 然后在执行 mvn install  -Dmaven.test.skip=true
#   3. 进入perf4j-zh目录，执行 mvn install  -Dmaven.test.skip=true
#   4. 进入apm-dashboard目录 执行 mvn jetty:run 启动dashboard项目
#   5. 进入perf4j-demo目录, 执行 mvn jetty:run 启动演示项目
#   6. 不断访问演示项目地址:http://127.0.0.1:8888/demo,使其产生请求量,查看dashboard项目查看监控数据
#==================================

ENV=$1
[ "x${ENV}" == "x" ] && ENV='dev' # dev test product
echo '----------------------------------------------'
echo "构建环境：${ENV}"
echo '----------------------------------------------'
echo "构建工具git、mvn 安装检测"

cmd_is_exist(){
    echo "check $1 cmd is install ... "
    _r=`which $1`
    if [ $? == 0 ];then
        echo "OK"
    else
        echo "请先安装$1，并添加$1到PATH变量中" && eit 1
    fi
}

cmd_is_exist "mvn"
cmd_is_exist "git"

echo '----------------------------------------------'

wait
echo "准备下载依赖包并开始构建...初次构建需要下载依赖包~~这可能需要点时间~~等会再回来吧"
sleep 2
#下载依赖包，最好手动将依赖包install到你的本
echo "下载依赖包peaceful-basic-platform"
[ -d "peaceful-basic-platform" ] && rm -rf peaceful-basic-platform
git clone https://github.com/WangJunTYTL/peaceful-basic-platform.git ||  exit 1
cd peaceful-basic-platform
mvn clean -P${ENV} -f peaceful-boot-parent/pom.xml install  -Dmaven.test.skip=true || exit 1
mvn clean -P${ENV} install -f peaceful-boot/pom.xml -Dmaven.test.skip=true || exit 1
cd ..

wait
rm -rf peaceful-basic-platform

mvn -P${ENV} clean install  -Dmaven.test.skip=true || exit 1
echo '-------------------------------------------------------------------------------'
echo "恭喜你~~构建成功！"
echo '-------------------------------------------------------------------------------'


