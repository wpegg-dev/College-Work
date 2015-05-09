//manages the memory of ram and disk
public class MemoryManager 
{
	private static Disk disk;
	private static RAM ram;
	//Default constructor
	public MemoryManager()
	{
		
		
	}
	//writes data to disk.
	public static void writeDiskData(String data)
	{
		disk.write(data);
	}
	//reads data in disk
	public static String readDiskData(int r)
	{
		return disk.read(r);
	}
	//writes data in RAM
	public void writeRAMData (String data, int location)
	{
		ram.write(data, location);
	}
	//reads data in RAM 
	public String readRAMData (int r)
	{
		return ram.read(r);
	}
	//prints data in disk 
	public String printDisk () 
	{
		return disk.toString();
		
	}
	//prints data in RAM 
	public String printRAM()
	{
		return ram.toString();
		
	}
	

}
