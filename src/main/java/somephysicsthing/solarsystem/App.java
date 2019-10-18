package somephysicsthing.solarsystem;

import somephysicsthing.solarsystem.bounded.Bounded;
import somephysicsthing.solarsystem.bounded.Point;
import somephysicsthing.solarsystem.bounded.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    private float arenaSize = 600;
    private float scaleFactor = 1;
    private float solarSystemSize = this.arenaSize / this.scaleFactor;

    private ArrayList<Planet> planets;
    private SolarSystem solarSystem;

    private Bounded simulationArea = new Rectangle(
            new Vec2(0, 0),
            this.arenaSize, this.arenaSize
    );

    private Bounded solarSystemArea = new Rectangle(
            new Vec2(this.solarSystemSize / 2,
                    this.solarSystemSize / 2),
            this.solarSystemSize,
            this.solarSystemSize
    );

    private App() {
        this.planets = new ArrayList<>(List.of(
                new Planet(
                        new Vec2(100, 100),
                        new Vec2(0, 0),
                        1000
                ),
                new Planet(
                        new Vec2(-100, 100),
                        new Vec2(0, 0),
                        1000
                )
//                new Planet(
//                        new Vec2(1, 1),
//                        new Vec2(0.1f, 0.1f),
//                        20000
//                )
        ));

        this.solarSystem = new SolarSystem((int) this.solarSystemSize, (int) this.solarSystemSize);
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
                "#ff00ff"
        );
    }

    private void drawRegion(Bounded r) {
        r = this.simulationArea.scaleBetween(this.solarSystemArea, r);

        Shape rect = new java.awt.Rectangle((int) r.getX(), (int) r.getY(), (int) r.getW(), (int) r.getH());

        this.solarSystem.drawExtra(rect);
    }

    private void innerMain() throws InterruptedException {
        while (!this.planets.isEmpty()) {
            // remove planets that exceed the edge of the world
            this.planets.removeIf((Planet p) -> !p.isValid(this.arenaSize, this.arenaSize));

            System.out.println(this.planets);

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
