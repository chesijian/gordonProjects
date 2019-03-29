package com.state.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class JFreeChartUtil {
	public static void main(String[] args) throws IOException
    {
       
		testBar();
    }
	/**
	 * 生成跨区利用率图片
	 * @author 车斯剑
	 * @date 2016年10月18日下午1:39:29
	 */
	public static void createImage(String tempPath ,Map<String,Object> picData){
		
		//Map<String,Object>  picData = JSON.parseObject(picStr, new TypeReference<Map<String,Object>>(){});
		DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
		List<Map<String,Object>> mainData = (List<Map<String, Object>>) picData.get("mainData");
		List<String> times = (List<String>) picData.get("mainTime");
		
		for (Map<String, Object> map : mainData) {
			List<Double> data = (List<Double>) map.get("data");
			for(int i=0; i<data.size();i++){
				localDefaultCategoryDataset.addValue(data.get(i),(String)map.get("name"),times.get(i));
			}
		}
		
		//创建主题样式  
        StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
        //设置标题字体  
        standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
        //设置图例的字体  
        standardChartTheme.setRegularFont(new Font("宋书",Font.BOLD,15));  
        //设置轴向的字体  
        standardChartTheme.setLargeFont(new Font("宋书",Font.BOLD,15));  
        //应用主题样式  
        ChartFactory.setChartTheme(standardChartTheme);
        
        JFreeChart chart = ChartFactory.createBarChart("跨区通道利用率", "时间", "利用率", localDefaultCategoryDataset);
        CategoryPlot localCategoryPlot = (CategoryPlot)chart.getPlot();
        localCategoryPlot.setBackgroundPaint(ChartColor.WHITE);
        localCategoryPlot.setRangeGridlinePaint(ChartColor.GRAY);
        localCategoryPlot.setDomainGridlinesVisible(true);
        localCategoryPlot.setRangeCrosshairVisible(true);
        localCategoryPlot.setRangeCrosshairPaint(Color.blue);
        NumberAxis localNumberAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
        localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer localBarRenderer = (BarRenderer)localCategoryPlot.getRenderer();
        localBarRenderer.setDrawBarOutline(false);
        localBarRenderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
        CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
        //设置X轴的字体及显示
        localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        localCategoryAxis.setTickLabelFont(new Font("宋书",Font.BOLD,15));
        //localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988D));
        try {
        	FileOutputStream fos = new FileOutputStream(tempPath);
			ChartUtilities.writeChartAsPNG(fos, chart, 800, 600);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static void testBar() throws IOException{
		//创建主题样式  
        StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
        //设置标题字体  
        standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
        //设置图例的字体  
        standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
        //设置轴向的字体  
        standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
        //应用主题样式  
        ChartFactory.setChartTheme(standardChartTheme);
        
        JFreeChart chart = ChartFactory.createBarChart("Bar Chart Demo 1", "Category", "Value", createDataset());
        CategoryPlot localCategoryPlot = (CategoryPlot)chart.getPlot();
        localCategoryPlot.setDomainGridlinesVisible(true);
        localCategoryPlot.setRangeCrosshairVisible(true);
        localCategoryPlot.setRangeCrosshairPaint(Color.blue);
        NumberAxis localNumberAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
        localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer localBarRenderer = (BarRenderer)localCategoryPlot.getRenderer();
        localBarRenderer.setDrawBarOutline(false);
        GradientPaint localGradientPaint1 = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
        GradientPaint localGradientPaint2 = new GradientPaint(0.0F, 0.0F, Color.green, 0.0F, 0.0F, new Color(0, 64, 0));
        GradientPaint localGradientPaint3 = new GradientPaint(0.0F, 0.0F, Color.red, 0.0F, 0.0F, new Color(64, 0, 0));
        localBarRenderer.setSeriesPaint(0, localGradientPaint1);
        localBarRenderer.setSeriesPaint(1, localGradientPaint2);
        localBarRenderer.setSeriesPaint(2, localGradientPaint3);
        localBarRenderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
        CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
        localCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.5235987755982988D));
	    
        ChartFrame chartFrame=new ChartFrame("某公司人员组织数据图",chart); 
        //chart要放在Java容器组件中，ChartFrame继承自java的Jframe类。该第一个参数的数据是放在窗口左上角的，不是正中间的标题。
        chartFrame.pack(); //以合适的大小展现图形
        chartFrame.setVisible(true);//图形是否可见
        FileOutputStream fos = new FileOutputStream("c:/test.png");
        ChartUtilities.writeChartAsPNG(fos, chart, 800, 600);
	}
	
	
	private static CategoryDataset createDataset()
	  {
		String str1 = "First";
	    String str2 = "Second";
	    String str3 = "Third";
	    String str4 = "Category 1";
	    String str5 = "Category 2";
	    String str6 = "Category 3";
	    String str7 = "Category 4";
	    String str8 = "Category 5";
	    DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
	    localDefaultCategoryDataset.addValue(1.0D, str1, str4);
	    localDefaultCategoryDataset.addValue(4.0D, str1, str5);
	    localDefaultCategoryDataset.addValue(3.0D, str1, str6);
	    localDefaultCategoryDataset.addValue(5.0D, str1, str7);
	    localDefaultCategoryDataset.addValue(5.0D, str1, str8);
	    localDefaultCategoryDataset.addValue(5.0D, str2, str4);
	    localDefaultCategoryDataset.addValue(7.0D, str2, str5);
	    localDefaultCategoryDataset.addValue(6.0D, str2, str6);
	    localDefaultCategoryDataset.addValue(8.0D, str2, str7);
	    localDefaultCategoryDataset.addValue(4.0D, str2, str8);
	    localDefaultCategoryDataset.addValue(4.0D, str3, str4);
	    localDefaultCategoryDataset.addValue(3.0D, str3, str5);
	    localDefaultCategoryDataset.addValue(2.0D, str3, str6);
	    localDefaultCategoryDataset.addValue(3.0D, str3, str7);
	    localDefaultCategoryDataset.addValue(6.0D, str3, str8);
	    return localDefaultCategoryDataset;
	  }
	
	public static void testPie() throws IOException{
		 DefaultPieDataset dpd=new DefaultPieDataset(); //建立一个默认的饼图
	        dpd.setValue("管理人员", 25);  //输入数据
	        dpd.setValue("市场人员", 25);
	        dpd.setValue("开发人员", 45);
	        dpd.setValue("其他人员", 10);
	        
	        
	      //创建主题样式  
	        StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
	        //设置标题字体  
	        standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
	        //设置图例的字体  
	        standardChartTheme.setRegularFont(new Font("宋书",Font.PLAIN,15));  
	        //设置轴向的字体  
	        standardChartTheme.setLargeFont(new Font("宋书",Font.PLAIN,15));  
	        //应用主题样式  
	        ChartFactory.setChartTheme(standardChartTheme);
	        
	        JFreeChart chart=ChartFactory.createPieChart("某公司人员组织数据图",dpd,true,true,false); 
	        //可以查具体的API文档,第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL
	        
	        ChartFrame chartFrame=new ChartFrame("某公司人员组织数据图",chart); 
	        //chart要放在Java容器组件中，ChartFrame继承自java的Jframe类。该第一个参数的数据是放在窗口左上角的，不是正中间的标题。
	        chartFrame.pack(); //以合适的大小展现图形
	        chartFrame.setVisible(true);//图形是否可见
	        FileOutputStream fos = new FileOutputStream("c:/test.png");
	        ChartUtilities.writeChartAsPNG(fos, chart, 800, 600);
	}
}
