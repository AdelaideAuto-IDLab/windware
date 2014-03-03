/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.ale.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.log4j.Logger;
import org.fosstrak.ale.exception.ECSpecValidationException;
import org.fosstrak.ale.exception.ImplementationException;
import org.fosstrak.ale.server.util.TagHelper;
import org.fosstrak.ale.util.ECReportSetEnum;
import org.fosstrak.ale.xsd.ale.epcglobal.ECFilterSpec;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReaderStat;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReaderStat.Sightings;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReport;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroup;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupCount;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupList;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMember;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMemberExtension;
import org.fosstrak.ale.xsd.ale.epcglobal.LogicalReader;
import org.fosstrak.ale.xsd.ale.epcglobal.MultiValue;
import org.fosstrak.ale.xsd.ale.epcglobal.SensorGroup;
import org.fosstrak.ale.xsd.ale.epcglobal.SingleValue;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMemberExtension.FieldList;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMemberExtension.Stats;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportMemberField;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportOutputFieldSpec;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportOutputSpecExtension;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportSensorDataExtension;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportSpec;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportSpecExtension;
import org.fosstrak.ale.xsd.ale.epcglobal.ECSightingStat;
import org.fosstrak.ale.xsd.ale.epcglobal.ECTagStat;
import org.fosstrak.ale.xsd.ale.epcglobal.ECTagStat.StatBlocks;
import org.fosstrak.tdt.TDTEngine;
import au.edu.adelaide.autoidlab.windware.core.Operation;
import au.edu.adelaide.autoidlab.windware.operation.DataExtractionManager;
import au.edu.adelaide.autoidlab.windware.operation.DataOperationManager;

/**
 * This class represents a report. It filters and groups tags, add them to the
 * report and build ec reports.
 * 
 * @author regli
 * @author swieland
 * @author wafa.soubra@orange.com
 * 
 * @author Yu Yan (modified)
 */
public class Report {

	/** logger. */
	private static final Logger LOG = Logger.getLogger(Report.class);

	/** name of this report. */
	private final String name;
	/** current event cycle delivers tags. */
	private final EventCycle currentEventCycle;

	/** patterns of tags which are included in this report. */
	private final Set<Pattern> includePatterns = new HashSet<Pattern>();
	/** patterns of tags which are excluded from this report. */
	private final Set<Pattern> excludePatterns = new HashSet<Pattern>();
	/** patterns to group the tags of this report. */
	private final Set<Pattern> groupPatterns = new HashSet<Pattern>();

	/** type of this report (current, additions or deletions). */
	private String reportType;

	/** ec report. */
	private ECReport report;

	/** ec report specification. */
	private ECReportSpec reportSpec;

	/** Data extraction manager */
	private DataExtractionManager de;

	/** key is reader; Value: key is tag id; value is the tag list for per tag */
	private HashMap<String, Map<String, ArrayList<Tag>>> tags_reader_group; // grouping
																			// tags
																			// by
																			// reader
																			// and
																			// then
																			// by
																			// id

	/** get rid of the duplicated tags in the first part of ec report */
	private HashMap<String, Tag> duplicateTags;

	/** data operation manager */
	private DataOperationManager data_Operation_Manager;

