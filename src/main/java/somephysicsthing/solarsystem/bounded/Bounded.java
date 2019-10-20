package somephysicsthing.solarsystem.bounded;

import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Represents a region of 2d space, a position, a width, and a height
 */
public interface Bounded {
    @Nonnull default Vec2 getPos() {
        return this.getRect().pos;
    }

    default double getX() {
        return this.getRect().pos.x;
    }

    default double getY() {
        return this.getRect().pos.y;
    }

    @Nonnull Rectangle getRect();

    @Nonnegative default double getW() {
        return this.getRect().w;
    }

    @Nonnegative default double getH() {
        return this.getRect().h;
    }

    /**
     * @return y coordinate of the top edge of the region
     */
    default double top() {
        return (this.getY() + (this.getH() / 2.0f));
    }

    /**
     * @return y coordinate of the bottom edge of the region
     */
    default double bottom() {
        return (this.getY() - (this.getH() / 2.0f));
    }

    /**
     * @return x coordinate of the left edge of the region
     */
    default double left() {
        return (this.getX() - (this.getW() / 2.0f));
    }

    /**
     * @return x coordinate of the right edge of the region
     */
    default double right() {
        return (this.getX() + (this.getW() / 2.0f));
    }

    /**
     * Test if `other` can fit inside this object completely
     * @param other the bounded to test
     * @return if `other` fits inside this
     */
    default boolean contains(@Nonnull Bounded other) {
        return (this.top() >= other.top() &&
                this.bottom() <= other.bottom() &&
                this.left() <= other.left() &&
                this.right() >= other.right()
                );
    }

    /**
     * @return the upper half of the region
     */
    @Nonnull
    default Rectangle northRect() {
        return new Rectangle(
                new Vec2(this.getX(), this.getY() + this.getH() / 4.0f),
                this.getW(), this.getH() / 2.0f
        );
    }

    /**
     * @return the bottom half of the region
     */
    @Nonnull default Rectangle southRect() {
        return new Rectangle(
                new Vec2(this.getX(), this.getY() - this.getH() / 4.0f),
                this.getW(), this.getH() / 2.0f
        );
    }
    /**
     * @return the left half of the region
     */
    @Nonnull default Rectangle westRect() {
        return new Rectangle(
                new Vec2(this.getX() - this.getW() / 4.0f, this.getY()),
                this.getW() / 2.0f, this.getH()
        );
    }
    /**
     * @return the right half of the region
     */
    @Nonnull default Rectangle eastRect() {
        return new Rectangle(
                new Vec2(this.getX() + this.getW() / 4.0f, this.getY()),
                this.getW() / 2.0f, this.getH()
        );
    }

    /**
     * Scale a region from one to another
     * @param target region to scale to
     * @param b region to scale between
     */
    @Nonnull default Bounded scaleBetween(Bounded target, Bounded b) {
        var normCoords = b.getPos().sub(this.getPos());
        var wScale = target.getW() / this.getW();
        var hScale = target.getH() / this.getH();

        return new Rectangle(new Vec2(
                (normCoords.x * wScale) + target.getX(),
                (normCoords.y * hScale) + target.getY()),
                b.getW() * wScale,
                b.getH() * hScale
        );
    }
}
