package somephysicsthing.solarsystem.propertytraits;

import somephysicsthing.solarsystem.Vec2;

import javax.annotation.Nonnull;

public interface HasVelocity {
    @Nonnull
    Vec2 getVelocity();
}
