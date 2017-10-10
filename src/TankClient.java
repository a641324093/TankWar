
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

//用容器装坦克
//增加难度选择

public class TankClient extends Frame {
	
	public static final int SCREENWIDE=800 ,SCREENHIGH=600;
	Tank mytank = new Tank(50,50,true,Direction.STOP,100,this);
	List <Bullet> bullets = new LinkedList<Bullet>();
	Image offScreenImage = null;
	List <Explode> explodes = new LinkedList<Explode>();
	List <Tank> tanks = new LinkedList<Tank>();
	Wall w = new Wall(100,100,30,300,this);
	HealBag healbag = new HealBag(100,300,25,25,this);
	private static Image backscreen [] = null;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	static
	{ 		backscreen = new Image[]
			{
			tk.getImage(TankClient.class.getClassLoader().getResource("Image/背景.jpg"))
			};
	}
	public void update(Graphics g) 
	{
		if(offScreenImage == null) {offScreenImage = this.createImage(SCREENWIDE,SCREENHIGH);}
		Graphics goffscreen = offScreenImage.getGraphics();
		goffscreen.setColor(Color.WHITE);
		goffscreen.fillRect(0,0,SCREENWIDE,SCREENHIGH);
		paint(goffscreen);
		g.drawImage(offScreenImage,0,0,null);
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawString("bullets has "+bullets.size(),10,50);
		g.drawString("explodes has "+explodes.size(),10,70);
		g.drawString("hostile tanks has "+tanks.size(),10,90);
		g.drawImage(backscreen[0], 0, 0, null);
		mytank.draw(g);	
		mytank.collisionWithTanks(tanks);
		healbag.draw(g);
		healbag.reMake();
		if(healbag.collisionWithsTank(mytank)) healbag.healBagDisappear(g);
		w.draw(g);
		for(int i=0;i<tanks.size();i++)
		{
			Tank t;
			t=tanks.get(i);
			if(t.getIslive()) 
			{
				t.draw(g);
				t.collisionWithWall(w);
				t.collisionWithTank(mytank);
				t.collisionWithTanks(tanks);
			}
			else tanks.remove(t);
		}
		for(int i=0;i<bullets.size();i++)
		{
			Bullet b;
			b=bullets.get(i);
			if(b.isIslive()) 
			{
				b.draw(g);
				b.hitTanks(tanks);
				b.hitTank(mytank);
				b.hitWall(w);

			}
			else bullets.remove(b);
		}
		for(int i=0;i<explodes.size();i++)
		{
			Explode e = explodes.get(i);
			e.draw(g);
		}
	}

	public void launchFrame()
	{
		for(int i=0;i<5;i++)
		{
			Tank t=new Tank(600,150+(i+1)*50, false, Direction.L,this);
			tanks.add(t);
		}
		this.setLocation(10,10);
		this.setSize(SCREENWIDE,SCREENHIGH);
		this.setTitle("Tank War");
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		this.setBackground(Color.green);
		setVisible(true);
		this.setResizable(false);
		new Thread(new PaintThread()).start();
	}
	
	private class PaintThread implements Runnable
	{
	public void run() 
			{
				while(true)
				{
					repaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	}

	private class KeyMonitor extends KeyAdapter
	{
		public void keyReleased(KeyEvent e) {
			mytank.keyReleased(e);
		}

		public void keyPressed (KeyEvent e)
		{
			System.out.println("ok!");
			mytank.keyPressed(e);
		}
	}
	
	public static void main (String[] args) 
	{ 
		TankClient t= new TankClient();
		t.launchFrame();
	}


}
