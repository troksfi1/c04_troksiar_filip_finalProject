package z_buffer.elements;

import transforms.Col;
import transforms.Point3D;
import z_buffer.model.Element;
import z_buffer.model.TopologyType;
import z_buffer.model.Vertex;

public class Line extends Element {

    public Line() {
        super(TopologyType.LINE, 6, 8);

        /*AXES*/
        vb.add(new Vertex(new Point3D(0, 0, 0), new Col(255, 0, 0))); // RED
        vb.add(new Vertex(new Point3D(10, 0, 0), new Col(255, 0, 0)));
        ib.add(6);
        ib.add(7);

        vb.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 255, 0))); // GREEN
        vb.add(new Vertex(new Point3D(0, 10, 0), new Col(0, 255, 0)));
        ib.add(8);
        ib.add(9);

        vb.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 0, 255))); //BLUE
        vb.add(new Vertex(new Point3D(0, 0, 10), new Col(0, 0, 255)));
        ib.add(10);
        ib.add(11);

        vb.add(new Vertex(new Point3D(0, 0, 10), new Col(0, 0, 255)));
        vb.add(new Vertex(new Point3D(0, 5, 10), new Col(0, 0, 255)));
        ib.add(12);
        ib.add(13);

        /*vb.add(new Vertex(new Point3D(10, 0, 0), new Col(255, 0, 0)));
        vb.add(new Vertex(new Point3D(0, 10, 0), new Col(0, 255, 0)));
        ib.add(12);
        ib.add(13);

        vb.add(new Vertex(new Point3D(0, 0, 10), new Col(0, 0, 255)));
        vb.add(new Vertex(new Point3D(0, 10, 0), new Col(0, 255, 0)));
        ib.add(14);
        ib.add(15);*/
    }
}
