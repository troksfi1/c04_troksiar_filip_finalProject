package z_buffer.controller;

import transforms.*;
import z_buffer.elements.Line;
import z_buffer.elements.Triangle;
import z_buffer.model.Element;
import z_buffer.model.Scene;
import z_buffer.model.TopologyType;
import z_buffer.model.Vertex;
import z_buffer.rasterize.Raster;
import z_buffer.renderer.GPURenderer;
import z_buffer.renderer.RendererZBuffer;
import z_buffer.view.Panel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final GPURenderer renderer;
    private final Panel panel;
    private final Raster<Integer> raster;

    private int oldX;
    private int oldY;

    private boolean shiftIsPressed;

    private double height;
    private double width;

    private double xRot = 0;
    private double yRot = 0;
    private double zRot = 0;

    private double scale = 1;

    private double translZ;

    private Scene scene;

    /*private final List<Element> elementBuffer;
    private final List<Vertex> vb; // vertex buffer
    private final List<Integer> ib; // index buffer*/

    /**SOLIDS**/
    private Line line;
    private Triangle triangle;

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.renderer = new RendererZBuffer(raster);

        height = panel.getHeight();
        width = panel.getWidth();

        initMatrices();
        initListeners(panel);
        initObjects();
    }

    private void initObjects() {
        scene = new Scene();

        scene.addElement(new Triangle());
        scene.addElement(new Line());


        /*scene.addElement(new Element(TopologyType.TRIANGLE, 0, 6));
        scene.addElement(new Element(TopologyType.LINE, 6,2));
        scene.addElement(new Element(TopologyType.LINE, 8,2));
        scene.addElement(new Element(TopologyType.LINE, 10,2));*/

        //scene.addElement(new Element(TopologyType.LINE, 12,2));
        //scene.addElement(new Element(TopologyType.LINE, 14,2));
        display();
        panel.repaint();
    }

    private void initMatrices() {
        model = new Mat4Identity();

        camera = new Camera().addAzimuth(-2.5).addZenith(-0.15).withPosition(new Vec3D(5,5,2));
        /*var e = new Vec3D(0, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-20));*/

        isPerspective(true);
    }

    private void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();

                display();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera = camera.addAzimuth((Math.PI/4*(e.getX()-oldX))/width);
                camera = camera.addZenith((Math.PI/4*(e.getY()-oldY))/height);

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

                /*SET PROJECTION (TZO)*//*
                if(e.getKeyCode() == KeyEvent.VK_P) {             //TODO DUBLI
                    projection = new Mat4OrthoRH(
                            Math.PI / 3,
                            raster.getHeight() / (float) raster.getWidth(),
                            0.5,
                            50
                    );
                }
                else if (e.getKeyCode() == KeyEvent.VK_O) {
                    projection = new Mat4OrthoRH(8,8,0.1,10);
                }*/


                /*SELECTED SOLID (NEXT)
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && selectedSolid < scene.getSolids().size() - 1) {
                    scene.getSolids().get(selectedSolid).setColor(temColor);
                    selectedSolid++;
                    temColor = scene.getSolids().get(selectedSolid).getColor();
                    scene.getSolids().get(selectedSolid).setColor(Color.YELLOW);
                }

                SELECTED SOLID (PREVIOUS)
                if (e.getKeyCode() == KeyEvent.VK_LEFT && selectedSolid > 1) {
                    scene.getSolids().get(selectedSolid).setColor(temColor);
                    selectedSolid--;
                    temColor = scene.getSolids().get(selectedSolid).getColor();
                    scene.getSolids().get(selectedSolid).setColor(Color.YELLOW);
                }*/

                /*CAMERA*/
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P -> isPerspective(true);
                    case KeyEvent.VK_O -> isPerspective(false);

                    case KeyEvent.VK_W -> camera = camera.forward(0.1);
                    case KeyEvent.VK_A -> camera = camera.left(0.1);
                    case KeyEvent.VK_S -> camera = camera.backward(0.1);
                    case KeyEvent.VK_D -> camera = camera.right(0.1);

                    case KeyEvent.VK_X -> xRot += 0.1;
                    case KeyEvent.VK_Y -> yRot += 0.1;
                    case KeyEvent.VK_C -> zRot += 0.1;

                    case KeyEvent.VK_UP -> translZ += 0.1;
                    case KeyEvent.VK_DOWN -> translZ -= 0.1;

                    case KeyEvent.VK_HOME -> scale += 0.1;
                    case KeyEvent.VK_END -> scale -= 0.1;
                }

                /*ROTATION*/
                switch (e.getKeyCode()) {

                }

                /*TRANSL*/
                switch (e.getKeyCode()) {

                }

                /*SCALE*/
                switch (e.getKeyCode()) {

                }

                if(e.isShiftDown()){
                    shiftIsPressed = true;
                }
                shiftIsPressed = false;

                display();
                //cube.setModel(new Mat4Transl(2,0.25,0));        /*PO TRANSFORMACI SE PREMISTI NA PUVODNI MISTO*/
                //crystal.setModel(new Mat4Transl(0.25,2,0));
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
            projection = new Mat4OrthoRH(width/height,height/width,0.5,10);
    }

    private synchronized void display() {
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        //renderer.draw(elementBuffer, ib, vb);


        renderer.draw(scene.getElements(), Element.getIb(), Element.getVb());

        panel.repaint();
    }
}
