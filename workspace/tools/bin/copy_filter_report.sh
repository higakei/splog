#!/bin/sh

#言語設定
export LANG=ja_JP.UTF-8

#変数定義
HOME=/opt/mysql/splog/tools

#クラスパス設定
CLASSPATH=$HOME
for i in `ls -al $HOME/*.jar | grep ^\- | sed -e "s/.* \(.*\)/\1/"`
do
CLASSPATH="$CLASSPATH:$i";
done
#echo $CLASSPATH
export CLASSPATH

#実行
java jp.co.hottolink.splogfilter.tools.copyfilter.CopyFilterReportUI $*
