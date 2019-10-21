package somephysicsthing.solarsystem;

import somephysicsthing.solarsystem.bounded.Bounded;
import somephysicsthing.solarsystem.bounded.Point;
import somephysicsthing.solarsystem.bounded.Rectangle;
import somephysicsthing.solarsystem.rendering.CartesianSolarSystem;
import somephysicsthing.solarsystem.rendering.SolarSystem;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {
    private double arenaSize = 6000;
    private double scaleFactor = 10;
    private double solarSystemSize = this.arenaSize / this.scaleFactor;

    private ArrayList<Planet> planets;
    private CartesianSolarSystem solarSystem;

    private Bounded simulationArea = new Rectangle(0, 0,
            this.arenaSize, this.arenaSize
    );

    private Bounded solarSystemArea = new Rectangle(
            this.solarSystemSize / 2,
            this.solarSystemSize / 2,
            this.solarSystemSize,
            this.solarSystemSize
    );

    private class PlanetGenerator {
        private Random gen = new Random(0);

        @Nonnull private Vec2 mkCoords() {
            var x = (this.gen.nextDouble() - 0.5f) * 2.0f * App.this.solarSystemSize;
            var y = (this.gen.nextDouble() - 0.5f) * 2.0f * App.this.solarSystemSize;

            return new Vec2(x, y);
        }

        @Nonnull private Vec2 mkVelo() {
            var x = (this.gen.nextDouble() - 0.5f) * 1000.0f;
            var y = (this.gen.nextDouble() - 0.5f) * 1000.0f;

            return new Vec2(x, y);
        }

        private double mkMass() {
            return this.gen.nextDouble() * 1.0f;
        }

        @Nonnull public Planet mkPlanet() {
            return new Planet(
                    this.mkCoords(),
                    this.mkVelo(),
                    this.mkMass()
            );
        }
    }

    private App() {
        var planetGen = new PlanetGenerator();

        this.planets = IntStream.rangeClosed(0, 10000)
                .mapToObj((_idx) -> planetGen.mkPlanet())
                .collect(Collectors.toCollection(ArrayList::new));

        this.planets.add(new Planet(new Vec2(0, 1000), new Vec2(-500, 0), 1000000000.0f, "#00ffff"));
        this.planets.add(new Planet(new Vec2(0, -1000), new Vec2(500, 0), 1000000000.0f, "#00ff00"));

        this.solarSystem = new CartesianSolarSystem((int) this.solarSystemSize, (int) this.solarSystemSize);
    }

    private void drawPlanet(Planet p) {
        var positionOnScreen = this.simulationArea.scaleBetween(this.solarSystemArea, p).getPos();

        if (!this.solarSystemArea.contains(new Point(positionOnScreen))) {
            return;
        }

        this.solarSystem.drawSolarObject(
                positionOnScreen.x,
                positionOnScreen.y,
                p.getDiameter(),
                p.colour
        );
    }

    private void drawRegion(Bounded r) {
        r = this.simulationArea.scaleBetween(this.solarSystemArea, r);

        // my rectangles are represented by the centre point, width and height
        // awt rectangles are represented by the upper left point, width and height

        Shape rect = new java.awt.Rectangle((int) r.left(), (int) r.bottom(), (int) r.getW(), (int) r.getH());

        this.solarSystem.drawExtra(rect);
    }

    private void innerMain() throws InterruptedException {
        while (!this.planets.isEmpty()) {
            // remove planets that exceed the edge of the world
            this.planets.removeIf((Planet p) -> !p.isValid(this.arenaSize, this.arenaSize));

            // System.out.println(this.planets);

            for (var planet : this.planets) {
                this.drawPlanet(planet);
            }

            var regions = (new MassSimulation<>(this.planets, this.arenaSize, this.arenaSize)).runSimulation(0.1f);

            for (Bounded bounded : regions.values()) {
                this.drawRegion(bounded);
            }

            this.solarSystem.finishedDrawing();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        (new App()).innerMain();
    }
}
