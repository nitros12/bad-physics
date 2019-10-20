package somephysicsthing.solarsystem.quadtree;

import org.junit.Test;
import somephysicsthing.solarsystem.propertytraits.HasPosition;
import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnull;

import static org.junit.Assert.*;

public class QuadtreeTest {

    @Test
    public void insert() {
        class TestPoint implements HasPosition {
            private final double x, y;
            private final double value;

            private TestPoint(double x, double y, double value) {
                this.x = x;
                this.y = y;
                this.value = value;
            }

            @Nonnull
            @Override
            public Vec2 getPosition() {
                return new Vec2(this.x, this.y);
            }
        }

        Quadtree<TestPoint> tree = new Quadtree<>(100, 100);

        assertTrue(tree.insert(new TestPoint(20, 20, 100)));
    }
}
