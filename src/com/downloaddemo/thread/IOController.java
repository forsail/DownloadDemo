package com.downloaddemo.thread;


public class IOController
{
	private static IOController instance;
	private OperationQueque executor;
	
	public synchronized static IOController get()
	{
		if (instance == null)
		{
			instance = new IOController();
		}
		return instance;
	}
	
	private IOController()
	{
		executor = new OperationQueque();
	}
	
	public void executeCmdOnThread(final Command cmd)
	{
		if (cmd == null)
		{
			return;
		}
		
		executor.addOperation(new DefaultOperation()
		{
			
			@Override
			public void doInBackground()
			{
				cmd.execute();
			}
		});
	}
	

}
