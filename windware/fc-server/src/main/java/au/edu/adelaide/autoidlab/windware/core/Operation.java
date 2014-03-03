package au.edu.adelaide.autoidlab.windware.core;

import java.util.List;
import java.util.Map;

import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;

/**
 * This interface represents the operation on all sensor data in one event cycle
 * 
 * @author Yu Yan
 * @date 16/04/2013
 */
public interface Operation {
	
	/**
	 * This method get the single values of one operation.
	 * 
	 * @param tags defines the tag list that requires to do the operation
	 * @param args defines the hashmap for the arguments
	 * @param id is the operationID
	 * 
	 * @return a single value list to store all single value for operations
	 */
	List<SingleValue> getSingleValue (List<Tag> tags, Map<String, String> args, String id);
	
	/**
	 * This method get the multiple values of one operation.
	 * 
	 * @param tags defines the tag list that requires to do the operation
	 * @param args defines the hashmap for the arguments
	 * @param id is the operationID
	 * 
	 * @return a multiple value list to store all multiple value for operations
	 */
	List<MultiValue> getMultiValue (List<Tag> tags, Map<String, String> args, String id);
	
	/**
	 * This method gets the name of the operation.
	 * 
	 * @return the name of the operation
	 */
	String getName();

}