	/**
	 * Constructor set parameters, read specifiaction and initializes patterns.
	 * 
	 * @param reportSpec
	 *            defines how the report should be generated
	 * @param currentEventCycle
	 *            this report belongs to
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	public Report(ECReportSpec reportSpec, EventCycle currentEventCycle)
			throws ImplementationException {

		// set name
		name = reportSpec.getReportName();

		LOG.debug("Create report '" + name + "'");

		// create ECReport
		report = new ECReport();

		// set ECReport name
		report.setReportName(name);

		// set type
		reportType = reportSpec.getReportSet().getSet();

		// set ECReportSpec
		this.reportSpec = reportSpec;

		// set currentEventCycle
		this.currentEventCycle = currentEventCycle;

		this.de = new DataExtractionManager();

		// acturaltags = new HashMap<String, ArrayList<Tag>>();
		tags_reader_group = new HashMap<String, Map<String, ArrayList<Tag>>>();
		duplicateTags = new HashMap<String, Tag>();
		data_Operation_Manager = new DataOperationManager();

		// init patterns
		initFilterPatterns();
		initGroupPatterns();

	}

	/**
	 * This method adds a tag to the report.
	 * 
	 * @param tag
	 *            to add
	 * @throws ECSpecValidationException
	 *             if the tag is invalid
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	public void addTag(Tag tag) throws ECSpecValidationException,
			ImplementationException {

		// get tag URI
		String tagURI = tag.getTagIDAsPureURI();

		// check if the tag is a member of this report (use filter patterns and
		// set spec)
		if (isMember(tagURI)) {

			LOG.debug("Event '" + tag + "' is member of report '" + name + "'");

			// extract the data of the tag and de-embedding
			tag = de.extractData(tag);

			// check isIncludeID and isIncludeSensor or both
			boolean is_id = reportSpec.getOutput().isIncludeID();
			boolean is_sensor = reportSpec.getOutput().isIncludeSensor();
			boolean is_both = reportSpec.getOutput().isIncludeID()
					&& reportSpec.getOutput().isIncludeSensor();
			boolean tag_is_sensor = tag.getIsSensor();

			if ((is_id == true && tag_is_sensor == false)
					|| (is_sensor == true && tag_is_sensor == true)
					|| (is_both == true)) {
				// add tag to report
				addTagToReportGroup(tag);
			}
			// LOG.debug("prefix:  " + tag.getCompanyPrefixLength());

		}
	}

	/**
	 * this method is for compatibility reasons such that eg ReportTest is not
	 * broken.
	 * 
	 * @param tag
	 *            to add
	 * @throws ECSpecValidationException
	 *             if the tag is invalid
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	public void addTag(
			org.fosstrak.reader.rprm.core.msg.notification.TagType tag)
			throws ECSpecValidationException, ImplementationException {
		Tag newtag = new Tag();
		newtag.setTagID(tag.getTagID());
		newtag.setTagIDAsPureURI(tag.getTagIDAsPureURI());
		addTag(newtag);
	}

	/**
	 * helper method to display tags that were added or deleted.
	 * 
	 * @param reportTags
	 *            a map holding the tags that were either added or deleted.
	 */
	private void writeTraceInformation(Map<String, Tag> reportTags) {
		String out = '\n' + "+++++++++++++++++++++++++++++++++++++++++++++++++++++" + '\n';
		out += '\t' + "eventcycle " + currentEventCycle.getName() + '\n';
		out += '\t' + "round " + currentEventCycle.getRounds() + '\n';
		if (reportTags == null) {
			out += '\t' + "no tags" + '\n';
			out += "+++++++++++++++++++++++++++++++++++++++++++++++++++++" + '\n';
			LOG.info(out);
			return;
		}

		for (Tag tag : reportTags.values()) {
			out += '\t' + tag.getTagIDAsPureURI() + '\n';
		}
		out += "+++++++++++++++++++++++++++++++++++++++++++++++++++++" + '\n';
		LOG.trace(out);
	}

