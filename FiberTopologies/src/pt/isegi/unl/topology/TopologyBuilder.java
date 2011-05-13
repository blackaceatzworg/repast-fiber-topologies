/**
 * 
 */
package pt.isegi.unl.topology;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.feature.Feature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.PropertyEquals;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.ShapefileWriter;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.ShortestPath;
import repast.simphony.util.collections.IndexedIterable;

/**
 * @author pmendes
 * 
 */
public class TopologyBuilder implements ContextBuilder<Object> {
	
	
	public static double maxODPdistance;
	public static double maxStepDistance;
	public static double minDistanceToCentralOffice;
	public static double maxDistanceToOpticalSplice;
	
	public static GeometryFactory factory;

	private static InfraNodeAgent centralOffice;
	private static Map<InfraNodeAgent, List<SurveyAgent>> nodeMap;
	private static List<EquipmentAgent> odpList;
	private static Geography<Object> geography;
	private static Context<Object> topologyContext;
	private static ShortestPath<InfraNodeAgent> shortestPaths4Network;

	private static int nextEquipmentId;
	private static int nextCableId;

	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("FiberTopologies");
		topologyContext = context;
		
		Parameters p = RunEnvironment.getInstance().getParameters();
		maxODPdistance = Utilities.convertFromMeters( (Double) p.getValue("maxODPdistance") );
		maxStepDistance = Utilities.convertFromMeters( (Double) p.getValue("maxStepDistance") );
		minDistanceToCentralOffice = Utilities.convertFromMeters( (Double) p.getValue("minDistanceToCentralOffice") );
		maxDistanceToOpticalSplice = Utilities.convertFromMeters( (Double) p.getValue("maxDistanceToOpticalSplice") );

		GeographyParameters<Object> geoParams = new GeographyParameters<Object>();
		geography = GeographyFactoryFinder.createGeographyFactory(null)
				.createGeography("TopologyGeography", topologyContext,
						geoParams);

		addSubContext(topologyContext);
		// sets the network origin InfraNode
		setCentralOffice();
		// builds the InfraNode network
		buildInfraNodeNetwork();
		// cleans the network
		cleanNetwork();
		// computes all the shortest paths in the network
		shortestPaths4Network = new ShortestPath<InfraNodeAgent>(getInfraNodeNetwork());
		
		/*
		 *  Determine which InfraNodes will be containing Optical Distribution Points
		 */
		int surveyNumberfromCO = 0;
		nodeMap = new HashMap<InfraNodeAgent, List<SurveyAgent>>();
		for (SurveyAgent surveyAgent : getSurveyContext().getObjects(SurveyAgent.class)){
			double currentDistance = new DistanceOp(surveyAgent.getGeometry(), getCentralOffice().getGeometry()).distance();
			// If the survey is less than minDistanceToCentralOffice to the CentralOffice
			// No need for equipment it will be directly connected to the central office
			InfraNodeAgent infraNodeAgent = null;
			if (currentDistance <= minDistanceToCentralOffice){
				surveyNumberfromCO += 1;
				surveyAgent.setNearToCentralOffice(true);
				infraNodeAgent = getCentralOffice();
				
			} else {
				// finds the nearest infranode agent
				double minimalDistance = Double.MAX_VALUE;
				for (InfraNodeAgent infranode : getInfraNodeNetwork().getNodes()){
					currentDistance = new DistanceOp(surveyAgent.getGeometry(), infranode.getGeometry()).distance();
					if (currentDistance < minimalDistance){
						minimalDistance = currentDistance;
						infraNodeAgent = infranode;
					}					
				}	
			}
			List<SurveyAgent> surveyList = new ArrayList<SurveyAgent>();
			if (nodeMap.containsKey(infraNodeAgent)) {
				surveyList = nodeMap.get(infraNodeAgent);
				surveyList.add(surveyAgent);
				nodeMap.put(infraNodeAgent, surveyList); // update Map
			} else {
				surveyList.add(surveyAgent);
				nodeMap.put(infraNodeAgent, surveyList); // add to map
			}				
			
		}
		System.out.println(nodeMap.size() + " entries in InfraNodes map");
		System.out.println(surveyNumberfromCO + " surveys supplied directly by the CO");

