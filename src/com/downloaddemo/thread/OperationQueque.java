package com.downloaddemo.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class OperationQueque
{
    private final int DEFAULT_THREAD_MUMBER = 6;
    private ExecutorService service;

    public OperationQueque()
    {
        service = Executors.newFixedThreadPool(DEFAULT_THREAD_MUMBER, new MyThreadFactory());
    }

    public void setMaxConcurrentOperationCount(int maxConcurrentOperationCount)
    {
        service = null;
        service = Executors.newFixedThreadPool(maxConcurrentOperationCount, new MyThreadFactory());
    }

    public void addOperation(Runnable operation)
    {
        service.execute(operation);
    }

    public class MyThreadFactory implements ThreadFactory
    {
        public Thread newThread(Runnable r)
        {
            Thread t = new Thread(r);
            t.setPriority(Thread.MAX_PRIORITY);
            return t;
        }
    }
}
