package final_project.model;

import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.List;

public class Element {

    private final TopologyType topologyType;
    private final int start;
    private final int count;
    private Mat4 model = new Mat4Identity();

    protected static final List<Vertex> vb = new ArrayList<>(); // vertex buffer
    protected static final List<Integer> ib = new ArrayList<>(); // index buffer

    public Element(TopologyType topologyType, int start, int count) {
        this.topologyType = topologyType;
        this.start = start;
        this.count = count;
    }

    public TopologyType getTopologyType() {
        return topologyType;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }

    public static List<Vertex> getVb() {
        return vb;
    }

    public static List<Integer> getIb() {
        return ib;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }
}
