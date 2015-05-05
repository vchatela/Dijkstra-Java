package base;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by valentin on 05/05/15.
 */
public class Graphique extends JPanel {
    private String titre;
    private String ordonnee;
    private String abscisse;
    private ArrayList<Float> valeurs;
    private ArrayList<String> series;
    private ArrayList<String> categories;
    private boolean legende;
    private Color couleurFond;
    private Color[] couleursBarres = {Color.cyan.darker(),
            Color.red, Color.green, Color.cyan, Color.magenta,
            Color.yellow, Color.pink, Color.darkGray, Color.orange};

    public Graphique(String titre, String abscisse, String ordonnee, ArrayList<Float> valeurs, Color fond, ArrayList<String> listeSeries, ArrayList<String> listeCategory, boolean legende) {
        super(new GridLayout(1, 0));
        this.titre = titre;
        this.ordonnee = ordonnee;
        this.abscisse = abscisse;
        this.valeurs = valeurs;
        this.series = listeSeries;
        this.categories = listeCategory;
        this.legende = legende;
        this.couleurFond = fond;
        initialiser();
    }

    private void initialiser() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int k = 0;
        for (int j = 0; j < categories.size(); j++) {
            for (int i = 0; i < series.size(); i++) {
                dataset.addValue(valeurs.get(k), series.get(i), categories.get(j));
                k++;
            }
        }
        JFreeChart chart = ChartFactory.createBarChart(
                titre,                    // chart title
                abscisse,                    // domain axis label
                ordonnee,                // range axis label
                dataset,                    // data
                PlotOrientation.VERTICAL,    // orientation
                legende,                    // include legend
                true,                        // tooltips
                false                        // URL
        );

        // definition de la couleur de fond
        chart.setBackgroundPaint(couleurFond);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        //valeur comprise entre 0 et 1 transparence de la zone graphique
        plot.setBackgroundAlpha(0.9f);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // pour la couleur des barres pour chaque serie

        for (int s = 0; s < series.size(); s++) {
            GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, couleursBarres[s],
                    0.0f, 0.0f, new Color(0, 40, 70));
            renderer.setSeriesPaint(s, gp0);

        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        add(chartPanel);
    }


}
