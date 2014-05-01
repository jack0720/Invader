package invader;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;

public abstract class AbstractObject {

	public static final int TYPE_PLAYER = 0;
	public static final int TYPE_ENEMY = 1;
	public static final int TYPE_PLAYER_SHOT = 2;
	public static final int TYPE_ENEMY_SHOT = 3;
	public static final int TYPE_BLOCK = 4;

	protected Point p;
	protected int objecttype;
	protected Image image;
	protected double buffer = 0;
	protected int object_x;
	protected int object_y;

	public abstract void move(double movevalue);
	public abstract void hit();

	public AbstractObject(int x,int y,Image image,ImageObserver io) {
		p = new Point(x,y);
		this.image = image;
		object_x = image.getWidth(io);
		object_y = image.getHeight(io);
	}
	
	public void reset(int x,int y) {
		this.p.x = x;
		this.p.y = y;
	}
	
	public int getX() {
		return p.x;
	}

	public void setX(int x) {
		p.x = x;
	}

	public int getY() {
		return p.y;
	}

	public void setY(int y) {
		p.y = y;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public Point getP() {
		return p;
	}
	
	public int getObject_x() {
		return object_x;
	}
	
	public int getObject_y() {
		return object_y;
	}
}


