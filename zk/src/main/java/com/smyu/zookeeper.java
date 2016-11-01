package com.smyu;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.I0Itec.zkclient.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created by ysm on 2016/11/1.
 */
public class zookeeper {
    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper("localhost:" + 2181,
                5000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
        Stat stat_root = zk.exists("/testRootPath", true);
        if (null == stat_root)
            // 创建一个目录节点
            zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        Stat stat_childPatOne = zk.exists("/testRootPath/testChildPathOne", true);
        if (null == stat_childPatOne)
            // 创建一个子目录节点
            zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(new String(zk.getData("/testRootPath", false, null)));
        // 取出子目录节点列表
        System.out.println(zk.getChildren("/testRootPath", true));
        // 修改子目录节点数据
        zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
        System.out.println("目录节点状态：[" + zk.exists("/testRootPath", true) + "]");

        Stat stat_childPathTwo = zk.exists("/testRootPath/testChildPathTwo", true);
        if (null == stat_childPathTwo)
            // 创建另外一个子目录节点
            zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo", true, null)));

        // 删除子目录节点
        zk.delete("/testRootPath/testChildPathTwo", -1);
        zk.delete("/testRootPath/testChildPathOne", -1);
        // 删除父目录节点
        zk.delete("/testRootPath", -1);
        // 关闭连接
        zk.close();
    }

}
