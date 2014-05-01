package invader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Mainframe extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int ENEMYSIZE_X = 11;//行あたりの敵数
	public static final int ENEMYSIZE_Y = 5;//列あたりの敵数
	public static final int FRAMESIZE_X = 1000;//フレームの縦幅
	public static final int FRAMESIZE_Y = 750;//フレームの横幅

	private Player player;//プレイヤー
	private Enemy[][] enemy;//敵
	private PlayerShot playershot;//プレイヤーの弾
	private ArrayList<EnemyShot> enemyshot;//敵の弾

	private JPanel graphic;//描画用パネル
	private Map<String,Image> imagemap;//画像管理用マップ
	//private Image offImage;//ダブルバッファ用

	private int moveway=1;//敵移動方向
	private double movespeed=0.5;//敵移動スピード

	private boolean gameover = true;
	private boolean leftkeypressed = false;
	private boolean rightkeypressed = false;

	public Mainframe() {
		int i,j;
		//イメージ読み込み
		loadImage();

		//メインフレームの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("InvederGame");
		setBounds(300,100,FRAMESIZE_X,FRAMESIZE_Y);

		//graphics表示用パネルの設定
		graphic = new JPanel();
		graphic.setSize(FRAMESIZE_X, FRAMESIZE_Y);
		add(graphic);

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

		setVisible(true);
		//offImage = createImage(FRAMESIZE_X, FRAMESIZE_Y);
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

	//キー入力
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

		//ダブルバッファ
		Graphics2D g2 = (Graphics2D)g;

		//クリア
		g2.setBackground(Color.black);
		g2.clearRect(0, 0, graphic.getWidth(), graphic.getHeight());

		//プレイヤー描写
		t = player.getP();
		g2.drawImage(player.getImage(), t.x, t.y, this);

		//敵描写
		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					t = enemy[i][j].getP();
					g2.drawImage(enemy[i][j].getImage(), t.x, t.y, this);
				}
			}
		}

		//味方の弾描写
		if (playershot.isExit()) {
			t = playershot.getP();
			g2.drawImage(playershot.getImage(), t.x, t.y, this);
		}

		//敵の弾描写
		for(i=0;i<enemyshot.size();i++) {
			EnemyShot temp = enemyshot.get(i);
			t = temp.getP();
			g2.drawImage(temp.getImage(), t.x, t.y, this);
		}
	}
	
	
/*	public void paint(Graphics g){
		int i,j;
		Point t;

		//ダブルバッファ
		Graphics gv = offImage.getGraphics();
		Graphics2D g2 = (Graphics2D)gv;

		//クリア
		g2.setBackground(Color.black);
		g2.clearRect(0, 0, graphic.getWidth(), graphic.getHeight());

		//プレイヤー描写
		t = player.getP();
		g2.drawImage(player.getImage(), t.x, t.y, this);

		//敵描写
		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					t = enemy[i][j].getP();
					g2.drawImage(enemy[i][j].getImage(), t.x, t.y, this);
				}
			}
		}

		//味方の弾描写
		if (playershot.isExit()) {
			t = playershot.getP();
			g2.drawImage(playershot.getImage(), t.x, t.y, this);
		}

		//敵の弾描写
		for(i=0;i<enemyshot.size();i++) {
			EnemyShot temp = enemyshot.get(i);
			t = temp.getP();
			g2.drawImage(temp.getImage(), t.x, t.y, this);
		}

		//バッファ反映
		g.drawImage(offImage, 0, 0, FRAMESIZE_X, FRAMESIZE_Y, this);
	}*/

	//更新処理
	public void update() {
		int i,j;
		int edge;//一番は端のインベーダーのｘ座標
		int lives = 0;

		//自機移動
		if(leftkeypressed) {
			player.move(-2.0);
		}
		if(rightkeypressed) {
			player.move(2.0);
		}
		
		//敵残機カウント
		for(i=0;i<ENEMYSIZE_Y;i++) {
			for(j=0;j<ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExit()) {
					lives++;
				}
			}
		}

		//敵の移動
		if(moveway == 1) {
			//右方向時
			edge = 0;
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					if(enemy[i][j].isExit() && edge<=enemy[i][j].getX()) {
						edge = enemy[i][j].getX();
					}
				}
			}
		} else {
			//左方向時
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
			//上下移動
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					enemy[i][j].setY(enemy[i][j].getY()+enemy[0][0].getObject_y() + 20);
				}
			}
			moveway *= -1;
		} else {
			//通常移動
			for(i=0;i<ENEMYSIZE_Y;i++) {
				for(j=0;j<ENEMYSIZE_X;j++) {
					enemy[i][j].move(movespeed*moveway);
					//敵弾を撃つか
					double t = enemy[i][j].shoot(lives);
					if(t != 0) {
						enemyshot.add(new EnemyShot(enemy[i][j].getX()+enemy[0][0].getObject_x()/2,enemy[i][j].getY()+enemy[0][0].getObject_y(),t,imagemap.get("enemyshot"),this));
					}
				}
			}
		}

		//味方の弾の移動
		if(playershot.isExit()) {
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
		//プレイヤーショット→敵
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

		//エネミーショット→自機
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
/*		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				repaint();
			}
		});*/
	}

	public boolean isGameover() {
		return gameover;
	}

	public void reset() {
		int i,j;

		//プレイヤー設定
		player.reset(50,FRAMESIZE_Y-50);
		playershot.reset(0,0);

		//敵設定
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
