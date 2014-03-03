package au.edu.adelaide.autoidlab.windware.operation;

import java.util.ArrayList;
import java.util.Hashtable;
import org.fosstrak.ale.server.Tag;

import au.edu.adelaide.autoidlab.windware.core.Operation;

/**
 * manages all data operations
 * @author Yu Yan
 * @date 22/05/2013
 *
 */
public class DataOperationManager {

	/** Map the operation name and operation */
	Hashtable<String, Operation> tag_operation = new Hashtable<String, Operation>();
	
	/** Map the tag type and list of tags */
	Hashtable<String, ArrayList<Tag>> taggroups = new Hashtable<String, ArrayList<Tag>>();
	
	/**
	 * Constructor
	 */
	public DataOperationManager()
	{
		//TODO: need to get the definitions from spring configuration
		tag_operation.put("Average", new AverageOperation());
		tag_operation.put("FFT", new FFTOperation());
		tag_operation.put("Sum", new SumOperation());
		tag_operation.put("Data", new DataOperation());
		ResultantAcceleration ra = new ResultantAcceleration();
		tag_operation.put(ra.getName(), ra);

	}
	
	/**
	 * gets the specific operation
	 * 
	 * @return a map to map the operation names and operations
	 */
	public Hashtable<String, Operation> get_tag_operation()
	{
		return this.tag_operation;
	}
	
	/**
	 * group different types of tags
	 */
	public void groupData (ArrayList<Tag> tags)
	{
		//group different types of data
		for(int i=0;i<tags.size();i++)
		{
			String type = tags.get(i).getSensorData().getSensorDataType();
			if(taggroups.containsKey(type))
			{
				taggroups.get(type).add(tags.get(i));
			}
			else
			{
				ArrayList<Tag> new_group = new ArrayList<Tag>();
				new_group.add(tags.get(i));
				taggroups.put(type, new_group);
			}
			
		}
	}
	
}
