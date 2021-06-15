package com.galaxy.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author lane
 * @date 2021年06月13日 下午12:09
 */
public class GetNode {

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        //其底层方法创建会话fluent风格
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181") //server地址
                .sessionTimeoutMs(50000)    //会话超时时间
                .connectionTimeoutMs(10000) //连接超时时间
                .retryPolicy(retryPolicy)   //重试策略
                .namespace("base")// 独立的命名空间 /base
                .build();
        client.start();
        System.out.println("客户端会话连接成功");
        String path = "/zk_a6/b1";
        //可以递归创建，如果父节点不存在则创建
        String forPath = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, "b1_context".getBytes("utf-8"));
        System.out.println("节点创建完成："+forPath);
        //普通查询
        byte[] bytes = client.getData().forPath(path);
        System.out.println(path+"的节点数据是"+new String(bytes));
        //状态查询
        Stat stat = new Stat();
        byte[] bytes1 = client.getData().storingStatIn(stat).forPath(path);
        System.out.println(path+"的节点数据是"+new String(bytes1)+" 状态信息是"+stat);

    }

}
