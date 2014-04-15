package au.edu.adelaide.autoidlab.windware.acceleration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;
import org.fosstrak.ale.xsd.ale.epcglobal.Value;

import au.edu.adelaide.autoidlab.windware.core.Operation;

/**
 * represents FFT operation
 * @author Yu Yan
 * @date 28/05/2013
 * 
 *
 */
public class FFTOperation implements Operation
{
	/** the start of the data stream */
	private static String FROM = "start";
	
	/** the end of the data stream */
	private static String END = "end";
	
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
	
	/** Type double */
	private static String TYPE_DOUBLE = "double";

	@Override
	public ArrayList<SingleValue> getSingleValue(List<Tag> tags, Map<String, String> args, String id) {
		// TODO nothing for FFT
		return null;
	}

	@Override
	public ArrayList<MultiValue> getMultiValue(List<Tag> tags, Map<String, String> args, String id) {
		// TODO get all multiple values for FFT operation
		ArrayList<MultiValue> multi_value = new ArrayList<MultiValue>();
		
		MultiValue fft_x = new MultiValue();
		fft_x.setName(X);
		fft_x.setType(TYPE_DOUBLE);
		fft_x.setOperationID(id);
		double[] fft_x_axis = fft_x(tags, args);
		
		for(int m=0; m<fft_x_axis.length; m++)
		{
			Value v = new Value();
			v.setKey(String.valueOf(m));
			v.setValue(String.valueOf(fft_x_axis[m]));
			
			if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				double less = Double.parseDouble(args.get(LESS_THAN));
				double x_value = fft_x_axis[m];
				if(x_value <= less)
				{
					fft_x.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				double greater = Double.parseDouble(args.get(GREATER_THAN));
				double x_value = fft_x_axis[m];
				if(x_value >= greater)
				{
					fft_x.getValue().add(v);
				}
			}
			else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				double greater = Double.parseDouble(args.get(GREATER_THAN));
				double less = Double.parseDouble(args.get(LESS_THAN));
				double x_value = fft_x_axis[m];
				if(x_value >= greater && x_value <= less)
				{
					fft_x.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				fft_x.getValue().add(v);
			}
			
		}
		
		MultiValue fft_y = new MultiValue();
		fft_y.setName(Y);
		fft_y.setType(TYPE_DOUBLE);
		fft_y.setOperationID(id);
		double[] fft_y_axis = fft_y(tags, args);
		
		for(int m=0; m<fft_y_axis.length; m++)
		{
			Value v = new Value();
			v.setKey(String.valueOf(m));
			v.setValue(String.valueOf(fft_y_axis[m]));
			
			if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				double less = Double.parseDouble(args.get(LESS_THAN));
				double y_value = fft_y_axis[m];
				if(y_value <= less)
				{
					fft_y.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				double greater = Double.parseDouble(args.get(GREATER_THAN));
				double y_value = fft_y_axis[m];
				if(y_value >= greater)
				{
					fft_y.getValue().add(v);
				}
			}
			else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				double greater = Double.parseDouble(args.get(GREATER_THAN));
				double less = Double.parseDouble(args.get(LESS_THAN));
				double y_value = fft_y_axis[m];
				if(y_value >= greater && y_value <= less)
				{
					fft_y.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				fft_y.getValue().add(v);
			}
		}
		
		MultiValue fft_z = new MultiValue();
		fft_z.setName(Z);
		fft_z.setType(TYPE_DOUBLE);
		fft_z.setOperationID(id);
		double[] fft_z_axis = fft_z(tags, args);
		
		for(int m=0; m<fft_z_axis.length; m++)
		{
			Value v = new Value();
			v.setKey(String.valueOf(m));
			v.setValue(String.valueOf(fft_z_axis[m]));
			
			double z_value = fft_z_axis[m];
			
			if(args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				double less = Double.parseDouble(args.get(LESS_THAN));
				if(z_value <= less)
				{
					fft_z.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				double greater = Double.parseDouble(args.get(GREATER_THAN));
				if(z_value >= greater)
				{
					fft_z.getValue().add(v);
				}
			}
			else if(args.containsKey(LESS_THAN) && args.containsKey(GREATER_THAN))
			{
				double less = Double.parseDouble(args.get(LESS_THAN));
				double greater = Double.parseDouble(args.get(GREATER_THAN));
				if(z_value >= greater && z_value <= less)
				{
					fft_z.getValue().add(v);
				}
			}
			else if(!args.containsKey(LESS_THAN) && !args.containsKey(GREATER_THAN))
			{
				fft_z.getValue().add(v);
			}
		}
		
		multi_value.add(fft_x);
		multi_value.add(fft_y);
		multi_value.add(fft_z);
		
		return multi_value;
	}

	@Override
	public String getName() {
		// TODO get the name of this operation
		return "FFT";
	}
	
	
	/**
	 * This method constructs FFT calculations for x axis.
	 * 
	 * @param tag is the list to do FFT
	 * @param args is the argument for FFT
	 */
	private double[] fft_x(List<Tag> tag, Map<String, String> args)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags = from_end(tag, args);
		
		powerTOtwo(tags);
		FastFourierTransformer fft_xs = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] fft_x_complex = new Complex[tags.size()];
		double[] fft_x_axis = new double[tags.size()];
		for(int i=0;i<tags.size();i++)
		{
			fft_x_axis[i] = ((AccelerationTag) tags.get(i).getSensorData()).getX_Axis();
		}
		fft_x_complex = fft_xs.transform(fft_x_axis, TransformType.FORWARD);
		double[] result = new double[fft_x_axis.length];
		for(int j=0;j<result.length;j++)
		{
			result[j] = fft_x_complex[j].abs();
		}
		return result;
	}
	
	/**
	 * This method constructs FFT calculations for y axis.
	 * 
	 * @param tag is the list to do FFT
	 * @param args is the argument for FFT
	 */
	private double[] fft_y(List<Tag> tag, Map<String, String> args)
	{
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags = from_end(tag, args);
		
		powerTOtwo(tags);
		
		FastFourierTransformer fft_ys = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] fft_y_complex = new Complex[tags.size()];
		double[] fft_y_axis = new double[tags.size()];
		for(int i=0;i<tags.size();i++)
		{
			fft_y_axis[i] = ((AccelerationTag) tags.get(i).getSensorData()).getY_Axis();
		}
		fft_y_complex = fft_ys.transform(fft_y_axis, TransformType.FORWARD);
		double[] result = new double[fft_y_axis.length];
		for(int j=0;j<result.length;j++)
		{
			result[j] = fft_y_complex[j].abs();
		}
		return result;
	}
	
	/**
	 * This method constructs FFT calculations for z axis.
	 * 
	 * @param tag is the list to do FFT
	 * @param args is the argument for FFT
	 */
	private double[] fft_z(List<Tag> tag, Map<String, String> args)
	{
		
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags = from_end(tag, args);
		
		powerTOtwo(tags);
		FastFourierTransformer fft_zs = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] fft_z_complex = new Complex[tags.size()];
		double[] fft_z_axis = new double[tags.size()];
		for(int i=0;i<tags.size();i++)
		{
			fft_z_axis[i] = ((AccelerationTag) tags.get(i).getSensorData()).getZ_Axis();
		}
		fft_z_complex = fft_zs.transform(fft_z_axis, TransformType.FORWARD);
		double[] result = new double[fft_z_axis.length];
		for(int j=0;j<result.length;j++)
		{
			result[j] = fft_z_complex[j].abs();
		}
		return result;
	}
	
	/**
	 * This method makes the tag list to the size of the power of two
	 * 
	 * @param tag is the tag list
	 */
	private ArrayList<Tag> powerTOtwo(ArrayList<Tag> tags)
	{
		double number = Math.log10(tags.size()) / Math.log10(2);
		int number_int = (int) number;
		if(number_int != number)
		{
			number_int += 1;
		}
		int power = (int) Math.pow(2, number_int);
		Tag t = tags.get(tags.size()-1);
		int tagsize = tags.size();
		for(int i=tagsize;i<power;i++)
		{
			tags.add(t);
		}
		return tags;
	}
	
	/**
	 * This method takes the arguments and set the start and the end point to do FFT
	 * 
	 * @param tag is the list
	 * @param args is the argument
	 */
	private ArrayList<Tag> from_end (List<Tag> tag, Map<String, String> args)
	{
		int from = 0;
		int end = 0;
		if(args.containsKey(FROM) && args.containsKey(END))
		{
			String from_v = args.get(FROM);
			String end_v = args.get(END);
			from = Integer.parseInt(from_v);
			end = Integer.parseInt(end_v);
		}
		else if(!args.containsKey(FROM) && args.containsKey(END))
		{
			from = 0;
			String end_v = args.get(END);
			end = Integer.parseInt(end_v);
		}
		else if(args.containsKey(FROM) && !args.containsKey(END))
		{
			end = tag.size();
			String from_v = args.get(FROM);
			from = Integer.parseInt(from_v);
		}
		else
		{
			from = 0;
			end = tag.size();
		}
		
		if(end > tag.size())
		{
			end = tag.size();
		}
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (int index = from; index < end; index ++)
		{
			tags.add(tag.get(index));
		}
		
		return tags;
	}
	
	
	
}
