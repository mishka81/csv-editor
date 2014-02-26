package system.io;

import java.io.Serializable;

public class XY implements Serializable {
	
	private static final long serialVersionUID = -2706134661499955343L;

	private int x;
	
	private int y;
	
	public XY(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}
	
	private XY() {
		super();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(X=" + x + ", Y=" + y + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		XY other = (XY) obj;
		return other.x == x && other.y == y;
	}
}
