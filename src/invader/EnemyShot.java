package invader;

import java.awt.Image;
import java.awt.image.ImageObserver;

public class EnemyShot extends AbstractObject {

	private double movespeed = 0.0;
	
	public EnemyShot(int x,int y,double movespeed,Image image,ImageObserver io) {
		super(x,y,image,io);
		objecttype = TYPE_ENEMY_SHOT;
		this.movespeed = movespeed;
	}
	
	public void move() {
		p.y += movespeed;
	}
	
	@Override
	public void move(double movevalue) {
		p.y += movevalue;
	}

	@Override
	public void hit() {

	}

}
