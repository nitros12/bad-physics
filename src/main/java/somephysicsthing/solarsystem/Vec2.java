package somephysicsthing.solarsystem;

import javax.annotation.Nonnull;

/**
 * A 2d-vector
 */
public class Vec2 {
    public final float x, y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Nonnull public Vec2 scale(float by) {
        return new Vec2(this.x * by, this.y * by);
    }

    @Nonnull public Vec2 add(@Nonnull Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    @Nonnull public Vec2 sub(@Nonnull Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public float abs() {
        return (float) Math.sqrt(this.absSquared());
    }

    public float absSquared() {
        return this.x * this.x + this.y * this.y;
    }

    @Nonnull public Vec2 normal() {
        return this.scale(1.0f / this.abs());
    }

    @Nonnull
    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + this.x +
                ", y=" + this.y +
                '}';
    }
}
