package org.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ZKManagerImpl implements ZKManager {
    private static ZooKeeper zkeeper;
    private static ZKConnection zkConnection;

    public ZKManagerImpl() throws IOException, InterruptedException {
        initialize();
        System.out.println("zookeeper connection initialized");
        closeConnection();
    }

    private void initialize() throws IOException, InterruptedException {
        zkConnection = new ZKConnection();
        zkeeper = zkConnection.connect("localhost");
    }

    public void closeConnection() throws InterruptedException {
        zkConnection.close();
    }

    public void create(String path, byte[] data)
            throws KeeperException,
            InterruptedException {

        zkeeper.create(
                path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
    }

    public Object getZNodeData(String path, boolean watchFlag) throws InterruptedException, KeeperException {

        byte[] b;
        b = zkeeper.getData(path, null, null);
        return new String(b, StandardCharsets.UTF_8);
    }

    public void update(String path, byte[] data) throws KeeperException,
            InterruptedException {
        int version = zkeeper.exists(path, true).getVersion();
        zkeeper.setData(path, data, version);
    }
}