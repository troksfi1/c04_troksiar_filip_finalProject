package final_project.model;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Element> elements;

    public Scene(){
        elements = new ArrayList<>();
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
}
