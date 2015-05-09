/**
* A CPU Emulator
*
* @author William Jeff Lett
* @author Kevin DeBrito
*/

public class CPU {	

	/** Jeff's Debugging variable.  Set to true for his debugging output. */
	public final static boolean DEBUG = true;

	/** VARIABLES */
	
	/** Program Counter */
	private static int pc;
	
	/** Ram */
	private static RAM ram;	
	
	/** Cache */
	private static String[] cache;
	
	/** Cache Size */
	private static int cacheSize;	
	
	/** Current process */
	private static PCB thisProcess;
	
	/** Registers */
	private static int[] registers;
	
	/** Status of the CPU */
	private static Driver.Status status;
	
	/** The sum of the percentage of ram used for each cycle (for stats) */
	private static int totalRamUsed;
	
	/** The sum of the percentage of cache used for each cycle (for stats) */
	private static int totalCacheUsed; 
	

	/** Setters */
	
	public static void setPc(int p) { pc=p; }
	public static void setRegisters(int[] r) { 
		registers=r; 
	}
	public static void setStatus(Driver.Status s) { status=s; }
	public static void setThisProcess(PCB p) { thisProcess=p; }
	public static void setCache(String[] c) { cache=c; }
	public static void setCacheSize(int c) { cacheSize=c; }
	
	/** Getters */
	
	public static int getPc() { return pc; }
	public static int[] getRegisters() { return registers; }
	public static Driver.Status getStatus() { return status; }
	public static PCB getThisProcess() { return thisProcess; }	
	public static String[] getCache() { return cache; }
	public static int getCacheSize() { return cacheSize; }
	
	
	/**
	* Constructor for CPU
	*/
	public CPU() {
		pc=0;
		thisProcess=null;
		status=Driver.Status.NEW;
		registers=new int[Driver.NUM_REGISTERS];
		for(int i=0;i<Driver.NUM_REGISTERS;++i)
			registers[i]=0;
	}
	
	/**
	* Constructor for CPU
	* @param r The Ram for this CPU
	*
	public CPU(RAM r) {
		pc=0;
		thisProcess=null;
		status=Driver.Status.NEW;
		registers=new int[Driver.NUM_REGISTERS];
		for(int i=0;i<Driver.NUM_REGISTERS;++i)
			registers[i]=0;
		ram=r;
		cache=new String[size];
		cacheSize=size;
		totalRamUsed=0;
		totalCacheused=0;	
	}*/
	
	/**
	* Constructor for CPU
	* @param r The Ram for this CPU
	* @param size The largest process's size (for the cache size)
	*/
	public CPU(RAM r,int size) {
		pc=0;
		thisProcess=null;
		status=Driver.Status.NEW;
		registers=new int[Driver.NUM_REGISTERS];
		for(int i=0;i<Driver.NUM_REGISTERS;++i)
			registers[i]=0;
		ram=r;
		cache=new String[size];
		cacheSize=size;
		totalRamUsed=0;
		totalCacheUsed=0;	
	}
	

	/**
	* Prints this CPU
	*/
	public void print() {
		System.out.println(toString());	
	}	
	
	/**
	* Converts this CPU into a string for debugging
	* @return A string representation of this CPU
	*/
	public String toString() {
		String tmp="";
		tmp += "PC:"+pc;
		tmp += " Status:"+status;
		tmp += " Registers:";
		for(int i=0;i<Driver.NUM_REGISTERS;++i)
			tmp += " ["+i+"]:"+registers[i];
		tmp += "\n";
		for(int i=0;i<cacheSize;++i) {
			if(i%8!=0) 
				tmp+=",";			
			tmp +=cache[i];
			if(i%8==7)
				tmp+=":"+i+"\n";	
		}
		return tmp;	
	}		
	/**
	* Returns the type from an instruction
	* @param s The instruction to get the type of
	* @return The type
	*/
	public static int getType(String s) {
		if(s==null || s=="")
			return 0;
		String type = s.substring(0,2);
		return Integer.parseInt(type,2);	
	}
	
	/** 
	* Returns the opcode from an instruction
	* @param s The instruction to get the opcode from
	* @return The opcode
	*/	
	public static int getOpCode(String s) {	
		if(s==null || s=="")
			return 0;
		String opCode = s.substring(2,8);
		return Integer.parseInt(opCode,2);
	}
	
