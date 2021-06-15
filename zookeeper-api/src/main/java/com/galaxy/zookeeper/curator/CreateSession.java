package com.galaxy.zookeeper.curator;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


import java.util.List;

/**
 * @author lane
 * @date 2021年06月13日 上午11:08
 */
public class CreateSession {

    public static void main(String[] args) {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建客户端连接
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        //创建会话
        curatorFramework.start();
        System.out.println("客户端会话连接成功");

        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 5000,1000,retryPolicy);
        client.start();
        System.out.println("客户端会话2连接成功");
        //其底层方法创建会话fluent风格
        CuratorFramework build = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(1000)
                .retryPolicy(retryPolicy)
                .namespace("base")// 独立的命名空间 /base
                .build();
        build.start();
        System.out.println("客户端会话3连接成功");
    }
/*

    public class CuratorFrameworkFactory {

    public static CuratorFrameworkFactory.Builder builder() {
        return new CuratorFrameworkFactory.Builder();
    }
    //静态内部类
    public static class Builder {
         //set方法没有加set且返回为当前对象
         public CuratorFrameworkFactory.Builder sessionTimeoutMs(int sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }
        //build方法返回当前对象
          public CuratorFramework build() {
            return new CuratorFrameworkImpl(this);
        }

         private Builder() {
            this.sessionTimeoutMs = CuratorFrameworkFactory.DEFAULT_SESSION_TIMEOUT_MS;
            this.connectionTimeoutMs = CuratorFrameworkFactory.DEFAULT_CONNECTION_TIMEOUT_MS;
            this.maxCloseWaitMs = CuratorFrameworkFactory.DEFAULT_CLOSE_WAIT_MS;
            this.threadFactory = null;
            this.authInfos = null;
            this.defaultData = CuratorFrameworkFactory.LOCAL_ADDRESS;
            this.compressionProvider = CuratorFrameworkFactory.DEFAULT_COMPRESSION_PROVIDER;
            this.zookeeperFactory = CuratorFrameworkFactory.DEFAULT_ZOOKEEPER_FACTORY;
            this.aclProvider = CuratorFrameworkFactory.DEFAULT_ACL_PROVIDER;
            this.canBeReadOnly = false;
            this.useContainerParentsIfAvailable = true;
        }
    }

    }
   }
 */

/*public class CuratorFrameworkImpl implements CuratorFramework {
    public CuratorFrameworkImpl(CuratorFrameworkFactory.Builder builder) {
        ZookeeperFactory localZookeeperFactory = this.makeZookeeperFactory(builder.getZookeeperFactory());
        this.client = new CuratorZookeeperClient(localZookeeperFactory, builder.getEnsembleProvider(), builder.getSessionTimeoutMs(), builder.getConnectionTimeoutMs(), new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                CuratorEvent event = new CuratorEventImpl(CuratorFrameworkImpl.this, CuratorEventType.WATCHED, watchedEvent.getState().getIntValue(), CuratorFrameworkImpl.this.unfixForNamespace(watchedEvent.getPath()), (String) null, (Object) null, (Stat) null, (byte[]) null, (List) null, watchedEvent, (List) null);
                CuratorFrameworkImpl.this.processEvent(event);
            }
        }, builder.getRetryPolicy(), builder.canBeReadOnly());
    }*/

}
