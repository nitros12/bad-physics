package somephysicsthing.solarsystem.rendering;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CartesianSolarSystem extends SolarSystem {
    private ArrayList<Object> things;

    @Nonnull
    private ArrayList<Shape> moreThings = new ArrayList<>();

    private final Constructor<?> solarObjectCtor = SolarSystem.class.getDeclaredClasses()[0].getDeclaredConstructors()[0];
    private Function<String, Color> getColourFromStringFn;


    /**
     * Create a view of the Solar System.
     * Once an instance of the SolarSystem class is created,
     * a window of the appropriate size is displayed, and
     * objects can be displayed in the solar system
     *
     * @param width  the width of the window in pixels.
     * @param height the height of the window in pixels.
     */
    public CartesianSolarSystem(int width, int height) {
        super(width, height);
        this.solarObjectCtor.setAccessible(true);
        try {
            this.things = this.getThingsHack();
            this.getColourFromStringFn = this.getColourFromStringFnHack();
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawSolarObject(double x, double y, double diameter, String col) {
        synchronized (this) {
            try {
                Color colour = this.getColourFromStringFn.apply(col);
                this.things.add(solarObjectCtor.newInstance(this, (int) x, (int) y, (int) diameter, colour));
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics gr) {
        BufferedImage i = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = i.createGraphics();

        synchronized (this) {
            g.setColor(this.getBackground());
            try {
                g.clearRect(0, 0, this.getWidthHack(), this.getHeightHack());
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            for (var s : this.moreThings) {
                g.setColor(new Color(0x003300));
                g.draw(s);
            }

            try {
                for (var t : this.getThingsViewHack()) {
                    g.setColor(t.getColorHack());
                    g.fillOval(t.getXHack(), t.getYHack(), t.getDiameterHack(), t.getDiameterHack());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            gr.drawImage(i, 0, 0, this);
        }
    }

    public void drawExtra(Shape s) {
        synchronized (this) {
            this.moreThings.add(s);
        }
    }

    public void finishedDrawing() {
        this.repaint();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {
        }
        synchronized (this) {
            this.things.clear();
            this.moreThings.clear();
        }
    }

    private static int getPrivateIntFieldHack(Object instance, Class<?> klass, String name) throws NoSuchFieldException, IllegalAccessException {
        Field f = klass.getDeclaredField(name);
        f.setAccessible(true);
        return f.getInt(instance);
    }

    private static Object getPrivateFieldHack(Object instance, Class<?> klass, String name) throws NoSuchFieldException, IllegalAccessException {
        Field f = klass.getDeclaredField(name);
        f.setAccessible(true);
        return f.get(instance);
    }

    private static void setPrivateFieldHack(Object instance, Class<?> klass, String name, Object val) throws NoSuchFieldException, IllegalAccessException {
        Field f = klass.getDeclaredField(name);
        f.setAccessible(true);
        f.set(instance, val);
    }

    private int getHeightHack() throws IllegalAccessException, NoSuchFieldException {
        return getPrivateIntFieldHack(this, this.getClass().getSuperclass(), "height");
    }

    private int getWidthHack() throws IllegalAccessException, NoSuchFieldException {
        return getPrivateIntFieldHack(this, this.getClass().getSuperclass(), "width");
    }

    private Function<String, Color> getColourFromStringFnHack() throws NoSuchMethodException {
        Method m = this.getClass().getSuperclass().getDeclaredMethod("getColourFromString", String.class);
        m.setAccessible(true);
        return (s -> {
            try {
                return (Color) m.invoke(this, s);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Color.WHITE;
        });
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Object> getThingsHack() throws NoSuchFieldException, IllegalAccessException {
        return (ArrayList<Object>) getPrivateFieldHack(this, this.getClass().getSuperclass(), "things");
    }

    private ArrayList<SolarObjectHacked> getThingsViewHack() throws NoSuchFieldException, IllegalAccessException {
        return this.things.stream().map(SolarObjectHacked::new).collect(Collectors.toCollection(ArrayList::new));
    }

    private static class SolarObjectHacked {
        private final Object inner;

        SolarObjectHacked(Object inner) {
            this.inner = inner;
        }

        private int getXHack() throws IllegalAccessException, NoSuchFieldException {
            return getPrivateIntFieldHack(this.inner, this.inner.getClass(), "x");
        }

        private int getYHack() throws IllegalAccessException, NoSuchFieldException {
            return getPrivateIntFieldHack(this.inner, this.inner.getClass(), "y");
        }

        private int getDiameterHack() throws IllegalAccessException, NoSuchFieldException {
            return getPrivateIntFieldHack(this.inner, this.inner.getClass(), "diameter");
        }

        private Color getColorHack() throws IllegalAccessException, NoSuchFieldException {
            return (Color) getPrivateFieldHack(this.inner, this.inner.getClass(), "col");
        }
    }
}