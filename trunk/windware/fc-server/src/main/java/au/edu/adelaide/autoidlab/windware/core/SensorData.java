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
	/** sensor data. */
	private String sensordata;
	/** HW Serial #. */
	private String hwSerial;
	
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
	 * get the sensor data field
	 */
	public String getSensorData() {
		return sensordata;
	}

	/**
	 * get the hwSerial
	 */
	public String getHWSerial() {
		return hwSerial;
	}

	/**
	 * set z axis
	 * 
	 * @param z the z axis
	 */
	public void setSensordata(String sensordata) {
		this.sensordata = sensordata;
	}

	/**
	 * set the hwSerial
	 */
	public void setHWSerial(String hwSeiral) {
		this.hwSerial = hwSeiral;
	}
}
