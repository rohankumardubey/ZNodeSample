package org.example;

import org.apache.zookeeper.*;

public class ZooKeeperExample {

    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = connectToZooKeeper();

        // Create a znode
        String znodePath = "/exampleNode";
        String znodeValue = "Hello, ZooKeeper!";
        createZnode(zooKeeper, znodePath, znodeValue);

        // Read data from the created znode
        String data = readZnodeData(zooKeeper, znodePath);
        System.out.println("Data from " + znodePath + ": " + data);

        // Close the ZooKeeper connection
        zooKeeper.close();
    }

    private static ZooKeeper connectToZooKeeper() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // Handle ZooKeeper events if needed
                System.out.println("ZooKeeper event: " + event.getType());
            }
        });
        // Wait for the connection to be established
        while (zooKeeper.getState() != ZooKeeper.States.CONNECTED) {
            Thread.sleep(100);
        }
        System.out.println("Connected to ZooKeeper!");
        return zooKeeper;
    }

    private static void createZnode(ZooKeeper zooKeeper, String path, String data) throws Exception {
        // Create a znode with the specified path and data
        String createdPath = zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Znode created at path: " + createdPath);
    }

    private static String readZnodeData(ZooKeeper zooKeeper, String path) throws Exception {
        // Read data from the specified znode
        byte[] dataBytes = zooKeeper.getData(path, false, null);
        return new String(dataBytes);
    }
}
