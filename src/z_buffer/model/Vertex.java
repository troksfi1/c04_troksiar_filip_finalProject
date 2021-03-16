package z_buffer.model;

import transforms.Col;
import transforms.Point3D;

import java.util.Optional;

public class Vertex {

    private final Point3D point;
    private final Col color;
//    public final double x, y, z, w;

    public Vertex(Point3D point, Col color) {
        this.point = point;
        this.color = color;
//        x = point.getX();
//        y = point.getY();
//        z = point.getZ();
//        w = point.getW();
    }

    public Vertex mul(double t) {
        return new Vertex(point.mul(t), color.mul(t));
    }

    public Vertex add(Vertex other) {
        return new Vertex(point.add(other.getPoint()), color.add(other.getColor()));
    }

    public Optional<Vertex> dehomog() {
//        Optional<Vec3D> dehomog = point.dehomog();
//        if (dehomog.isPresent()) {
//            Vertex newVertex = new Vertex(new Point3D(dehomog.get()), color);
//            return Optional.of(newVertex);
//        } else {
//            return Optional.empty();
//        }
        return point.dehomog().map(vec3D -> new Vertex(new Point3D(vec3D), color));
    }

    public Point3D getPoint() {
        return point;
    }

    public Col getColor() {
        return color;
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public double getZ() {
        return point.getZ();
    }

    public double getW() {
        return point.getW();
    }

}
