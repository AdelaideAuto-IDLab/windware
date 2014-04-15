package au.edu.adelaide.autoidlab.windware.acceleration;

import au.edu.adelaide.autoidlab.windware.core.SensorData;

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
	
}
