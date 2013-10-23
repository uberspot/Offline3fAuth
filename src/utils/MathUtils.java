package utils;

import java.util.Collection;
import java.util.Iterator;


/**
 * Some math utilities.
 * @author Alan Zanoni Peixinho
 *
 */
public class MathUtils {
	/**
	 * Compute the Chi-Square distance between samples.
	 * @param i1 First sample.
	 * @param i2 Second sample.
	 * @return Return the Chi-Square sample between samples.
	 */
	public static double chiSquareDistance(Collection<Double> i1, Collection<Double> i2){
		
		double dist = 0.0;
		double diff;
		double v1, v2;
		double sum;
		assert(i1.size()==i2.size()):"Dimensions must agree.";
		
		Iterator<Double> it1 = i1.iterator();
		Iterator<Double> it2 = i2.iterator();
		
		//Log.v("Info", String.format("Features Number - distance function: %d %d", i1.size(), i2.size()));
		
		int nFeatures = i1.size();
		for (int i = 0; i < nFeatures; i++)
		{
		   v1 = it1.next();
		   v2 = it2.next();
		   
		   diff = v1 - v2;
		   sum = v1 + v2;
		   
		   //Log.v("Chi Square", String.format("%f, %f", v1, v2));
		   
		   if(sum>0.0)
			   dist += (diff*diff)/sum;
		}//while
		
		return 0.5*dist;
	}
	
	/**
	 * Compute the Euclidean distance between samples.
	 * @param i1 First sample.
	 * @param i2 Second Sample.
	 * @return  Return the Euclidean distance between samples.
	 */
	public static double euclideanDistance(Collection<Double> i1, Collection<Double> i2){
		double dist = 0.0;
		double value;
		
		assert(i1.size()==i2.size()):"Dimensions must agree.";
		
		Iterator<Double> it1 = i1.iterator();
		Iterator<Double> it2 = i2.iterator();
		
		while(it1.hasNext() && it2.hasNext())
		{
		   value = it1.next() - it2.next();
		   
		   dist += value*value;
		}//while
		
		return Math.sqrt(dist);
	}
	
	/**
	 * Computes the set mean.
	 * @param c Values set.
	 * @return Return the average value of the set.
	 */
	public static double mean(Collection<Double> c)
    {
    	double sum = 0.0;
    	for (Double t : c) {
			sum+= t;
		}
    	
		return sum/c.size();	
    }
    
	/**
	 * Computes the standard deviation of a set, given the average value.
	 * @param c Values set
	 * @param mean Average value of the set.
	 * @return Return the standard deviation of the set.
	 */
    public static double stdDeviation(Collection<Double> c, double mean)
    {
    	double var = variance(c, mean);
    	return Math.sqrt(var/(c.size()-1));
    }
    
    public static double variance(Collection<Double> c, double mean){
    	double sum = 0.0;
    	
    	for (Double i: c) {
			double cur = i-mean;
    		sum+=cur*cur;
    	}
    	
    	return sum;
    }
    
    public static double variance(Collection<Double> c){
    	return variance(c, mean(c));
    }
    
    /**
	 * Computes the standard deviation of a set.
	 * @param c Values set
	 * @return Return the standard deviation of the set.
	 */
    public static double stdDeviation(Collection<Double> c){
    	return stdDeviation(c, mean(c));
    }
	
}
