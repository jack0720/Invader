package invader;

import java.awt.Image;
import java.awt.image.ImageObserver;

public class PlayerShot extends AbstractObject {

	private boolean exit = false;
	
	public PlayerShot(int x,int y,Image image,ImageObserver io) {
		super(x,y,image,io);
		objecttype = TYPE_PLAYER_SHOT;
	}
	
	@Override
	public void move(double movevalue) {
		p.y -= movevalue;
		if(p.y < 0) {
			exit = false;
		}
	}

	@Override
	public void hit() {
		exit = false;
		p.x = 0;
		p.y = 0;
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

}
