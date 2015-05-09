package loader;

import java.io.*;

import process_control.PCB;

public class Loader
{
	private static Loader diskLoader;
	private int jobNum;
	
	private Loader()
	{	
		try
		{
			FileReader fr = new FileReader("input");
			BufferedReader br = new BufferedReader(fr);
			String in = br.readLine();
			System.out.println("First: " + in);
			
			while(in != null)
			{
				System.out.println(in);
				if(in.contains("JOB"))
				{
					jobNum = PCB.getInstance().addJob(in);
				}
				else if(in.contains("Data"))
				{
					PCB.getInstance().addData(in, jobNum);
					jobNum = -1;
				}
				else
				{
					//push to disk.	
				}
				
				in = br.readLine();
			}
			br.close();
			fr.close();
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	
	}
	
	public static synchronized Loader getInstance()
	{
		if(diskLoader == null)
		{
			diskLoader = new Loader();
		}
		
		return diskLoader;
	}
}