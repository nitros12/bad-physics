package somephysicsthing.solarsystem;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class provides a graphical user interface to a model of the solar system
 * @author Joe Finney
 */
public class SolarSystem extends JFrame 
{
	private int width = 300;
	private int height = 300;

	@Nonnull
	private ArrayList<SolarObject> things = new ArrayList<>();

	@Nonnull
	private ArrayList<Shape> moreThings = new ArrayList<>();

	/**
	 * Create a view of the Solar System.
	 * Once an instance of the SolarSystem class is created,
	 * a window of the appropriate size is displayed, and
	 * objects can be displayed in the solar system
	 *
	 * @param width the width of the window in pixels.
	 * @param height the height of the window in pixels.
	 */
	public SolarSystem(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.setTitle("The Solar System");
		this.setSize(width, height);
		this.setBackground(Color.BLACK);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);		
	}

	/**
	 * A method called by the operating system to draw onto the screen - <p><B>YOU DO NOT (AND SHOULD NOT) NEED TO CALL THIS METHOD.</b></p>
	 */
	public void paint (@Nonnull Graphics gr)
	{
		BufferedImage i = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = i.createGraphics();
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		synchronized (this)
		{
		    g.setColor(this.getBackground());
			g.clearRect(0,0, this.width, this.height);
			for(SolarObject t : this.things)
			{
				g.setColor(t.col);
				g.fillOval(t.x, t.y, t.diameter, t.diameter);
			}

			for (var s : this.moreThings) {
				g.draw(s);
			}

			gr.drawImage(i, 0, 0, this);
		}
	}

	//
	// Shouldn't really handle colour this way, but the student's haven't been introduced
	// to constants properly yet, and Color.getColor() doesn't seem to work... hmmm....
	// 
	private Color getColourFromString(@Nonnull String col)
	{
		Color color;
		
		if (col.charAt(0) == '#')
		{
			color = new Color(
    	        Integer.valueOf( col.substring( 1, 3 ), 16 ),
        	    Integer.valueOf( col.substring( 3, 5 ), 16 ),
            	Integer.valueOf( col.substring( 5, 7 ), 16 ) );
		} 
		else 
		{
			try 
			{
				java.lang.reflect.Field field = Color.class.getField(col);
				color = (Color)field.get(null);
			} catch (Exception e) {
				color = Color.WHITE;
			}
		}
		return color;
	}
	
	/**
	 * Draws a round shape in the window at the given co-ordinates that represents an object in the solar system.
	 * The SolarSystem class uses <i>Polar Co-ordinates</i> to represent the location
	 * of objects in the solar system.
	 *
	 * @param diameter the size of the object.
	 * @param col the colour of this object, as a string. Case insentive. <p>One of: BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, 
	 * MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW. Alternatively, a 24 bit hexadecimal string representation of an RGB colour is also accepted, e.g. "#FF0000"</p>
	 */
	public void drawSolarObject(double x, double y, double diameter, @Nonnull String col)
	{
		Color colour = this.getColourFromString(col);

		synchronized (this)
		{
			SolarObject t = new SolarObject((int) x, (int) y, (int) diameter, colour);
			this.things.add(t);
		}
	}

	public void drawExtra(Shape s) {
		synchronized (this) {
			this.moreThings.add(s);
		}
	}

	/**
	 * Makes all objects drawn recently drawn to be made visible on the screen.
	 *  
	 * Once called, all suns, planets and moons are displayed in the window.
	 */
	public void finishedDrawing()
	{
		this.repaint();
		try {
			Thread.sleep(10);
		} catch (InterruptedException ignored) {
		}
		synchronized (this)
		{
			this.things.clear();
			this.moreThings.clear();
		}
	}
	
	private static class SolarObject
	{
		public int x;
		public int y;
		public int diameter;
		public Color col;

		public SolarObject(int x, int y, int diameter, Color col)
		{
			this.x = x;
			this.y = y;
			this.diameter = diameter;
			this.col = col;
		}
	}
}
