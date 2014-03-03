package org.fosstrak.ale.server;


import org.fosstrak.ale.util.HexUtil;

import au.edu.adelaide.autoidlab.windware.core.AccelerationTag;
import au.edu.adelaide.autoidlab.windware.core.SensorData;

/**
 * represents a tag that has been read on one of the readers in the Logical Reader API.
 * @author swieland
 * @author alessio orlando
 * @author roberto vergallo
 * @author wafa.soubra@orange.com
 * 
 * @author Yu Yan (Modified)
 *
 */
public class Tag {
	
	/** name of the (composite) reader where the tag has been read. */
	private String reader = null;
	
	/** name of the reader where the tag was read from a physicalReader. */
	private String origin  = null;
	
	/** id of this tag. */
	private byte[] tagID = null;
	
	/** id as pure uri*/
	private String tagIDAsPureURI = null;
	
	/** id as binary string. */
	private String binary = null;
	
	/** trace where the tag passed through the ALE.  */
	private String trace = null;
	
	/** timestamp when the tag occured. */
	private long timestamp = -1;
	
	/** the tag length. */
	private String tagLength = null;
	
	/** the filter value. */
	private String filter = null;
	
	/** the company prefix length. */
	private String companyPrefixLength = null;
	
	/** ORANGE: user memory of the tag. */
	private String userMemory = null;
	
	/** sensor data. */
	private SensorData sensorData = null;
	
	/** tag has sensor data or not. */
	private boolean isSensor = false;
	
	/**
	 * constructor for a tag. (default constructor).
	 */
	public Tag() {		
	}
	
	/**
	 * assignment constructor for Tag.
	 * @param origin reader where the tag was read originally 
	 * @param tagId the tagID
	 * @param timestamp the timestamp
	 */
	public Tag(String origin, byte[] tagId, String tagIDAsPureURI, long timestamp) {
		setOrigin(origin);
		setReader(origin);
		setTagID(tagId);
		setTagIDAsPureURI(tagIDAsPureURI);
		setTimestamp(timestamp);	
		//SensorData sensordata = new SensorData();
	}
	
	/**
	 * assignment constructor for Tag.
	 * @param origin reader where the tag was read originally 
	 * @param tagId the tagID
	 * @param timestamp the timestamp
	 * @param sensordata the one type of SensorData
	 */
	public Tag(String origin, byte[] tagId, String tagIDAsPureURI, long timestamp, SensorData sensordata) {
		setOrigin(origin);
		setReader(origin);
		setTagID(tagId);
		setTagIDAsPureURI(tagIDAsPureURI);
		setTimestamp(timestamp);		
		setSensorData(sensordata);
	}
	
	/**
	 * constructor for a tag.(assignment constructor).
	 * @param origin the baseReader where the tag has been read
	 */
	public Tag(String origin) {
		setOrigin(origin);
		setReader(origin);
	}
	
	/**
	 * constructs a tag from another existing tag. (copy constructor).
	 * @param tag the tag to be copied into a new tag
	 */
	public Tag(Tag tag) {
		setOrigin(tag.getOrigin());
		setTimestamp(tag.getTimestamp());
		setReader(tag.getReader());
		this.trace = tag.getTrace();
		setTagID(tag.getTagID());
	}
	
	/**
	 * set the sensor data.
	 * @return name of a logicalReader
	 */
	public void setSensorData(SensorData sensordata) {
		sensorData = sensordata;
	}
	
	/**
	 * get the sensor data.
	 * @return name of a logicalReader
	 */
	public SensorData getSensorData() {
		return sensorData;
	}

	/**
	 * gets the name of the reader that read this tag.
	 * @return name of a logicalReader
	 */
	public String getReader() {
		return reader;
	}

	/**
	 * sets the name of the reader that read this tag. when a
	 * reader is part of a composite reader then the reader will set
	 * to the name of the compositeReader. if you want to get the 
	 * original reader refer to origin.
	 * @param reader a name of a logicalReader
	 */
	public void setReader(String reader) {
		this.reader = reader;
	}

	/**
	 * returns the id of this tag.
	 * @return byte[] containing the tag id
	 */
	public byte[] getTagID() {
		return tagID;
	}

	/**
	 * sets the tag id.
	 * @param tagID a byte[] holding the tag id.
	 */
	public void setTagID(byte[] tagID) {
		this.tagID = tagID;
	}
	
	/**
	 * returns the timestamp when the tag occured.
	 * @return timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * sets the timestamp when a tag has been read.
	 * @param timestamp time when tag has been read.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * prepends a trace to the given trace.
	 * @param suffix  a trace path item
	 */
	public void addTrace(String suffix) {
		if (trace == null) {
			trace = suffix;
		} else {
			trace = trace + "-" + suffix;
		}
	}
	
