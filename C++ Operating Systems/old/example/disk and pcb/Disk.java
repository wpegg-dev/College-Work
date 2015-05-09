public class Disk{
	private static int nextWrite = 0;
	private static String disk [] = new String [2048];
	public static boolean write(String a){
			disk[nextWrite] = a;
			nextWrite = nextWrite + a.length();
		}
		return true;
	}
	public static String read(int a, int b){
		String c;
		for(a;a<=b;a++)
			c=+disk[a];
		return c;
	}
}