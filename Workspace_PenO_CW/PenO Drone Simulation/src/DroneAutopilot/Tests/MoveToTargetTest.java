package DroneAutopilot.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import DroneAutopilot.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MoveToTargetTest {

	@Before
	public void setUp() {
		this.setmTT(new MoveToTarget(null));
	}
	
	@Test
    public void cOGTest() {
    }
	
	/**
	 * @return the mTT
	 */
	public MoveToTarget getmTT() {
		return mTT;
	}

	/**
	 * @param mTT the mTT to set
	 */
	public void setmTT(MoveToTarget mTT) {
		this.mTT = mTT;
	}

	private MoveToTarget mTT;
	
}
