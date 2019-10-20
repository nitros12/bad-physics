package somephysicsthing.solarsystem.bounded;

import org.junit.Test;
import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnull;

import static org.junit.Assert.*;

public class BoundedTest {
    @Nonnull private Bounded b = new Rectangle(100, 100, 100, 100);

    @Test
    public void getPos() {
        assertEquals(this.b.getPos(), new Vec2(100, 100));
    }

    @Test
    public void getX() {
        assertEquals(this.b.getX(), 100, 0.1);
    }

    @Test
    public void getY() {
        assertEquals(this.b.getY(), 100, 0.1);
    }

    @Test
    public void getRect() {
        assertEquals(this.b.getRect(), this.b);
    }

    @Test
    public void getW() {
        assertEquals(this.b.getW(), 100, 0.1);
    }

    @Test
    public void getH() {
        assertEquals(this.b.getH(), 100, 0.1);
    }

    @Test
    public void top() {
        assertEquals(this.b.top(), 150, 0.1);
    }

    @Test
    public void bottom() {
        assertEquals(this.b.bottom(), 50, 0.1);
    }

    @Test
    public void left() {
        assertEquals(this.b.left(), 50, 0.1);
    }

    @Test
    public void right() {
        assertEquals(this.b.right(), 150, 0.1);
    }

    @Test
    public void contains() {
        assertTrue(this.b.contains(new Point(100, 100)));
    }

    @Test
    public void northRect() {
        assertEquals(this.b.northRect(), new Rectangle(100, 125, 100, 50));
    }

    @Test
    public void southRect() {
        assertEquals(this.b.southRect(), new Rectangle(100, 75, 100, 50));
    }

    @Test
    public void westRect() {
        assertEquals(this.b.westRect(), new Rectangle(75, 100, 50, 100));
    }

    @Test
    public void eastRect() {
        assertEquals(this.b.eastRect(), new Rectangle(125, 100, 50, 100));
    }

    @Test
    public void scaleBetween() {
        var startRegion = new Rectangle(0, 0, 100, 100);
        var finalRegion = new Rectangle(0, 0, 200, 200);

        var toScaleRegion = new Rectangle(50, 50, 50, 50);
        var expectedScaledRegion = new Rectangle(100, 100, 100, 100);

        assertEquals(startRegion.scaleBetween(finalRegion, toScaleRegion), expectedScaledRegion);
    }
}