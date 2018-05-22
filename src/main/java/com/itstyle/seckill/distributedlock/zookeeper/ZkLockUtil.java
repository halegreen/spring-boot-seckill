package com.itstyle.seckill.distributedlock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;

/**
 * zookeeper 分布式锁
 * @author 科帮网 By https://blog.52itstyle.com
 */
public class ZkLockUtil{
	
	@Value("${zookeeper.address}")
	private static String address;
	
	public static CuratorFramework client;
	
	static{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3); 
        client = CuratorFrameworkFactory.newClient(address, retryPolicy); 
        client.start();
	}
	
    //获得了锁
    public static void acquire(String lockKey){
    	try {
    		InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock/"+lockKey); 
    		mutex.acquire();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    //释放锁
    public static void release(String lockKey){
    	try {
    		InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock/"+lockKey); 
    		mutex.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}  
