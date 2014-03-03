package au.edu.adelaide.autoidlab.windware.core;

/**
 * This class represents the sensor data of the tag
 * 
 * @author Yu Yan
 * @date 15/04/2013
 */
public abstract class SensorData {

	/** sensor data type. */
	private String sensorDataType;
	
	/**
	 * Constructor
	 */
	public SensorData() {}
	
	/**
	 * Constructor
	 */
	public SensorData(String sensorDataType)
	{
		this.sensorDataType = sensorDataType;
	}
	
	/**
	 * get the sensor data type
	 */
	public String getSensorDataType()
	{
		return sensorDataType;
	}
	
	/**
	 * This method sets the sensor data type.
	 * 
	 * @param sensor data type (8 bits)
	 */
	public void setSensorDataType(String sensorType)
	{
		this.sensorDataType = sensorType;
	}
	
	/**
	 * This method gets the x axis.
	 */
	public abstract float getX_Axis();
	
	/**
	 * This method gets the y axis.
	 */
	public abstract float getY_Axis();
	
	/**
	 * This method gets the z axis.
	 */
	public abstract float getZ_Axis();
	
	/**
	 * This method gets the sensor data from an EPC.
	 */
	public abstract String getSensorData();
	
	/**
	 * This method gets the hardware serial number from an EPC.
	 */
	public abstract String getHWSerial();
	
	/**
	 * This method sets the x axis to the tag.
	 * 
	 * @param x axis
	 */
	public abstract void setX_Axis(float x);
	
	/**
	 * This method sets the y axis to the tag.
	 * 
	 * @param y axis
	 */
	public abstract void setY_Axis(float y);
	
	/**
	 * This method sets the z axis to the tag.
	 * 
	 * @param z axis
	 */
	public abstract void setZ_Axis(float z);
	
	/**
	 * This method sets the sensor data to the tag.
	 * 
	 * @param sensor data
	 */
	public abstract void setSensordata(String sensordata);
	
	/**
	 * This method sets the hardware serial number to the tag.
	 * 
	 * @param hardware serial number
	 */
	public abstract void setHWSerial(String hwSeiral);
}
