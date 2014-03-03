package au.edu.adelaide.autoidlab.windware.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;

import au.edu.adelaide.autoidlab.windware.core.Operation;

public class SumOperation implements Operation {
	
	/** argument name */
	private static String TOTAL = "total";
	
	/** argument less than */
	private static String LESS_THAN = "less than";
	
	/** argument greater than */
	private static String GREATER_THAN = "greater than";
	
	/** X axis */
	private static String X = "x";
	
	/** Y axis */
	private static String Y = "y";
	
	/** Z axis */
	private static String Z = "z";
	
	/** Type float */
	private static String TYPE_FLOAT = "float";

	@Override
	public ArrayList<SingleValue> getSingleValue(List<Tag> tags, Map<String, String> args, String id) {
		// TODO get all single values for sum operation
		ArrayList<SingleValue> single_value = new ArrayList<SingleValue>();
		
		SingleValue sv_sum_x = new SingleValue();
		float x_sum = this.x_sum(tags, args);
		sv_sum_x.setName(X);
		sv_sum_x.setType(TYPE_FLOAT);
		sv_sum_x.setValue(x_sum);
		sv_sum_x.setOperationID(id);
		
		SingleValue sv_sum_y = new SingleValue();
		float y_sum = this.y_sum(tags, args);
		sv_sum_y.setName(Y);
		sv_sum_y.setType(TYPE_FLOAT);
		sv_sum_y.setValue(y_sum);
		sv_sum_y.setOperationID(id);
		
		SingleValue sv_sum_z = new SingleValue();
		float z_sum = this.z_sum(tags, args);
		sv_sum_z.setName(Z);
		sv_sum_z.setType(TYPE_FLOAT);
		sv_sum_z.setValue(z_sum);
		sv_sum_z.setOperationID(id);
		
		if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
		{
			float v = Float.parseFloat(args.get(LESS_THAN));
			if(x_sum <= v)
			{
				single_value.add(sv_sum_x);
			}
			if(y_sum <= v)
			{
				single_value.add(sv_sum_y);
			}
			if(z_sum <= v)
			{
				single_value.add(sv_sum_z);
			}
		}
		else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
		{
			float v = Float.parseFloat(args.get(GREATER_THAN));
			if(x_sum >= v)
			{
				single_value.add(sv_sum_x);
			}
			if(y_sum >= v)
			{
				single_value.add(sv_sum_y);
			}
			if(z_sum >= v)
			{
				single_value.add(sv_sum_z);
			}
		}
		else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
		{
			float great = Float.parseFloat(args.get(GREATER_THAN));
			float less = Float.parseFloat(args.get(LESS_THAN));
			if(x_sum >= great && x_sum <= less)
			{
				single_value.add(sv_sum_x);
			}
			if(y_sum >= great && y_sum <= less)
			{
				single_value.add(sv_sum_y);
			}
			if(z_sum >= great && z_sum <= less)
			{
				single_value.add(sv_sum_z);
			}
		}
		else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
		{
			single_value.add(sv_sum_x);
			single_value.add(sv_sum_y);
			single_value.add(sv_sum_z);
		}
		
		
		return single_value;
	}

	@Override
	public ArrayList<MultiValue> getMultiValue(List<Tag> tags, Map<String, String> args, String id) {
		// TODO nothing for this operation
		return null;
	}

	@Override
	public String getName() {
		// TODO get the name of this operation
		return "Sum";
	}
	
	/**
	 * This method calculates the sum of x axis.
	 * 
	 * @param tags
	 */
	private float x_sum(List<Tag> tags, Map<String, String> args)
	{
		int number = 0;
		if(args.containsKey(TOTAL))
		{
			String arg = args.get(TOTAL);
			number = Integer.parseInt(arg);
		}
		else
		{
			number = tags.size();
		}
		
		int count = 0;
		if(tags.size() < number)
		{
			count = tags.size();
		}
		else
		{
			count = number;
		}
		
		float sum = 0f;
		for(int i = 0; i < count; i++)
		{
			sum += tags.get(i).getSensorData().getX_Axis();
		}
		
		return sum;
	}
	
	/**
	 * This method calculates the sum of y axis.
	 * 
	 * @param tags
	 */
	private float y_sum(List<Tag> tags, Map<String, String> args)
	{
		
		int number = 0;
		if(args.containsKey(TOTAL))
		{
			String arg = args.get(TOTAL);
			number = Integer.parseInt(arg);
		}
		else
		{
			number = tags.size();
		}
		
		float sum = 0f;
		int count = 0;
		if(tags.size() < number)
		{
			count = tags.size();
		}
		else
		{
			count = number;
		}
		for(int i = 0; i < count; i++)
		{
			sum += tags.get(i).getSensorData().getY_Axis();
		}
		
		return sum;
	}
	
	/**
	 * This method calculates the sum of z axis.
	 * 
	 * @param tags
	 */
	private float z_sum(List<Tag> tags, Map<String, String> args)
	{
		int number = 0;
		if(args.containsKey(TOTAL))
		{
			String arg = args.get(TOTAL);
			number = Integer.parseInt(arg);
		}
		else
		{
			number = tags.size();
		}
		
		float sum = 0f;
		int count = 0;
		if(tags.size() < number)
		{
			count = tags.size();
		}
		else
		{
			count = number;
		}
		for(int i = 0; i < count; i++)
		{
			sum += tags.get(i).getSensorData().getZ_Axis();
		}
		
		return sum;
	}
	

}
