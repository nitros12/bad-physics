package somephysicsthing.solarsystem.quadtree;

import somephysicsthing.solarsystem.Vec2;
import somephysicsthing.solarsystem.propertytraits.HasPosition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Function;

import static somephysicsthing.solarsystem.quadtree.Direction.*;

/**
 * Implements a quadtree data structure for storing 2-d points
 */
public class Quadtree<T extends HasPosition> {
    @Nonnull private Quadrant<T> inner;
    @Nonnull private Bounded bounds;

    public Quadtree(float width, float height) {
        this.bounds = new Rectangle(new Vec2(0, 0), width, height);
        this.inner = new LeafNode(this.bounds);
    }

    public boolean insert(@Nonnull T p) {
        if (!this.bounds.contains(p))
            return false;

        this.inner = this.inner.insert(p);
        return true;
    }

    public <R> R applyFold(QuadtreeFolder<T, R> folder) {
        var path = new Stack<Direction>();
        return this.inner.applyFold(folder, path);
    }

    /**
     * Get a map of paths to regions that fit a predicate.
     * @param pred predicate function to test when we should stop traversing bounds
     * @return map of paths to regions that fit a predicate.
     */
    @Nonnull
    public HashSet<List<Direction>> getRegionsFitting(Function<Bounded, Boolean> pred) {
        var map = new HashSet<List<Direction>>();
        var path = new Stack<Direction>();

        this.inner.getRegionsFitting(pred, map, path);

        return map;
    }

    private interface Quadrant<T> {
        @Nonnull Bounded getBounded();
        @Nonnull Quadrant<T> insert(T p);

        <R> R applyFold(QuadtreeFolder<T, R> folder, Stack<Direction> pathSoFar);
        void getRegionsFitting(Function<Bounded, Boolean> pred, HashSet<List<Direction>> collector, Stack<Direction> pathSoFar);

        /**
         * Get the direction to go to get to `where`
         *
         * NOTE: Assumes that `where` fits inside the bounds of the quadrant
         * @param where location to move to
         * @return the direction to move to get to `where`
         */
        @Nonnull default Direction getDirection(@Nonnull Bounded where) {
            var bounds = this.getBounded();

            var in_top = bounds.northRect().contains(where);
            var in_left = bounds.westRect().contains(where);

            if (in_top) {
                if (in_left)
                    return NW;
                return NE;
            } else {
                if (in_left)
                    return SW;
                return SE;
            }
        }
    }

    private class InternalNode implements Quadrant<T> {
        @Nonnull private final Bounded bounds;

        @Nonnull private Quadrant<T> nw;
        @Nonnull private Quadrant<T> ne;
        @Nonnull private Quadrant<T> sw;
        @Nonnull private Quadrant<T> se;

        InternalNode(@Nonnull Bounded bounds) {
            this.bounds = bounds;
            this.nw = new LeafNode(bounds.westRect().northRect());
            this.ne = new LeafNode(bounds.eastRect().northRect());
            this.sw = new LeafNode(bounds.westRect().southRect());
            this.se = new LeafNode(bounds.eastRect().southRect());
        }

        @Override
        @Nonnull
        public Bounded getBounded() {
            return this.bounds;
        }

        @Nonnull
        @Override
        public Quadrant<T> insert(@Nonnull T p) {
            var direction = this.getDirection(p);

            switch (direction) {
                case NW: this.nw = this.nw.insert(p); break;
                case NE: this.ne = this.ne.insert(p); break;
                case SW: this.sw = this.sw.insert(p); break;
                case SE: this.se = this.se.insert(p); break;
            }

            return this;
        }

        public <R> R applyFold(@Nonnull QuadtreeFolder<T, R> folder, @Nonnull Stack<Direction> pathSoFar) {
            pathSoFar.push(Direction.NW);
            var nw = this.nw.applyFold(folder, pathSoFar);
            pathSoFar.pop();

            pathSoFar.push(Direction.NE);
            var ne = this.ne.applyFold(folder, pathSoFar);
            pathSoFar.pop();

            pathSoFar.push(Direction.SW);
            var sw = this.sw.applyFold(folder, pathSoFar);
            pathSoFar.pop();

            pathSoFar.push(Direction.SE);
            var se = this.se.applyFold(folder, pathSoFar);
            pathSoFar.pop();

            return folder.visitQuad(List.copyOf(pathSoFar), nw, ne, sw, se);
        }

        @Override
        public void getRegionsFitting(@Nonnull Function<Bounded, Boolean> pred, @Nonnull HashSet<List<Direction>> collector, @Nonnull Stack<Direction> pathSoFar) {
            if (!pred.apply(this.bounds)) {
                collector.add(List.copyOf(pathSoFar));
                return;
            }

            pathSoFar.push(Direction.NW);
            this.nw.getRegionsFitting(pred, collector, pathSoFar);
            pathSoFar.pop();

            pathSoFar.push(Direction.NE);
            this.ne.getRegionsFitting(pred, collector, pathSoFar);
            pathSoFar.pop();

            pathSoFar.push(Direction.SW);
            this.sw.getRegionsFitting(pred, collector, pathSoFar);
            pathSoFar.pop();

            pathSoFar.push(Direction.SE);
            this.se.getRegionsFitting(pred, collector, pathSoFar);
            pathSoFar.pop();
        }
    }

    private class LeafNode implements Quadrant<T> {
        @Nonnull private final Bounded bounds;

        @Nullable T elem;

        LeafNode(@Nonnull Bounded bounds, @Nonnull T elem) {
            this.bounds = bounds;
            this.elem = elem;
        }

        LeafNode(@Nonnull Bounded bounds) {
            this.bounds = bounds;
            this.elem = null;
        }

        @Override
        @Nonnull
        public Bounded getBounded() {
            return this.bounds;
        }

        @Nonnull private Quadrant<T> split() {
            Quadrant<T> new_quad = new InternalNode(this.bounds);
            return new_quad.insert(this.elem);
        }

        @Nonnull
        @Override
        public Quadrant<T> insert(T p) {
            if (this.elem == null) {
                this.elem = p;
                return this;
            }

            var new_node = this.split();
            new_node.insert(p);
            return new_node;
        }

        public <R> R applyFold(@Nonnull QuadtreeFolder<T, R> folder, @Nonnull Stack<Direction> pathSoFar) {
            if (this.elem == null)
                return folder.visitEmpty(List.copyOf(pathSoFar));
            return folder.visitLeaf(List.copyOf(pathSoFar), this.elem);
        }

        @Override
        public void getRegionsFitting(Function<Bounded, Boolean> pred, @Nonnull HashSet<List<Direction>> collector, @Nonnull Stack<Direction> pathSoFar) {
            collector.add(List.copyOf(pathSoFar));
        }
    }
}
