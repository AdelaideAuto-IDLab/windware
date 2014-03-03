package au.edu.adelaide.autoidlab.windware.operation;

import org.fosstrak.ale.server.Tag;

import au.edu.adelaide.autoidlab.windware.core.DataExtraction;

/**
 * This class conducts the calculation for acceleration sensor data
 * 
 * @author Yu Yan
 * @date 15/04/2013
 */
public class AccelerationImpl implements DataExtraction{
	
	/** the factor for acceleration standard tag. */
	private static float ALPHA_STANDARD = 1f;
	
	/** the factor for acceleration quick tag. */
	private static float ALPHA_QUICK = 1.16f;
	
	/** Tag type for quick acceleration. */
	private static String QUICK = "0b";
	
	/** Tag type for standard acceleration. */
	private static String STANDARD = "0d";
	
	/** Prefix of wisp pure URI */
	private static String WISP_PURE_PREFIX = "urn:epc:id:wisp:";
	
	/** Prefix of wisp pure URI */
	private static String CLEAR_SENSOR = "0000000000000000";
	
	/**
	 * Constructor
	 */
	public AccelerationImpl(){};

	@Override
	public void deembeding(Tag tag) {
		// TODO data deembeding and set the sensor value to thte tag
		String binary = tag.getTagAsBinary();
		String epc = binaryToBytes(binary);
		
		String pure = tag.getTagIDAsPureURI();
		String[] purePrefix = pure.split(":");
		String pure_prefix = "";
		
		for(int i = 0; i < purePrefix.length - 1; i++)
		{
			pure_prefix += purePrefix[i] + ":";
		}
		
		String[] deembed = new String[4];
		
		deembed[0] = epc.substring(0, 2);
		deembed[1] = epc.substring(2, 18);
		deembed[2] = epc.substring(18, 20);
		deembed[3] = epc.substring(20);
		
		//if(deembed[0].equals("3D") || deembed[0].equals("3d"))
		if(pure_prefix.equals(WISP_PURE_PREFIX));
		{
			tag.setIsSensor(true);
		}
		
		String sensordata = deembed[1];
		tag.getSensorData().setSensordata(sensordata);
		
		String x = deembed[1].substring(0, 4);
		int x_data = Integer.parseInt(x, 16);
		float acceleration_x = 0;
		if (deembed[2].equals(QUICK) || deembed[0].equals(STANDARD))
		{
			acceleration_x = 100 * ALPHA_QUICK * x_data / 1024;
		}
		else
		{
			acceleration_x = 100 * ALPHA_STANDARD * x_data / 1024;
		}
		float x_axis = 100 - acceleration_x;
		tag.getSensorData().setX_Axis(x_axis);
		
		String y = deembed[1].substring(4, 8);
		int y_data = Integer.parseInt(y, 16);
		float acceleration_y = 0;
		if (deembed[2].equals(QUICK) || deembed[0].equals(STANDARD))
		{
			acceleration_y = 100 * ALPHA_QUICK * y_data / 1024;
		}
		else
		{
			acceleration_y = 100 * ALPHA_STANDARD * y_data / 1024;
		}
		float y_axis = 100 - acceleration_y;
		tag.getSensorData().setY_Axis(y_axis);
		
		String z = deembed[1].substring(8, 12);
		int z_data = Integer.parseInt(z, 16);
		float acceleration_z = 0;
		if (deembed[2].equals(QUICK) || deembed[0].equals(STANDARD))
		{
			acceleration_z = 100 * ALPHA_QUICK * z_data / 1024;
		}
		else
		{
			acceleration_z = 100 * ALPHA_STANDARD * z_data / 1024;
		}
		float z_axis = acceleration_z;
		tag.getSensorData().setZ_Axis(z_axis);
		
		// Do corrections
    //todo: Ideally these values need to be looked up for each tag
		double corx = 1.119;
		double cory = 1.105;
		double corz = 0.8741;
		double dix = 11.62;
		double diy = 11.3;
		double diz =8.79; 

		float xp = (float) ((tag.getSensorData().getX_Axis())*corx);
		float yp = (float) (tag.getSensorData().getY_Axis()*cory);
		float zp =(float) (tag.getSensorData().getZ_Axis()*corz);

		tag.getSensorData().setX_Axis((float) ((xp-50)/dix));
		tag.getSensorData().setY_Axis((float) ((yp-50)/diy));
		tag.getSensorData().setZ_Axis((float) ((zp-50)/diz));
		
		String hw = deembed[3];
		tag.getSensorData().setHWSerial(hw);
		
		tag.getSensorData().setSensordata(CLEAR_SENSOR);
		
	}
	
	/**
	 * This method transfers binary code to 12 bytes epc
	 * 
	 * @param a 96-bit binary requires to be transfered
	 */
	private String binaryToBytes (String binary)
	{
		String result = "";
		String[] epcByte = new String[24];
		if (binary.length() == 96)
		{
			String[] epc = new String[24];
			for (int i = 0; i < 96; i = i + 4)
			{
				epc[i/4] = binary.substring(i, i + 4);
			}

			for (int j = 0; j < epc.length; j++)
			{
				epcByte[j] = Integer.toHexString(Integer.parseInt(epc[j], 2));
			}
		}
		else
		{
			System.out.print("Not 96 bits");
			System.exit(0);
		}
		
		for(int k = 0; k < epcByte.length; k++)
		{
			result += epcByte[k];
		}
		return result;
	}

	@Override
	public String[] getTypes() {
		return new String[]{"0D","0B","0d", "0b"};
	}

}
