package au.edu.adelaide.autoidlab.windware.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;

import au.edu.adelaide.autoidlab.windware.core.Operation;

/**
 * represents average operation
 * @author Yu Yan
 * @date 28/05/2013
 *
 */
public class AverageOperation implements Operation {
	
	/** argument total */
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
		// TODO get all single values for average operation
		ArrayList<SingleValue> single_value = new ArrayList<SingleValue>();
		
		SingleValue sv_aver_x = new SingleValue();
		float x_ave = this.x_aveg(tags, args);
		sv_aver_x.setName(X);
		sv_aver_x.setType(TYPE_FLOAT);
		sv_aver_x.setOperationID(id);
		sv_aver_x.setValue(x_ave);
		
		SingleValue sv_aver_y = new SingleValue();
		float y_ave = this.y_aveg(tags, args);
		sv_aver_y.setName(Y);
		sv_aver_y.setType(TYPE_FLOAT);
		sv_aver_y.setOperationID(id);
		sv_aver_y.setValue(y_ave);
		
		SingleValue sv_aver_z = new SingleValue();
		float z_ave = this.z_aveg(tags, args);
		sv_aver_z.setName(Z);
		sv_aver_z.setType(TYPE_FLOAT);
		sv_aver_z.setOperationID(id);
		sv_aver_z.setValue(z_ave);
		
		if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
		{
			float v = Float.parseFloat(args.get(LESS_THAN));
			if(x_ave <= v)
			{
				single_value.add(sv_aver_x);
			}
			if(y_ave <= v)
			{
				single_value.add(sv_aver_y);
			}
			if(z_ave <= v)
			{
				single_value.add(sv_aver_z);
			}
		}
		else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
		{
			float v = Float.parseFloat(args.get(GREATER_THAN));
			if(x_ave >= v)
			{
				single_value.add(sv_aver_x);
			}
			if(y_ave >= v)
			{
				single_value.add(sv_aver_y);
			}
			if(z_ave >= v)
			{
				single_value.add(sv_aver_z);
			}
		}
		else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
		{
			float great = Float.parseFloat(args.get(GREATER_THAN));
			float less = Float.parseFloat(args.get(LESS_THAN));
			if(x_ave >= great && x_ave <= less)
			{
				single_value.add(sv_aver_x);
			}
			if(y_ave >= great && y_ave <= less)
			{
				single_value.add(sv_aver_y);
			}
			if(z_ave >= great && z_ave <= less)
			{
				single_value.add(sv_aver_z);
			}
		}
		else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
		{
			single_value.add(sv_aver_x);
			single_value.add(sv_aver_y);
			single_value.add(sv_aver_z);
		}
		
		return single_value;
	}

	@Override
	public ArrayList<MultiValue> getMultiValue(List<Tag> tags, Map<String, String> args, String id) {
		// TODO nothing for average
		return null;
	}

	@Override
	public String getName() {
		// TODO get the average operation name
		return "Average";
	}
	
	/**
	 * This method calculates the average of x axis.
	 * 
	 * @param tags
	 */
	private float x_aveg(List<Tag> tags, Map<String, String> args)
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
		float aveg = sum / count ;
		
		return aveg;
	}
	
	/**
	 * This method calculates the average of y axis.
	 * 
	 * @param tags
	 */
	private float y_aveg(List<Tag> tags, Map<String, String> args)
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
			sum += tags.get(i).getSensorData().getY_Axis();
		}
		float aveg = sum / count;
		
		return aveg;
	}
	
	/**
	 * This method calculates the average of z axis.
	 * 
	 * @param tags
	 */
	private float z_aveg(List<Tag> tags, Map<String, String> args)
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
			sum += tags.get(i).getSensorData().getZ_Axis();
		}
		float aveg = sum / count;
		
		return aveg;
	}

}
