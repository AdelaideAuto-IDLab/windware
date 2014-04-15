package au.edu.adelaide.autoidlab.windware.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.fosstrak.ale.server.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.edu.adelaide.autoidlab.windware.acceleration.AverageOperation;
import au.edu.adelaide.autoidlab.windware.acceleration.DataOperation;
import au.edu.adelaide.autoidlab.windware.acceleration.FFTOperation;
import au.edu.adelaide.autoidlab.windware.acceleration.ResultantAcceleration;
import au.edu.adelaide.autoidlab.windware.acceleration.SumOperation;

/**
 * manages all data operations
 * @author Yu Yan
 * @date 22/05/2013
 *
 */
@Component
public class DataOperationManager {

	/** Map the operation name and operation */
	Hashtable<String, Operation> tag_operation = new Hashtable<String, Operation>();
	
	/** Map the tag type and list of tags */
	Hashtable<String, ArrayList<Tag>> taggroups = new Hashtable<String, ArrayList<Tag>>();
	
	/**
	 * Constructor
	 */
	
	@Autowired
	private List<Operation> availableOperations;
	
	@PostConstruct
	public void iniPostConstruct(){
		if (availableOperations!=null){
			for(Operation op:availableOperations){
				tag_operation.put(op.getName(), op);
			}
		}
	}
	
	public DataOperationManager()
	{
		

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
