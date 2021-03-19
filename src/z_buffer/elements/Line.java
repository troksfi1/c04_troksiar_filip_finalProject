package z_buffer.elements;

import transforms.Col;
import transforms.Point3D;
import z_buffer.model.Element;
import z_buffer.model.TopologyType;
import z_buffer.model.Vertex;

public class Line extends Element {

    public Line() {
        super(TopologyType.LINE, 6, 8);

        Col red = new Col(255, 0, 0);
        Col green = new Col(0, 255, 0);
        Col blue = new Col(0, 0, 255);

        /*AXES*/
        vb.add(new Vertex(new Point3D(0, 0, 0), red)); // RED
        vb.add(new Vertex(new Point3D(10, 0, 0), red));
        ib.add(6);
        ib.add(7);

        vb.add(new Vertex(new Point3D(0, 0, 0), green)); // GREEN
        vb.add(new Vertex(new Point3D(0, 10, 0), green));
        ib.add(8);
        ib.add(9);

        vb.add(new Vertex(new Point3D(0, 0, 0), blue)); //BLUE
        vb.add(new Vertex(new Point3D(0, 0, 10), blue));
        ib.add(10);
        ib.add(11);

        vb.add(new Vertex(new Point3D(0, 0, 10), blue));
        vb.add(new Vertex(new Point3D(0, 5, 10), blue));
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
