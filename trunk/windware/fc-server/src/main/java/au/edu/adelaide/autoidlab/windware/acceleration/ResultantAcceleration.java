

package au.edu.adelaide.autoidlab.windware.acceleration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fosstrak.ale.server.Tag;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;
import org.fosstrak.ale.xsd.ale.epcglobal.Value;

import au.edu.adelaide.autoidlab.windware.core.Operation;
import au.edu.adelaide.autoidlab.windware.core.SensorData;

/**
 * This is the resultant acceleration operation implementation for the WISP tag.
 * Two argumets are accepted in order to filter resultant acceleration values; {@code less_than} and {@code greater_than}.
 * If {@code less_than} < {@code greater_than} the values with in the interval [{@code less_than},{@code greater_than}] is not reported unless otherwise.
 * 
 *  
 * @author Asanga Wickramasinghe (asanga.wickramasinghe@adelaide.edu.au)
 * @since 1.2.1
 */

public class ResultantAcceleration implements Operation {

	// name
	private final String OPERATION_NAME = "ResultantAcceleration";

	// Arguments
	private final String LESS_THAN = "less_than";
	private final String GREATER_THAN = "greater_than";

	@Override
	public List<SingleValue> getSingleValue(List<Tag> tags,
			Map<String, String> args, String id) {
		return null;
	}

	@Override
	public List<MultiValue> getMultiValue(List<Tag> tags,
			Map<String, String> args, String id) {
		List<Double> resultant = getResultant(tags);
		List<Long> timeStamps = getTimeStamps(tags);
		boolean[] include = new boolean[resultant.size()];
		
		double above = Double.NEGATIVE_INFINITY;
		double bellow = Double.POSITIVE_INFINITY;

		if (args.containsKey(GREATER_THAN)) {
			above = Double.parseDouble(args.get(GREATER_THAN));
		}
		if (args.containsKey(LESS_THAN)) {
			bellow = Double.parseDouble(args.get(LESS_THAN));
		}
		
		if(above<bellow){
			for (int i = 0; i < include.length; i++) {
				if (resultant.get(i) > above && resultant.get(i)< bellow) {
					include[i] = true;
				}
			}
		}else{
			for (int i = 0; i < include.length; i++) {
				if (resultant.get(i) > above || resultant.get(i)<bellow) {
					include[i] = true;
				}
			}
		}
		
		
		// Create output
		MultiValue ml = new MultiValue();
		ml.setName("resultant");
		ml.setOperationID(id);
		ml.setType("float");
		int size = resultant.size();
		for (int i = 0; i < size; i++) {
			if (include[i]) {
				Value v = new Value();
				v.setKey(timeStamps.get(i).toString());
				v.setValue(resultant.get(i).toString());
				ml.getValue().add(v);
			}
		}
		List<MultiValue> list = null;
		if (!ml.getValue().isEmpty()) {
			list = new ArrayList<MultiValue>();
			list.add(ml);
		}
		return list;
	}

	@Override
	public String getName() {
		return OPERATION_NAME;
	}

	private List<Long> getTimeStamps(List<Tag> tags) {
		List<Long> out = new ArrayList<Long>(tags.size());
		for (Tag t : tags) {
			out.add(t.getTimestamp());
		}

		return out;
	}

	private List<Double> getResultant(List<Tag> tags) {
		List<AccelerationTag> accn = convert(tags);
		List<Double> out = new ArrayList<Double>(accn.size());
		for (AccelerationTag data : accn) {
			double res = Math.sqrt(data.getX_Axis() * data.getX_Axis()
					+ data.getY_Axis() * data.getY_Axis() + data.getZ_Axis()
					* data.getZ_Axis());
			out.add(res);
		}

		return out;
	}
	
	private List<AccelerationTag> convert(List<Tag> tags){
		List<AccelerationTag> accn = new ArrayList<AccelerationTag>(tags.size());
		for(Tag t : tags){
			if (t.getSensorData() instanceof AccelerationTag){
				accn.add((AccelerationTag)t.getSensorData());
			}
		}
		
		return accn;
	}

}
