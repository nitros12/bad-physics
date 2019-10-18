package somephysicsthing.solarsystem;

class Tuple2<T, U> {
    final T left;
    final U right;

    Tuple2(T left, U right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "left=" + this.left +
                ", right=" + this.right +
                '}';
    }
}
