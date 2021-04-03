package final_project.elements;

import transforms.Col;
import transforms.Point3D;
import final_project.model.Element;
import final_project.model.TopologyType;
import final_project.model.Vertex;

public class Arrow extends Element {

    public Arrow() {
        super(TopologyType.LINE, 38, 12);

        final Col red = new Col(255, 0, 0);
        final Col green = new Col(0, 255, 0);
        final Col blue = new Col(0, 0, 255);

        vb.add(new Vertex(new Point3D(5, 0, 0), red));
        vb.add(new Vertex(new Point3D(4.9, 0, 0.1), red));      //ARROW BLUE
        vb.add(new Vertex(new Point3D(4.9, 0, -0.1), red));
        ib.add(20);//38
        ib.add(21);
        ib.add(20);
        ib.add(22);

        vb.add(new Vertex(new Point3D(0, 5, 0), green));
        vb.add(new Vertex(new Point3D(0, 4.9, 0.1), green));      //ARROW GREEN
        vb.add(new Vertex(new Point3D(0, 4.9, -0.1), green));
        ib.add(23);
        ib.add(24);
        ib.add(23);
        ib.add(25);

        vb.add(new Vertex(new Point3D(0, 0, 5), blue));
        vb.add(new Vertex(new Point3D(0, 0.1, 4.9), blue));      //ARROW BLUE
        vb.add(new Vertex(new Point3D(0, -0.1, 4.9), blue));
        ib.add(26);
        ib.add(27);
        ib.add(26);
        ib.add(28);
    }
}
