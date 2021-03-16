package z_buffer.renderer;

import transforms.Mat4;
import z_buffer.model.Element;
import z_buffer.model.Vertex;

import java.util.List;

public interface GPURenderer {

    void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb);

    void clear();

    void setModel(Mat4 model);

    void setView(Mat4 view);

    void setProjection(Mat4 projection);

}
