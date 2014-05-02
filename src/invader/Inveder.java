package invader;

import javax.swing.SwingUtilities;

public class Inveder {

	public static void main(String[] args) {

		long error = 0;  
		int fps = 60;  
		long idealSleep = (1000 << 16) / fps;  
		long oldTime;  
		long newTime = System.currentTimeMillis() << 16; 

		Mainframe frame = new Mainframe();

		while(true) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			while(!frame.isGameover()) {
				oldTime = newTime; 

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						frame.update();
					}
				});

				newTime = System.currentTimeMillis() << 16; 
				long sleepTime = idealSleep - (newTime - oldTime) - error; // 休止できる時間

				if (sleepTime < 0x20000) {
					sleepTime = 0x20000; // 最低でも2msは休止  
				}
				oldTime = newTime; 

				try {
					Thread.sleep(sleepTime >> 16); // 休止  
					newTime = System.currentTimeMillis() << 16;  
					error = newTime - oldTime - sleepTime; // 休止時間の誤差
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