	/**
	* Converts a hex string to a binary string
	* @param s Hex String to convert
	* @return The converted binary string
	*/
	public static String hexToBinary (String s) {
		if(s!=null && s!="") {
			String ret = Long.toBinaryString(Long.parseLong(s,16));	
	  		for(int i=0+ret.length();i<32;++i)
	  			ret = "0"+ret;
	  		return ret; 
		}	
		return "";
	}

	/** 
	* Fetches the next instruction from RAM
	* @param r The ram to read from
	* @param p The next process (from the dispatcher) 
	*/
	public static String fetch(PCB p) {
		thisProcess = p;		
		return cache[pc-p.getInstMemLoc()];
	}
	
	/**
	* Decodes an instruction from hex to binary
	* @param s The instruction to convert
	* @return The converted binary instruction
	*/
	public static String decode(String s) {
		return hexToBinary(s);
	}
	
	/**
	* Splits an arithmetic instruction into a readable string for debugging
	* @param s The instruction to be converted
	*/
	public static String arithString(String s) {
		return getType(s)+"|"+getOpCode(s)+"|"+getArithSReg1(s)+"|"+getArithSReg2(s)+"|"+getArithDReg(s)+"|"+s.substring(20,32);
	}	
	/**
	* Splits a conditional instruction into a readable string for debugging
	* @param s The instruction to be converted
	*/
	public static String condString(String s) {
		return getType(s)+"|"+getOpCode(s)+"|"+getCondBReg(s)+"|"+getCondDReg(s)+"|"+getCondAddress(s);	
	}
	
	/** 
	* Splits an unconditional instruction into a readable string for debugging
	* @param s The instruction to be converted
	*/
	public static String uncondString(String s) {
		return getType(s)+"|"+getOpCode(s)+"|"+getUncondAddress(s);
	}	
	/**
	* Splits an input / output instruction into a readable string for debugging
	* @param s The instruction to be converted
	*/
	public static String inAndOutString(String s) {
		return condString(s);	
	}
	
	/**
	* Gets the SReg1 from an arithmetic instruction
	* @param s The arithmetic instruction to get the SReg1 from
	* @return The SReg1
	*/
	public static String getArithSReg1(String s) {
		return s.substring(8,12);
	} 
	/**
	* Gets the SReg2 from an arithmetic instruction
	* @param s The arithmetic instruction to get the SReg2 from
	* @return The SReg2
	*/
	public static String getArithSReg2(String s) {
		return s.substring(12,16);
	} 
	/**
	* Gets the DReg from an arithmetic instruction
	* @param s The arithmetic instruction to get the SReg from
	* @return The SReg2
	*/
	public static String getArithDReg(String s) {
		return s.substring(16,20);
	} 
	/**
	* Gets the BReg from a conditional branch instruction
	* @param s The conditional instruction to get the BReg from
	* @return The BReg
	*/
	public static String getCondBReg(String s) {
		return s.substring(8,12);	
	}

	/**
	* Gets the DReg from a conditional branch instruction
	* @param s The conditional instruction to get the DReg from
	* @return The DReg
	*/
	public static String getCondDReg(String s) {
		return s.substring(12,16);	
	}

	/**
	* Gets the address from a conditional branch instruction
	* @param s The conditional instruction to get the address from
	* @return The address
	*/
	public static String getCondAddress(String s) {
		return s.substring(16,32);	
	}
	
	/**
	* Gets the address from an unconditional branch instruction
	* @param s The unconditional instruction to get the address from
	* @return The address
	*/
	public static String getUncondAddress(String s) {
		return s.substring(8,32);	
	}
	
	/**
	* Formats a hex string for cache
	* @param s The hex string to format
	* @return The formatted hex string
	*/
	public static String hexFormat(String s) {
		if(s!=null && s!="") {
			s=s.toUpperCase();
			for(int i=0+s.length();i<8;++i)
				s="0"+s;
			return s;
		}
		return "00000000";
	}	
	
