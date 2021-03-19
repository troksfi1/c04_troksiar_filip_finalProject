package z_buffer.elements;

import transforms.Col;
import transforms.Point3D;
import z_buffer.model.Element;
import z_buffer.model.TopologyType;
import z_buffer.model.Vertex;

public class Triangle extends Element {
    public Triangle() {
        super(TopologyType.TRIANGLE, 0,6);

        setColor(new Col(255,0,0));

        vb.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0))); // 0 // nejvíce vlevo
        vb.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0))); // 1 // nejvíce dole
        vb.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0))); // 2 // společný
        /*Druhy troj zel*/
        vb.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0))); // 3 // nejvíce vpravo
        vb.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120))); // 4 // nejvíce nahoře
        vb.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255))); // 5 // nejvíce nahoře

        ib.add(0);
        ib.add(2);
        ib.add(1);
        /*druhy troj*/
        ib.add(3);
        ib.add(4);
        ib.add(5);
    }
}
