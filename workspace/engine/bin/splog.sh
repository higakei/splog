#!/bin/sh

#変数定義
HOME=.

#クラスパス設定
export CLASSPATH
export CLASSPATH=$CLASSPATH:$HOME
export CLASSPATH=$CLASSPATH:$HOME/splogfilter-1.1.jar
export CLASSPATH=$CLASSPATH:$HOME/mysql-connector-java-5.1.6-bin.jar
export CLASSPATH=$CLASSPATH:$HOME/log4j-1.2.15.jar
export CLASSPATH=$CLASSPATH:$HOME/serializer.jar

#実行
mysql -u root blogfeeds < $HOME/truncate_result.sql 
java jp.co.hottolink.splogfilter.takeda.SplogFilterByDB $*