		/*
		 * For the generation of OpticalDistributionPoint
		 */
		odpList = new ArrayList<EquipmentAgent>();
		for (Map.Entry<InfraNodeAgent, List<SurveyAgent>> map : nodeMap.entrySet()) {
			Geometry geometry = ((InfraNodeAgent) map.getKey()).getGeometry();
			boolean createNew = true;
			for (EquipmentAgent equipAgent : odpList) {
				// is it in getMaxODPdistance() meters?
				if (geometry.isWithinDistance(equipAgent.getDestinationNode().getGeometry(), maxODPdistance)) {
					createNew = false;
					List<SurveyAgent> surveyList = equipAgent.getSurveyList();
					surveyList.addAll(map.getValue());
					break;
				}
			}
			if (createNew) {
				Entity entity;
				if(map.getKey().getEntity().equals(Entity.CentralOffice.description())){
					entity = Entity.OpticalDivider;
				} else {
					entity = Entity.OpticalDistributionPoint;
				}
				EquipmentAgent equipmentAgent = new EquipmentAgent(
						getGeography(),
						getNextEquipmentId(),
						entity.description(),
						entity.network(),
						map.getValue(),
						centralOffice,
						map.getKey());

				odpList.add(equipmentAgent);
			}
		}
		System.out.println(odpList.size()
				+ " Equipment agents in Optical Distribution Point list");

		
		// stops the simulation when all ODP had been created
		// RunEnvironment.getInstance().endAt(50);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule(); 
		ScheduleParameters endParams = ScheduleParameters.createAtEnd(ScheduleParameters.LAST_PRIORITY);
		schedule.schedule(endParams, this, "end"); 
		ScheduleParameters startParams = ScheduleParameters.createOneTime(1); 
		schedule.schedule(startParams, this, "start");
		 
		int surveysNumber = 0;
		ScheduleParameters agentParams = ScheduleParameters.createRepeating(1, 1, 0);
		for (EquipmentAgent agent : odpList) {
			surveysNumber += agent.getSurveyList().size();
			getEquipmentContext().add(agent);
			geography.move(agent, centralOffice.getGeometry());
			schedule.schedule(agentParams, agent, "step");
		}
		System.out.println(surveysNumber + " surveys supplied by a ODP");
		
