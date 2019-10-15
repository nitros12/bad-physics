package somephysicsthing.solarsystem;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
        float arenaSize = 30000;
        float scaleFactor = 100;

        ArrayList<Planet> planets = new ArrayList<>(List.of(
                new Planet(
                        new Vec2(10000, 10000),
                        new Vec2(-0.1f, 0),
                        10000
                ),
                new Planet(
                        new Vec2(-10000, 10000),
                        new Vec2(0, -0.1f),
                        10000
                ),
                new Planet(
                        new Vec2(10000, -10000),
                        new Vec2(0, 0.1f),
                        10000
                ),
                new Planet(
                        new Vec2(-10000, -10000),
                        new Vec2(0.1f, 0),
                        10000
                )
//                new Planet(
//                        new Vec2(1, 1),
//                        new Vec2(0.1f, 0.1f),
//                        20000
//                )
        ));

        var solarSystem = new SolarSystem((int) (arenaSize / scaleFactor), (int) (arenaSize / scaleFactor));

        System.out.println(planets.toString());
        while (!planets.isEmpty()) {
            // remove planets that exceed the edge of the world
            planets.removeIf((Planet p) -> !p.isValid(arenaSize, arenaSize));

            for (var planet : planets) {
                solarSystem.drawSolarObject(
                        (planet.getX() + arenaSize / 2.0f) / scaleFactor,
                        (planet.getY() + arenaSize / 2.0f) / scaleFactor,
                        planet.getDiameter(),
                        "#ff00ff"
                );
            }
            Thread.sleep(10);
            solarSystem.finishedDrawing();

            (new MassSimulation<>(planets, arenaSize, arenaSize)).runSimulation(10f);

            System.out.println(planets.toString());
            // Thread.sleep(100);
        }
    }
}
