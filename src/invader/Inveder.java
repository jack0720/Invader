package invader;

import javax.swing.SwingUtilities;

public class Inveder {

	public static void main(String[] args) {

		Mainframe frame = new Mainframe();

		while(true) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			while(!frame.isGameover()) {
				long oldTime = System.currentTimeMillis();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						frame.update();
					}
				});

				long newTime = System.currentTimeMillis();
				long sleepTime = 16 - (newTime - oldTime);
				if(sleepTime < 2) {
					sleepTime = 2;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


			frame.reset();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.repaint();
				}
			});
		}

	}
}
