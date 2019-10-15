package somephysicsthing.solarsystem;

import somephysicsthing.solarsystem.propertytraits.*;
import somephysicsthing.solarsystem.quadtree.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;

public class MassSimulation<T extends MutPosition & MutVelocity & HasMass> {
    @Nonnull private final List<T> elems;
    private final float width, height;
    private final float theta;
    private final float g;

    MassSimulation(@Nonnull List<T> elems, float width, float height) {
        this.elems = elems;
        this.width = width;
        this.height = height;
        this.theta = 1.5f;
        this.g = 6.67e-11f;
    }

    public MassSimulation(@Nonnull List<T> elems, float width, float height, float theta, float g) {
        this.elems = elems;
        this.width = width;
        this.height = height;
        this.theta = theta;
        this.g = g;
    }

    void runSimulation(float ts) {
        var tree = new Quadtree<T>(this.width, this.height);

        for (var elem : this.elems) {
            tree.insert(elem);
        }

        HashMap<List<Direction>, MassyPoint> weights = (new CalculateWeights()).calculate(tree);

        for (var elem : this.elems) {
            var massesToUse = tree.getRegionsFitting((Bounded region) -> {
                var avgWidth = (region.getH() + region.getW()) / 2.0f;
                var dist = region.getPos().sub(elem.getPosition()).abs();

                return (avgWidth / dist) < this.theta;
            });

            for (var massPath : massesToUse) {
                var mass = weights.get(massPath);

                if (mass == null)
                    continue;

                var newValues = this.calcValuesFromGravity(elem, mass, ts);

                elem.setPosition(newValues.left);
                elem.setVelocity(newValues.right);
            }
        }
    }

    @Nonnull
    private Vec2 calcAccellInner(@Nonnull Vec2 pos, float mass, @Nonnull MassyPoint p2) {
        var dist = pos.sub(p2.pos).abs();

        var force = (this.g * mass * p2.mass) / Math.sqrt(dist);

        var xComponent = (pos.x - p2.getX()) / dist * force;
        var yComponent = (pos.y - p2.getY()) / dist * force;

        return new Vec2((float)-xComponent, (float)-yComponent);
    }

    /**
     * calculate the new position and velocity for a particle
     * @param p1 the point to focus on for calculating force
     * @param p2 the point affecting it
     * @param ts the timestep
     * @return a 2-tuple of new pos, new velocity
     */
    @Nonnull private Tuple2<Vec2, Vec2> calcValuesFromGravity(@Nonnull T p1, @Nonnull MassyPoint p2, float ts) {
        var pos = p1.getPosition();
        var vel = p1.getVelocity();
        var mass = p1.getMass();

        // perform runge kutta
        var k1dx = this.calcAccellInner(pos, mass, p2);
        var k2dx = this.calcAccellInner(pos.add(vel).scale(ts / 2), mass, p2);
        var k2x = vel.add(vel).scale(ts / 2);
        var k3dx = this.calcAccellInner(pos.add(k2x).scale(ts / 2), mass, p2);
        var k3x = vel.add(k2dx).scale(ts / 2);
        var k4dx = this.calcAccellInner(pos.add(k3x).scale(ts), mass, p2);
        var k4x = vel.add(k3dx).scale(ts);

        var dx = vel.add(k1dx.add(k2dx.scale(2)).add(k3dx.scale(2)).add(k4dx).scale(ts / 6));
        var d =  pos.add(vel.add(k2x.scale(2)).add(k3x.scale(2)).add(k4x).scale(ts / 6));

        return new Tuple2<>(d, dx);
    }

    private static class MassyPoint implements HasPosition, HasMass {
        @Nonnull private final Vec2 pos;
        private final float mass;

        MassyPoint(@Nonnull Vec2 pos, float mass) {
            this.pos = pos;
            this.mass = mass;
        }

        @Nonnull
        @Override
        public Vec2 getPosition() {
            return this.pos;
        }

        @Override
        public float getMass() {
            return this.mass;
        }

        @Nonnull
        MassyPoint merge(@Nonnull MassyPoint other) {
            var combinedMass = this.mass + other.mass;
            var pos = this.pos.scale(this.mass).add(other.pos.scale(other.mass)).scale(1.0f / combinedMass);

            return new MassyPoint(pos, combinedMass);
        }
    }

    class CalculateWeights implements QuadtreeFolder<T, MassyPoint> {
        private HashMap<List<Direction>, MassyPoint> centresOfMass;

        CalculateWeights() {
            this.centresOfMass = new HashMap<>();
        }

        @Nonnull HashMap<List<Direction>, MassyPoint> calculate(@Nonnull Quadtree<T> tree) {
            tree.applyFold(this);
            return this.centresOfMass;
        }

        @Nonnull
        @Override
        public MassyPoint visitEmpty(@Nonnull List<Direction> path) {
            // nodes with no elements clearly have no mass
            return new MassyPoint(new Vec2(0, 0), 0);
        }

        @Nonnull
        @Override
        public MassyPoint visitLeaf(@Nonnull List<Direction> path, @Nonnull T elem) {
            var massPoint = new MassyPoint(elem.getPosition(), elem.getMass());
            this.centresOfMass.put(path, massPoint);
            return massPoint;
        }

        @Nonnull
        @Override
        public MassyPoint visitQuad(@Nonnull List<Direction> path, @Nonnull MassyPoint nw, @Nonnull MassyPoint ne, @Nonnull MassyPoint sw, @Nonnull MassyPoint se) {
            var massPoint = nw.merge(ne).merge(sw).merge(se);
            this.centresOfMass.put(path, massPoint);
            return massPoint;
        }
    }
}
