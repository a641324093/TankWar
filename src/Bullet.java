
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//子弹应该设置一次能发射的总量或者设置发射冷却；

class Bullet 
{
	int x,y;
	Direction dir;
	private boolean islive = true;
	public static final int XSPEED=13,YSPEED=13,BULLETW=10,BULLETH=10;
	public static final int BULLET_DAMAGE=10;
	private boolean just;
	TankClient tc=null;
	private static Image [] imgs=null;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Map<String,Image> bullet_images = new HashMap<String,Image>();
	static
	{
		imgs = new Image[]
		{
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹左.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹左上.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹上.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹右上.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹右.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹右下.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹下.jpg")),
		tk.getImage(Bullet.class.getClassLoader().getResource("Image/子弹左下.jpg"))
		};
		bullet_images.put("L", imgs[0]);
		bullet_images.put("LU", imgs[1]);
		bullet_images.put("U", imgs[2]);
		bullet_images.put("RU", imgs[3]);
		bullet_images.put("R", imgs[4]);
		bullet_images.put("RD", imgs[5]);
		bullet_images.put("D", imgs[6]);
		bullet_images.put("LD", imgs[7]);
		
	}
	
	public boolean isIslive() {
		return islive;
	}
	public Bullet(int x, int y,boolean just, Direction dir) 
	{
		this.x=x;
		this.y=y;
		this.just=just;
		this.dir = dir;
	}
	public Bullet(int x, int y,boolean just, Direction dir,TankClient tc) 
	{
		this(x,y,just,dir);
		this.tc=tc;
	}
	public void draw(Graphics g)
	{
		if(!islive) return;
		switch (dir)
		{
		case L:g.drawImage(bullet_images.get("L"), x, y, null);break;
		case LU:g.drawImage(bullet_images.get("LU"), x, y, null);break;
		case U:g.drawImage(bullet_images.get("U"), x, y, null);break;
		case RU:g.drawImage(bullet_images.get("RU"), x, y, null);break;
		case R:g.drawImage(bullet_images.get("R"), x, y, null);break;
		case RD:g.drawImage(bullet_images.get("RD"), x, y, null);break;
		case D:g.drawImage(bullet_images.get("D"), x, y, null);break;
		case LD:g.drawImage(bullet_images.get("LD"), x, y, null);break;
		}
		move();
	}
	public void move()
	{
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
		}
		if(x<0||y<0||x>TankClient.SCREENWIDE||y>TankClient.SCREENHIGH) 
		{
			islive=false;
			tc.bullets.remove(this);
		}
	}
	public Rectangle getRectangle()
	{	
		System.out.println(imgs[0].getWidth(null)+imgs[0].getHeight(null));
		return new Rectangle(x,y,imgs[0].getWidth(null),imgs[0].getHeight(null));
	}
	public void bulletDisappear()
	{
		islive = false;
		Explode e = new Explode(x,y,tc);
		tc.explodes.add(e);
	}
	public boolean hitTank(Tank t)
	{
		if(getRectangle().intersects(t.getRectangle())&&t.getIslive()&&(just != t.getIsjust())) 
		{
			t.setLife(t.getLife()-BULLET_DAMAGE);
			bulletDisappear();
			if(t.getLife()<=0)
			{
				t.setIslive(false);
			}
			return true;
		}
		else return false;
	}
	public int hitTanks(List<Tank> tanks)
	{
		Tank t;
		int hashit=0;
		for(int i=0;i<tanks.size();i++)
		{
			t=tanks.get(i);
			if(hitTank(t)) hashit++;
		}
		return hashit;
	}
	public boolean hitWall(Wall w)
	{
		if(getRectangle().intersects(w.getRectangle())==true)
		{
			bulletDisappear();
			return true;
		}
		return false;
	}
}