	/**
	* Prints the cache info (stats)
	* The number of cycles that have ran
	*/
	public static void printCacheInfo(int totalCycleCounter) {
		System.out.println("Average Cache Used: "+(totalCacheUsed/totalCycleCounter)+"%");	
	}	
	
	
	/**
	* Performs arithmetic instructions
	*/
	public static void arithmetic(String s) {
		int sReg1=Integer.parseInt(getArithSReg1(s),2),sReg2=Integer.parseInt(getArithSReg2(s),2),dReg=Integer.parseInt(getArithDReg(s),2);
		if(DEBUG)
			;//System.out.println(arithString(s));//System.out.println("s="+s+" sReg1="+sReg1+" sReg2="+sReg2+" dReg:"+dReg);
		if(s!="") {
		
			if(getOpCode(s)==4) {
				if(DEBUG)				
					;//System.out.println("MOV registers["+sReg1+"]:"+registers[sReg1]+"=registers["+sReg2+"]:"+registers[sReg2]);		
				registers[sReg1]=registers[sReg2];
			}			
			else if(getOpCode(s)==5) {
				if(DEBUG)
					;//System.out.println("ADD r["+sReg1+"]="+registers[sReg1]+" + r["+sReg2+"]="+registers[sReg2]+" = "+(registers[sReg1]+registers[sReg2])+" put into registers["+dReg+"] s="+arithString(s));		
				registers[dReg]=registers[sReg1]+registers[sReg2];			
			}
			else if(getOpCode(s)==6) {
				if(DEBUG)				
					;//System.out.println("SUB "+registers[sReg1]+" - "+registers[sReg2]+" = "+(registers[sReg1]-registers[sReg2])+" put into registers["+dReg+"]");
				registers[dReg]=registers[sReg1]-registers[sReg2];							
			}		
			else if(getOpCode(s)==7) {	
				if(DEBUG)			
					;//System.out.println("MUL "+registers[sReg1]+" * "+registers[sReg2]+" = "+(registers[sReg1]*registers[sReg2])+" put into registers["+dReg+"]");
				registers[dReg]=registers[sReg1]*registers[sReg2];		
			}
			else if(getOpCode(s)==8) {
				if(registers[sReg2]!=0) {			
					if(DEBUG)				
						;//System.out.println("DIV "+registers[sReg1]+" / "+registers[sReg2]+" = "+(registers[sReg1]/registers[sReg2])+" put into registers["+dReg+"]");				
					registers[dReg]=registers[sReg1]/registers[sReg2];
				}
				else
					;//System.out.println("Error:  OTHER DIV");			
			}		
			else if(getOpCode(s)==9) {
				if(DEBUG)				
					;//System.out.println("AND r["+sReg1+"]="+registers[sReg1]+" & r["+sReg2+"]="+registers[sReg2]+" = "+(registers[sReg1]&registers[sReg2])+" put into registers["+dReg+"]");		
				registers[dReg]=registers[sReg1]&registers[sReg2];		
			}		
			else if(getOpCode(s)==10) {
				if(DEBUG)		
					;//System.out.println("OR r["+sReg1+"]="+registers[sReg1]+" | r["+sReg2+"]="+registers[sReg2]+" = "+(registers[sReg1]|registers[sReg2])+" put into registers["+dReg+"]");		
				registers[dReg]=registers[sReg1]|registers[sReg2];
			}		
			else if(getOpCode(s)==16) {
				if(DEBUG)				
					;//System.out.println("SLT r["+sReg1+"]="+registers[sReg1]+" > r["+sReg2+"]="+registers[sReg2]+" = "+(registers[sReg1]>registers[sReg2]?0:1)+" put into registers["+dReg+"]");		
				registers[dReg]=registers[sReg1]>registers[sReg2]?0:1;	
			}	
			else
				;//System.out.println("Missed:"+arithString(s)+" opCode:"+getOpCode(s));	
		}
	}
	
