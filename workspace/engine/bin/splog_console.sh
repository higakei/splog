#!/bin/sh

#変数定義
HOME=.

#クラスパス設定
export CLASSPATH
export CLASSPATH=$CLASSPATH:$HOME
export CLASSPATH=$CLASSPATH:$HOME/splogfilter-1.1.jar
export CLASSPATH=$CLASSPATH:$HOME/log4j-1.2.15.jar
export CLASSPATH=$CLASSPATH:$HOME/serializer.jar
export CLASSPATH=$CLASSPATH:$HOME/te-common.jar

#実行
java jp.co.hottolink.splogfilter.takeda.SplogFilterByCosole $*
