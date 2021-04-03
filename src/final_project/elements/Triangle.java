package final_project.elements;

import transforms.Col;
import transforms.Point3D;
import final_project.model.Element;
import final_project.model.TopologyType;
import final_project.model.Vertex;

public class Triangle extends Element {

    public Triangle() {
        super(TopologyType.TRIANGLE, 8, 6);

        vb.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0)));
        vb.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0)));
        vb.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0)));

        vb.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0)));
        vb.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120)));
        vb.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255)));

        ib.add(8);
        ib.add(9);
        ib.add(10);

        ib.add(11);
        ib.add(12);
        ib.add(13);
    }
}
