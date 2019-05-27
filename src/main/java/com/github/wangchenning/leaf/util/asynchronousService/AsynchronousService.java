package com.github.wangchenning.leaf.util.asynchronousService;

public interface AsynchronousService {
	interface RunnableTask extends Runnable {
	}
	void sumbitRunnableTask(RunnableTask task);
	void shutdown();
}