	public static void conditional(String s) {
		int bReg=Integer.parseInt(getCondBReg(s),2),dReg=Integer.parseInt(getCondDReg(s),2),address=Integer.parseInt(getCondAddress(s),2);
		//TMP FIX FOR A BUG		
		if(address!=1)		
			address/=4;		
		if(Driver.DEBUG)
			;//System.out.println("CONDITIONAL\n"+condString(s));//System.out.println("s="+s+" sReg1="+sReg1+" sReg2="+sReg2+" address:"+address);
		
		if(getOpCode(s)==2) {
			//System.out.println("ST ["+dReg+"]:"+registers[dReg]+"=["+bReg+"]:"+registers[bReg]+" address:"+address);
			if(dReg!=0) { //Use dReg
				if(DEBUG)
					;//System.out.println("ST "+Integer.toHexString(registers[bReg])+" into:"+(thisProcess.getInstMemLoc()+registers[dReg]));	
				cache[registers[dReg]]=hexFormat(Integer.toHexString(registers[bReg]));		
				//ram.write(Integer.toHexString(registers[bReg]),(thisProcess.getInstMemLoc()+registers[dReg]));
//				registers[dReg]=registers[bReg];//System.out.println("NO DREG*!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");	
			}
			else { //Use address
				if(DEBUG)			
					;//System.out.println("ST "+Integer.toHexString(registers[bReg])+" into:"+(thisProcess.getInstMemLoc()+address));	
				//ram.write(Integer.toHexString(registers[bReg]),(thisProcess.getInstMemLoc()+address));
				cache[address]=hexFormat(Integer.toHexString(registers[bReg]));		
			}		
			++pc;
			thisProcess.addIo();
		}		
		else if(getOpCode(s)==3) {
			if(DEBUG)			
				;//System.out.println("LW ["+dReg+"]="+Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+registers[bReg]),16));
			//registers[dReg]=Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+registers[bReg]),16);
			registers[dReg]=Integer.parseInt(cache[registers[bReg]],16);			
			//Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+registers[sReg2]),16);
			//System.out.println("LW ["+bReg+"]:"+registers[bReg]+"=["+dReg+"]:"+registers[dReg]+" address:"+address +" s="+condString(s));					
			//registers[bReg]=registers[dReg];//System.out.println("NO DREG*!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");	
			++pc;
			thisProcess.addIo();
		}		
		else if(getOpCode(s)==11) {
			if(address==0) {
				if(DEBUG)			
					;//System.out.println("MOVI setting ["+dReg+"]="+registers[dReg]+" to ["+bReg+"]="+registers[bReg]);
				registers[dReg]=registers[bReg];
			}
			else {	
				if(DEBUG)			
					;//System.out.println("MOVI setting ["+dReg+"]="+registers[dReg]+" to address:"+address);
				registers[dReg]=address;
			}
			++pc;		
		}
		else if(getOpCode(s)==12) {	
			if(DEBUG)			
				;//System.out.println("ADDI ["+dReg+"]:"+registers[dReg]+"=["+bReg+"]:"+"+"+address+" "+condString(s));
			registers[dReg]+=address;
			++pc;
		}
		else if(getOpCode(s)==13) {
			if(DEBUG)		
				;//System.out.println("MULI ["+dReg+"]:"+registers[dReg]+"=["+bReg+"]:"+"*"+address);
			registers[dReg]*=address;	
			++pc;		
		}
		else if(getOpCode(s)==14) {
			if(DEBUG)			
				;//System.out.println("DIVI ["+dReg+"]:"+registers[dReg]+"=["+bReg+"]:"+"/"+address);
			registers[dReg]/=address;	
			++pc;		
		}
		else if(getOpCode(s)==15) {
			if(DEBUG)			
				;//System.out.println("LDI ["+dReg+"]:"+registers[dReg]+"="+address);
			registers[dReg]=address;		
			++pc;		
		}				
		else if(getOpCode(s)==17) {
			if(DEBUG)			
				;//System.out.println("SLTI");
			registers[dReg]=registers[bReg]>address?0:1;		
			++pc;		
		}
		else if(getOpCode(s)==21) {	
			if(DEBUG)			
				;//System.out.println("BEQ");	
			if(registers[bReg]==registers[dReg])		
				setPc(thisProcess.getInstMemLoc()+address);
			else
				++pc;
		}
		else if(getOpCode(s)==22) {
			if(DEBUG)
				;//System.out.println("BNE bReg="+bReg+" dReg="+dReg+" = "+(registers[bReg]!=registers[dReg]?"Not Equal":"Equal")+" address:"+address);
			if(registers[bReg]!=registers[dReg])
				setPc(thisProcess.getInstMemLoc()+address);		
			else
				++pc;
		}
		else if(getOpCode(s)==23) {
			if(DEBUG)
				;//System.out.println("BEZ");
			if(registers[dReg]==0)
				setPc(thisProcess.getInstMemLoc()+address);
			else
				++pc;		
		}				
		else if(getOpCode(s)==24) {
			if(DEBUG)
				;//System.out.println("BNZ");
			if(registers[bReg]!=0)
				setPc(thisProcess.getInstMemLoc()+address);	
			else
				++pc;	
		}
		else if(getOpCode(s)==25) {
			if(DEBUG)
				;//System.out.println("BGZ");
			if(registers[bReg]>0)
				setPc(thisProcess.getInstMemLoc()+address);
			else
				++pc;
		}
		else if(getOpCode(s)==26) {
			if(DEBUG)
				;//System.out.println("BLZ");
			if(registers[bReg]<0)
				setPc(thisProcess.getInstMemLoc()+address);
			else
				++pc;
		}
		else
			;//System.out.println("Missed:"+arithString(s)+" opCode:"+getOpCode(s));	
					
	}
	
