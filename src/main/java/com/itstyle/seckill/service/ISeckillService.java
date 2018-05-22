package com.itstyle.seckill.service;

import java.util.List;

import com.itstyle.seckill.common.entity.Result;
import com.itstyle.seckill.common.entity.Seckill;

public interface ISeckillService {

	/**
	 * 查询全部的秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	/**
	 * 查询秒杀售卖商品
	 * @param seckillId
	 * @return
	 */
	Long getSeckillCount(long seckillId);
	/**
	 * 删除秒杀售卖商品记录
	 * @param seckillId
	 * @return
	 */
	void deleteSeckill(long seckillId);
	
	/**
	 * 秒杀 一、会出现数量错误
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	Result startSeckil(long seckillId,long userId);
	
	/**
	 * 秒杀 二、程序锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	Result startSeckilLock(long seckillId,long userId);
	/**
	 * 秒杀 二、程序锁AOP
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	Result startSeckilAopLock(long seckillId,long userId);
	
	/**
	 * 秒杀 二、数据库悲观锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	Result startSeckilDBPCC_ONE(long seckillId,long userId);
	/**
	 * 秒杀 三、数据库悲观锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	Result startSeckilDBPCC_TWO(long seckillId,long userId);
	/**
	 * 秒杀 三、数据库悲观锁
	 * @param seckillId
	 * @param userId
	 * @return
	 */
	Result startSeckilDBOCC(long seckillId,long userId,long number);
    
}
