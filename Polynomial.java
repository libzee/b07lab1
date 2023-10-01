import java.io.*;
import java.util.regex.*;

public class Polynomial {
	private double[] coefficients;
	private int[] exponents;
	
    public Polynomial() {
        this.coefficients = new double[] { 0 };
        this.exponents = new int[] { 0 };
    }
		
	public Polynomial(double[] coefficient, int[] exponents) {
		this.coefficients = coefficient;
		this.exponents = exponents;
	}
	
	public Polynomial add(Polynomial other) {
	    int maxLength = coefficients.length + other.coefficients.length;
	    double[] resultCoefficients = new double[maxLength];
	    int[] resultExponents = new int[maxLength];

	    int i = 0, j = 0, k = 0;

	    while (i < coefficients.length && j < other.coefficients.length) {
	        if (exponents[i] == other.exponents[j]) {
	            resultCoefficients[k] = coefficients[i] + other.coefficients[j];
	            resultExponents[k] = exponents[i];
	            i++;
	            j++;
	        } else if (exponents[i] > other.exponents[j]) {
	            resultCoefficients[k] = coefficients[i];
	            resultExponents[k] = exponents[i];
	            i++;
	        } else {
	            resultCoefficients[k] = other.coefficients[j];
	            resultExponents[k] = other.exponents[j];
	            j++;
	        }
	        k++;

	        // Check if result arrays need to be resized
	        if (k == maxLength) {
	            maxLength *= 2;
	            double[] newResultCoefficients = new double[maxLength];
	            int[] newResultExponents = new int[maxLength];
	            
	            System.arraycopy(resultCoefficients, 0, newResultCoefficients, 0, k);
	            System.arraycopy(resultExponents, 0, newResultExponents, 0, k);
	            
	            resultCoefficients = newResultCoefficients;
	            resultExponents = newResultExponents;
	        }
	    }

	    // Copy any remaining terms from both polynomials
	    while (i < coefficients.length) {
	        resultCoefficients[k] = coefficients[i];
	        resultExponents[k] = exponents[i];
	        i++;
	        k++;
	    }

	    while (j < other.coefficients.length) {
	        resultCoefficients[k] = other.coefficients[j];
	        resultExponents[k] = other.exponents[j];
	        j++;
	        k++;
	    }

	    // Trim the result arrays to the actual size
	    double[] finalResultCoefficients = new double[k];
	    int[] finalResultExponents = new int[k];
	    
	    System.arraycopy(resultCoefficients, 0, finalResultCoefficients, 0, k);
	    System.arraycopy(resultExponents, 0, finalResultExponents, 0, k);

	    return new Polynomial(finalResultCoefficients, finalResultExponents);
	}
	
	public Polynomial multiply(Polynomial other) {
	    int maxLength = coefficients.length * other.coefficients.length;
	    double[] resultCoefficients = new double[maxLength];
	    int[] resultExponents = new int[maxLength];

	    int k = 0;

	    for (int i = 0; i < coefficients.length; i++) {
	        for (int j = 0; j < other.coefficients.length; j++) {
	            double newCoefficient = coefficients[i] * other.coefficients[j];
	            int newExponent = exponents[i] + other.exponents[j];

	            // Check if there's already a term with the same exponent
	            boolean found = false;
	            for (int l = 0; l < k; l++) {
	                if (resultExponents[l] == newExponent) {
	                    resultCoefficients[l] += newCoefficient;
	                    found = true;
	                    break;
	                }
	            }

	            // If not found, add the new term to the result
	            if (!found) {
	                resultCoefficients[k] = newCoefficient;
	                resultExponents[k] = newExponent;
	                k++;
	            }
	        }
	    }

	    // Trim the result arrays to the actual size
	    double[] finalResultCoefficients = new double[k];
	    int[] finalResultExponents = new int[k];

	    System.arraycopy(resultCoefficients, 0, finalResultCoefficients, 0, k);
	    System.arraycopy(resultExponents, 0, finalResultExponents, 0, k);

	    return new Polynomial(finalResultCoefficients, finalResultExponents);
	}


    public Polynomial(File file) throws IOException {
        // Read the contents of the file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();

        if (line == null) {
            throw new IllegalArgumentException("File is empty.");
        }

        // Split the line into terms using regular expressions
        String[] termStrings = line.split("(?=[+-])");

        // Initialize arrays to store coefficients and exponents
        double[] parsedCoefficients = new double[termStrings.length];
        int[] parsedExponents = new int[termStrings.length];

        for (int i = 0; i < termStrings.length; i++) {
            String term = termStrings[i].trim();
            Matcher matcher = Pattern.compile("([-+]?\\d*\\.?\\d*)(x(\\d+))?").matcher(term);

            if (matcher.matches()) {
                String coefficientStr = matcher.group(1);
                String exponentStr = matcher.group(3);

                double coefficient = (coefficientStr.isEmpty()) ? 1.0 : Double.parseDouble(coefficientStr);
                int exponent = (exponentStr == null) ? 0 : Integer.parseInt(exponentStr);

                parsedCoefficients[i] = coefficient;
                parsedExponents[i] = exponent;
            } else {
                throw new IllegalArgumentException("Invalid polynomial format in the file.");
            }
        }

        // Set the coefficients and exponents
        this.coefficients = parsedCoefficients;
        this.exponents = parsedExponents;
    }
	
    public double evaluate(double x) {
        double result = 0;

        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }

        return result;
    }
	
	public boolean hasRoot(double x) {
		return evaluate(x) == 0;
	}

    public void saveToFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (int i = 0; i < coefficients.length; i++) {
            double coefficient = coefficients[i];
            int exponent = exponents[i];

            // Format the term based on the coefficient and exponent
            String term;
            if (exponent == 0) {
                term = String.valueOf(coefficient);
            } else if (exponent == 1) {
                term = coefficient + "x";
            } else {
                term = coefficient + "x" + exponent;
            }

            // Write the term to the file
            if (i == 0) {
                // The first term doesn't have a leading '+' or '-'
                writer.write(term);
            } else if (coefficient >= 0) {
                // Positive coefficient
                writer.write(" + " + term);
            } else {
                // Negative coefficient
                writer.write(" - " + term.substring(1)); // Skip the '-' character
            }
        }

        // Close the writer
        writer.close();
    }
}