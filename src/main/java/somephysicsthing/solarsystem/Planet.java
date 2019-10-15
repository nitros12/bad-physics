package somephysicsthing.solarsystem;

import somephysicsthing.solarsystem.propertytraits.HasMass;
import somephysicsthing.solarsystem.propertytraits.MutPosition;
import somephysicsthing.solarsystem.propertytraits.MutVelocity;

import javax.annotation.Nonnull;

public class Planet implements MutVelocity, MutPosition, HasMass {
    @Nonnull private Vec2 pos;
    @Nonnull private Vec2 vel;
    private float mass;

    /**
     * @param pos initial position of the planet
     * @param vel initial velocity of the planet
     * @param mass mass of the planet
     */
    Planet(@Nonnull Vec2 pos, @Nonnull Vec2 vel, float mass) {
        this.pos = pos;
        this.vel = vel;
        this.mass = mass;
    }

    float getDiameter() {
        return (float) Math.min(Math.sqrt(mass / 100), 5);
    }

    /**
     * @param worldW width of the world
     * @param worldH height of the world
     * @return if this planet is valid in the world
     */
    boolean isValid(float worldW, float worldH) {
        if (Float.isNaN(this.pos.x)
                || Float.isNaN(this.pos.y)
                || Float.isNaN(this.vel.x)
                || Float.isNaN(this.vel.y)) {
            return false;
        }

        return !(this.pos.x > worldW / 2)
                && !(this.pos.y > worldH / 2);
    }

    @Override
    public float getMass() {
        return this.mass;
    }

    @Nonnull
    @Override
    public Vec2 getPosition() {
        return this.pos;
    }

    @Override
    public void setPosition(@Nonnull Vec2 position) {
        this.pos = position;
    }

    @Nonnull
    @Override
    public Vec2 getVelocity() {
        return this.vel;
    }

    @Override
    public void setVelocity(@Nonnull Vec2 velocity) {
        this.vel = velocity;
    }

    @Nonnull
    @Override
    public String toString() {
        return "Planet{" +
                "pos=" + this.pos +
                ", vel=" + this.vel +
                ", mass=" + this.mass +
                '}';
    }
}
