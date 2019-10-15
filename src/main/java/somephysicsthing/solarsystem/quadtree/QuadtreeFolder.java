package somephysicsthing.solarsystem.quadtree;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * An interface for folding over a quadtree
 * @param <T>
 * @param <R>
 */
public interface QuadtreeFolder<T, R> {
    @Nonnull
    R visitEmpty(@Nonnull List<Direction> path);
    @Nonnull
    R visitLeaf(@Nonnull List<Direction> path, @Nonnull T elem);
    @Nonnull
    R visitQuad(@Nonnull List<Direction> path, R nw, R ne, R sw, R se);
}
