package invader;

import java.awt.Image;
import java.awt.image.ImageObserver;

public class PlayerShot extends AbstractObject {

	private boolean exist = false;
	
	public PlayerShot(int x,int y,Image image,ImageObserver io) {
		super(x,y,image,io);
		objecttype = TYPE_PLAYER_SHOT;
	}
	
	@Override
	public void move(double movevalue) {
		p.y -= movevalue;
		if(p.y < 0) {
			exist = false;
		}
	}

	@Override
	public void hit() {
		exist = false;
		p.x = 0;
		p.y = 0;
	}

	public boolean isExist() {
		return exist;
	}

	public void setExit(boolean exit) {
		this.exist = exit;
	}

}
