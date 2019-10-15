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

    @Nonnull Vec2 scale(float by) {
        return new Vec2(this.x * by, this.y * by);
    }

    @Nonnull Vec2 add(@Nonnull Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    @Nonnull Vec2 sub(@Nonnull Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    float abs() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    float absSquared() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * @return the vector representing the direction of the vector.
     */
    @Nonnull Vec2 direction() {
        var phi = Math.atan2(this.x, this.y);

        return new Vec2((float) Math.cos(phi), (float) Math.sin(phi));
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
