package invader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Mainframe extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int ENEMYSIZE_X = 11;
	public static final int ENEMYSIZE_Y = 5;
	public static final int FRAMESIZE_X = 1000;
	public static final int FRAMESIZE_Y = 750;

	private Player player;
	private Enemy[][] enemy;
	private PlayerShot playershot;
	private ArrayList<EnemyShot> enemyshot;

	private JPanel graphic;
	private Map<String,Image> imagemap;


	private int moveway=1;
	private double movespeed=0.5;

	private boolean gameover = true;
	private boolean leftkeypressed = false;
	private boolean rightkeypressed = false;

	public Mainframe() {
		int i,j;

		loadImage();


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("InvederGame");
		getContentPane().setPreferredSize(new Dimension(FRAMESIZE_X,FRAMESIZE_Y));
		pack();
		setLocationRelativeTo(null);


		graphic = new JPanel();
		graphic.setSize(FRAMESIZE_X, FRAMESIZE_Y);
		add(graphic);


		player = new Player(50,FRAMESIZE_Y - 50,imagemap.get("player"),this);
		playershot = new PlayerShot(0,0,imagemap.get("playershot"),this);


		enemy = new Enemy[ENEMYSIZE_Y][ENEMYSIZE_X];
		int enemy_x = 30;
		int enemy_y = 30;
		for(i=0;i<ENEMYSIZE_Y;i++) {
			enemy_x = 30;
			for(j=0;j<ENEMYSIZE_X;j++) {
				enemy[i][j] = new Enemy(enemy_x,enemy_y,imagemap.get("enemy"),this);
				enemy_x += enemy[0][0].getObject_x() + 20;
			}
			enemy_y +=  enemy[0][0].getObject_y() + 20;
		}
		enemyshot = new ArrayList<EnemyShot>();


		addKeyListener((KeyListener) this);

		setVisible(true);

	}

	private void loadImage() {
		imagemap = new HashMap<String,Image>();
		try {
			imagemap.put("enemy", ImageIO.read(new File("./src/enemy.png")));
			imagemap.put("player", ImageIO.read(new File("./src/player.png")));
			imagemap.put("playershot", ImageIO.read(new File("./src/playershot.png")));
			imagemap.put("enemyshot", ImageIO.read(new File("./src/enemyshot.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_ENTER) {
			if(gameover) {
				gameover = false;
			}
		}
		if(!gameover) {
			if (key == KeyEvent.VK_RIGHT){
				rightkeypressed = true;
			}else if(key == KeyEvent.VK_LEFT) {
				leftkeypressed = true;
			}else if(key == KeyEvent.VK_UP) {
				if(!playershot.isExit()) {
					playershot.setX(player.getX()+player.getObject_x()/2-playershot.getObject_x()/2);
					playershot.setY(player.getY()-playershot.getObject_y());
					playershot.setExit(true);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_RIGHT){
			rightkeypressed = false;
		}else if(key == KeyEvent.VK_LEFT) {
			leftkeypressed = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void paint(Graphics g){
		int i,j;
		Point t;

		Graphics2D g2 = (Graphics2D)graphic.getGraphics();

		g2.setBackground(Color.black);
		g2.clearRect(0, 0, graphic.getWidth(), graphic.getHeight());


		t = player.getP();
		g2.drawImage(player.getImage(), t.x, t.y, this);


		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					t = enemy[i][j].getP();
					g2.drawImage(enemy[i][j].getImage(), t.x, t.y, this);
				}
			}
		}


		if (playershot.isExit()) {
			t = playershot.getP();
			g2.drawImage(playershot.getImage(), t.x, t.y, this);
		}


		for(i=0;i<enemyshot.size();i++) {
			EnemyShot temp = enemyshot.get(i);
			t = temp.getP();
			g2.drawImage(temp.getImage(), t.x, t.y, this);
		}
	}


/*	public void paint(Graphics g){
		int i,j;
		Point t;


		Graphics gv = offImage.getGraphics();
		Graphics2D g2 = (Graphics2D)gv;


		g2.setBackground(Color.black);
		g2.clearRect(0, 0, graphic.getWidth(), graphic.getHeight());


		t = player.getP();
		g2.drawImage(player.getImage(), t.x, t.y, this);


		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					t = enemy[i][j].getP();
					g2.drawImage(enemy[i][j].getImage(), t.x, t.y, this);
				}
			}
		}


		if (playershot.isExit()) {
			t = playershot.getP();
			g2.drawImage(playershot.getImage(), t.x, t.y, this);
		}


		for(i=0;i<enemyshot.size();i++) {
			EnemyShot temp = enemyshot.get(i);
			t = temp.getP();
			g2.drawImage(temp.getImage(), t.x, t.y, this);
		}


		g.drawImage(offImage, 0, 0, FRAMESIZE_X, FRAMESIZE_Y, this);
	}*/


	public void update() {
		int i,j;
		int edge;
		int lives = 0;


		if(leftkeypressed) {
			player.move(-2.0);
		}
		if(rightkeypressed) {
			player.move(2.0);
		}


		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					lives++;
				}
			}
		}


		if(moveway == 1) {

			edge = 0;
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					if(enemy[i][j].isExit() && edge<=enemy[i][j].getX()) {
						edge = enemy[i][j].getX();
					}
				}
			}
		} else {

			edge = FRAMESIZE_X;
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					if(enemy[i][j].isExit() && edge>=enemy[i][j].getX()) {
						edge = enemy[i][j].getX();
					}
				}
			}
		}

		if(edge <= 30 && moveway != 1 || edge >= FRAMESIZE_X - (enemy[0][0].getObject_x() + 30) && moveway == 1) {

			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					enemy[i][j].setY(enemy[i][j].getY()+enemy[0][0].getObject_y() + 20);
				}
			}
			moveway *= -1;
		} else {

			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					enemy[i][j].move(movespeed*moveway);

					double t = enemy[i][j].shoot(lives);
					if(t != 0) {
						enemyshot.add(new EnemyShot(enemy[i][j].getX()+enemy[0][0].getObject_x()/2,enemy[i][j].getY()+enemy[0][0].getObject_y(),t,imagemap.get("enemyshot"),this));
					}
				}
			}
		}


		if(playershot.isExit()) {
			playershot.move(5.8);
		}


		for(i=0;i<enemyshot.size();i++) {
			EnemyShot t = enemyshot.get(i);
			t.move();
			if(t.getY() >= FRAMESIZE_Y) {
				enemyshot.remove(i);
			}
		}



		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					if(!(playershot.getX()+playershot.getObject_x() < enemy[i][j].getX() || enemy[i][j].getX()+enemy[i][j].getObject_x() < playershot.getX())) {
						if(!(playershot.getY()+playershot.getObject_y() < enemy[i][j].getY() || enemy[i][j].getY()+enemy[i][j].getObject_y() < playershot.getY())) {
							playershot.hit();
							enemy[i][j].hit();
						}
					}
				}
			}
		}


		for(i=0;i<enemyshot.size();i++) {
			EnemyShot t = enemyshot.get(i);
			if(!(t.getX()+t.getObject_x() < player.getX() || player.getX()+player.getObject_x() < t.getX())) {
				if(!(t.getY()+t.getObject_y() < player.getY() || player.getY()+player.getObject_y() < t.getY())) {
					player.hit();
					enemyshot.remove(i);
					gameover = true;
				}
			}
		}

		repaint();
	}

	public boolean isGameover() {
		return gameover;
	}

	public void reset() {
		int i,j;


		player.reset(50,FRAMESIZE_Y-50);
		playershot.reset(0,0);


		int enemy_x = 30;
		int enemy_y = 30;
		for(i=0;i<ENEMYSIZE_Y;i++) {
			enemy_x = 30;
			for(j=0;j<ENEMYSIZE_X;j++) {
				enemy[i][j].reset(enemy_x,enemy_y);
				enemy_x += enemy[0][0].getObject_x() + 20;
			}
			enemy_y +=  enemy[0][0].getObject_y() + 20;
		}
		enemyshot.clear();
		gameover = true;
	}
}
