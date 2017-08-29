import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class NaiveBayes {
	private static JFileChooser fileChooser = new JFileChooser();
	private static final String TRAINSET_FILENAME = "spamLabelled.dat";
	private static final String TESTSET_FILENAME = "spamUnlabelled.dat";
	private static final int NUM_ATTRIBUTES = 12;
	private static ArrayList<Email> trainEmails = new ArrayList<>();
	private static double[][] table = new double[NUM_ATTRIBUTES+1][4];
	/**
	 * table format (2d double array)
	 * 1. fill with counts , then
	 * 2. refill with probabilities
	 * 
	 * 		Spam						NotSpam
	 * F1	[fIsTrue, fIsNotTrue	,	fIsTrue, fIsNotTrue]
	 * F2	[fIsTrue, fIsNotTrue	,	fIsTrue, fIsNotTrue]
	 * F3	[fIsTrue, fIsNotTrue	,	fIsTrue, fIsNotTrue]
	 * .......
	 */
	private static ArrayList<Email> testEmails = new ArrayList<>();
	
	public static void main (String[] args) {
		chooseDir();
		System.out.println("Counting");
		ArrayList<Email> trues = subArray(trainEmails, true);
		ArrayList<Email> falses = subArray(trainEmails, false);
		table[0][0]=trues.size();
		table[0][1]=trues.size();
		table[0][2]=falses.size();
		table[0][3]=falses.size();
		
		for (int i = 1; i < table.length; i++) {
			//spam
			table[i][0]=countOccurance(trues, 1, i-1);
			table[i][1]=countOccurance(trues, 0, i-1);
			//not spam
			table[i][2]=countOccurance(falses, 1, i-1);
			table[i][3]=countOccurance(falses, 0, i-1);
		}
		printArray(table);
		
		boolean addOne = hasZeroCount(table);
		System.out.println("HAS ZERO: "+addOne);
		if(addOne) {
			System.out.println("ADDING ONE TO ALL COUNTED VALUES");
			addOneToAll(table);
			printArray(table);
		}
		System.out.println("Computing probability");
		computeProbability(table, addOne);
		printArray(table);
		
		System.out.println("Classifying Test Data");
		for (Email e : testEmails) {
			System.out.println(e.toString()+" classified as "+e.isSpam(table));
		}
	}
	
	private static void printArray(double[][] table) {		
		System.out.printf("\tSpam\t\t\tNotSpam");
		System.out.printf("\n\tfIs1\tfIs0\t\tfIs1\tfIs0");
		System.out.printf("\nClass\t%.2f\t%.2f\t\t%.2f\t%.2f",table[0][0],table[0][1],table[0][2],table[0][3]);
		for (int i = 1; i < table.length; i++) {
			System.out.printf("\nF"+i+"\t%.2f\t%.2f\t\t%.2f\t%.2f",table[i][0],table[i][1],table[i][2],table[i][3]);
		}
		System.out.printf("\n");
	}

	private static void computeProbability(double[][] table, boolean addedOne) {
		//fIs1
		for (int i = 1; i < table.length; i++) {
			double rowTotal = table[i][0]+table[i][2];
			for (int j = 0; j < table[i].length; j+=2) {
				if(addedOne) {
					table[i][j] = table[i][j]/rowTotal;
				} else {
					table[i][j] = table[i][j]/table[0][j];
				}
			}
		}
		//fIs0
		for (int i = 1; i < table.length; i++) {
			double rowTotal = table[i][1]+table[i][3];
			for (int j = 1; j < table[i].length; j+=2) {
				if(addedOne) {
					table[i][j] = table[i][j]/rowTotal;
				} else {
					table[i][j] = table[i][j]/table[0][j];
				}
			}
		}
		
		double total = table[0][0]+table[0][2];
		table[0][0] = table[0][0]/total;
		table[0][1] = table[0][1]/total;
		//same total as above
		table[0][2] = table[0][2]/total;
		table[0][3] = table[0][3]/total;
	}

	private static void addOneToAll(double[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				table[i][j]++;
			}
		}
	}

	private static boolean hasZeroCount(double[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == 0) {
					return true;
				}
			}
		}
		return false;
	}

	private static double countOccurance(ArrayList<Email> list, int type, int i) {
		double result = 0;
		for (Email e : list) {
			if(e.features.get(i) == type) {
				result++;
			}
		}
		return result;
	}

	private static ArrayList<Email> subArray(ArrayList<Email> list, boolean type) {
		ArrayList<Email> result = new ArrayList<>();
		for (Email e : list) {
			if(e.spam==type) {
				result.add(e);
			}
		}
		return result;
	}

	private static void chooseDir() {
		File train = null, test = null;

		// set up the file chooser
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Select input directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// run the file chooser and check the user didn't hit cancel
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// get the files in the selected directory and match them to
			// the files we need.
			File directory = fileChooser.getSelectedFile();
			File[] files = directory.listFiles();

			for (File f : files) {
				if (f.getName().equals(TRAINSET_FILENAME)) {
					train = f;
				} else if (f.getName().equals(TESTSET_FILENAME)) {
					test = f;
				}
			}

			// check none of the files are missing, and call the load
			// method in your code.
			if (train == null || test == null) {
				JOptionPane.showMessageDialog(null, "Directory does not contain correct files", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} else {
				readFile(train, test);
			}
		}
	}
	
	private static void readFile(File train, File test) {
		try {
			String line = null;
			
			System.out.println("Loading training.");
			BufferedReader data = new BufferedReader(new FileReader(train));
			while ((line = data.readLine()) != null) {
				trainEmails.add(new Email(line,NUM_ATTRIBUTES));
			}
			data.close();
			
			System.out.println("Loading test.");
			data = new BufferedReader(new FileReader(test));
			while ((line = data.readLine()) != null) {
				testEmails.add(new Email(line));
			}
			data.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
