package somephysicsthing.solarsystem.bounded;

import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class Rectangle implements Bounded {
    @Nonnull final Vec2 pos;
    @Nonnegative final double w, h;

    public Rectangle(@Nonnull Vec2 pos, @Nonnegative double w, @Nonnegative double h) {
        this.pos = pos;
        this.w = w;
        this.h = h;
    }

    public Rectangle(double x, double y, @Nonnegative double w, @Nonnegative double h) {
        this.pos = new Vec2(x, y);
        this.w = w;
        this.h = h;
    }

    @Nonnull
    @Override
    public Rectangle getRect() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (Double.compare(rectangle.w, this.w) != 0) return false;
        if (Double.compare(rectangle.h, this.h) != 0) return false;
        return this.pos.equals(rectangle.pos);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = this.pos.hashCode();
        temp = Double.doubleToLongBits(this.w);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.h);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
