package somephysicsthing.solarsystem.propertytraits;

import somephysicsthing.solarsystem.Vec2;
import somephysicsthing.solarsystem.quadtree.Bounded;
import somephysicsthing.solarsystem.quadtree.Rectangle;

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
