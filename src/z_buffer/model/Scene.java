package z_buffer.model;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Element> elements;
    /*private final List<Vertex> vb; // vertex buffer
    private final List<Integer> ib; // index buffer*/

    public Scene(){
        elements = new ArrayList<>();
       /* vb = new ArrayList<>();
        ib = new ArrayList<>();*/
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    /*public List<Vertex> getVb() {
        return vb;
    }

    public List<Integer> getIb() {
        return ib;
    }*/
}
