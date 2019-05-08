package com.zzq.test.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author Zhou Zhong Qing
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: java api 永久监听
 * @date 2019/5/7 10:04
 */
public class ZookeeperClientUpgrade   {


    private ZooKeeper zooKeeper;

    private ConfigureContainer configureContainer;


    public ZookeeperClientUpgrade(ConfigureContainer configureContainer ){
        this.configureContainer = configureContainer;
    }

    public void initialize() {
        try {
            zooKeeper = new ZooKeeper("localhost:2181", 20000, new ZkWatcher(configureContainer));
            configureContainer.setZooKeeper(zooKeeper);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }



    public void exists(String path , boolean watch) throws  Exception{
        zooKeeper.exists(path,watch);
    }

   /* public void handleMessage(String watchPath) throws  Exception {
        System.out.println("收到消息");
        //再监听该子节点
        List<String> Children = getChildren(watchPath);
        for (String a : Children) {
            String childrenpath = watchPath + "/" + a;
            byte[] recivedata = getData(childrenpath);
            String recString = new String(recivedata, "UTF-8");
            System.out.println("receive the path:" + childrenpath + ":data:" + recString);
            //做完了之后，删除该节点
            deletNode(childrenpath, -1);
        }
    }*/

 /*   public List<String> getChildren(String path) throws Exception  {
        //监听该节点子节点的变化情况
        return zooKeeper.getChildren(path, this);
    }*/

    public Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
        return zooKeeper.setData(path, data, version);
    }

    public void deletNode(String path, int version) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, version);
    }

    public byte[] getData(String path) throws KeeperException, InterruptedException {
        return zooKeeper.getData(path, true, null);
    }

    public String create(String path, byte[] data, List<ACL> acl, CreateMode createMode) throws  Exception {
        Stat stat = zooKeeper.exists(path, true);
       /* if(null != stat){
            deletNode(path,-1);
        }*/
        /*
        CreateMode类型分为4种

        1.PERSISTENT--持久型

        2.PERSISTENT_SEQUENTIAL--持久顺序型

        3.EPHEMERAL--临时型

        4.EPHEMERAL_SEQUENTIAL--临时顺序型

        1、2种类型客户端断开后不会消失

        3、4种类型客户端断开后超时时间内没有新的连接节点将会消失
        * */
        return zooKeeper.create(path, data, acl,
                createMode);
    }

}