	/**
	* Handles unconditional instructions
	* @param s The binary instruction to execute
	*/
	public static void unconditional(String s) {
		int address = Integer.parseInt(getUncondAddress(s),2)/4;
		if(Driver.DEBUG)
			;//System.out.println("UNCONDITIONAL\n"+uncondString(s));//System.out.println("s="+s+" address:"+address);
		if(getOpCode(s)==18) {
			thisProcess.setStatus(Driver.Status.TERMINATED);
			if(Driver.MESSAGES)			
				;//System.out.println("Terminating Process:"+thisProcess.getPid());
		}	
		else if(getOpCode(s)==20) {
			if(DEBUG)			
				;//System.out.println("JMPING: address:"+address);//thisProcess.setPc();//System.out.println("JMP");
			thisProcess.setPc(address);		
		}
		else
			;//System.out.println("Missed:"+arithString(s)+" opCode:"+getOpCode(s));			
	}
	
	/**
	* Handles the input and output instructions
	* @param s The binary instruction to execute
	*/ 
	public static void inputAndOutput(String s) {
		
		//Split up the string		
		int sReg1=Integer.parseInt(getCondBReg(s),2),sReg2=Integer.parseInt(getCondDReg(s),2),address=Integer.parseInt(getCondAddress(s),2)/4;
		
		if(Driver.DEBUG)
			;//System.out.println("INPUT AND OUTPUT\n"+inAndOutString(s));//System.out.println("s="+s+" sReg1="+sReg1+" sReg2="+sReg2+" address:"+address/4);
		//RD Instruction
		thisProcess.addIo();
		if(getOpCode(s)==0) {	
			      
			if(sReg2!=0) {
				//read from sReg2
				if(DEBUG) {
					//System.out.println("RD ["+sReg1+"]="+Integer.parseInt(ram.read((thisProcess.getInstMemLoc()+registers[sReg2])),16));  
					//System.out.println("read="+Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+registers[sReg2]),16)+" thisProcess.getDataMemLoc():"+thisProcess.getDataMemLoc()+" registers[sReg2]:"+registers[sReg2]);
				}
				registers[sReg1]=Integer.parseInt(cache[registers[sReg2]],16);
				
				//registers[sReg1]=Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+registers[sReg2]),16);
			}
			else {
				//read from address	
				if(DEBUG) {				
					//System.out.println("RD ["+sReg1+"]="+Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+address),16));
					//System.out.println("read="+ram.read(thisProcess.getDataMemLoc())+" ram.read:"+Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+address),16));
				}				
//				registers[sReg1]=Integer.parseInt(ram.read(thisProcess.getInstMemLoc()+address),16);	
				registers[sReg1]=Integer.parseInt(cache[address],16);	
			}			
		}
		else if(getOpCode(s)==1) {			
			if(sReg2!=0) {
				//read from register
				if(DEBUG)
					;//System.out.println("WR [sReg1]:"+Integer.toHexString(registers[sReg1])+" getInstMemLoc()+registers[sReg2]:"+thisProcess.getInstMemLoc()+registers[sReg2]);
				cache[registers[sReg2]]=hexFormat(Integer.toHexString(registers[sReg1]));		
			}
			else {
				//read from address
				if(DEBUG)
					;//System.out.println("WR [sReg1]:"+Integer.toHexString(registers[sReg1])+" getInstMemLoc()+address:"+(thisProcess.getInstMemLoc()+address));
//				ram.write(Integer.toHexString(registers[sReg1]),(thisProcess.getInstMemLoc()+address));
				cache[address]=hexFormat(Integer.toHexString(registers[sReg1]));			
			}	
		}
		else
			System.out.println("Missed:"+arithString(s)+" opCode:"+getOpCode(s));					
	}	

	
	/** 
	* Execute a decoded instruction
	* @param s A decoded binary instruction string
	*/	
	public static void execute(String s) {
			if(getOpCode(s)==19)
				System.out.println("NOP found ***************************************************************");	
				
			switch(getType(s)) {
				case 0: 	arithmetic(s);											
							++pc;
							break;
				case 1:	conditional(s);
							break;
				case 2:	unconditional(s);												
							break;
				case 3:	inputAndOutput(s);							
							++pc;						
							break;
			}
			
			//Get Cache % info for stats			
			int cacheUsed=0;
			for(int i=0;i<cacheSize;++i) {
				if(cache[i].equals("00000000")) 
					;				
				else 
					++cacheUsed;		
				
			}
			if(DEBUG) {
				;//System.out.println("cacheUsed:"+cacheUsed);	
			}
			totalCacheUsed+=cacheUsed;
			
			
	}



}