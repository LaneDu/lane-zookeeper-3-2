package com.galaxy.zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * 创建会话
 * cmd + option + l 格式化代码
 * @author lane
 * @date 2021年06月12日 下午12:07
 */
public class UpdateNode implements Watcher {
    //countDownLatch这个类使⼀个线程等待,主要不让main⽅法结束
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {


    /* 客户端可以通过创建⼀个zk实例来连接zk服务器
     new Zookeeper(connectString,sesssionTimeOut,Wather)
     connectString: 连接地址：IP：端⼝
     sesssionTimeOut：会话超时时间：单位毫秒
     Wather：监听器(当特定事件触发监听时，
     zk会通过watcher通知到客户端)*/

        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UpdateNode());
        System.out.println(zooKeeper.getState());
        //等待下会话的连接，要异步向客户端发送会话完成才算是真正的连接
        countDownLatch.await();



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
                System.out.println("发送通知完成");
                //表示会话真正建⽴
                System.out.println("Client Connected to zookeeper");
                try {
                    updateNodeSync();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }

        }
    }

    private void updateNodeSync() throws KeeperException, InterruptedException {

         /*
            path:路径
            data:要修改的内容 byte[]
            version:为-1，表示对最新版本的数据进行修改
            zooKeeper.setData(path, data,version);
         */


        byte[] data = zooKeeper.getData("/zk_persistent", false, null);
        System.out.println("修改前的值：" + new String(data));

        //修改/lg-persistent 的数据 stat: 状态信息对象
        Stat stat = zooKeeper.setData("/zk_persistent", "持久节点修改01".getBytes(), -1);

        byte[] data2 = zooKeeper.getData("/zk_persistent", false, null);
        System.out.println("修改后的值：" + new String(data2));


    }



}
