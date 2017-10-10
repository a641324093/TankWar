
import java.awt.*;
import java.awt.event.*;
import java.util.*;

//自我改进：gun，Bullet应该是Tank的内部类；
//建立能够创造敌我机器人坦克

public class Tank {

	public static final int XSPEED=7,YSPEED=7,TANKSIZE_W=50,TANKSIZE_H=50;
	public int x,y,oldx,oldy;
	private int bx,by;
	private int life = 10;
	boolean bl=false,bu=false,br=false,bd=false;
	private Direction dir = Direction.STOP;
	private Direction gun_dir=Direction.D;
	TankClient tc = null;
	private boolean isjust,islive=true;
	private static Random rn = new Random();
	private static Random rs = new Random();
	private int step = rs.nextInt(10)+3;
	private static Random rf = new Random();
	private int firetimes = rf.nextInt(100);
	private static Image [] imgs=null;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Map<String,Image> tank_images = new HashMap<String,Image>();
	static
	{
		imgs = new Image[]
		{
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1左.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1左上.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1上.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1右上.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1右.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1右下.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1下.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/坦克1左下.jpg"))
		};
		tank_images.put("L", imgs[0]);
		tank_images.put("LU", imgs[1]);
		tank_images.put("U", imgs[2]);
		tank_images.put("RU", imgs[3]);
		tank_images.put("R", imgs[4]);
		tank_images.put("RD", imgs[5]);
		tank_images.put("D", imgs[6]);
		tank_images.put("LD", imgs[7]);
		
	}
	
