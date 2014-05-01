package invader;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Enemy extends AbstractObject {
	
	private boolean shooted = false;
	private boolean exist = false;
	
	public Enemy (int x,int y,Image image,ImageObserver io) {
		super(x,y,image,io);
		objecttype = TYPE_ENEMY;
		exist = true;
	}

	@Override
	public void hit() {
		exist = false;
	}

	@Override
	public void move(double movevalue) {
		buffer += movevalue;
		if(Math.abs(buffer)>=15) {
			p.x += (int)buffer;
			buffer=0;
			shooted = false;
		}
	}

	public double shoot(int lives) {
		Random rdm = new Random();
		int t = rdm.nextInt(400+lives*10);
		
		if(t >= 30 || shooted || !exist) {
			shooted = true;
			return 0;
		}else if(t >= 23) {
			shooted = true;
			return 2.5;
		}else if(t >= 7) {
			shooted = true;
			return 3.8;
		}else{
			shooted = true;
			return 6.5;
		}

	}
	
	public void reset(int x,int y) {
		super.reset(x, y);
		exist = true;
	}

	public boolean isExit() {
		return exist;
	}

}
