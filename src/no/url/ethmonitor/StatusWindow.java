package no.url.ethmonitor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import eu.hansolo.steelseries.gauges.DisplaySingle;
import eu.hansolo.steelseries.gauges.Radial;

public class StatusWindow extends JFrame{
	private static final long serialVersionUID = 742476492963869922L;
	JTabbedPane master_pane;
	JTabbedPane main_pane;
	JTabbedPane[] gpu_panes;
	JButton restart;
	
	Radial total_hashrate;
	DisplaySingle avg_temp_display;
	DisplaySingle avg_fan_display;
	DisplaySingle total_wattage_display;
	
	DisplaySingle running_time;
	DisplaySingle shares;
	DisplaySingle shares_per_min;
	
	List<Radial> gpu_hashrate = new ArrayList<Radial>();
	List<DisplaySingle> gpu_temp = new ArrayList<DisplaySingle>();
	List<DisplaySingle> gpu_fan = new ArrayList<DisplaySingle>();
	List<DisplaySingle> gpu_watt = new ArrayList<DisplaySingle>();
	
	GraphPanel main_hashrate_graph;
	GraphPanel main_temperature_graph;
	GraphPanel main_fan_graph;
	GraphPanel main_wattage_graph;
	
	List<GraphPanel> gpu_hashrate_graph = new ArrayList<GraphPanel>();
	List<GraphPanel> gpu_temperature_graph = new ArrayList<GraphPanel>();
	List<GraphPanel> gpu_fan_graph = new ArrayList<GraphPanel>();
	List<GraphPanel> gpu_wattage_graph = new ArrayList<GraphPanel>();
	
	
	
	Main main;
	
