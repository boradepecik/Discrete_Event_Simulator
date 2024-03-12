import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;


public class Project3 {

	public static void main(String[] args) throws IOException {
		//ArrayList<ACC> centers = new ArrayList<ACC>();
		File inp = new File(args[0]);
		File out = new File(args[1]);
		FileWriter fw = new FileWriter(out);
		BufferedWriter writer = new BufferedWriter(fw);
		HashMap<String,ACC> centers = new HashMap<String,ACC>();
		HashMap<String,Airport> airports = new HashMap<String,Airport>();
		Scanner sc = new Scanner(inp);
		Integer A = sc.nextInt();
		Integer F = sc.nextInt();
		for (int i = 0;i<A;i++) { //taking input of ACC and respective airports
			String name = sc.next();
			ACC center = new ACC(name);
			//centers.add(center);
			centers.put(name, center);
			String[] airportstr = sc.nextLine().split(" ");
			for (String str: airportstr) {
				Airport airport = new Airport(str,center);
				center.addAirport(airport);
				airports.put(str, airport);
			}
			
		}
		for (int j = 0;j<F;j++) { //taking input of flights
			int admission = sc.nextInt();
			String code = sc.next();
			String contr = sc.next();
			ACC controller = centers.get(contr);
			Airport departure = airports.get(sc.next());
			Airport landing = airports.get(sc.next());
			int[] asd = new int[22];
			for (int i = 0;i<21;i++) {
				Integer op = sc.nextInt();
				//flight.timeList[i+1] = op;
				asd[i+1] = op;
			}
			Flight flight = new Flight(controller,departure,landing,code,admission,asd);
			flight.timeLeft = admission;
			controller.allEvents.add(flight);
			controller.allFlights.add(flight);
		}
		for (ACC c : centers.values()) {
			while (c.allFlights.size() != 0) { //while there are flights, keep pulling next event
				c.moveToNext();
			}
			writer.write(c.code + " " + c.time);
			if (c.table[0].isBlank()) {
				c.table[0] = null;
			}
			for (int i = 0;i<1000;i++) { //print the hash table for atcs
				if (c.table[i] !=null) {
					if (i<10) {
						writer.write(" "+ c.table[i] + "00" + i);
					}
					else if (i < 100) {
						writer.write(" "+ c.table[i] + "0" + i);
					}
					else {
						writer.write(" "+ c.table[i] + "" + i); // dont forget to add 0 to beginning
					}
				}
			}
			writer.newLine();
		}
		writer.close();

	}

}
