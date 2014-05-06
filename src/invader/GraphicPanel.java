package invader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;

public class GraphicPanel extends JPanel{

	private Player player;
	private Enemy[][] enemy;
	private PlayerShot playershot;
	private List<EnemyShot> enemyshot;

	public GraphicPanel() {

	}

	public void setObject(Player player, Enemy[][] enemy, PlayerShot playershot, List<EnemyShot> enemyshot) {
		this.player = player;
		this.enemy = enemy;
		this.playershot = playershot;
		this.enemyshot = enemyshot;
	}

	public void paint(Graphics g){
		int i,j;
		Point t;

		Graphics2D g2 = (Graphics2D)g;

		g2.setBackground(Color.black);
		g2.clearRect(0, 0, getWidth(), getHeight());


		t = player.getP();
		g2.drawImage(player.getImage(), t.x, t.y, this);


		for(i=0;i<Mainframe.ENEMYSIZE_Y;i++) {
			for(j=0;j<Mainframe.ENEMYSIZE_X;j++) {
				if(enemy[i][j].isExist()) {
					t = enemy[i][j].getP();
					g2.drawImage(enemy[i][j].getImage(), t.x, t.y, this);
				}
			}
		}


		if (playershot.isExist()) {
			t = playershot.getP();
			g2.drawImage(playershot.getImage(), t.x, t.y, this);
		}


		for(i=0;i<enemyshot.size();i++) {
			EnemyShot temp = enemyshot.get(i);
			t = temp.getP();
			g2.drawImage(temp.getImage(), t.x, t.y, this);
		}

	}
}
