import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Gradient extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = new Color(54, 52, 52);
        //Color color2 = new Color(187, 201, 187);
        Color color2 = new Color(40, 38, 38);
        GradientPaint gp = new GradientPaint(0, 100, color1, 100, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h); // x coordinate, y coordinate, width, height
    }

}