package com.galaxy.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 递归创建节点
 * @author lane
 * @date 2021年06月12日 下午4:07
 */
public class GetNode {

    public static void main(String[] args) throws InterruptedException {
        /*
            创建一个zkclient实例就可以完成连接，完成会话的创建
            serverString : 服务器连接地址
            注意：zkClient通过对zookeeperAPI内部封装，将这个异步创建会话的过程同步化了..
         */
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        //同步的了
        System.out.println("客户端创建完成");
        //创建节点
        /*
            cereateParents : 是否要创建父节点，如果值为true,那么就会递归创建节点
         */
        zkClient.createPersistent("/zk_a1/zk_b4", false);

        List<String> children = zkClient.getChildren("/zk_a1");

        System.out.println("创建完成"+children);
          /*
            客户端可以对一个不存在的节点进行子节点变更的监听
            只要该节点的子节点列表发生变化，或者该节点本身被创建或者删除，都会触发监听
         */
        zkClient.subscribeChildChanges("/zk_a4", new IZkChildListener() {
            /*
                s : parentPath
                list : 变化后子节点列表
             */
            @Override
            public void handleChildChange(String parentPath, List<String> list) throws Exception {
                System.out.println(parentPath+"的子节点发生了变化，变化后为"+list);

            }
        });

        /*
            cereateParents : 是否要创建父节点，如果值为true,那么就会递归创建节点
         */
        zkClient.createPersistent("/zk_a4");
        TimeUnit.SECONDS.sleep(1);

        zkClient.createPersistent("/zk_a4/zk_b1");
        TimeUnit.SECONDS.sleep(1);
    }




}
