package somephysicsthing.solarsystem.bounded;

import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class Rectangle implements Bounded {
    final Vec2 pos;
    @Nonnegative final float w, h;

    public Rectangle(Vec2 pos, @Nonnegative float w, @Nonnegative float h) {
        this.pos = pos;
        this.w = w;
        this.h = h;
    }

    @Nonnull
    @Override
    public Rectangle getRect() {
        return this;
    }
}
