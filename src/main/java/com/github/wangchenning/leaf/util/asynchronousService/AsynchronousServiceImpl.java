package com.github.wangchenning.leaf.util.asynchronousService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsynchronousServiceImpl implements AsynchronousService {

	private static final Logger logger = LoggerFactory.getLogger(AsynchronousServiceImpl.class);
	private final ExecutorService executorService;
	public  AsynchronousServiceImpl() {
		executorService = Executors.newSingleThreadExecutor();
		logger.info("异步调度服务已开启");
	}
	@Override
	public void sumbitRunnableTask(RunnableTask task) {
		executorService.submit(task);
	}
	@Override
	public void shutdown() {
		executorService.shutdown();
		if (executorService.isShutdown()) {
			logger.info("异步调度服务已关闭");
		}
	}
	
	@PreDestroy
	public void detroy() {
		shutdown();
	}

}
