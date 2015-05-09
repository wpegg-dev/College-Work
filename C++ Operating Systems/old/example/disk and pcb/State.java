package process_control;

import log_files.ErrorLog;


/**
 * The State class records the state of a process on 
 * interupt.  This class contains many values needed to
 * continue running.
 * 
 * @author brandonmckune
 *
 */
public class State 
{
	private int pc;
	private int regCurrent;
	private int regNext;
	private int inputBuff, outputBuff;
	private int cache;
	private int activeBlock;

	public State()
	{
		pc = -1;
		regCurrent = -1;
		regNext = -1;
		inputBuff = -1;
		outputBuff = -1;
		cache = -1;
		activeBlock = -1;
	}

	public int getPc() 
	{
		return pc;
	}

	public int getRegCurrent() 
	{
		return regCurrent;
	}

	public int getRegNext() 
	{
		return regNext;
	}

	public int getInputBuff() 
	{
		return inputBuff;
	}

	public int getOutputBuff() 
	{
		return outputBuff;
	}

	public int getCache() 
	{
		return cache;
	}

	public int getActiveBlock() 
	{
		return activeBlock;
	}

	public void setRegCurrent(int regCurrent) 
	{
		if(regCurrent >= 0)
		{
			this.regCurrent = regCurrent;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setPc || >> Invalid Parameter.  x < 0.");
			throw new IllegalArgumentException();
		}
	}

	public void setPc(int pc) 
	{
		if(pc >= 0)
		{
			this.pc = pc;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setPc || >> Invalid Parameter.  x < 0.");
			throw new IllegalArgumentException();
		}
	}
	
	public void setRegNext(int regNext) 
	{
		if(regNext >= 0)
		{
			this.regNext = regNext;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setRegNext || >> Invalid Parameter. x < 0.");
			throw new IllegalArgumentException();
		}
	}

	public void setInputBuff(int inputBuff) 
	{
		if(inputBuff >= 0)
		{
			this.inputBuff = inputBuff;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setInputBuff || >> Invalid Parameter. x < 0.");
			throw new IllegalArgumentException();
		}
	}

	public void setOutputBuff(int outputBuff) 
	{	
		if(outputBuff >= 0)
		{
			this.outputBuff = outputBuff;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setOutputBuff || >> Invalid Parameter. x < 0.");
			throw new IllegalArgumentException();
		}
	}

	public void setCache(int cache) 
	{
		if(cache >= 0)
		{
			this.cache = cache;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setCache || >> Invalid Parameter. x < 0.");
			throw new IllegalArgumentException();
		}
	}

	public void setActiveBlock(int activeBlock) 
	{
		if(activeBlock >= 0)
		{
			this.activeBlock = activeBlock;
		}
		else
		{
			ErrorLog.getInstance().writeError("State::setActiveBlock || >> Invalid Parameter. x < 0.");
			throw new IllegalArgumentException();
		}
	}
}
