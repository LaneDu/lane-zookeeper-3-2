package com.galaxy.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author lane
 * @date 2021年06月12日 下午4:07
 */
public class CreateSession {

    public static void main(String[] args) {
        /*
            创建一个zkclient实例就可以完成连接，完成会话的创建
            serverString : 服务器连接地址
            注意：zkClient通过对zookeeperAPI内部封装，将这个异步创建会话的过程同步化了..
         */
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        //同步的了
        System.out.println("客户端创建完成");


    }




}
