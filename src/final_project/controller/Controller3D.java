package final_project.controller;

import transforms.*;
import final_project.elements.Arrow;
import final_project.elements.Crystal;
import final_project.elements.Axis;
import final_project.elements.Triangle;
import final_project.model.Element;
import final_project.model.Scene;
import final_project.rasterize.Raster;
import final_project.renderer.GPURenderer;
import final_project.renderer.RendererZBuffer;
import final_project.view.Panel;

import java.awt.event.*;

public class Controller3D {

    private final GPURenderer renderer;
    private final Panel panel;

    private int oldX;
    private int oldY;

    private boolean shiftIsPressed;

    private final double height;
    private final double width;

    private Mat4 temModel;

    private double xRot = 0;
    private double yRot = 0;
    private double zRot = 0;

    private double scale = 1;

    private int selectedElement = 0;

    private double translZ;

    private Scene scene;

    private Triangle triangle;
    private Crystal crystal;

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        Raster<Integer> raster = panel.getRaster();
        this.renderer = new RendererZBuffer(raster);

        height = panel.getHeight();
        width = panel.getWidth();

        initMatrices();
        initListeners(panel);
        initObjects();
    }

    private void initObjects() {
        scene = new Scene();

        /*SOLIDS*/
        Axis axis = new Axis();
        triangle = new Triangle();
        crystal = new Crystal();
        Arrow arrow = new Arrow();


        scene.addElement(axis);
        scene.addElement(triangle);
        scene.addElement(crystal);
        scene.addElement(arrow);

        axis.setModel(new Mat4Identity());
        triangle.setModel(new Mat4Transl(1,.25,0));
        crystal.setModel(new Mat4Transl(.25,1,0));

        temModel = scene.getElements().get(selectedElement).getModel();

        display();
        panel.repaint();
    }

    private void initMatrices() {
        model = new Mat4Identity();
        camera = new Camera().addAzimuth(-2.5).addZenith(-0.15).withPosition(new Vec3D(5,5,2));
        isPerspective(false);
    }

    private void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera = camera.addAzimuth((Math.PI/2*(e.getX()-oldX))/width);
                camera = camera.addZenith((Math.PI/2*(e.getY()-oldY))/height);

                oldX = e.getX();
                oldY = e.getY();
                display();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                panel.requestFocus();
                panel.requestFocusInWindow();


                /*SELECTED SOLID (NEXT)*/
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && selectedElement < scene.getElements().size() - 1) {
                    scene.getElements().get(selectedElement).setModel(temModel);
                    selectedElement++;
                    temModel = scene.getElements().get(selectedElement).getModel();
                    //scene.getElements().get(selectedElement).getModel().mul(new Mat4Scale(2,2,2));
                }

                /*SELECTED SOLID (PREVIOUS)*/
                if (e.getKeyCode() == KeyEvent.VK_LEFT && selectedElement > 1) {
                    scene.getElements().get(selectedElement).setModel(temModel);
                    selectedElement--;
                    temModel = scene.getElements().get(selectedElement).getModel();
                    //scene.getElements().get(selectedElement).getModel().mul(new Mat4Scale(2,2,2));
                }

                /*CAMERA*/
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P -> isPerspective(true);
                    case KeyEvent.VK_O -> isPerspective(false);

                    case KeyEvent.VK_W -> camera = camera.forward(0.1);
                    case KeyEvent.VK_A -> camera = camera.left(0.1);
                    case KeyEvent.VK_S -> camera = camera.backward(0.1);
                    case KeyEvent.VK_D -> camera = camera.right(0.1);

                    case KeyEvent.VK_L -> renderer.wireModel(true);
                    case KeyEvent.VK_K -> renderer.wireModel(false);
                }

                /*ROTATION*/
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_X -> xRot += 0.1;          //UKALADAT DO PROMENNE JE KRAVINA
                    case KeyEvent.VK_Y -> yRot += 0.1;
                    case KeyEvent.VK_C -> zRot += 0.1;

                }

                /*TRANSL*/
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> translZ += 0.1;
                    case KeyEvent.VK_DOWN -> translZ -= 0.1;

                }

                /*SCALE*/
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_HOME -> scale += 0.1;
                    case KeyEvent.VK_END -> scale -= 0.1;
                }

                if(e.isShiftDown()){
                    shiftIsPressed = true;
                    display();
                }
                shiftIsPressed = false;

                display();
                triangle.setModel(new Mat4Transl(1,0.25,0));        /*PO TRANSFORMACI SE PREMISTI NA PUVODNI MISTO*/
                crystal.setModel(new Mat4Transl(0.25,1,0));
            }
        });
    }

    private void isPerspective(boolean perspective) {
        if(perspective)
            projection = new Mat4PerspRH(
                    Math.PI / 3,
                    height / width,
                    0.5,
                    50
            );
        else
            projection = new Mat4OrthoRH((width/height)*8,(height/width)*8,0.5,100);
    }

    private synchronized void display() {
        renderer.clear();

        if(shiftIsPressed) {   //ALL ELEMENTS ARE SELECTED
            for (int i = 1; i < scene.getElements().size();i++) {
                Mat4 model2 = scene.getElements().get(i).getModel();

                scene.getElements().get(i).setModel(new Mat4RotXYZ(xRot*Math.PI/3, yRot*Math.PI/4, zRot*Math.PI/5)
                        .mul(new Mat4Transl(2,2,translZ))
                        .mul(new Mat4Scale(scale, scale, scale)));
                scene.getElements().get(i).setModel(model2);
            }
        } else if(selectedElement != 0) {    //ONE ELEMENT IS SELECTED
            scene.getElements().get(selectedElement).setModel(new Mat4RotXYZ(xRot*Math.PI/3, yRot*Math.PI/4, zRot*Math.PI/5)
                    .mul(new Mat4Transl(2,2,translZ))
                    .mul(new Mat4Scale(scale, scale, scale)));
        }

        renderer.setModel(model);                   //MODEL
        renderer.setView(camera.getViewMatrix());   //CAMERA
        renderer.setProjection(projection);         //TZO

        renderer.draw(scene.getElements(), Element.getIb(), Element.getVb());

        panel.repaint();
    }
}
