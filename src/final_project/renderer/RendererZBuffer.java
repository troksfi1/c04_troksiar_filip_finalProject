package final_project.renderer;

import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import final_project.model.Element;
import final_project.model.TopologyType;
import final_project.model.Vertex;
import final_project.rasterize.DepthBuffer;
import final_project.rasterize.Raster;

import java.util.List;
import java.util.Optional;

public class RendererZBuffer implements GPURenderer {

    private final Raster<Integer> raster;
    private final DepthBuffer depthBuffer;

    private Mat4 model, view, projection;
    boolean wireModelSelected;

    public RendererZBuffer(Raster<Integer> raster) {
        this.raster = raster;
        depthBuffer = new DepthBuffer(raster.getWidth(), raster.getHeight());
    }

    @Override
    public void draw(List<Element> elements, List<Integer> ib, List<Vertex> vb) {
        for (Element element : elements) {
            final TopologyType type = element.getTopologyType();
            final int start = element.getStart();
            final int count = element.getCount();

            if (type == TopologyType.TRIANGLE) {
                for (int i = start; i < start + count; i += 3) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Integer i3 = ib.get(i + 2);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    final Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3, element.getModel());
                }

            } else if (type == TopologyType.LINE) {
                for (int i = start; i < start + count; i += 2) {
                    final Integer i1 = ib.get(i);
                    final Integer i2 = ib.get(i + 1);
                    final Vertex v1 = vb.get(i1);
                    final Vertex v2 = vb.get(i2);
                    prepareLine(v1, v2);
                }
            } else if (type == TopologyType.POINT) {
                for (int i = start; i < start + count; i++) {
                    final Integer i1 = ib.get(i);
                    final Vertex v1 = vb.get(i1);
                    drawPixel((int) Math.round(v1.getX()), (int) Math.round(v1.getY()), v1.getZ(), v1.getColor());   //TODO WITHOUT TRIM
                }
            }
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3, Mat4 model) {

        // 1. transformace vrcholů
        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());
        Vertex c = new Vertex(v3.getPoint().mul(model).mul(view).mul(projection), v3.getColor());

        // 2. ořezání
        if ((a.x > a.w && b.x > b.w && c.x > c.w) || (a.x < -a.w && b.x < -b.w && c.x < -c.w) ||
                (a.y > a.w && b.y > b.w && c.x > c.w) || (a.y < -a.w && b.y < -b.w && c.y < -c.w) ||
                (a.z > a.w && b.z > b.w && c.z > c.w) || (a.z < 0 && b.z < 0 && c.z < 0)) return;
        // ořezat trojúhelníky, které jsou CELÉ mimo zobrazovací objem

        // 3. seřazení vrcholů podle Z (a.z > b.z > c.z)
        // slide 97
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        // teď je v C vrchol, jehož Z je k nám nejblíže (je nejmenší, může být i za námi)
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        // teď máme seřazeno - Z od největšího po nejmenší: A,B,C

        // 4. ořezání podle Z
        if (a.getZ() < 0) {
            return;
            // A.Z je menší než nula => všechny Z jsou menší než nula => není co zobrazit (vše je za námi)
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrcholy B,C nejsou

            // víme, že va.z má být 0

            // odečíst minimum, dělit rozsahem
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože nový vrchol má mít Z souřadnici 0

            Vertex mulA = a.mul(t1);
            Vertex mulB = b.mul(1 - t1);
            Vertex va = mulA.add(mulB);

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex vb = a.mul(t2).add(c.mul(1 - t2));

            drawTriangle(a, va, vb);

        } else if (c.getZ() < 0) {
            // vrcholy A,B jsou vidět, C není

            double t1 = (0 - b.getZ()) / (c.getZ() - b.getZ());
            Vertex bc = b.mul(t1).add(c.mul(1 - t1));
            drawTriangle(a, b, bc);

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(t2).add(c.mul(1 - t2));
            drawTriangle(a, bc, ac);

        } else {
            // vidíme celý trojúhelník (podle Z)
            drawTriangle(a, b, c);
        }
    }

    private void prepareLine(Vertex v1, Vertex v2) {
        // 1. transformace vrcholů
        Vertex a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
        Vertex b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());

        if ((a.x > a.w && b.x > b.w) || (a.x < -a.w && b.x < -b.w) ||
                (a.y > a.w && b.y > b.w) || (a.y < -a.w && b.y < -b.w) ||
                (a.z > a.w && b.z > b.w) || (a.z < 0 && b.z < 0)) return;

        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        // 4. ořezání podle Z
        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());

            Vertex mulA = a.mul(t1);
            Vertex mulB = b.mul(1 - t1);
            Vertex va = mulA.add(mulB);

            drawLine(a, va);

        } else {
            drawLine(a, b);
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        // 1. dehomogenizace
        Optional<Vertex> o1 = a.dehomog();
        Optional<Vertex> o2 = b.dehomog();
        Optional<Vertex> o3 = c.dehomog();

        // zahodit trojúhleník, pokud některý vrchol má w==0
        if (o1.isEmpty() || o2.isEmpty() || o3.isEmpty()) return;

        a = o1.get();
        b = o2.get();
        c = o3.get();

        // 2. transformace do okna
        a = transformToWindow(a);
        b = transformToWindow(b);
        c = transformToWindow(c);

        // 3. seřazení podle Y
        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getY() > c.getY()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        // 4. interpolace podle Y
        // slide 125
        // z A do B
        long start = (long) Math.max(Math.ceil(a.getY()), 0);
        long end = (long) Math.min(b.getY(), raster.getHeight() - 1.);
        for (long y = start; y <= end; y++) {

            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));

            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            if (wireModelSelected) {
                fillLine(y, ab, ac);
            } else {
                drawPixel((int) Math.round(ab.getX()), (int) Math.round(ab.getY()), ab.getZ(), ab.getColor());
                drawPixel((int) Math.round(ac.getX()), (int) Math.round(ac.getY()), ac.getZ(), ac.getColor());
            }
        }

        // z B do C
        long start1 = (long) Math.max(Math.ceil(b.getY()), 0);
        long end1 = (long) Math.min(c.getY(), raster.getHeight() - 1.);
        for (long y = start1; y <= end1; y++) {

            double t1 = (y - b.getY()) / (c.getY() - b.getY());
            Vertex bc = b.mul(1 - t1).add(c.mul(t1));

            double t2 = (y - a.getY()) / (c.getY() - a.getY());
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));


            if (wireModelSelected) {
                fillLine(y, bc, ac);
            } else {
                drawPixel((int) Math.round(bc.getX()), (int) Math.round(bc.getY()), bc.getZ(), bc.getColor());
                drawPixel((int) Math.round(ac.getX()), (int) Math.round(ac.getY()), ac.getZ(), ac.getColor());
            }
        }
    }

    private void drawLine(Vertex a, Vertex b) {

        // 1. dehomogenizace
        Optional<Vertex> o1 = a.dehomog();
        Optional<Vertex> o2 = b.dehomog();

        // zahodit trojúhleník, pokud některý vrchol má w==0
        if (o1.isEmpty() || o2.isEmpty()) return;

        a = o1.get();
        b = o2.get();

        // 2. transformace do okna
        a = transformToWindow(a);
        b = transformToWindow(b);

        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        long start = (long) Math.max(Math.ceil(a.getY()), 0);
        long end = (long) Math.min(b.getY(), raster.getHeight() - 1.);

        for (long y = start; y <= end; y++) {

            double t1 = (y - a.getY()) / (b.getY() - a.getY());
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));

            drawPixel((int) Math.round(ab.getX()), (int) Math.round(ab.getY()), ab.getZ(), ab.getColor());
        }
    }

    private void fillLine(long y, Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        int start = (int) Math.max(Math.ceil(a.getX()), 0);
        double end = Math.min(b.getX(), raster.getWidth() - 1.);

        for (int x = start; x <= end; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vertex finalVertex = a.mul(1 - t).add(b.mul(t));

            drawPixel(x, (int) y, finalVertex.getZ(), finalVertex.getColor());
        }
    }

    private void drawPixel(int x, int y, double z, Col color) {
        Optional<Double> zOptional = depthBuffer.getElement(x, y);
        if (zOptional.isPresent() && zOptional.get() > z) {
            depthBuffer.setElement(x, y, z);
            raster.setElement(x, y, color.getRGB());
        }
    }

    private Vertex transformToWindow(Vertex vertex) {
        Vec3D vec3D = new Vec3D(vertex.getPoint())
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed a my chceme, aby bylo vlevo nahoře
                .mul(new Vec3D(raster.getWidth() / 2f, raster.getHeight() / 2f, 1));
        return new Vertex(new Point3D(vec3D), vertex.getColor());
    }

    @Override
    public void clear() {
        // TODO
        raster.clear();
        depthBuffer.clear();
    }

    @Override
    public void setModel(Mat4 model) {
        // TODO
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        // TODO
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        // TODO
        this.projection = projection;
    }

    @Override
    public void wireModel(Boolean wireModelSelected) {
        this.wireModelSelected = wireModelSelected;
    }
}