	public StatusWindow(Main main, int gpus){
		this.main = main;
		this.setTitle("Mining Status");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/icon32.png")));
		
		//this.setUndecorated(true);
		
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screensize.width/2, (screensize.height/2)); //May not work for smaller screen sizes
		this.setLocation(screensize.width-(screensize.width/2), 0); //screensize.height-(screensize.height/4));
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Main.RUNNING = false;
				System.exit(0);
				
			}
			
		});
		JPanel outer = new JPanel();
		outer.setLayout(new BoxLayout(outer, BoxLayout.PAGE_AXIS));
		master_pane = new JTabbedPane();
		main_pane = new JTabbedPane();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		

		//JPanel panel0 = new JPanel();
		total_hashrate = new Radial();
		total_hashrate.setTitle("Total Hashrate");
		total_hashrate.setUnitString("Mh/s");
		total_hashrate.setMaxValue(main.gauge_max_status);
		total_hashrate.setLcdDecimals(2);
		total_hashrate.setLedVisible(false);
		total_hashrate.setPreferredSize(new Dimension(450,450));
		//panel0.add(total_hashrate);
        //TODO
		panel.add(total_hashrate, BorderLayout.LINE_START);
        
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
        
        
        panel1.add(new JLabel("Average Temp"));
        avg_temp_display = new DisplaySingle();
        avg_temp_display.setPreferredSize(new Dimension(450,50));
        avg_temp_display.setLcdUnitString(" C");
        panel1.add(avg_temp_display);
        
        panel1.add(new JLabel("Average Fan"));
        
        avg_fan_display = new DisplaySingle();
        avg_fan_display.setPreferredSize(new Dimension(450,50));
        avg_fan_display.setLcdUnitString(" %");
        panel1.add(avg_fan_display);
        

        if(main.detailed_result) {
	        panel1.add(new JLabel("Total Wattage"));
	        total_wattage_display = new DisplaySingle();
	        total_wattage_display.setPreferredSize(new Dimension(450,50));
	        total_wattage_display.setLcdUnitString(" W");
	        panel1.add(total_wattage_display);
        }
        panel1.add(new JLabel("Running Time"));
        running_time = new DisplaySingle();
        running_time.setPreferredSize(new Dimension(450,50));
        running_time.setLcdUnitStringVisible(false);
        running_time.setLcdDecimals(0);
        panel1.add(running_time);
        
        panel1.add(new JLabel("Shares"));
        shares = new DisplaySingle();
        shares.setPreferredSize(new Dimension(450,50));
        shares.setLcdUnitStringVisible(false);
        shares.setLcdDecimals(0);
        panel1.add(shares);
        
        panel1.add(new JLabel("Shares Per Min"));
        shares_per_min = new DisplaySingle();
        shares_per_min.setPreferredSize(new Dimension(450,50));
        shares_per_min.setLcdUnitStringVisible(false);
        shares_per_min.setLcdDecimals(4);
        panel1.add(shares_per_min);
        
        panel.add(panel1, BorderLayout.LINE_END);
    
        
        main_pane.addTab("Status", panel);
        
        main_hashrate_graph = new GraphPanel(new ArrayList<Double>());
        main_pane.add("Hashrate", main_hashrate_graph);
        
        main_temperature_graph = new GraphPanel(new ArrayList<Double>());
        main_pane.add("Temp", main_temperature_graph);
        
        main_fan_graph = new GraphPanel(new ArrayList<Double>());
        main_pane.add("Fans", main_fan_graph);
        if(main.detailed_result) {
            main_wattage_graph = new GraphPanel(new ArrayList<Double>());
            main_pane.add("Wattage", main_wattage_graph);        	
        }
		master_pane.addTab("Main", main_pane);
		
		for(int i=0;i<gpus;i++) {
			JTabbedPane gpu_tab = new JTabbedPane();
			JPanel _panel = new JPanel();
			
			_panel.setLayout(new BorderLayout());
			
			
			Radial hashrate = new Radial();
			hashrate.setTitle("GPU Hashrate");
			hashrate.setUnitString("Mh/s");
			hashrate.setMaxValue(main.gauge_max_gpu);
			hashrate.setLcdDecimals(2);
			hashrate.setPreferredSize(new Dimension(450,450));
	        gpu_hashrate.add(hashrate);
	        _panel.add(hashrate, BorderLayout.LINE_START);
	        
	        
			JPanel gpu_panel = new JPanel();
			gpu_panel.setLayout(new BoxLayout(gpu_panel, BoxLayout.PAGE_AXIS));
	        
	        gpu_panel.add(new JLabel("GPU "+i+" Temp"));
	        DisplaySingle temp_display = new DisplaySingle();
	        temp_display.setPreferredSize(new Dimension(450,50));
	        temp_display.setLcdUnitString(" C");
	        gpu_temp.add(temp_display);
	        gpu_panel.add(temp_display);
	        
	        gpu_panel.add(new JLabel("GPU "+i+" Fan"));
	        DisplaySingle fan_display = new DisplaySingle();
	        fan_display.setPreferredSize(new Dimension(450,50));
	        fan_display.setLcdUnitString(" %");
	        gpu_fan.add(fan_display);
	        gpu_panel.add(fan_display);
	        

	        if(main.detailed_result) {
		        gpu_panel.add(new JLabel("GPU "+i+" Wattage"));
		        DisplaySingle wattage_display = new DisplaySingle();
		        wattage_display.setPreferredSize(new Dimension(450,50));
		        wattage_display.setLcdUnitString(" W");
		        gpu_watt.add(wattage_display);
		        gpu_panel.add(wattage_display);
	        }
	        _panel.add(gpu_panel, BorderLayout.LINE_END);
	        
	        gpu_tab.addTab("Status", _panel);
	        
	        GraphPanel gpu_hashrate = new GraphPanel(new ArrayList<Double>());
	        gpu_tab.add("Hashrate", gpu_hashrate);
	        gpu_hashrate_graph.add(gpu_hashrate);
	        
	        GraphPanel gpu_temperature = new GraphPanel(new ArrayList<Double>());
	        gpu_tab.add("Temp", gpu_temperature);
	    	gpu_temperature_graph.add(gpu_temperature);
	    	
	        GraphPanel gpu_fan = new GraphPanel(new ArrayList<Double>());
	        gpu_tab.add("Fan", gpu_fan);
	    	gpu_fan_graph.add(gpu_fan);
	    	
	    	if(main.detailed_result) {  	
		        GraphPanel gpu_watt = new GraphPanel(new ArrayList<Double>());
		        gpu_tab.add("Watt", gpu_watt);
		    	gpu_wattage_graph.add(gpu_watt);
	    	}
	        
	        master_pane.add("GPU "+i, gpu_tab);
			
		}
		outer.add(master_pane);
		/*
		 * restart = new JButton("Restart"); restart.addActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { for(Server server :
		 * Main.servers) try { main.connect(server.ip_address, server.port,
		 * Main.RESTART); } catch (IOException | ParseException e) {
		 * e.printStackTrace(); } }
		 * 
		 * }); outer.add(restart);
		 */
		this.getContentPane().add(outer);
		this.setVisible(true);
		
		
	}
	
}
