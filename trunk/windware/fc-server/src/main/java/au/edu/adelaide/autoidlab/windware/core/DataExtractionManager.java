package au.edu.adelaide.autoidlab.windware.core;

import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.server.util.TagHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * manages data extraction
 * @author Yu Yan
 * @date 20/05/2013
 *
 */
@Component
public class DataExtractionManager {
	
	/**  identify the data extraction type*/
	private Hashtable<String, DataExtractor> tagtype = new Hashtable<String, DataExtractor>();
	
	/** Prefix of wisp pure URI */
	private static String WISP_PURE_PREFIX = "urn:epc:id:wisp:";
	
	/** Prefix of wisp pure URI */
	private static String RESERVED_TYPE = "1x";
	
	private String clearString;
	
	@Autowired
	private List<DataExtractor> dataDataExtractions;
	/**
	 * Constructor
	 * Initial the key and value
	 */
	
	@PostConstruct
	public void iniPostConstruct(){
		if(dataDataExtractions!=null)
		for( DataExtractor ex:dataDataExtractions){
			String[] types = ex.getTypes();
			for(String ty:types){
				tagtype.put(ty.toLowerCase(), ex);
			}
			
		}
	}
	
	public DataExtractionManager()
	{
//		DataExtraction ex = new AccelerationImpl();
//		String[] types = ex.getTypes();
//		for(int i = 0; i < types.length; i++)
//		{
//			tagtype.put(types[i], new AccelerationImpl());
//		}
//		
		//implement other types of data below
		char clear[]=new char[64];
		for (int i = 0;i<64;i++){
			clear[i]='0';
		}
		
		clearString = new String(clear);
		
	}
	
	private Hashtable<String, DataExtractor> getTag()
	{
		return this.tagtype;
	}
	
	/**
	 * This method de-embed the tag type from the epc and set the type to the tag
	 * 
	 * @param tag is a tag
	 */
	public Tag extractData(Tag tag)
	{
		String binary = tag.getTagAsBinary();
		String epc = this.binaryToBytes(binary);
		//String prefix = epc.substring(0, 2);
		
		String pure = tag.getTagIDAsPureURI();
		
		// To be able to process unidentified tags
		if(pure==null) return tag;
		
		String[] purePrefix = pure.split(":");
		String pure_prefix = "";
		
		for(int i = 0; i < purePrefix.length - 1; i++)
		{
			pure_prefix += purePrefix[i] + ":";
		}
		
		
		// this is for WISP tags
		//if(prefix.equals("3D") || prefix.equals("3d"))
		if(pure_prefix.equals(WISP_PURE_PREFIX))
		{
			String type = epc.substring(18, 20); // fix the tag type
			tag.setIsSensor(true);
			if(type.startsWith("1"))
			{
				type = RESERVED_TYPE;
			}
			
			DataExtractor dataE = tagtype.get(type.toLowerCase());
			
			if(dataE!=null){
				dataE.deembed(tag);
			}
			
			//Clear sensor data and reconstruct tag ID 
			StringBuilder binCler = new StringBuilder(binary);
			binCler.replace(8, 72, clearString);
			
			tag.setTagAsBinary(binCler.toString());
			tag.setTagID(tag.getTagAsBinary().getBytes());;
			String pureID =	TagHelper.convert_to_PURE_IDENTITY(
					tag.getTagLength(),
					tag.getFilter(),
					tag.getCompanyPrefixLength(),
					tag.getTagAsBinary());	
					tag.setTagIDAsPureURI(pureID);
			
			/*String[] acceleration = accelerationTypes();
			boolean isAcceleration = false;
			for(int i = 0; i < acceleration.length; i++)
			{
				if(acceleration[i].equals(type))
				{
					isAcceleration = true;
					break;
				}
				else
				{
					
					isAcceleration = false;
				}
			}
			
			// add more types of data below
			
			
			if(isAcceleration == true) // judge the type of the data
			{
				SensorData sensor = new AccelerationTag();
				tag.setSensorData(sensor);
				tag.getSensorData().setSensorDataType(type);
				String tagtype = tag.getSensorData().getSensorDataType();
				DataExtractor dataE = getTag().get(tagtype);
				dataE.deembeding(tag);
			}
			else
			{
				tag.setIsSensor(false);
			}*/
			
		}		
		return tag;
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
			//System.exit(0);
		}
		
		for(int k = 0; k < epcByte.length; k++)
		{
			result += epcByte[k];
		}
		return result;
	}

	private String[] accelerationTypes() {
		return new String[]{"0D","0B","0d", "0b"};
	}
}
