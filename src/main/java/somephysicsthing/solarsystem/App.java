package somephysicsthing.solarsystem;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        float arenaSize = 30000;

        ArrayList<Planet> planets = new ArrayList<>(List.of(
                new Planet(
                        new Vec2(100, 100),
                        new Vec2(10, 10),
                        100
                ),
                new Planet(
                        new Vec2(-100, 100),
                        new Vec2(10, 10),
                        100
                ),
                new Planet(
                        new Vec2(100, -100),
                        new Vec2(10, 10),
                        100
                ),
                new Planet(
                        new Vec2(-100, -100),
                        new Vec2(10, 10),
                        100
                )
        ));

        System.out.println(planets.toString());
        while (!planets.isEmpty()) {
            // remove planets that exceed the edge of the world
            planets.removeIf((Planet p) -> !p.isValid(arenaSize, arenaSize));

            (new MassSimulation<>(planets, arenaSize, arenaSize)).runSimulation(0.1f);

            System.out.println(planets.toString());
        }
    }
}
