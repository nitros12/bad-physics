package somephysicsthing.solarsystem;

import somephysicsthing.solarsystem.bounded.Bounded;
import somephysicsthing.solarsystem.bounded.Point;
import somephysicsthing.solarsystem.bounded.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class App {
    private float arenaSize = 30000;
    private float scaleFactor = 100;

    private ArrayList<Planet> planets;
    private SolarSystem solarSystem;

    private Bounded simulationArea = new Rectangle(
            new Vec2(0, 0),
            arenaSize, arenaSize
    );

    private Bounded solarSystemArea = new Rectangle(
            new Vec2((arenaSize / scaleFactor) / 2,
                    (arenaSize / scaleFactor) / 2),
            arenaSize / scaleFactor,
            arenaSize / scaleFactor
    );

    private App() {
        this.planets = new ArrayList<>(List.of(
                new Planet(
                        new Vec2(10000, 10000),
                        new Vec2(-0.14f, 0),
                        10000
                )
//                new Planet(
//                        new Vec2(-10000, 10000),
//                        new Vec2(0.01f, -0.14f),
//                        10000
//                ),
//                new Planet(
//                        new Vec2(10000, -10000),
//                        new Vec2(0, 0.1f),
//                        10000
//                ),
//                new Planet(
//                        new Vec2(-10000, -10000),
//                        new Vec2(0.1f, 0),
//                        10000
//                )
//                new Planet(
//                        new Vec2(1, 1),
//                        new Vec2(0.1f, 0.1f),
//                        20000
//                )
        ));

        this.solarSystem = new SolarSystem((int) (arenaSize / scaleFactor), (int) (arenaSize / scaleFactor));
    }

    private void drawPlanet(Planet p) {
        var positionOnScreen = this.simulationArea.scaleBetween(this.solarSystemArea, p.getPos());

        if (!this.solarSystemArea.contains(new Point(positionOnScreen))) {
            return;
        }

        solarSystem.drawSolarObject(
                positionOnScreen.x,
                positionOnScreen.y,
                p.getDiameter(),
                "#ff00ff"
        );
    }

    private void innerMain() throws InterruptedException {
        System.out.println(planets.toString());
        while (!planets.isEmpty()) {
            // remove planets that exceed the edge of the world
            this.planets.removeIf((Planet p) -> !p.isValid(arenaSize, arenaSize));

            for (var planet : planets) {
                this.drawPlanet(planet);
            }

            Thread.sleep(10);
            solarSystem.finishedDrawing();

            (new MassSimulation<>(planets, arenaSize, arenaSize)).runSimulation(10f);

            System.out.println(planets.toString());
            // Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        (new App()).innerMain();
    }
}
