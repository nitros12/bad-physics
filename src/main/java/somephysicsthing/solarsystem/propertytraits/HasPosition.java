package somephysicsthing.solarsystem.propertytraits;

import somephysicsthing.solarsystem.Vec2;
import somephysicsthing.solarsystem.bounded.Bounded;
import somephysicsthing.solarsystem.bounded.Rectangle;

import javax.annotation.Nonnull;

public interface HasPosition extends Bounded {
    @Nonnull
    Vec2 getPos();

    @Nonnull
    @Override
    default Rectangle getRect() {
        return new Rectangle(this.getPos(),0, 0);
    }
}
