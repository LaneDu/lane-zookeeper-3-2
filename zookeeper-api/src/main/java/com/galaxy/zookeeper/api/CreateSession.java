package com.galaxy.zookeeper.api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 创建会话
 * cmd + option + l 格式化代码
 * @author lane
 * @date 2021年06月12日 下午12:07
 */
public class CreateSession implements Watcher {
    //countDownLatch这个类使⼀个线程等待,主要不让main⽅法结束
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {


    /* 客户端可以通过创建⼀个zk实例来连接zk服务器
     new Zookeeper(connectString,sesssionTimeOut,Wather)
     connectString: 连接地址：IP：端⼝
     sesssionTimeOut：会话超时时间：单位毫秒
     Wather：监听器(当特定事件触发监听时，
     zk会通过watcher通知到客户端)*/

        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateSession());
        System.out.println(zooKeeper.getState());
        //等待下会话的连接，要异步向客户端发送会话完成才算是真正的连接
        countDownLatch.await();
        //表示会话真正建⽴
        System.out.println("=========Client Connected tozookeeper==========");

    }

    // 当前类实现了Watcher接⼝，重写了process⽅法，
    // 该⽅法负责处理来⾃Zookeeper服务端的 watcher通知，
    // 在收到服务端发送过来的SyncConnected事件之后，
    // 解除主程序在CountDownLatch上 的等待阻塞，
    // ⾄此，会话创建完毕
    @Override
    public void process(WatchedEvent watchedEvent) {
        //当连接创建了，服务端发送给客户端SyncConnected事件
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            {
                countDownLatch.countDown();
            }
        }
    }
}
