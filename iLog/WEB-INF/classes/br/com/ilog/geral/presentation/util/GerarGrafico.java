package br.com.ilog.geral.presentation.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class GerarGrafico {
	
	// Criar grafico no jfreechart
	public static StreamedContent gerarGrafico(String titulo, String tituloHorizontal, String tituloVertical, DefaultCategoryDataset dataset,
			DefaultCategoryDataset datasetMeta, int largura){
        try {
        	StreamedContent chart;  
        	//Chart  
        	JFreeChart jfreechart = ChartFactory.createBarChart(
        			titulo, // chart title
        			tituloHorizontal, // range axis label
        			tituloVertical, // domain axis label
        			dataset, // data
        			PlotOrientation.VERTICAL, // orientation
        			true, // include legend
        			true, // tooltips?
        			false // URLs?
        			);
        
            LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
            lineandshaperenderer.setSeriesPaint(0, Color.red);
            lineandshaperenderer.setSeriesShapesVisible(0, false);
            lineandshaperenderer.setBaseItemLabelsVisible(true);
            lineandshaperenderer.setSeriesVisibleInLegend(0, true);
            
            NumberAxis numberaxis = new NumberAxis("Percent");
            numberaxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
            
            CategoryPlot categoryplot = (CategoryPlot)jfreechart.getPlot();
            categoryplot.setBackgroundPaint(Color.white);
            categoryplot.setRangeGridlinePaint(Color.black);
            categoryplot.setDataset(2, datasetMeta); //DataSet Metas            
            categoryplot.setRangeAxis(0, numberaxis);
            categoryplot.setRenderer(2, lineandshaperenderer);
            categoryplot.mapDatasetToRangeAxis(1, 1);
            categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
            
            CategoryAxis categoryaxis = categoryplot.getDomainAxis();
            
            categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
                       
        	BarRenderer renderer = (BarRenderer) categoryplot.getRenderer();
        	GradientPaint gp1 = new GradientPaint(  
        	            0.0f, 0.0f, new Color(0,127,127),   
        	            0.0f, 0.0f, new Color(0, 60, 60));  
        	  
        	renderer.setSeriesPaint(0, gp1);
        	renderer.setSeriesItemLabelsVisible(0, true);
        	renderer.setDrawBarOutline(false);
        	renderer.setSeriesItemLabelsVisible(0, true);        	
        	
        	CategoryItemRenderer catItemRenderer = categoryplot.getRenderer();
        	CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0%"));
        	catItemRenderer.setBaseItemLabelGenerator(generator);
        	
        	        	
        	File chartFile = new File("WeeklyBasis");  
			ChartUtilities.saveChartAsPNG(chartFile, jfreechart, largura, 400);
			chart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
			return chart;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}  
	}
}
