package somephysicsthing.solarsystem;

import somephysicsthing.solarsystem.bounded.Point;
import somephysicsthing.solarsystem.bounded.Rectangle;
import somephysicsthing.solarsystem.propertytraits.HasMass;
import somephysicsthing.solarsystem.propertytraits.MutPosition;
import somephysicsthing.solarsystem.propertytraits.MutVelocity;

import javax.annotation.Nonnull;

public class Planet implements MutVelocity, MutPosition, HasMass {
    @Nonnull private Vec2 pos;
    @Nonnull private Vec2 vel;
    private double mass;
    final String colour;

    /**
     * @param pos initial position of the planet
     * @param vel initial velocity of the planet
     * @param mass mass of the planet
     */
    Planet(@Nonnull Vec2 pos, @Nonnull Vec2 vel, double mass) {
        this.pos = pos;
        this.vel = vel;
        this.mass = mass;
        this.colour = "#ff00ff";
    }

    Planet(@Nonnull Vec2 pos, @Nonnull Vec2 vel, double mass, String colour) {
        this.pos = pos;
        this.vel = vel;
        this.mass = mass;
        this.colour = colour;
    }


    double getDiameter() {
        return Math.max(Math.cbrt(this.mass / 1000000), 2);
    }

    /**
     * @param worldW width of the world
     * @param worldH height of the world
     * @return if this planet is valid in the world
     */
    boolean isValid(double worldW, double worldH) {
        if (Double.isNaN(this.pos.x)
                || Double.isNaN(this.pos.y)
                || Double.isNaN(this.vel.x)
                || Double.isNaN(this.vel.y)) {
            return false;
        }

        return (new Rectangle(0, 0, worldW, worldH).contains(new Point(this.pos)));
    }

    @Override
    public double getMass() {
        return this.mass;
    }

    @Nonnull
    @Override
    public Vec2 getPos() {
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
