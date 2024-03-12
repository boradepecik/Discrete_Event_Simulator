import java.util.ArrayList;
import java.util.PriorityQueue;

public class ATC {

	ArrayList<Flight> tempFlights = new ArrayList<Flight>();
	ACC controller;
	Flight qwe = new Flight(null,null,null,null,0,null);
	String code;
	PriorityQueue<Flight> flights = new PriorityQueue(qwe.comparator());
	boolean available = true;
	Flight processedFlight;
	public ATC(ACC controller,String code) {
		this.controller = controller;
		this.code = code;

	}
	

}