	/** 
	 * returns the trace path of the tag.
	 * @return a string containing the tracepath
	 */
	public String getTrace() {
		return trace;
	}
	
	@Override
	public String toString() {
		return String.format("[Tag id: %s, ReaderName: %s, OriginName: %s, Trace: %s, Timestamp: %d, Binary: %s, PureID: %s]", 
				HexUtil.byteArrayToHexString(getTagID()), getReader(), getOrigin(), getTrace(), getTimestamp(), getTagAsBinary(), getTagIDAsPureURI());
	}

	/**
	 * returns the name of the baseReader where the tag has been read.
	 * @return returns the name of the baseReader where the tag has been read.
	 */
	public String getOrigin() {
		return origin;
	}
	
	/**
	 * sets the origin  (baseReader) where the tag has been read.
	 * @param origin the name of the baseReader where the tag has been read.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * comparator to check whether two tags are the same.
	 * @param tag the other tag to be checked.
	 * @return boolean value flagging whether equal or not
	 */
	public boolean equalsTag(Tag tag) {
		// if the origin in both tags is null, then do not take the 
		// origin into account when comparing
		if ((getOrigin() != null) && (tag.getOrigin() != null)) { 
			// compare the origin
			if (!tag.getOrigin().equalsIgnoreCase(getOrigin())) {
				return false;
			}
		} else if ((getOrigin() == null) || (tag.getOrigin() == null)) {
			return false;
		}
		
		// if the reader in both tags is null, then do not take the
		// reader into account when comparing
		if ((getReader() != null) && (tag.getReader() != null)) {
			// compare the reader
			if (!tag.getReader().equalsIgnoreCase(getReader()))  {
				return false;
			}
		} else if ((getReader() == null) || (tag.getReader() == null)) {
			return false;
		}
		
		// compare the tag id
		if ((null != tag.getTagIDAsPureURI()) && (null != getTagIDAsPureURI())) {
			// both not null, so compare
			if (!tag.getTagIDAsPureURI().equalsIgnoreCase(getTagIDAsPureURI())) {
				return false;
			}				
		} else if ((null != tag.getTagIDAsPureURI()) || (null != getTagIDAsPureURI())) {
			// only one null, therefore not equals
			return false;
		}
		
		// try to compare the binary value
		if ((null != tag.getTagAsBinary()) && (null != getTagAsBinary())) {
			// both not null, so compare
			if (!tag.getTagAsBinary().equalsIgnoreCase(getTagAsBinary())) {
				return false;
			}				
		} else if ((null != tag.getTagAsBinary()) || (null != getTagAsBinary())) {
			// only one null, therefore not equals
			return false;
		}
		
		return true;
	}
	
	public boolean equals(Tag tag) {
		return equalsTag(tag);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (binary == null) {
			if (other.binary != null)
				return false;
		} else if (!binary.equals(other.binary))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((binary == null) ? 0 : binary.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	/**
	 * returns the id of this tag as pure uri.
	 * @return String containing the tag id
	 */
	public String getTagIDAsPureURI() {
		return tagIDAsPureURI;
	}

	/**
	 * sets the tag id as pure uri.
	 * @param tagIDAsPureURI a string holding the tag id.
	 */
	public void setTagIDAsPureURI(String tagIDAsPureURI) {
		this.tagIDAsPureURI = tagIDAsPureURI;
	}
	
	/**
	 * sets the tag in binary format.
	 * @param binary the tag in binary format.
	 */
	public void setTagAsBinary(String binary) {
		this.binary = binary;
	}
	
	/**
	 * @return the tag in binary format.
	 */
	public String getTagAsBinary() {
		return binary;
	}
		
	/**
	 * @param tagLength the tagLength to set
	 */
	public void setTagLength(String tagLength) {
		this.tagLength = tagLength;
	}

	/**
	 * @return the tagLength
	 */
	public String getTagLength() {
		return tagLength;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param companyPrefixLength the companyPrefixLength to set
	 */
	public void setCompanyPrefixLength(String companyPrefixLength) {
		this.companyPrefixLength = companyPrefixLength;
	}

	/**
	 * @return the companyPrefixLength
	 */
	public String getCompanyPrefixLength() {
		return companyPrefixLength;
	}
	
	/**
	 * ORANGE: returns the user memory of this tag.
	 * @return String containing the tag user memory.
	 */
	public String getUserMemory() {
		return userMemory;
	}

	/**
	 * ORANGE: sets the user memory of this tag. 
	 * @param userMemory the String holding the tag user memory.
	 */
	public void setUserMemory(String userMemory) {
		this.userMemory = userMemory;
	}
	
	/**
	 * @return has sensor tag or not
	 */
	public boolean getIsSensor()
	{
		return this.isSensor;
	}
	
	/**
	 * @param set the tag if has sensor data
	 */
	public void setIsSensor(boolean t)
	{
		this.isSensor = t;
	}
}