	/**
	 * This method returns the new ec report.
	 * 
	 * @return ec report
	 * @throws ECSpecValidationException
	 *             if a tag is invalid
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	public ECReport getECReport() throws ECSpecValidationException,
			ImplementationException {
		Set<Tag> currentCycleTags = currentEventCycle.getTags();
		Set<Tag> lastCycleTags = currentEventCycle.getLastEventCycleTags();

		long start_time = System.nanoTime();

		// generate new ECReport
		if (ECReportSetEnum.isSameECReportSet(ECReportSetEnum.ADDITIONS,
				reportType)) {

			// get additional tags
			Map<String, Tag> reportTags = new HashMap<String, Tag>();

			// add tags from current EventCycle
			for (Tag tag : currentCycleTags) {
				reportTags.put(tag.getTagIDAsPureURI(), tag);
			}

			// remove tags from last EventCycle
			if (lastCycleTags != null) {
				for (Tag tag : lastCycleTags) {
					reportTags.remove(tag.getTagIDAsPureURI());
				}
			}

			for (Tag tag : reportTags.values()) {
				addTag(tag);
			}
			// writeDebugInformation(reportTags);

		} else if (ECReportSetEnum.isSameECReportSet(ECReportSetEnum.CURRENT,
				reportType)) {

			// get tags from current EventCycle
			for (Tag tag : currentCycleTags) {
				addTag(tag);
			}
		} else if (ECReportSetEnum.isSameECReportSet(ECReportSetEnum.DELETIONS,
				reportType)) {
			// get removed tags
			// changed here
			Map<String, Tag> reportTags = new HashMap<String, Tag>();

			// add tags from last EventCycle
			if (lastCycleTags != null) {
				for (Tag tag : lastCycleTags) {
					reportTags.put(tag.getTagIDAsPureURI(), tag);
				}
			}

			// remove tags from current EventCycle
			for (Tag tag : currentCycleTags) {
				reportTags.remove(tag.getTagIDAsPureURI());
			}

			// add tags to report with filtering
			for (Tag tag : reportTags.values()) {
				addTag(tag);
			}
			if (LOG.isTraceEnabled()) {
				writeTraceInformation(reportTags);
			}
		} else if (ECReportSetEnum.isSameECReportSet(ECReportSetEnum.SENSOR,
				reportType)) {

			// if the ECReportSet is Sensor Only report sensor data
			reportSpec.getOutput().setIncludeID(false);
			reportSpec.getOutput().setIncludeSensor(true);

			// get sensor tags from current EventCycle
			for (Tag tag : currentCycleTags) {
				addTag(tag);
			}
		} else {
			LOG.info("unknown reportType: " + reportType);
		}

		if (reportSpec.isReportIfEmpty() || !isEmpty()) {

			if (reportSpec.getReportSet().getSet().equals("SENSOR")) {
				add_Sensor();
			}

			ECReport temp = report;
			report = new ECReport();
			report.setReportName(name);
			tags_reader_group.clear();
			duplicateTags.clear();
			long estimated = System.nanoTime() - start_time;
			LOG.debug("Estimated time for event cycle: " + estimated);
			return temp;
		}

		else {
			report = new ECReport();
			report.setReportName(name);
			long estimated = System.nanoTime() - start_time;
			LOG.debug("Estimated time for event cycle: " + estimated);
			return null;
		}

	}

	/**
	 * add the single value list to the extension
	 * 
	 * @param e
	 *            the ECReportSensorDataExtension defined by scheme.
	 * @param singleV
	 *            the single value list needed to be added
	 */
	private void add_singleValue(ECReportSensorDataExtension e,
			List<SingleValue> singleV) {
		if (singleV != null) {
			for (int sin = 0; sin < singleV.size(); sin++) {
				e.getSensordata().add(singleV.get(sin));
			}
		}
	}

	/**
	 * add the multiple value list to the extension
	 * 
	 * @param e
	 *            the ECReportSensorDataExtension defined by scheme.
	 * @param multiV
	 *            the multiple value list needed to be added
	 */
	private void add_multiValue(ECReportSensorDataExtension e,
			List<MultiValue> multiV) {
		if (multiV != null) {
			for (int mul = 0; mul < multiV.size(); mul++) {
				e.getMultiValue().add(multiV.get(mul));
			}
		}
	}

	/**
	 * add all single values and multiple values (operations) to the report
	 * 
	 * @param tags_per_hw
	 *            all tags for one hardwareSerial number.
	 * @param ecsde
	 *            the ECReportSensorDataExtension defined by scheme.
	 */
	private void add_sensor_toReport(ArrayList<Tag> tags_per_hw,
			ECReportSensorDataExtension ecsde) {
		for (int index = 0; index < reportSpec.getOperations().getOperation()
				.size(); index++) {
			String names = reportSpec.getOperations().getOperation().get(index)
					.getName();
			HashMap<String, String> arguements = new HashMap<String, String>();

			for (int index_2 = 0; index_2 < reportSpec.getOperations()
					.getOperation().get(index).getArg().size(); index_2++) {
				String argument_name = reportSpec.getOperations()
						.getOperation().get(index).getArg().get(index_2)
						.getName();
				String argument_value = reportSpec.getOperations()
						.getOperation().get(index).getArg().get(index_2)
						.getValue();
				arguements.put(argument_name, argument_value);
			}

			String opeID = "";
			if (reportSpec.getOperations().getOperation().get(index)
					.getOperationID() != null) {
				opeID = reportSpec.getOperations().getOperation().get(index)
						.getOperationID();
			} else {
				opeID = reportSpec.getOperations().getOperation().get(index)
						.getName();
			}

			Operation operation = data_Operation_Manager.get_tag_operation()
					.get(names);
			if (operation != null) {
				List<SingleValue> singleV = operation.getSingleValue(
						tags_per_hw, arguements, opeID);
				List<MultiValue> multiV = operation.getMultiValue(tags_per_hw,
						arguements, opeID);
				add_singleValue(ecsde, singleV);
				add_multiValue(ecsde, multiV);
			} else {
				LOG.debug("Operation not implemeted:" + names);
			}


		}
	}

