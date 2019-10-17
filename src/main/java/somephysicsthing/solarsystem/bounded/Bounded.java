package somephysicsthing.solarsystem.bounded;

import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface Bounded {
    @Nonnull default Vec2 getPos() {
        return this.getRect().pos;
    }

    default float getX() {
        return this.getRect().pos.x;
    }

    default float getY() {
        return this.getRect().pos.y;
    }

    @Nonnull Rectangle getRect();

    @Nonnegative default float getW() {
        return this.getRect().w;
    }

    @Nonnegative default float getH() {
        return this.getRect().h;
    }

    default float top() {
        return (this.getY() + (this.getH() / 2.0f));
    }

    default float bottom() {
        return (this.getY() - (this.getH() / 2.0f));
    }

    default float left() {
        return (this.getX() - (this.getW() / 2.0f));
    }

    default float right() {
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
     * Scale a point between two regions
     * @param other region to scale to
     * @param point point to scale between
     */
    @Nonnull default Vec2 scaleBetween(Bounded other, Vec2 point) {
        var normCoords = point.sub(this.getPos());
        var wScale = other.getW() / this.getW();
        var hScale = other.getH() / this.getH();

        return new Vec2(
                (normCoords.x * wScale) + other.getX(),
                (normCoords.y * hScale) + other.getY()
        );
    }
}