package somephysicsthing.solarsystem.bounded;

import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnull;

public class Point implements Bounded {
    @Nonnull
    private final Vec2 pos;

    public Point(@Nonnull Vec2 pos) {
        this.pos = pos;
    }

    @Nonnull
    @Override
    public Rectangle getRect() {
        return new Rectangle(this.pos, 0, 0);
    }
}
