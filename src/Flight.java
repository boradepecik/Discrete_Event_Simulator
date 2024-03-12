import java.util.Comparator;


public class Flight {
	Airport departure;
	Airport landing;
	ACC controller;
	String code;
	int admission;
	boolean newFlight = true;
	public int[] timeList = new int[22];
	int time;
	int state = 0;
	int timeLeft;
	public Flight(ACC controller, Airport departure, Airport landing, String code,int admission,int[] asd) {
		this.controller = controller;
		this.departure = departure;
		this.landing = landing;
		this.code = code;
		this.admission = admission;
		this.timeList = asd;
	}
	Comparator<Flight> comparator(){
		return new CustomComparator();
	}
	Comparator<Flight> timeComparator(){
		return new timeLeftComparator();
	}
	private class CustomComparator implements Comparator<Flight>{
		@Override
		public int compare(Flight o1, Flight o2) {
			if (o1.time > o2.time) {
				return 1;
			}
			else if (o2.time == o1.time) {
				if (o1.newFlight && !o2.newFlight) {
					return -1;
				}
				if (o2.newFlight && !o1.newFlight) {
					return 1;
				}
				
				return o1.code.compareTo(o2.code);
			}
			return -1;
		}
	}
	public class timeLeftComparator implements Comparator<Flight>{
		public int compare(Flight f1, Flight f2) {
			if (f1.timeLeft == f2.timeLeft) {
				if (f1.state == 1 || f1.state == 3 || f1.state == 11 || f1.state == 13 || f1.state == 21) {
					if (f1.timeList[f1.state] >30) {
						return 1;
					}
				}
				if (f2.state == 1 || f2.state == 3 || f2.state == 11 || f2.state == 13 || f2.state == 21) {
					if (f2.timeList[f2.state] >30) {
						return -1;
					}
				}
				return f1.code.compareTo(f2.code);
			}
			return f1.timeLeft - f2.timeLeft;
		}
	}
	public class TimeComparator implements Comparator<Flight> {
	    public int compare(Flight f1, Flight f2) {
	        return f1.admission - f2.admission;
	    }
	}
}