	/**
	 * add the sensor data to the report
	 */
	private void add_Sensor() {
		// add the sensor report to ECReport
		LogicalReader wisp_LR = new LogicalReader();

		Set<String> all_LR_set = tags_reader_group.keySet();
		ArrayList<String> all_LR = new ArrayList(all_LR_set);

		for (int i = 0; i < all_LR.size(); i++) {
			Map<String, ArrayList<Tag>> lr_tags_list = tags_reader_group
					.get(all_LR.get(i));
			SensorGroup wispgroup = new SensorGroup();
			wispgroup.setLogicalReader(all_LR.get(i));
			Iterator it = lr_tags_list.entrySet().iterator();
			while (it.hasNext()) {
				ECReportSensorDataExtension ecsde = new ECReportSensorDataExtension();

				Entry obj = (Entry) it.next();
				String hwS = (String) obj.getKey();

				ArrayList<Tag> tags_per_hw = (ArrayList<Tag>) obj.getValue();
				boolean is_sensor = tags_per_hw.get(0).getIsSensor();
				 String tagId=   tags_per_hw.get(0).getTagIDAsPureURI();

				if (tags_per_hw.get(0).getReader().equals(all_LR.get(i))
						&& is_sensor == true) {
					String hwserial = stringToHex(hwS);
					ecsde.setSensorTag(hwserial);
//					ecsde.setSensorTag(tagId);

					if (is_sensor) {
						add_sensor_toReport(tags_per_hw, ecsde);
					}

					wispgroup.getSensorTAGMember().add(ecsde);
				}

			}

			wisp_LR.getSensorGroup().add(wispgroup);
		}

		report.getGroup().get(0).getGroupList().getSensorTag().add(wisp_LR);
	}

	/**
	 * This method initializes the filter patterns on the basis of the ec report
	 * specification.
	 */
	private void initFilterPatterns() {

		LOG.debug("Init filter patterns");

		// get filter spec
		ECFilterSpec filterSpec = reportSpec.getFilterSpec();
		if (filterSpec != null) {

			// add ECIncludePatterns from spec to includePatterns set
			List<String> ecIncludePatterns = filterSpec.getIncludePatterns()
					.getIncludePattern();
			if (ecIncludePatterns != null) {
				for (String pattern : ecIncludePatterns) {
					try {
						includePatterns.add(new Pattern(pattern,
								PatternUsage.FILTER));
					} catch (ECSpecValidationException e) {
						LOG.debug("Specification Validation Exception: ", e);
					}
				}
			}

			// add ECExcludePatterns from spec to excludePatterns set
			List<String> ecExcludePatterns = filterSpec.getExcludePatterns()
					.getExcludePattern();
			if (ecExcludePatterns != null) {
				for (String pattern : ecExcludePatterns) {
					try {
						excludePatterns.add(new Pattern(pattern,
								PatternUsage.FILTER));
					} catch (ECSpecValidationException e) {
						LOG.debug("Specification Validation Exception: ", e);
					}
				}
			}
		}

	}

