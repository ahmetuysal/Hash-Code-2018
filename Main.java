import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Car {
	int xCoor;
	int yCoor;
	int t = 0; // current time
	static int T;
	static int totalScore;

	Car(int x, int y) {
		this.xCoor = x;
		this.yCoor = y;
	}

	Car() {
		this(0, 0);
	}
}

class Ride {
	int startRow;
	int startCol;
	int finishRow;
	int finishCol;
	int earliestStart;
	int latestFinish;
	boolean isDone = false;
	static int totalNumRides;
	static Ride[] allRides;
	static int currentNumRides = 0;
	int rideLength;
	static ArrayList<Integer> finishX = new ArrayList<Integer>();

	public Ride(int a, int b, int x, int y, int s, int f) {
		this.startRow = a;
		this.startCol = b;
		this.finishRow = x;
		this.finishCol = y;
		this.earliestStart = s;
		this.latestFinish = f;
		finishX.add(x);
		this.rideLength = this.distance(x, y);
		allRides[currentNumRides] = this;
		currentNumRides++;
	}

	public int distance(int x, int y) {
		return Math.abs(this.startRow - x) + Math.abs(this.startCol - y);
	}

	public static int getSmallest(int x, int y, int t) {
		int smallest = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < Ride.allRides.length; i++) {
			Ride ride = Ride.allRides[i];
			if (!ride.isDone) {
				int distance = ride.distance(x, y) + t;
				
				// use this constraint only on part e
				//if (distance > ride.earliestStart) {
				//	continue;
				//}
				
				/*
				 * if (ride.getFinishRow() + ride.getFinishCol() > 19000) { continue; }
				 */

				if (distance < ride.earliestStart) {
					distance = ride.earliestStart;
				}
				if (distance + ride.rideLength > ride.latestFinish)
					continue;
				if (distance < smallest) {
					smallest = distance;
					index = i;
				}
			}
		}
		return index;
	}

	public static void initializeRides() {
		allRides = new Ride[totalNumRides];
	}

}
public class Main {

	private static String FILE_NAME = "d_metropolis.in";

	public static void main(String[] args) {
		int R, C, F, N, B, T;
		try {
			PrintWriter writer = new PrintWriter(new FileWriter("d_metropolis.out"));
			BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
			String firstLine = br.readLine();
			StringTokenizer tokenizer = new StringTokenizer(firstLine);
			R = Integer.parseInt(tokenizer.nextToken());
			C = Integer.parseInt(tokenizer.nextToken());
			F = Integer.parseInt(tokenizer.nextToken());
			N = Integer.parseInt(tokenizer.nextToken());
			B = Integer.parseInt(tokenizer.nextToken());
			T = Integer.parseInt(tokenizer.nextToken());
			Ride.totalNumRides = N;
			Ride.initializeRides();
			Car.T = T;
			for (int i = 0; i < N; i++) {
				int a, b, x, y, s, f;
				String line = br.readLine();
				StringTokenizer lineToken = new StringTokenizer(line);
				a = Integer.parseInt(lineToken.nextToken());
				b = Integer.parseInt(lineToken.nextToken());
				x = Integer.parseInt(lineToken.nextToken());
				y = Integer.parseInt(lineToken.nextToken());
				s = Integer.parseInt(lineToken.nextToken());
				f = Integer.parseInt(lineToken.nextToken());
				new Ride(a, b, x, y, s, f);

			}
			br.close();

			for (int count = 0; count < F; count++) {
				Car car = new Car();
				ArrayList<Integer> rides = new ArrayList<Integer>();
				while (true) {
					int index = Ride.getSmallest(car.xCoor, car.yCoor, car.t);
					if (index == -1)
						break;
					Ride ride = Ride.allRides[index];
					car.t += ride.distance(car.xCoor, car.yCoor);
					if (car.t < ride.earliestStart) {
						car.t = ride.earliestStart;
					}
					car.t += ride.rideLength;
					if (car.t > T || ride.latestFinish < car.t)
						break;
					car.xCoor = ride.finishRow;
					car.yCoor = ride.finishCol;
					Ride.allRides[index].isDone = true;
					rides.add(index);
				}
				writer.print(rides.size());
				for (int i : rides) {
					writer.print(" " + i);
				}
				writer.println();
			}

			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
