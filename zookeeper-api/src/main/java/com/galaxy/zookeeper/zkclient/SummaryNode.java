package com.galaxy.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

/**
 * 递归创建节点
 * @author lane
 * @date 2021年06月12日 下午4:07
 */
public class SummaryNode {

    public static void main(String[] args) throws InterruptedException {
        /*
            创建一个zkclient实例就可以完成连接，完成会话的创建
            serverString : 服务器连接地址
            注意：zkClient通过对zookeeperAPI内部封装，将这个异步创建会话的过程同步化了..
         */
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        //同步的了
        System.out.println("客户端创建完成");

        //判断节点是否存在
        boolean zk_a5 = zkClient.exists("/zk_a5");
        if (!zk_a5){
            //创建节点
            zkClient.createEphemeral("/zk_a5","a5_context");
        }
        //读取节点内容
        Object readData = zkClient.readData("/zk_a5");
        System.out.println("节点内容为："+readData);
        //注册监听
        zkClient.subscribeDataChanges("/zk_a5", new IZkDataListener() {
            /*
               当节点数据内容发生变化时，执行的回调方法
               s: path
               o: 变化后的节点内容
            */
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println(s+" 节点变更后的内容为："+o);
            }
            /*
                当节点被删除时，会执行的回调方法
                s : path
             */
            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s+ "被删除了");
            }
        });
        //更新节点内容
        zkClient.writeData("/zk_a5", "a5_context_change");
        System.out.println("此时更新节点完成");
        TimeUnit.SECONDS.sleep(1);
        //删除节点
        zkClient.delete("/zk_a5");
        System.out.println("此时删除节点完成");
        TimeUnit.SECONDS.sleep(1);

    }




}
