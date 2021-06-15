package com.galaxy.zookeeper.api;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * 创建会话
 * cmd + option + l 格式化代码
 * @author lane
 * @date 2021年06月12日 下午12:07
 */
public class CreateNode implements Watcher {
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

        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateNode());
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
                    //创建节点
                    createNodeSync();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }

        }
    }

    public static void createNodeSync() throws UnsupportedEncodingException, KeeperException, InterruptedException {
        /**
         *  path        ：节点创建的路径
         *  data[]      ：节点创建要保存的数据，是个byte类型的
         *  acl         ：节点创建的权限信息(4种类型)
         *                 ANYONE_ID_UNSAFE    : 表示任何人
         *                 AUTH_IDS    ：此ID仅可用于设置ACL。它将被客户机验证的ID替换。
         *                 OPEN_ACL_UNSAFE    ：这是一个完全开放的ACL(常用)--> world:anyone
         *                 CREATOR_ALL_ACL  ：此ACL授予创建者身份验证ID的所有权限
         *  createMode    ：创建节点的类型(4种类型)
         *                  PERSISTENT：持久节点
         *				    PERSISTENT_SEQUENTIAL：持久顺序节点
         *                  EPHEMERAL：临时节点
         *                  EPHEMERAL_SEQUENTIAL：临时顺序节点
         String node = zookeeper.create(path,data,acl,createMode);
         */

        String node_persistent = zooKeeper.create("/zk_persistent2", "持久节点".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        String node_persistent_sequential = zooKeeper.create("/zk_persistent_sequential2", "持久节点".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        String node_ephemeral = zooKeeper.create("/zk_ephemeral2", "临时节点".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("持久节点："+node_persistent);
        System.out.println("持久顺序节点："+node_persistent_sequential);
        System.out.println("临时节点："+node_ephemeral);


    }


}
