
import java.awt.*;

public class HealBag
{
	public int x=300,y=300,width=30,height=30;
	private int x_speed=5,y_speed=5;
	boolean islive = true;
	private int x_m[]={200,250,350,333,355,377,344,311,222,211,202,189};
	private int y_m[]={300,311,322,355,377,400,433,422,411,377,344,320};
	private TankClient tc;
	public static final int  HEAL_VOLUME=50;
	private int move_step=0;
	
	public HealBag(int x, int y, int weight, int hight,TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.width = weight;
		this.height = hight;
		this.tc=tc;
	}
	
	public void draw(Graphics g)
	{
		if(!islive) return;
		Color c = g.getColor();
		g.setColor(Color.PINK);
		g.fillRoundRect(x, y, width, height,5,5);
		g.setColor(c);
		move();
	}
	
	private void move()
	{
		int add=0;
		x+=x_speed;
		if(y<200)
		{
			add++;
		}
		else if(y>400)
		{
			add--;
		}
		y_speed+=add;
		y+=y_speed;
		if(x>=800) x=0;
	}
	
	public void reMake()
	{
		if(tc.mytank.getLife()<=30&&islive==false) 
			{
				islive=true;
				x=10;
				y=300;
			}
	}
	
	public void healBagDisappear(Graphics g)
	{
		int add=1;
		islive=false;
		Color c = g.getColor();
		g.setColor(Color.pink);
		while(width<60||height<60)
		{
			width+=add;
			height+=add;
			g.fillOval(x, y, width, height);
		}
		while(width>0||height>0)
		{
			width-=add;
			height-=add;
			g.fillOval(x, y, width, height);
		}
		g.setColor(c);
	}
	private void healTank(Tank t)
	{
		if(t.getIsjust())
		{
			if(t.getLife()+HEAL_VOLUME>=100) t.setLife(100);
			else t.setLife(t.getLife()+HEAL_VOLUME);
		}
	}
	public Rectangle getRectangle()
	{
		return new Rectangle(x,y,width,height);
	}
	public boolean collisionWithsTank(Tank t)
	{
		if(this.getRectangle().intersects(t.getRectangle())&&islive)
		{
			healTank(t);
			return true;
		}
		return false;
	}
}