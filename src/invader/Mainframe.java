package invader;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Mainframe extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	//敵の数　フレームサイズ
	public static final int ENEMYSIZE_X = 11;
	public static final int ENEMYSIZE_Y = 5;
	public static final int FRAMESIZE_X = 1000;
	public static final int FRAMESIZE_Y = 750;

	//ゲームを構成するオブジェクト
	private Player player;
	private Enemy[][] enemy;
	private PlayerShot playershot;
	private List<EnemyShot> enemyshot;

	private GraphicPanel graphic;//描画用パネル
	private Map<String,Image> imagemap;//画像管理マップ

	private int moveway=1;//敵の移動方向
	private double movespeed=0.5;//敵の移動速度

	//ゲーム管理用
	private boolean gameover = true;
	private boolean leftkeypressed = false;
	private boolean rightkeypressed = false;

	public Mainframe() {
		int i,j;

		loadImage();//画像をマップに登録

		//フレーム設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("InvederGame");
		getContentPane().setPreferredSize(new Dimension(FRAMESIZE_X,FRAMESIZE_Y));
		pack();
		setLocationRelativeTo(null);

		//プレイヤー設定
		player = new Player(50,FRAMESIZE_Y - 50,imagemap.get("player"),this);
		playershot = new PlayerShot(0,0,imagemap.get("playershot"),this);

		//敵設定
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

		//リスナー登録
		addKeyListener((KeyListener) this);
		
		//描画用パネル設定
		graphic = new GraphicPanel();
		graphic.setSize(FRAMESIZE_X, FRAMESIZE_Y);
		graphic.setObject(player, enemy, playershot, enemyshot);
		add(graphic);

		setVisible(true);
	}

	private void loadImage() {
		//画像をマップに登録
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
				if(!playershot.isExist()) {
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

	public void update() {
		int i,j;
		int edge;
		int lives = 0;

		//プレイヤーの移動
		if(leftkeypressed) {
			player.move(-5.0);
		}
		if(rightkeypressed) {
			player.move(5.0);
		}

		//敵の生存数カウント
		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExist()) {
					lives++;
				}
			}
		}

		//敵の端の位置を確認
		if(moveway == 1) {
			//右方向移動中の場合
			edge = 0;
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					if(enemy[i][j].isExist() && edge<=enemy[i][j].getX()) {
						edge = enemy[i][j].getX();
					}
				}
			}
		} else {
			//左方向移動中の場合
			edge = FRAMESIZE_X;
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					if(enemy[i][j].isExist() && edge>=enemy[i][j].getX()) {
						edge = enemy[i][j].getX();
					}
				}
			}
		}

		//横移動か縦移動
		if(edge <= 30 && moveway != 1 || edge >= FRAMESIZE_X - (enemy[0][0].getObject_x() + 30) && moveway == 1) {
			//縦移動
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					enemy[i][j].setY(enemy[i][j].getY()+enemy[0][0].getObject_y() + 20);
				}
			}
			moveway *= -1;
		} else {
			//横移動
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					enemy[i][j].move(movespeed*moveway);
					//敵が弾を撃つかどうか
					double t = enemy[i][j].shoot(lives);
					if(t != 0) {
						enemyshot.add(new EnemyShot(enemy[i][j].getX()+enemy[0][0].getObject_x()/2,enemy[i][j].getY()+enemy[0][0].getObject_y(),t,imagemap.get("enemyshot"),this));
					}
				}
			}
		}

		//プレイヤーの弾の移動
		if(playershot.isExist()) {
			playershot.move(5.8);
		}

		//敵の弾の移動
		for(i=0;i<enemyshot.size();i++) {
			EnemyShot t = enemyshot.get(i);
			t.move();
			if(t.getY() >= FRAMESIZE_Y) {
				enemyshot.remove(i);
			}
		}

		//当たり判定
		//プレイヤーの弾が敵にあたった場合
		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExist()) {
					if(!(playershot.getX()+playershot.getObject_x() < enemy[i][j].getX() || enemy[i][j].getX()+enemy[i][j].getObject_x() < playershot.getX())) {
						if(!(playershot.getY()+playershot.getObject_y() < enemy[i][j].getY() || enemy[i][j].getY()+enemy[i][j].getObject_y() < playershot.getY())) {
							playershot.hit();
							enemy[i][j].hit();
						}
					}
				}
			}
		}

		//敵の弾がプレイヤーにあたった場合
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
		
		graphic.repaint();
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