	Tank()
	{
		x=50;
		y=50;
	}
	Tank(int x, int y,boolean isjust) 
	{
		this.x = x;
		this.y = y;
		this.isjust=isjust;
	}
	Tank(int x,int y,boolean isjust,Direction dir,TankClient tc)
	{
		this.x=x;
		this.y=y;
		this.isjust=isjust;
		this.dir=dir;
		this.tc=tc;
	}
	Tank(int x,int y,boolean isjust,Direction dir,int life,TankClient tc)
	{
		this(x,y,isjust,dir,tc);
		this.life=life;
	}
	public boolean getIsjust() {
		return isjust;
	}
	public boolean getIslive() {
		return islive;
	}
	public void setIslive(boolean islive) {
		this.islive = islive;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean collisionWithWall(Wall w)
	{
		if(getRectangle().intersects(w.getRectangle())==true)
		{
			stay();
			return true;
		}
		return false;
	}
	public boolean collisionWithTank(Tank t)
	{
		if(getRectangle().intersects(t.getRectangle())==true&&t!=this&&t.islive&&this.islive)
		{
			stay();
			t.stay();
			return true;
		}
		return false;
	}
	public int  collisionWithTanks(java.util.List<Tank> tanks)
	{
		int num=0;
		for(int i=0;i<tanks.size();i++)
		{
			Tank t;
			t = tanks.get(i);
			if(collisionWithTank(t)==true) num++;
		}
		return num;
	}
	private void stay()
	{
		x=oldx;
		y=oldy;
	}
	public void draw (Graphics g)
	{ 
		if(!islive) 
			{
				return;
			}
		switch (gun_dir)
		{
		case L:g.drawImage(tank_images.get("L"), x, y, null);break;
		case LU:g.drawImage(tank_images.get("LU"), x, y, null);break;
		case U:g.drawImage(tank_images.get("U"), x, y, null);break;
		case RU:g.drawImage(tank_images.get("RU"), x, y, null);break;
		case R:g.drawImage(tank_images.get("R"), x, y, null);break;
		case RD:g.drawImage(tank_images.get("RD"), x, y, null);break;
		case D:g.drawImage(tank_images.get("D"), x, y, null);break;
		case LD:g.drawImage(tank_images.get("LD"), x, y, null);break;
		}
		move();
		if(isjust)
		drawHeath(g);
	}
	public Bullet fire(Direction gun_dir)
	{
		if(!islive) return null ;
		switch (gun_dir)
		{
		case L:bx=x-10;by=y+Tank.TANKSIZE_H/2+5;break;
		case LU:bx=x;by=y+10;break;
		case U:bx=x+Tank.TANKSIZE_W/2-5;by=y;break;
		case RU:bx=x+Tank.TANKSIZE_W;by=y;break;
		case R:bx=x+Tank.TANKSIZE_W;by=y+Tank.TANKSIZE_H/2+5;break;
		case RD:bx=x+Tank.TANKSIZE_W;by=y+Tank.TANKSIZE_H+10;break;
		case D:bx=x+Tank.TANKSIZE_W/2-5;by=y+Tank.TANKSIZE_H+10;break;
		case LD:bx=x-10;by=y+Tank.TANKSIZE_H;break;
		}
		Bullet bullet = new Bullet(bx,by,isjust,gun_dir,tc);
		tc.bullets.add(bullet);
		return bullet;
	}
	public void superFire()
	{
		Direction [] dir = Direction.values();
		for(int i=0;i<8;i++)
		{
			fire(dir[i]);
		}
	}
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_RIGHT) br=true;
		else if(key==KeyEvent.VK_LEFT) bl=true;
		else if(key==KeyEvent.VK_UP) bu=true;
		else if(key==KeyEvent.VK_DOWN) bd=true;
		locatDirection();
	}
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key==KeyEvent.VK_RIGHT) br=false;
		else if(key==KeyEvent.VK_LEFT) bl=false;
		else if(key==KeyEvent.VK_UP) bu=false;
		else if(key==KeyEvent.VK_DOWN) bd=false;
		else if(key==KeyEvent.VK_A) fire(gun_dir);
		else if(key==KeyEvent.VK_S) superFire();
		else if(key==KeyEvent.VK_2) if(islive==false) {islive=true; life=100;};
		if(key==KeyEvent.VK_3) producRobots(600, 50, false, Direction.L, 5, tc);
		locatDirection();
	}
	public void move()
	{
		if(!isjust) 
		{
			Direction [] dirs = dir.values();
			if(step==0)
			{
				dir=dirs[rn.nextInt(dirs.length)];
				step=rs.nextInt(10)+3;
			}
			step--;
			firetimes=rf.nextInt(100);
			if(firetimes>90)
			{
				this.fire(gun_dir);
			}
		}
		oldx=x;
		oldy=y;
		switch (dir)
		{
		case L: x-=XSPEED;break;
		case LU:x-=XSPEED;y-=YSPEED;break;
		case U:y-=YSPEED;break;
		case RU:x+=XSPEED;y-=YSPEED;break;
		case R:x+=XSPEED;break;
		case RD:x+=XSPEED;y+=YSPEED;break;
		case D:y+=YSPEED;break;
		case LD:x-=XSPEED;y+=YSPEED;break;
		case STOP:;break;
		}
		if(x<0) x=0;
		if(y<30) y=30;
		if(x+Tank.TANKSIZE_W>TankClient.SCREENWIDE) x=TankClient.SCREENWIDE-Tank.TANKSIZE_W;
		if(y+Tank.TANKSIZE_H>TankClient.SCREENHIGH) y=TankClient.SCREENHIGH-Tank.TANKSIZE_H;
		if(dir!=Direction.STOP) gun_dir=dir;
	}
	
	public void locatDirection()
	{
		if(bl==true && bu==false && br==false && bd==false) dir=Direction.L;
		else if(bl==true && bu==true && br==false && bd==false) dir=Direction.LU;
		else if(bl==false && bu==true && br==false && bd==false) dir=Direction.U;
		else if(bl==false && bu==true && br==true && bd==false) dir=Direction.RU;
		else if(bl==false && bu==false && br==true && bd==false) dir=Direction.R;
		else if(bl==false && bu==false && br==true && bd==true) dir=Direction.RD;
		else if(bl==false && bu==false && br==false && bd==true) dir=Direction.D;
		else if(bl==true && bu==false && br==false && bd==true) dir=Direction.LD;
		else if(bl==false && bu==false && br==false && bd==false) dir=Direction.STOP;
	}
	public Rectangle getRectangle()
	{
		System.out.println(imgs[0].getWidth(null));
		return new Rectangle(x,y,imgs[0].getWidth(null),imgs[0].getHeight(null));
	}
	private void drawHeath(Graphics g)
	{
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.drawRect(x-10,y-20,50,10);
		if(life>=50)g.setColor(Color.yellow);
		else if(life>10&&life<50) g.setColor(Color.orange);
		else if(life<=10) g.setColor(Color.red);
		g.fillRect(x+1-10,y-20,life/2*1,10);
		g.setColor(c);
	}
	public void producRobots(int x,int y,boolean isjust,Direction dir,int num,TankClient tc)
	{
		//if(tc.tanks.size()>0) return;
		int i;
		for(i=0;i<num;i++)
		{
			Tank t = new Tank(x,y*(1+i),isjust,dir,tc);
			tc.tanks.add(t);
		}
	}
}
