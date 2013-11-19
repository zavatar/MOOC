public class SeamCarver {

 private Picture pic;
 private double[] energy; 

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public     int width() {
        return pic.width();
    }

    // height of current picture
    public     int height() {
        return pic.height();
    }
    
    private double gradient(java.awt.Color x, java.awt.Color y) {
        double r = x.getRed() - y.getRed();
        double g = x.getGreen() - y.getGreen();
        double b = x.getBlue() - y.getBlue();
        return r*r + g*g + b*b;
    }

    // energy of pixel at column x and row y
    public  double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IndexOutOfBoundsException();
        if (x == 0 || y == 0 || x == width()-1 || y == height()-1)
            return 195075;
        return gradient(pic.get(x-1, y), pic.get(x+1, y)) + gradient(pic.get(x, y-1), pic.get(x, y+1));
    }

    private void computeEnergy() {
        //double maxE = 0;
        energy = new double[width() * height()];
        for (int r = 0; r < height(); r++)
            for (int c = 0; c < width(); c++) {
                energy[r*width() + c] = energy(c, r);
                //maxE = Math.max(maxE, energy[r*width() + c]);
            }
        /*
        Picture energyPic = new Picture(width(), height());
        for (int r = 0; r < height(); r++)
            for (int c = 0; c < width(); c++) {
                float color = (float)(energy[r*width() + c] / maxE);
                energyPic.set(c, r, new java.awt.Color(color, color, color));
            }
        energyPic.show();
        */
    }

    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() {
        computeEnergy();
        int s = width() * height();
        int t = s+1;
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(t+1);
        for (int r = 0; r < height(); r++)
            g.addEdge(new DirectedEdge(s, r, energy[r*width()]));
        for (int r = 0; r < height(); r++)
            for (int c = 1; c < width(); c++) {
                int i = r*width() + c;
                int j = c*height() + r;
                if (r > 0) g.addEdge(new DirectedEdge(j-height()-1, j, energy[i]));
                g.addEdge(new DirectedEdge(j-height(), j, energy[i]));
                if (r < height()-1) g.addEdge(new DirectedEdge(j-height()+1, j, energy[i]));
            }
        for (int r = 0; r < height(); r++)
            g.addEdge(new DirectedEdge(s-1-r, t, 0));
        AcyclicSP asp = new AcyclicSP(g, s);
        int[] seam = new int[width()];
        int i = 0;
        for (DirectedEdge e : asp.pathTo(t))
            if (i < seam.length)
                seam[i++] = e.to() % height();
        return seam;
    }

    // sequence of indices for vertical seam
    public   int[] findVerticalSeam() {
        computeEnergy();
        int s = width() * height();
        int t = s+1;
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(t+1);
        for (int c = 0; c < width(); c++)
            g.addEdge(new DirectedEdge(s, c, energy[c]));
        for (int r = 1; r < height(); r++)
            for (int c = 0; c < width(); c++) {
                int i = r*width() + c;
                if (c > 0) g.addEdge(new DirectedEdge(i-width()-1, i, energy[i]));
                g.addEdge(new DirectedEdge(i-width(), i, energy[i]));
                if (c < width()-1) g.addEdge(new DirectedEdge(i-width()+1, i, energy[i]));
            }
        for (int c = 0; c < width(); c++)
            g.addEdge(new DirectedEdge(s-1-c, t, 0));
        AcyclicSP asp = new AcyclicSP(g, s);
        int[] seam = new int[height()];
        int i = 0;
        for (DirectedEdge e : asp.pathTo(t))
            if (i < seam.length)
                seam[i++] = e.to() % width();
        return seam;
    }

    // remove horizontal seam from picture
    public    void removeHorizontalSeam(int[] a) {
        if (height() <= 1)
            throw new IllegalArgumentException();
        if (a.length != width())
            throw new IllegalArgumentException("Wrong Length");

        Picture p = new Picture(width(), height()-1);
        //Picture seamPic = new Picture(pic);
        int prerow = a[0];
        for (int c = 0; c < width(); c++) {
            if (Math.abs(a[c] - prerow) > 1)
                throw new IllegalArgumentException("Non-valid seam");
            if (a[c] < 0 || a[c] >= height())
                throw new IndexOutOfBoundsException();
            //seamPic.set(c, a[c], java.awt.Color.red);
            prerow = a[c];
            for (int r = 0; r < height()-1; r++)
                if (r < a[c])
                    p.set(c, r, pic.get(c, r));
                else
                    p.set(c, r, pic.get(c, r+1));
        }
        pic = p;
        //seamPic.show();
    }

    // remove vertical seam from picture
    public    void removeVerticalSeam(int[] a) {
        if (width() <= 1)
            throw new IllegalArgumentException();
        if (a.length != height())
            throw new IllegalArgumentException("Wrong Length");

        Picture p = new Picture(width()-1, height());
        //Picture seamPic = new Picture(pic);
        int precol = a[0];
        for (int r = 0; r < height(); r++) {
            if (Math.abs(a[r] - precol) > 1)
                throw new IllegalArgumentException("Non-valid seam");
            if (a[r] < 0 || a[r] >= width())
                throw new IndexOutOfBoundsException();
            //seamPic.set(a[r], r, java.awt.Color.red);
            precol = a[r];
            for (int c = 0; c < width()-1; c++)
                if (c < a[r])
                    p.set(c, r, pic.get(c, r));
                else
                    p.set(c, r, pic.get(c+1, r));
        }
        pic = p;
        //seamPic.show();
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        picture.show();
        SeamCarver seamCarver = new SeamCarver(picture);
        for (int i = 0; i < 10; i++)
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        for (int i = 0; i < 10; i++)
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
    }
}