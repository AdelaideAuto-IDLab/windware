package au.edu.adelaide.autoidlab.windware.core;

import org.fosstrak.ale.server.Tag;

/**
 * This interface represents the identification of the sensor data
 * 
 * @author Yu Yan
 * @date 14/04/2013
 */
public interface DataExtractor {
	
	/**
	 * This method deembeds the the EPC.
	 * 
	 * @param tag needs to be deemeded
	 */
	void deembed (Tag tag);
	
	/**
	 * This method gets the strings for one tag type.
	 * 
	 * @return a string array
	 */
	String[] getTypes();
	
}
