package au.edu.adelaide.autoidlab.windware.acceleration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;
import org.fosstrak.ale.xsd.ale.epcglobal.Value;

import au.edu.adelaide.autoidlab.windware.core.Operation;

public class DataOperation implements Operation {
	
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
		// TODO nothing for this operation
		return null;
	}

	@Override
	public ArrayList<MultiValue> getMultiValue(List<Tag> tags, Map<String, String> args, String id) 
	{
		// TODO get all multiple values for DATA operation
		ArrayList<MultiValue> multi_value = new ArrayList<MultiValue>();
		
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
		
		// X Y Z
		MultiValue x_multi = new MultiValue();
		x_multi.setType(TYPE_FLOAT);
		x_multi.setName(X);
		x_multi.setOperationID(id);
		
		for(int m=0;m<count;m++)
		{
			Value v = new Value();
			AccelerationTag tag = null;
			Tag data = tags.get(m);
			if(data.getSensorData() instanceof AccelerationTag){
				tag = (AccelerationTag) data.getSensorData();
			}else{
				continue;
			}
			v.setKey(String.valueOf(tags.get(m).getTimestamp()));
			v.setValue(String.valueOf(tag.getX_Axis()));
			
			float x_value = tag.getX_Axis();
			
			if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				float less = Float.parseFloat(args.get(LESS_THAN));
				if(x_value <= less)
				{
					x_multi.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				float greater = Float.parseFloat(args.get(GREATER_THAN));
				if(x_value >= greater)
				{
					x_multi.getValue().add(v);
				}
			}
			else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				float greater = Float.parseFloat(args.get(GREATER_THAN));
				float less = Float.parseFloat(args.get(LESS_THAN));
				if(x_value >= greater && x_value <= less)
				{
					x_multi.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				x_multi.getValue().add(v);
			}
			
		}
		
		MultiValue y_multi = new MultiValue();
		y_multi.setType(TYPE_FLOAT);
		y_multi.setName(Y);
		y_multi.setOperationID(id);
		
		for(int m=0;m<count;m++)
		{
			Value v = new Value();
			AccelerationTag tag = null;
			Tag data = tags.get(m);
			if(data.getSensorData() instanceof AccelerationTag){
				tag = (AccelerationTag) data.getSensorData();
			}else{
				continue;
			}
			
			v.setKey(String.valueOf(tags.get(m).getTimestamp()));
			v.setValue(String.valueOf(tag.getY_Axis()));
			
			float y_value = tag.getY_Axis();
			
			if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				float less = Float.parseFloat(args.get(LESS_THAN));
				if(y_value <= less)
				{
					y_multi.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				float greater = Float.parseFloat(args.get(GREATER_THAN));
				if(y_value >= greater)
				{
					y_multi.getValue().add(v);
				}
			}
			else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				float greater = Float.parseFloat(args.get(GREATER_THAN));
				float less = Float.parseFloat(args.get(LESS_THAN));
				if(y_value >= greater && y_value <= less)
				{
					y_multi.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				y_multi.getValue().add(v);
			}
		}
		
		MultiValue z_multi = new MultiValue();
		z_multi.setType(TYPE_FLOAT);
		z_multi.setName(Z);
		z_multi.setOperationID(id);
		
		for(int m=0;m<count;m++)
		{
			Value v = new Value();
			
			AccelerationTag tag = null;
			Tag data = tags.get(m);
			if(data.getSensorData() instanceof AccelerationTag){
				tag = (AccelerationTag) data.getSensorData();
			}else{
				continue;
			}
			
			v.setKey(String.valueOf(tags.get(m).getTimestamp()));
			v.setValue(String.valueOf(tag.getZ_Axis()));
			
			float z_value = tag.getZ_Axis();
			
			if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				float less = Float.parseFloat(args.get(LESS_THAN));
				if(z_value <= less)
				{
					z_multi.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				float greater = Float.parseFloat(args.get(GREATER_THAN));
				if(z_value >= greater)
				{
					z_multi.getValue().add(v);
				}
			}
			else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				float greater = Float.parseFloat(args.get(GREATER_THAN));
				float less = Float.parseFloat(args.get(LESS_THAN));
				if(z_value >= greater && z_value <= less)
				{
					z_multi.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				z_multi.getValue().add(v);
			}
		}
		
		multi_value.add(x_multi);
		multi_value.add(y_multi);
		multi_value.add(z_multi);
				
		return multi_value;
	}

	@Override
	public String getName() {
		// TODO get the operation name
		return "Data";
	}
	
}
