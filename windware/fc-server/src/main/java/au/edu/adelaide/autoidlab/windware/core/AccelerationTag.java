package au.edu.adelaide.autoidlab.windware.core;
/**
 * This interface represents the identification of the sensor data
 * 
 * @author Yu Yan
 * @date 15/04/2013
 */
public class AccelerationTag extends SensorData{

	/** x Axis of acceleration. */
	private float x_Axis;
	
	/** y Axis of acceleration. */
	private float y_Axis;
	
	/** z Axis of acceleration. */
	private float z_Axis;
	
	/** sensor data. */
	private String sensordata;
	
	/** HW Serial #. */
	private String hwSerial;
	
	/**
	 * Constructor
	 */
	public AccelerationTag(String sensorDataType)
	{
		super(sensorDataType);
	}
	
	/**
	 * Constructor
	 */
	public AccelerationTag()
	{
		super();
	}
	
	/**
	 * get the x axis
	 */
	public float getX_Axis()
	{
		return x_Axis;
	}
	
	/**
	 * get the y axis
	 */
	public float getY_Axis()
	{
		return y_Axis;
	}
	
	/**
	 * get the z axis
	 */
	public float getZ_Axis()
	{
		return z_Axis;
	}
	
	/**
	 * get the sensor data field
	 */
	public String getSensorData()
	{
		return sensordata;
	}
	
	/**
	 * get the hwSerial
	 */
	public String getHWSerial()
	{
		return hwSerial;
	}
	
	/**
	 * set x axis
	 * 
	 * @param x the x axis
	 */
	public void setX_Axis(float x)
	{
		this.x_Axis = x;
	}
	
	/**
	 * set y axis
	 * 
	 * @param y the y axis
	 */
	public void setY_Axis(float y)
	{
		this.y_Axis = y;
	}
	
	/**
	 * set z axis
	 * 
	 * @param z the z axis
	 */
	public void setZ_Axis(float z)
	{
		this.z_Axis = z;
	}
	
	/**
	 * set z axis
	 * 
	 * @param z the z axis
	 */
	public void setSensordata(String sensordata)
	{
		this.sensordata = sensordata;
	}
	
	/**
	 * set the hwSerial
	 */
	public void setHWSerial(String hwSeiral)
	{
		this.hwSerial = hwSeiral;
	}
	
}