	/**
	 * This method initializes the group patterns on the basis of the ec report
	 * specification.
	 */
	private void initGroupPatterns() {

		LOG.debug("Init group patterns");
		if (reportSpec.getGroupSpec() != null) {
			// get group spec
			List<String> groupSpec = reportSpec.getGroupSpec().getPattern();
			// add ECGroupPatterns from spec to groupPatterns set
			for (String pattern : groupSpec) {
				try {
					groupPatterns.add(new Pattern(pattern, PatternUsage.GROUP));
				} catch (ECSpecValidationException e) {
					LOG.debug("Specification Validation Exception: ", e);
				}
			}
		}
	}

	/**
	 * This method checks on the basis of the filter patterns if the specified
	 * tag could be a member of this report.
	 * 
	 * @param tagURI
	 *            to check for possible membership
	 * @return true if the tag could be a member of this report and false
	 *         otherwise
	 * @throws ECSpecValidationException
	 *             if the tag is invalid
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	private boolean isMember(String tagURI) throws ECSpecValidationException,
			ImplementationException {

		if (ECReportSetEnum.isSameECReportSet(ECReportSetEnum.ADDITIONS,
				reportType)) {

			// if report type is additions the tag is only a member if it wasn't
			// a member of the last event cycle
			Set<Tag> tags = currentEventCycle.getLastEventCycleTags();
			if (tags != null) {
				for (Tag tag : tags) {
					if (tag.getTagIDAsPureURI().equals(tagURI)) {
						return false;
					}
				}
			}
		}

		// check if tagURI is member of an exclude pattern
		for (Pattern pattern : excludePatterns) {
			if (pattern.isMember(tagURI)) {
				return false;
			}
		}

		// check if there are include patterns specified
		if (includePatterns.size() == 0) {
			return true;
		} else {

			// check if tagURI is a member of an include pattern
			for (Pattern pattern : includePatterns) {
				if (pattern.isMember(tagURI)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * This method adds a tag to the matching group of the report.
	 * 
	 * @param tag
	 *            to add
	 * @throws ECSpecValidationException
	 *             if the tag is invalid
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	private void addTagToReportGroup(Tag tag) throws ImplementationException,
			ECSpecValidationException {

		// duplicate tags

		// get tag URI
		String tagURI;
		// get group name (use group patterns)
		String groupName;

		// get matching group
		ECReportGroup matchingGroup;
		// create group list member
		ECReportGroupListMember groupMember;

		// get rid of the duplicated tags
		if (!duplicateTags.containsKey(tag.getSensorData().getHWSerial())) {

			duplicateTags.put(tag.getSensorData().getHWSerial(), tag);
			tagURI = tag.getTagIDAsPureURI();
			// if this one is null, try something different to compense
			// crashes...
			if (null == tagURI) {
				tagURI = TagHelper.getTDTEngine().bin2hex(tag.getTagAsBinary());
			}
			groupName = getGroupName(tagURI);
			LOG.debug("The group name for tag '" + tagURI + "' is '"
					+ groupName + "'");
			matchingGroup = null;
			List<ECReportGroup> groups = report.getGroup();
			// if (groups == null) {
			if (groups.isEmpty()) {
				matchingGroup = null;
			} else {
				for (ECReportGroup group : groups) {
					if (groupName == null) {
						if (group.getGroupName() == null) {
							matchingGroup = group;
						}
					} else {
						if (groupName.equals(group.getGroupName())) {
							matchingGroup = group;
						}
					}
				}
			}
			// create group if group does not already exist
			if (matchingGroup == null) {

				LOG.debug("Group '" + groupName
						+ "' does not already exist, create it");
				// create group
				matchingGroup = new ECReportGroup();

				// set name
				matchingGroup.setGroupName(groupName);

				// set count
				if (reportSpec.getOutput().isIncludeCount()) {
					ECReportGroupCount groupCount = new ECReportGroupCount();
					groupCount.setCount(0);
					matchingGroup.setGroupCount(groupCount);
				}

				// create and set group list
				matchingGroup.setGroupList(new ECReportGroupList());

				// add to groups
				report.getGroup().add(matchingGroup);

			}
			groupMember = new ECReportGroupListMember();
			TDTEngine tdt = TagHelper.getTDTEngine();
			// RAW DECIMAL
			if (TagHelper.isReportOutputSpecIncludeRawDecimal(reportSpec
					.getOutput())) {
				TagHelper.addTagAsRawDecimal(tdt, groupMember, tag);
			}
			// TAG ENCODING
			if (TagHelper.isReportOutputSpecIncludeTagEncoding(reportSpec
					.getOutput())) {
				TagHelper.addTagAsTagEncoding(tdt, groupMember, tag);
			}
			// RAW HEX
			if (TagHelper.isReportOutputSpecIncludeRawHex(reportSpec
					.getOutput())) {
				TagHelper.addTagAsRawHex(tdt, groupMember, tag);
			}
			// EPC
			if (TagHelper.isReportOutputSpecIncludeEPC(reportSpec.getOutput())) {
				TagHelper.addTagAsEPC(tdt, groupMember, tag);
			}
			// check if we need to add tag stats
			ECReportSpecExtension ecReportSpecExtension = reportSpec
					.getExtension();
			if ((null != ecReportSpecExtension)
					&& (null != ecReportSpecExtension.getStatProfileNames())) {

				LOG.debug("adding stat profile");
				addStatProfiles(tag, groupMember, ecReportSpecExtension
						.getStatProfileNames().getStatProfileName());
			}
			// ORANGE: check if we need to add user memory in the report
			ECReportOutputSpecExtension outputExtension = reportSpec
					.getOutput().getExtension();
			if (outputExtension != null) {
				if (outputExtension.getFieldList() != null) {
					for (ECReportOutputFieldSpec outputFieldSpec : outputExtension
							.getFieldList().getField()) {
						String fieldName = outputFieldSpec.getFieldspec()
								.getFieldname();
						if (fieldName.equalsIgnoreCase("UserMemory")
								&& outputFieldSpec.isIncludeFieldSpecInReport()) {
							addUserMemoryToReport(tag, groupMember, fieldName);
						}
					}
				}
			}

			List<ECReportGroupListMember> members = matchingGroup
					.getGroupList().getMember();
			members.add(groupMember);

			// increment group counter
			if (reportSpec.getOutput().isIncludeCount()) {
				matchingGroup.getGroupCount().setCount(
						matchingGroup.getGroupCount().getCount() + 1);
			}

			LOG.debug("Tag '" + tagURI + "' successfully added to group '"
					+ groupName + "' of report '" + name + "'");
		}
		// ORANGE End
		// add list member to group list

		// out put members
		// group the tags
		String readerName = tag.getReader();
		String hws = tag.getSensorData().getHWSerial();
		if (!tags_reader_group.containsKey(readerName)) {
			HashMap<String, ArrayList<Tag>> tag_list = new HashMap<String, ArrayList<Tag>>();

			if (!tag_list.containsKey(hws)) {
				ArrayList<Tag> tag_l = new ArrayList<Tag>();
				tag_l.add(tag);
				tag_list.put(hws, tag_l);
			} else {
				tag_list.get(hws).add(tag);
			}
			tags_reader_group.put(readerName, tag_list);
		} else {
			if (!tags_reader_group.get(readerName).containsKey(hws)) {

				ArrayList<Tag> newTag = new ArrayList<Tag>();
				newTag.add(tag);
				tags_reader_group.get(readerName).put(hws, newTag);
			} else {
				tags_reader_group.get(readerName).get(hws).add(tag);
			}
		}

	}

	/**
	 * for each statistics profile name add the respective statistics profile.
	 * 
	 * @param tag
	 *            the tag holding information the statistics.
	 * @param groupMember
	 *            the group member where to add the statistics.
	 * @param statProfileName
	 *            a list of statistic profile names.
	 */
	private void addStatProfiles(Tag tag, ECReportGroupListMember groupMember,
			List<String> statProfileName) {

		ECReportGroupListMemberExtension extension = new ECReportGroupListMemberExtension();
		groupMember.setExtension(extension);

		extension.setStats(new Stats());
		List<ECTagStat> ecTagStats = extension.getStats().getStat();
		for (String profile : statProfileName) {
			LOG.debug("adding stat profile: " + profile);

			ECTagStat ecTagStat = new ECTagStat();
			ecTagStats.add(ecTagStat);

			ecTagStat.setProfile(profile);
			ecTagStat.setStatBlocks(new StatBlocks());
			ECReaderStat readerStat = new ECReaderStat();
			ecTagStat.getStatBlocks().getStatBlock().add(readerStat);

			readerStat.setReaderName(tag.getReader());
			readerStat.setSightings(new Sightings());
			readerStat.getSightings().getSighting().add(new ECSightingStat());
		}
	}

