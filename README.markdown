﻿#ボードゲームのサーバ
[![Build Status](https://drone.io/bitbucket.org/OkunoHosomichi/boardgameserver/status.png)](https://drone.io/bitbucket.org/OkunoHosomichi/boardgameserver/latest)

##概要
ボードゲームをプレイできるチャットサーバです。プレイするゲームはサーバのメニューから切り替えられるようにしたいと考えています。

##必要動作環境
* Java SE Runtime Environment 7の最新版

##ライセンスについて
Apache License 2.0

##Mavenについて
ビルドやテストはMavenを利用して実行します。

###利用しているMavenプラグイン
* [Exec Maven Plugin](http://mojo.codehaus.org/exec-maven-plugin/)
* [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
* [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
* [Maven Surefire Report Plugin](https://maven.apache.org/surefire/maven-surefire-report-plugin/)

###利用しているライブラリ
* [Google Guice](https://code.google.com/p/google-guice/)
* [JMockit](https://code.google.com/p/jmockit/)
* [Netty](http://netty.io/)
* [TestNG](http://testng.org/doc/index.html)

###Mavenのプロファイルについて
* LocalWindowsプロファイル : AllEnvとGUIOnlyグループのテストを実行します。
* LocalLinuxプロファイル : AllEnvとGUIOnlyとLinuxOnlyグループのテストを実行します。
* Drone_ioプロファイル : AllEnvとLinuxOnlyグループのテストを実行します。

##テストについて
Mavenのプロファイルを使ってローカルとDrone.ioで実行するテストが変わるようにしています。
**Swing関係のテストにはGUIOnlyグループを指定**し、**ファイルのパーミッションをいじるテストにはLinuxOnlyグループを指定**し、**どの環境でも動かせるならAllEnvグループを指定**します。
ローカルでテストする場合には"mvn surefire:test -P LocalWindows"又は"mvn surefire:test -P LocalLinux"と指定します。  
テスト名には日本語を使っています。基本的にテストを書いてから機能を実装するようにしていますが、そうでない場合もあります。単純なメソッド(ゲッタ/セッタ/委譲など)の場合はテストを書かない場合もあります。プライベートなメソッドは気分次第でテストを書いたり書かなかったりです(それを呼び出すパブリックなメソッドはテストします)。

##Drone.ioについて
[Drone.io](https://drone.io/)を利用しているのは気分です。Bitbucketのリポジトリにpushするだけで実行してくれる上に公開リポジトリは無料でテストできるのでやってみました。Swing関係のテストを実行できませんが大した問題じゃないです。

##Hg-Gitについて
メインはBitbucketですが、Hg-Gitを試そうとGithubにもPushしています。問題が出たらメモします。  
* Hg-Gitを使ってGithubにPushしようとすると変更点が無いと言われてPushできない時がある。そんな時は"hg bookmark -f master"と入力すると良い。