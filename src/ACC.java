import java.util.ArrayList;
import java.util.PriorityQueue;

public class ACC {

	ArrayList<Airport> airports;
	ArrayList<ATC> traffics;
	String code;
	int time = 0;
	boolean available = true;
	Flight processedFlight;
	int processTime = 0;
	ArrayList<Flight> allFlights = new ArrayList<Flight>(); // dont forget to sort all flights
	//ArrayList<Flight> activeFlights = new ArrayList<Flight>(); // use custom comparator
	String[] table = new String[1000];
	Flight asd = new Flight(null,null,null,null,0,null);
	PriorityQueue<Flight> allEvents = new PriorityQueue(asd.timeComparator());
	PriorityQueue<Flight> flights = new PriorityQueue(asd.comparator());
	boolean finished = true;
	public ACC(String code) {
		this.airports = new ArrayList<Airport>();
		this.traffics = new ArrayList<ATC>();
		this.code = code;
	}
	public void addAirport(Airport airport) { // method to add airports to ACC and atcs to hashtable
		airports.add(airport);
		Integer atcnum = addTable(airport);
		String atccode = code + (atcnum.toString());
		ATC traffic = new ATC(this,atccode);
		airport.setATC(traffic);
		traffics.add(traffic);
	}
	public void moveToNext() { // method that finds the next closest event and handles it
		//System.out.println("time: " + time + " moving one tick");
		Flight nextEvent = allEvents.poll();
		if (nextEvent != null) {
			finished = true;
			int reqTime = nextEvent.timeLeft-time;
			//for (Flight f: allEvents) {
				//f.timeLeft -= reqTime; //changed (not necessary code)
			//}
			//System.out.println("INITIAL TIME: " + time);
			//System.out.println(nextEvent.state);
			time += reqTime;
			if (nextEvent.state == 1 || nextEvent.state == 3 || nextEvent.state == 11 || nextEvent.state == 13 || nextEvent.state == 21) { //if the next event is acc, then make acc available
				available = true;
				if (nextEvent.timeList[nextEvent.state] <=30) {
					nextEvent.newFlight = true;
					//System.out.println((time-reqTime)+ "--> "+ (time) +  " flight: " + nextEvent.code + " ended ACC");
				}
				else {
					//System.out.println((time-reqTime) + "-->>> CURRENT TIME : " + time + " flight: " + nextEvent.code + " didn't end ACC, send to queue again ");
					finished = false;
					nextEvent.timeList[nextEvent.state] -= 30;
					nextEvent.time = time;
					nextEvent.newFlight = false;
					flights.add(nextEvent);
					
				}
			}
			if (nextEvent.state == 4 || nextEvent.state == 6 || nextEvent.state == 8 || nextEvent.state == 10) { //if the next event is atc make atc available
				nextEvent.departure.responsible.available = true;
			}
			if (nextEvent.state == 14 || nextEvent.state == 16 || nextEvent.state == 18 || nextEvent.state == 20) {
				nextEvent.landing.responsible.available = true;
			}
			if (finished){ //if next event isn't ACC >30
				nextEvent.state++;
				if (nextEvent.state == 22) { // terminate the flight if state is 22
					//System.out.println("Flight with code: " + nextEvent.code + " landed at : " + time);
					allFlights.remove(nextEvent);
				}
				else {
					if (nextEvent.state == 1 || nextEvent.state == 3 || nextEvent.state == 11 || nextEvent.state == 13 || nextEvent.state == 21) {
						nextEvent.time = time;
						nextEvent.newFlight = true;
						flights.add(nextEvent);
						}
					}
					if (nextEvent.state == 4 || nextEvent.state == 6 || nextEvent.state == 8 || nextEvent.state == 10) {
						nextEvent.time = time;
						nextEvent.departure.responsible.flights.add(nextEvent);
					}
					if (nextEvent.state == 14 || nextEvent.state == 16 || nextEvent.state == 18 || nextEvent.state == 20 ) {
						nextEvent.time = time;
						nextEvent.landing.responsible.flights.add(nextEvent);
					}
					if (nextEvent.state == 2 || nextEvent.state == 5 || nextEvent.state == 7 || nextEvent.state == 9 || nextEvent.state == 12 || nextEvent.state == 15 || nextEvent.state == 17 || nextEvent.state == 19) {
						nextEvent.timeLeft = nextEvent.timeList[nextEvent.state]+time; //changed
						//System.out.println("Flight " + nextEvent.code + " started waiting for " + nextEvent.timeLeft);
						allEvents.add(nextEvent);
					}
				}
			//Flight laterEvent = allEvents.peek();
			update(nextEvent);
			}

		else {
			//System.out.println("no event found");
		}
		
					
			
		}
	
	public void update(Flight nextEvent) { //poll the next flight from queue if available
		if (available) {
			processedFlight = flights.poll();
			if (processedFlight != null) {
				available = false;
				if (processedFlight.timeList[processedFlight.state]>30) {
					processedFlight.timeLeft = 30+time;//changed
				}
				else {
					processedFlight.timeLeft = processedFlight.timeList[processedFlight.state]+time;//changed
				}
				allEvents.add(processedFlight);
				//System.out.println("CURRENT TIME: " + time + " Flight with code: " + processedFlight.code + " will be processed by atc for: " + processedFlight.timeLeft);
			}
			else {
				available = true;
			}
		
		}
		if (nextEvent.departure.responsible.available) {
			processedFlight = nextEvent.departure.responsible.flights.poll();
			if (processedFlight != null) {
				nextEvent.departure.responsible.available = false;
				processedFlight.timeLeft = processedFlight.timeList[processedFlight.state]+time;//changed
				allEvents.add(processedFlight);
			}
			else {
				nextEvent.departure.responsible.available = true;
			}
		}
		if (nextEvent.landing.responsible.available) {
			processedFlight = nextEvent.landing.responsible.flights.poll();
			if (processedFlight != null) {
				nextEvent.landing.responsible.available = false;
				processedFlight.timeLeft = processedFlight.timeList[processedFlight.state]+time;//changed
				allEvents.add(processedFlight);
			}
			else {
				nextEvent.landing.responsible.available = true;
			}
		}
	}
		
	public int addTable(Airport airport) { //method to store atcs in hash table with linear probing
		char[] arr = airport.code.toCharArray();
		int sum = 0;
		for (int j = 0;j<airport.code.length();j++) {
			sum += Math.pow(31, j)* ((int) arr[j]);
		}
		sum = sum % 1000;
		for (int i = 0;i<1000;i++) {
			if (table[(sum+i)%1000] == null) {
				table[(sum+i)%1000] = airport.code;
				return (sum+i)%1000;
			}
			if ((sum+i)%1000 == 0) {
				if(table[0].length() != 3) {
					table[0] = airport.code;
					return 0;
				}
			}
		}
		return 0;
	}
	
}
