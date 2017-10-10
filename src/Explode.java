import java.awt.*;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Toolkit;
//����ȫ����ը
//�Ż���ը������

class Explode
{
	int x,y;
	private boolean islive=true;
	private int [] diameter={5,12,22,35,47,66,40,25};
	public TankClient tc;
	private int step=0;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private boolean initialize=false;
	private static Image [] imgs =
	{
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը1.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը2.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը3.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը4.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը5.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը6.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը7.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը8.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը9.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը10.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը11.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը12.jpg")),
		tk.getImage(Explode.class.getClassLoader().getResource("Image/̹�˱�ը13.jpg"))
	};
	
	public Explode(int x, int y,TankClient tc)
	{
		this.x = x;
		this.y = y;
		this.tc=tc;
	}
	
	public void  draw(Graphics g)
	{
		if(initialize==false)
		{
			for(int i=0;i<imgs.length;i++)
			{
				g.drawImage(imgs[i],-100,-100,null);
			}
		}
		if(!islive) 
		{
			tc.explodes.remove(this);
			return;
		}
		if(step==imgs.length) 
		{
			islive=false;
			step=0;
			return;
		}
		g.drawImage(imgs[step],x,y,null);
		step++;
	}
}