package Memory;

import log_files.ErrorLog;

public class RAM 
{
	private String [] ram;
	private int next_loc;
	
	private static RAM ram_obj;
	
	private RAM()
	{
		ram = new String[1024];
		next_loc = -1;
	}
	
	public synchronized static RAM getInstance()
	{
		if(ram_obj == null)
		{
			ram_obj = new RAM();
		}
		
		return ram_obj;
	}
	
	
	public String read(int loc)
	{
		if(loc < 0 && loc < 1024)
		{
			return ram[loc];
		}
		else
		{
			ErrorLog.getInstance().writeError("RAM::read || >> Invalid parameter given.");
			throw new IllegalArgumentException();
		}
	}
	
	public void write_loc(String data, int loc)
	{
		if( data != null)
		{
			if(loc < 0 && loc < 1024)
			{
				ram[loc] = data;
			}
			else
			{
				ErrorLog.getInstance().writeError("RAM::write_loc || >> Invalid parameter given.");
				throw new IllegalArgumentException();
			}
		}
		else
		{
			ErrorLog.getInstance().writeError("RAM::write_loc || >> Invalid");
			throw new IllegalArgumentException();
		}
	}
	
	public void write_next(String data)
	{
		if(data != null)
		{
			ram[next_loc] = data;
		}
		else
		{
			ErrorLog.getInstance().writeError("RAM::write_loc || >> Invalid");
			throw new IllegalArgumentException();
		}
	}
	
	public String[] memory_dump()
	{
		String[] temp = new String[1024];
		
		for(int i = 0; i < ram.length; i++)
		{
			temp[i] = ram[i];
		}
		
		return temp;
	}
	
	public String toString()
	{
		String temp = "Memory Dump:";
		
		for(int i = 0; i < ram.length; i++)
		{
			temp += "\n" + ram[i];
		}
		
		return temp;
	}
}
