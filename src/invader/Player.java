package invader;

import java.awt.Image;
import java.awt.image.ImageObserver;

public class Player extends AbstractObject {

	public Player(int x,int y,Image image,ImageObserver io) {
		super(x,y,image,io);
		objecttype = TYPE_PLAYER;
	}

	@Override
	public void move(double movevalue) {
		p.x += movevalue;
		if(p.x <= 0) {
			p.x = 0;
		}else if(p.x >= 1000 - object_x) {
			p.x = 1000 - object_x;
		}
	}

	@Override
	public void hit() {

	}

}
