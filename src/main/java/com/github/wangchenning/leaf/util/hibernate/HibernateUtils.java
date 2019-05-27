package com.github.wangchenning.leaf.util.hibernate;//import javax.persistence.CacheRetrieveMode;

///** 
// * Copyright (c) 2005-2012 https://github.com/zhangkaitao 
// * 
// * Licensed under the Apache License, Version 2.0 (the "License"); 
//		<dependency>
//            <groupId>org.hibernate</groupId>
//            <artifactId>hibernate-core</artifactId>
//            <version>5.2.11.Final</version>
//        </dependency>
//        
//        <dependency>
//		   <groupId>org.hibernate</groupId>
//		   <artifactId>hibernate-entitymanager</artifactId>
//		   <version>5.2.11.Final</version>
//		</dependency>
//q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);//跳过从缓存获取数据
//HibernateUtils.clearLevel2Cache(em);//清理一二级缓存
// */  
//package com.github.wangchenning.leaf.util.hibernate;
//import org.hibernate.Cache;  
//import org.hibernate.Session;  
//import org.hibernate.SessionFactory;  
//import org.hibernate.ejb.HibernateEntityManagerFactory;  
//  
//import javax.persistence.EntityManager;  
//import javax.persistence.EntityManagerFactory;  
//  
///** 
// * 根据 jpa api 获取hibernate相关api 
// * <p>User: wangchenning
// * <p>Date: 13-5-23 下午6:24 
// * <p>Version: 1.0 
// */  
//public class HibernateUtils {  
//  
//    /** 
//     * 根据jpa EntityManager 获取 hibernate Session API 
//     * @param em 
//     * @return 
//     */  
//    public static Session getSession(EntityManager em) {  
//        return (Session) em.getDelegate();  
//    }  
//  
//    /** 
//     * 根据jpa EntityManager 获取 hibernate SessionFactory API 
//     * @see #getSessionFactory(javax.persistence.EntityManagerFactory) 
//     * @param em 
//     * @return 
//     */  
//    public static SessionFactory getSessionFactory(EntityManager em) {  
//        return getSessionFactory(em.getEntityManagerFactory());  
//    }  
//  
//    /** 
//     * 根据jpa EntityManagerFactory 获取 hibernate SessionFactory API 
//     * @param emf 
//     * @return 
//     */  
//    public static SessionFactory getSessionFactory(EntityManagerFactory emf) {  
//        return ((HibernateEntityManagerFactory)emf).getSessionFactory();  
//    }  
//  
//    /** 
//     * 根据 jpa EntityManager 获取hibernate Cache API 
//     * @see #getCache(javax.persistence.EntityManagerFactory) 
//     * @param em 
//     * @return 
//     */  
//    public static Cache getCache(EntityManager em) {  
//        return getCache(em.getEntityManagerFactory());  
//    }  
//  
//    /** 
//     * 根据jpa EntityManagerFactory 获取 hibernate Cache API 
//     * @param emf 
//     * @return 
//     */  
//    public static Cache getCache(EntityManagerFactory emf) {  
//        return getSessionFactory(emf).getCache();  
//    }  
//  
//    /** 
//     * 清空一级缓存 
//     * @param em 
//     */  
//    public static void clearLevel1Cache(EntityManager em) {  
//        em.clear();  
//    }  
//  
//    /** 
//     * 根据jpa EntityManager 清空二级缓存 
//     * @see #clearLevel2Cache(javax.persistence.EntityManagerFactory) 
//     * @param em 
//     */  
//    public static void clearLevel2Cache(EntityManager em) {  
//        clearLevel2Cache(em.getEntityManagerFactory());  
//    }  
//  
//    /** 
//     * 根据jpa EntityManagerFactory 清空二级缓存 包括： 
//     * 1、实体缓存 
//     * 2、集合缓存 
//     * 3、查询缓存 
//     * 注意： 
//     *  jpa Cache api 只能evict 实体缓存，其他缓存是删不掉的。。。 
//     *  @see org.hibernate.ejb.EntityManagerFactoryImpl.JPACache#evictAll() 
//     * 
//     * @param emf 
//     */  
//    public static void clearLevel2Cache(EntityManagerFactory emf) {  
//        Cache cache = HibernateUtils.getCache(emf);  
//        cache.evictEntityRegions();  
//        cache.evictCollectionRegions();  
//        cache.evictDefaultQueryRegion();  
//        cache.evictQueryRegions();  
//        cache.evictNaturalIdRegions();  
//    }  
//}  