	/**
	 * ORANGE: Gets the value of the user memory and added to the generated
	 * report.
	 * 
	 * @param tag
	 *            the tag holding information of user memory.
	 * @param groupMember
	 *            the group member where to add the user memory.
	 * @param fieldName
	 *            the field name "UserMemory".
	 */
	private void addUserMemoryToReport(Tag tag,
			ECReportGroupListMember groupMember, String fieldName) {
		ECReportGroupListMemberExtension extension = new ECReportGroupListMemberExtension();
		groupMember.setExtension(extension);
		extension.setFieldList(new FieldList());
		List<ECReportMemberField> ecReportMemberFields = extension
				.getFieldList().getField();
		ECReportMemberField ecReportMemberField = new ECReportMemberField();
		ecReportMemberFields.add(ecReportMemberField);
		ecReportMemberField.setName(fieldName);
		ecReportMemberField.setValue(tag.getUserMemory());
	}

	/**
	 * This method get the matching group of this report for the specified tag.
	 * 
	 * @param tagURI
	 *            to search group for
	 * @return group name
	 * @throws ECSpecValidationException
	 *             if the tag is invalid
	 * @throws ImplementationException
	 *             if an implementation exception occurs
	 */
	private String getGroupName(String tagURI) throws ImplementationException,
			ECSpecValidationException {

		for (Pattern pattern : groupPatterns) {
			if (pattern.isMember(tagURI)) {
				return pattern.getGroupName(tagURI);
			}
		}

		return null;

	}

