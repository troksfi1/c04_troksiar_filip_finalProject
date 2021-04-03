package final_project.elements;

import transforms.Col;
import transforms.Point3D;
import final_project.model.Element;
import final_project.model.TopologyType;
import final_project.model.Vertex;

public class Crystal extends Element {

    public Crystal() {
        super(TopologyType.TRIANGLE, 14, 24);

        final Col yellow = new Col(255, 255, 0);
        final Col yy = new Col(0, 255, 200);

        vb.add(new Vertex(new Point3D(0, 0, 0), yy)); // 14 // nejvíce vlevo
        vb.add(new Vertex(new Point3D(0, 1, 0), yy)); // 15 // nejvíce dole
        vb.add(new Vertex(new Point3D(1, 1, 0), yy)); // 16 // společný
        vb.add(new Vertex(new Point3D(1, 0, 0), yy)); // 17 // společný

        vb.add(new Vertex(new Point3D(0.5, 0.5, 1), yellow)); // 18 // společný
        vb.add(new Vertex(new Point3D(0.5, 0.5, -1), yellow)); // 19 // společný

        ib.add(14);
        ib.add(15);
        ib.add(18);

        ib.add(15);
        ib.add(16);
        ib.add(18);

        ib.add(16);
        ib.add(17);
        ib.add(18);

        ib.add(17);
        ib.add(14);
        ib.add(18);

        ib.add(14);
        ib.add(15);
        ib.add(19);

        ib.add(15);
        ib.add(16);
        ib.add(19);

        ib.add(16);
        ib.add(17);
        ib.add(19);

        ib.add(17);
        ib.add(14);
        ib.add(19);
    }
}
