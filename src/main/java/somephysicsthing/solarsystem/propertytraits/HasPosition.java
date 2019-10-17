package somephysicsthing.solarsystem.propertytraits;

import somephysicsthing.solarsystem.Vec2;
import somephysicsthing.solarsystem.bounded.Bounded;
import somephysicsthing.solarsystem.bounded.Rectangle;

import javax.annotation.Nonnull;

public interface HasPosition extends Bounded {
    @Nonnull
    Vec2 getPosition();

    @Nonnull
    @Override
    default Rectangle getRect() {
        return new Rectangle(this.getPosition(),0, 0);
    }
}