	/**
	 * This method indicates if the report contains any tags.
	 * 
	 * @return true if the report is empty and false otherwise
	 */
	private boolean isEmpty() {

		List<ECReportGroup> groups = report.getGroup();
		if (groups != null) {
			for (ECReportGroup group : groups) {
				ECReportGroupList groupList = group.getGroupList();
				if (groupList.getMember().size() > 0) {
					return false;
				}
			}
		}
		return true;

	}

	/**
	 * This method transfer a string(hardware serial number) to binary format.
	 * 
	 * @return binary format
	 */
	private String stringToHex(String hw) {
		String result = "";
		if (hw.length() == 4) {
			String[] a = { hw.substring(0, 1), hw.substring(1, 2),
					hw.substring(2, 3), hw.substring(3) };
			for (int j = 0; j < 4; j++) {
				if (a[j].equals("f") || a[j].equals("F")) {
					a[j] = "1111";
				}
				if (a[j].equals("e") || a[j].equals("E")) {
					a[j] = "1110";
				}
				if (a[j].equals("d") || a[j].equals("D")) {
					a[j] = "1101";
				}
				if (a[j].equals("c") || a[j].equals("C")) {
					a[j] = "1100";
				}
				if (a[j].equals("b") || a[j].equals("B")) {
					a[j] = "1011";
				}
				if (a[j].equals("a") || a[j].equals("A")) {
					a[j] = "1010";
				}
				if (a[j].equals("9")) {
					a[j] = "1001";
				}
				if (a[j].equals("8")) {
					a[j] = "1000";
				}
				if (a[j].equals("7")) {
					a[j] = "0111";
				}
				if (a[j].equals("6")) {
					a[j] = "0110";
				}
				if (a[j].equals("5")) {
					a[j] = "0101";
				}
				if (a[j].equals("4")) {
					a[j] = "0100";
				}
				if (a[j].equals("3")) {
					a[j] = "0011";
				}
				if (a[j].equals("2")) {
					a[j] = "0010";
				}
				if (a[j].equals("1")) {
					a[j] = "0001";
				}
				if (a[j].equals("0")) {
					a[j] = "0000";
				}
			}
			result = a[0] + a[1] + a[2] + a[3];
		}

		return result;
	}

}