		return topologyContext;
	}

	/**
	 * The actual order that subcontexts are added is important 
	 * because of the agents maintain some dependencies between them
	 * 
	 * @param context the main context that will contain the other contexts
	 */
	private void addSubContext(Context<Object> context) {
		context.addSubContext(new InfraNodeContext());
		context.addSubContext(new RouteContext());
		context.addSubContext(new SurveyContext());
		context.addSubContext(new EquipmentContext());
		context.addSubContext(new CableContext());
	}

	/**
	 * Adds edges between node1 and node2 to the InfraNodeNetwork
	 */
	private boolean buildInfraNodeNetwork() {
		Network<InfraNodeAgent> infraNodeNetwork = getInfraNodeNetwork();
		IndexedIterable<RouteAgent> routeCollection = getRouteContext().getObjects(RouteAgent.class);
		for (RouteAgent routeAgent : routeCollection) {
			if (routeAgent.getNode1() != null && routeAgent.getNode2() != null) {
				RepastEdge<InfraNodeAgent> edge = new RepastEdge<InfraNodeAgent>(
						routeAgent.getNode1(), routeAgent.getNode2(), false,
						routeAgent.getGeometry().getLength());
				if (!infraNodeNetwork.containsEdge(edge)) {
					try {
						routeAgent.getNode1().addRoute(routeAgent);
						routeAgent.getNode2().addRoute(routeAgent);
						
						infraNodeNetwork.addEdge(edge);
					} catch (Exception e) {
						System.out.println(routeAgent.toString());
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("skiping " + routeAgent.toString());
			}
		}
		return infraNodeNetwork.size() > 0 ? true : false;
	}
	
	
	/**
	 * Removes all edges containing nodes that don't have any connectivity to the C.O. 
	 */
	private boolean cleanNetwork() {
		ShortestPath<InfraNodeAgent> localPaths = new ShortestPath<InfraNodeAgent>(getInfraNodeNetwork());
		List<RepastEdge<InfraNodeAgent>> edges2remove = new ArrayList<RepastEdge<InfraNodeAgent>>();
		List<InfraNodeAgent> nodes2remove = new ArrayList<InfraNodeAgent>();
		for (InfraNodeAgent node : getInfraNodeNetwork().getNodes()){
			if (!node.equals(getCentralOffice())){
				if(localPaths.getPath(getCentralOffice(), node).size() < 1){
					System.out.println(node.getLabel());
					nodes2remove.add(node);
					for(RepastEdge<InfraNodeAgent> edge : getInfraNodeNetwork().getEdges(node)){
						edges2remove.add(edge);
					} 
				}
			}
		}
		// System.out.println(edges2remove.size() + " edges to remove in the InfraNodeNetwork. The number of edges before the operation is " + getInfraNodeNetwork().getDegree());
		// removes edges from the network
		for (RepastEdge<InfraNodeAgent> edge : edges2remove){
			getInfraNodeNetwork().removeEdge(edge);
		}
		// System.out.println("The number of edges after the operation is " + getInfraNodeNetwork().getDegree());
		// remove nodes from the infranode context
		// System.out.println(nodes2remove.size() + " infranodes to remove in the InfraNodeContext? " + removed);
		return getInfraNodeContext().removeAll(nodes2remove);
	
	}

	/**
	 * @param feature
	 * @return the numeric part of the feature id
	 */
	protected static int extractFeatureId(Feature feature) {
		return Integer.parseInt(feature.getID().substring(
				feature.getID().indexOf(".") + 1));
	}
	
	protected static boolean generateTopologyShapefiles() {
		
		ShapefileWriter writer = new ShapefileWriter(getGeography());
	    try {
	    	File efile = new File("misc/output/equipments_"+System.nanoTime()+".shp");
			writer.write(getGeography().getLayer(EquipmentAgent.class).getName(), efile.toURI().toURL());
			File cfile = new File("misc/output/cables_"+System.nanoTime()+".shp");
			writer.write(getGeography().getLayer(CableAgent.class).getName(), cfile.toURI().toURL());
			
			if (!efile.canRead() || !cfile.canRead())
				System.out.println("Couldn't export topology layers!");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	    

		return true;
	}
	
	/**
	 * 
	 */
	public static void end() {
		TopologyBuilder.generateTopologyShapefiles();
	}
	/**
	 * 
	 */
	public static void start() {}

	/**
	 * @return the sequentialEquipmentId
	 */
	public static int getNextEquipmentId() {
		return nextEquipmentId++;
	}

	/**
	 * @return the sequentialCableId
	 */
	public static int getNextCableId() {
		return nextCableId++;
	}
	
	/**
	 * @return an Iterator for a survey collection
	 */
	public static List<SurveyAgent> getCOSurveys(){
		List<SurveyAgent> surveyList = new ArrayList<SurveyAgent>();
		for(SurveyAgent surveyAgent : getSurveyContext().getObjects(SurveyAgent.class)){
			if(surveyAgent.isNearToCentralOffice()){
				surveyList.add(surveyAgent);
			}
		}
		if(surveyList.isEmpty()){
			throw new NoSuchElementException("There ins't any survey near the C.O.");
		}
		return surveyList;
	}
	
	/**
	 * Sets the C.O.
	 * @param infraNodeAgent
	 */
	public static void setCentralOffice(){		
		// Find where's the Central Office. It will be the origin InfraNode in the network where all Equipment
		// agents will born
		PropertyEquals<InfraNodeAgent> query = new PropertyEquals<InfraNodeAgent>(
				getInfraNodeContext(), "entity", "Central Office");
		if (query != null && query.query().iterator().hasNext()) {
			centralOffice = query.query().iterator().next();
		} else {
			throw new NoSuchElementException(
					"There ins't any Central Office in context "
							+ getInfraNodeContext().toString());
		}
	}

	/**
	 * @return the centralOffice
	 */
	public static InfraNodeAgent getCentralOffice() {
		return centralOffice;
	}

	/**
	 * @return the infraNodeNetwork
	 */
	@SuppressWarnings("unchecked")
	public static Network<InfraNodeAgent> getInfraNodeNetwork() {
		return (Network<InfraNodeAgent>) getInfraNodeContext().getProjection(Network.class, "InfraNodeNetwork");
	}

	/**
	 * @return the shortestPaths4Network
	 */
	public static ShortestPath<InfraNodeAgent> getShortestPaths4Network() {
		return shortestPaths4Network;
	}

	/**
	 * @return the topologyContext
	 */
	public static Context<Object> getTopologyContext() {
		return topologyContext;
	}

	/**
	 * @return the geography
	 */
	public static Geography<Object> getGeography() {
		return geography;
	}

	/**
	 * @return the routeContext
	 */
	public static Context<RouteAgent> getRouteContext() {
		return (RouteContext) topologyContext.findContext("RouteContext");
	}

	/**
	 * @return the infraNodeContext
	 */
	public static Context<InfraNodeAgent> getInfraNodeContext() {
		return (InfraNodeContext) topologyContext.findContext("InfraNodeContext");
	}

	/**
	 * @return the surveyContext
	 */
	public static Context<SurveyAgent> getSurveyContext() {
		return (SurveyContext) topologyContext.findContext("SurveyContext");
	}

	/**
	 * @return the EquipmentContext
	 */
	public static Context<EquipmentAgent> getEquipmentContext() {
		return (EquipmentContext) topologyContext.findContext("EquipmentContext");
	}
	
	/**
	 * @return the CableContext
	 */
	public static Context<CableAgent> getCableContext() {
		return (CableContext) topologyContext.findContext("CableContext");
	}
	
	public int countInfraNodes(){
		return getInfraNodeNetwork().size();
	}
	public int countRoutes(){
		return getInfraNodeNetwork().getDegree();
	}
	public int countODP(){
		PropertyEquals<EquipmentAgent> query = new PropertyEquals<EquipmentAgent>(
				getEquipmentContext(), "entity", Entity.OpticalDistributionPoint.description());
		int count = 0;
		if (query != null) {
			while(query.query().iterator().hasNext()){
				query.query().iterator().next();
				count++;
			}
		}
		return count;
	}
	public int countOS(){
		PropertyEquals<EquipmentAgent> query = new PropertyEquals<EquipmentAgent>(
				getEquipmentContext(), "entity", Entity.OpticalSplice.description());
		int count = 0;
		if (query != null) {
			while(query.query().iterator().hasNext()){
				query.query().iterator().next();
				count++;
			}
		}
		return count;
	}
	public int countPrimaryCables(){
		PropertyEquals<CableAgent> query = new PropertyEquals<CableAgent>(
				getCableContext(), "entity", Entity.PrimaryCable.description());
		int count = 0;
		if (query != null) {
			while(query.query().iterator().hasNext()){
				query.query().iterator().next();
				count++;
			}
		}
		return count;
	}
	public int countSecondaryCables(){
		PropertyEquals<CableAgent> query = new PropertyEquals<CableAgent>(
				getCableContext(), "entity", Entity.SecondaryCable.description());
		int count = 0;
		if (query != null) {
			while(query.query().iterator().hasNext()){
				query.query().iterator().next();
				count++;
			}
		}
		return count;
	}
	public int countDropCables(){
		PropertyEquals<CableAgent> query = new PropertyEquals<CableAgent>(
				getCableContext(), "entity", Entity.DropCable.description());
		int count = 0;
		if (query != null) {
			while(query.query().iterator().hasNext()){
				query.query().iterator().next();
				count++;
			}
		}
		return count;
	}
	public int countSurveys(){
		IndexedIterable<SurveyAgent> iterable = getSurveyContext().getObjects(SurveyAgent.class);
		int count = 0;
		if (iterable != null) {
			while(iterable.iterator().hasNext()){
				iterable.iterator().next();
				count++;
			}
		}
		return count;
	}

}
