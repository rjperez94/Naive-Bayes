import java.util.ArrayList;
import java.util.Scanner;

public class Email {
	public ArrayList<Integer> features = new ArrayList<>();
	public boolean spam;

	// for train
	public Email(String line, int numAttributes) {
		Scanner sc = new Scanner(line);
		for (int i = 0; i < numAttributes; i++) {
			features.add(sc.nextInt());
		}
		spam = (sc.nextInt() == 1) ? true : false;
		sc.close();
	}

	// for test
	public Email(String line) {
		Scanner sc = new Scanner(line);
		while (sc.hasNextInt()) {
			features.add(sc.nextInt());
		}
		sc.close();
		// NO TYPE
	}

	public String isSpam(double[][] table) {
		// spam
		double probTrue = 1;
		for (int i = 0; i < features.size(); i++) {
			if (features.get(i) == 1) {
				probTrue *= table[i+1][0];
			} else {
				probTrue *= table[i+1][1];
			}
		}
		// notspam
		double probFalse = 1;
		for (int i = 0; i < features.size(); i++) {
			if (features.get(i) == 1) {
				probFalse *= table[i+1][2];
			} else {
				probFalse *= table[i+1][3];
			}
		}
		
		if (probTrue > probFalse) {
			return "Spam";
		} else if (probTrue < probFalse) {
			return "Not Spam";
		}
		return "CAN'T BE CLASSIFIED";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email features=" + features);
		return builder.toString();
	}
}
