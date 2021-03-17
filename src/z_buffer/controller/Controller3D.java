package z_buffer.controller;

import transforms.*;
import z_buffer.model.Element;
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

    private boolean perspective = false;
    private boolean shiftIsPressed;

    private double height;
    private double width;

    private double xRot = 0;
    private double yRot = 0;
    private double zRot = 0;

    private double scale = 1;

    private double translZ;

    private final List<Element> elementBuffer;
    private final List<Vertex> vb; // vertex buffer
    private final List<Integer> ib; // index buffer

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.renderer = new RendererZBuffer(raster);

        elementBuffer = new ArrayList<>();
        vb = new ArrayList<>();
        ib = new ArrayList<>();

        initMatrices();
        initListeners(panel);
        initObjects();
    }

    private void initObjects() {
        vb.add(new Vertex(new Point3D(.5, .0, .9), new Col(255, 0, 0))); // 0 // nejvíce vlevo
        vb.add(new Vertex(new Point3D(.7, .7, .9), new Col(255, 120, 0))); // 1 // nejvíce dole
        vb.add(new Vertex(new Point3D(.0, .5, .3), new Col(255, 255, 0))); // 2 // společný
        /*Druhy troj zel*/
        vb.add(new Vertex(new Point3D(.3, .8, .5), new Col(0, 255, 0))); // 3 // nejvíce vpravo
        vb.add(new Vertex(new Point3D(.1, .2, 1), new Col(0, 255, 120))); // 4 // nejvíce nahoře
        vb.add(new Vertex(new Point3D(.7, .3, .2), new Col(0, 255, 255))); // 4 // nejvíce nahoře

        ib.add(0);
        ib.add(2);
        ib.add(1);
        /*druhy troj*/
        ib.add(3);
        ib.add(4);
        ib.add(5);

        /*AXES*/
        vb.add(new Vertex(new Point3D(0, 0, 0), new Col(255, 0, 0))); // 4 // RED
        vb.add(new Vertex(new Point3D(1, 0, 0), new Col(255, 0, 0))); // 4 // nejvíce nahoře
        ib.add(6);
        ib.add(7);

        vb.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 255, 0))); // 4 // GREEN
        vb.add(new Vertex(new Point3D(0, 1, 0), new Col(0, 255, 0))); // 4 // nejvíce nahoře
        ib.add(8);
        ib.add(9);

        vb.add(new Vertex(new Point3D(0, 0, 0), new Col(0, 0, 255))); // 4 // BLUE
        vb.add(new Vertex(new Point3D(0, 0, 1), new Col(0, 0, 255))); // 4 // nejvíce nahoře
        ib.add(10);
        ib.add(11);

        elementBuffer.add(new Element(TopologyType.TRIANGLE, 0, 6));
        elementBuffer.add(new Element(TopologyType.LINE, 6,2));
        elementBuffer.add(new Element(TopologyType.LINE, 8,2));
        elementBuffer.add(new Element(TopologyType.LINE, 10,2));
        display();


        height = panel.getHeight();
        width = panel.getWidth();

        panel.repaint();
    }

//    private void initObjects() {
//        vb.add(new Vertex(new Point3D(10, 10, 1), new Col(255, 0, 0))); // 0 // ten nejvíce vlevo
//        vb.add(new Vertex(new Point3D(100, 300, 1), new Col(255, 0, 0))); // 1 // ten nejvíce dole
//        vb.add(new Vertex(new Point3D(200, 50, 1), new Col(255, 0, 0))); // 2 // ten společný
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 3 // ten nejvíce nahoře
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 4 // ten nejvíce vpravo
//
////        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 5 // ten nahoře
////        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 6 // ten dole
////
////        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 7 // vpravo
////        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 8 // vlevo
//


//    private void initObjects2() {
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 0 // ten nejvíce vlevo
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 1 // ten nejvíce dole
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 2 // ten společný
//
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 3 // ten nahoře
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 4 // ten dole
//
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 5 // vpravo
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 6 // vlevo
//
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 7 // ten nejvíce nahoře
//        vb.add(new Vertex(new Point3D(1, 1, 1), new Col(255, 0, 0))); // 8 // ten nejvíce vpravo
//
//        ib.add(3);
//        ib.add(4);
//        // tyto 2 indexy tvoří 1 celou úsečku
//
//        ib.add(0);
//        ib.add(1);
//        ib.add(2);
//        // tyto 3 indexy tvoří 1 celý trojúhleník
//
//        ib.add(2);
//        ib.add(7);
//        ib.add(8);
//        // tyto 3 indexy tvoří 1 celý trojúhleník
//
//        ib.add(5);
//        ib.add(6);
//        // tyto 2 indexy tvoří 1 celou úsečku
//
//        elements.add(new Element(ElementType.LINE, 0, 2));
//        elements.add(new Element(ElementType.TRIANGLE, 2, 6));
//        elements.add(new Element(ElementType.LINE, 8, 2));
//    }

    private void initMatrices() {
        model = new Mat4Identity();

        var e = new Vec3D(0, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-20));

        projection = new Mat4PerspRH(
                Math.PI / 3,
                raster.getHeight() / (float) raster.getWidth(),
                0.5,
                50
        );
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
                camera = camera.addAzimuth((Math.PI*(e.getX()-oldX))/width);
                camera = camera.addZenith((Math.PI*(e.getY()-oldY))/height);

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

                /*SET PROJECTION (TZO)*/
                if(e.getKeyCode() == KeyEvent.VK_O)
                    perspective = false;
                if (e.getKeyCode() == KeyEvent.VK_P)
                    perspective = true;

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
                    case KeyEvent.VK_W -> camera = camera.forward(0.1);
                    case KeyEvent.VK_A -> camera = camera.left(0.1);
                    case KeyEvent.VK_S -> camera = camera.backward(0.1);
                    case KeyEvent.VK_D -> camera = camera.right(0.1);
                }

                /*ROTATION*/
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_X -> xRot += 0.1;
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
                //cube.setModel(new Mat4Transl(2,0.25,0));        /*PO TRANSFORMACI SE PREMISTI NA PUVODNI MISTO*/
                //crystal.setModel(new Mat4Transl(0.25,2,0));
            }
        });
//
//        panel.addMouseMotionListener(drag);
//        panel.removeMouseMotionListener(drag);
    }

    private synchronized void display() {
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        renderer.draw(elementBuffer, ib, vb);

        panel.repaint();
    }

}
