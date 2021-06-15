package com.galaxy.zookeeper.api;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 创建会话
 * cmd + option + l 格式化代码
 * @author lane
 * @date 2021年06月12日 下午12:07
 */
public class GetNodeData implements Watcher {
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

        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new GetNodeData());
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
            System.out.println("Client Connected to zookeeper");
                try {
                    getNoteDate();
                    //获取子节点列表
                    getChildren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
         /*
            子节点列表发生改变时，服务器端会发生noteChildrenChanged事件通知
            要重新获取子节点列表，同时注意：通知是一次性的，需要反复注册监听
         */
        if (watchedEvent.getType()==Event.EventType.NodeChildrenChanged){
            System.out.println("子节点列表发生改变被监听到");
            try {
                getChildren();
            countDownLatch.countDown();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }

    private void getNoteDate() throws KeeperException, InterruptedException {

        /**
         * path    : 获取数据的路径
         * watch    : 是否开启监听
         * stat    : 节点状态信息
         *        null: 表示获取最新版本的数据
         *  zk.getData(path, watch, stat);
         */

        byte[] data = zooKeeper.getData("/zk_persistent", false, null);
        System.out.println("节点内容为:"+new String(data));

    }
    public void getChildren() throws KeeperException, InterruptedException {

        /*
            path:路径
            watch:是否要启动监听，当子节点列表发生变化，会触发监听
            zooKeeper.getChildren(path, watch);
         */
        List<String> children = zooKeeper.getChildren("/zk_persistent", true);
        System.out.println("子节点列表为:"+children);

    }

}
