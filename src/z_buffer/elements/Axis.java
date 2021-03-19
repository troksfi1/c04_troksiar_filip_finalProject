package z_buffer.elements;

import transforms.Col;
import transforms.Point3D;
import z_buffer.model.Element;
import z_buffer.model.TopologyType;
import z_buffer.model.Vertex;

public class Axis extends Element {

    public Axis() {
        super(TopologyType.LINE, 0, 8);

        final Col red = new Col(255, 0, 0);
        final Col green = new Col(0, 255, 0);
        final Col blue = new Col(0, 0, 255);

        /*AXES*/
        vb.add(new Vertex(new Point3D(0, 0, 0), red)); // RED
        vb.add(new Vertex(new Point3D(5, 0, 0), red));
        ib.add(0);
        ib.add(1);

        vb.add(new Vertex(new Point3D(0, 0, 0), green)); // GREEN
        vb.add(new Vertex(new Point3D(0, 5, 0), green));
        ib.add(2);
        ib.add(3);

        vb.add(new Vertex(new Point3D(0, 0, 0), blue)); //BLUE
        vb.add(new Vertex(new Point3D(0, 0, 5), blue));
        ib.add(4);
        ib.add(5);

        vb.add(new Vertex(new Point3D(4.9, 0, 0.1), red));      //ARROW DIFFERENT TYPE
        vb.add(new Vertex(new Point3D(4.9, 0, -0.1), red));
        ib.add(6);
        ib.add(7);
    }
}
