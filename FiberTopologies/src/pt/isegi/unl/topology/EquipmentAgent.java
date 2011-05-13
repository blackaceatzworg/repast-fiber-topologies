/**
 * 
 */
package pt.isegi.unl.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameter;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.collections.IndexedIterable;

/**
 * @author pmendes
 * 
 */
@AgentAnnot(displayName = "Equipment Agent")
public class EquipmentAgent extends Agent {

	private InfraNodeAgent lastNode;
	private InfraNodeAgent startingNode;
	private InfraNodeAgent destinationNode;
	private List<EquipmentAgent> odpList;
	private List<SurveyAgent> surveyList;
	private List<CableAgent> cableList;
	private List<RepastEdge<InfraNodeAgent>> path;
	private List<Coordinate> pathCoordinates;
	private boolean atDestination;
	private boolean displayCables;
	private double coveredDistance;
	private int opticalSplicesCreated;


	/**
	 * @param entity
	 * @param cableList
	 */
	public EquipmentAgent(Geography<Object> geography, int id, String entity,
			String network, List<SurveyAgent> surveyList,
			InfraNodeAgent startingNode, InfraNodeAgent destinationNode) {
		setGeography(geography);
		setId(id);
		setEntity(entity);
		setNetwork(network);
		this.surveyList = surveyList;
		this.startingNode = startingNode;
		this.destinationNode = destinationNode;
		this.lastNode = TopologyBuilder.getCentralOffice();
		this.cableList = new ArrayList<CableAgent>();
		this.odpList = new ArrayList<EquipmentAgent>();
		this.path = new ArrayList<RepastEdge<InfraNodeAgent>>();
		this.pathCoordinates = new ArrayList<Coordinate>();
		this.opticalSplicesCreated++;

		try {
			setPath();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		/*this.pathLength = TopologyBuilder.factory.createLineString(pathCoordinates.toArray(new Coordinate[] {})).getLength();*/
	}
	
	// @ScheduledMethod(start = 1, pick = 1, interval = 1)
	public void step() {
		
		// arrived to destination?
		if (atDestination()){
			
			// are these equipment cables displayed?
			if (!this.isDisplayCables()){
				displayCables();
			}
			
			// checks if all equipments are at their destination
			// if so ends this run/simulation
			endRun();
			
			return;
		}
		
		if (this.pathCoordinates == null) {
			setPath(); // computes the path
		}
		
		double stepDistance = TopologyBuilder.maxStepDistance;
		
		if(this.getEntity().equals(Entity.OpticalSplice)){
			// optical splice will travel twice the speed
			stepDistance = stepDistance * 2;
		}
				
		double distanceTraveled = 0;
		boolean traveledMaxDistance = false;
		Geometry currentGeometry;
		Geometry targetGeometry;
		GeometryFactory geometryFactory = new GeometryFactory();		
		while (!traveledMaxDistance && !atDestination()){
			currentGeometry = TopologyBuilder.getGeography().getGeometry(this);
			targetGeometry = geometryFactory.createPoint(this.pathCoordinates.get(0));
			double distanceToTarget = DistanceOp.distance(currentGeometry, targetGeometry);
			if(distanceTraveled + distanceToTarget < stepDistance){
				distanceTraveled += distanceToTarget;
				TopologyBuilder.getGeography().move(this, targetGeometry);
				this.pathCoordinates.remove(0);
				this.coveredDistance += distanceTraveled;
				
			} else {
				double distanceToTravel = stepDistance - distanceTraveled;
				double angleInRadians = Utilities.angleInRadians(targetGeometry.getCoordinate(), currentGeometry.getCoordinate()) + Math.PI;
				TopologyBuilder.getGeography().moveByVector(this, Utilities.convertToMeters(distanceToTravel), angleInRadians);
				traveledMaxDistance = true;
				this.coveredDistance += distanceToTravel; 
			}
			evaluateCurrentPosition();
		}	
		
	}
	
	/**
	 * The run ends when all equipments are at their destination
	 * 
	 * @return true if all the equipments have arrived 
	 */
	private boolean endRun(){
		boolean allAtDestination = true;
		for (EquipmentAgent agent : TopologyBuilder.getEquipmentContext().getObjects(EquipmentAgent.class)){
			if(!agent.isAtDestination()){
				allAtDestination = false;
				break;
			}
		}
		if(allAtDestination){
			RunEnvironment.getInstance().endRun();
		}
		return allAtDestination;
	}
	
	/**
	 * Evaluates the current 
	 * @return
	 */
	private boolean evaluateCurrentPosition(){
		
		// System.out.println(coveredDistance);
		
		if(this.getEntity().equals(Entity.OpticalDistributionPoint.description())){
			
			if (TopologyBuilder.factory == null)
				TopologyBuilder.factory = new GeometryFactory();
			
			// if the ODP already traveled 500m * optical splices created, schedule an Optical Splice and continue traveling
			if(this.coveredDistance >= (TopologyBuilder.maxDistanceToOpticalSplice * this.opticalSplicesCreated) ){
				
				
				InfraNodeAgent currentNodeAgent = getCurrentInfraNode();
				
				// if there's already an optical splice in this currentNodeAgent there's no need to create a new one
				boolean alreadyPresent = false;
				for (EquipmentAgent agent : TopologyBuilder.getEquipmentContext().getObjects(EquipmentAgent.class)){
					if (agent.getDestinationNode().equals(currentNodeAgent)){
						agent.add2OdpList(this);
						alreadyPresent = true;
					}
				}
				if (!alreadyPresent){
					Entity entity = Entity.OpticalSplice;
					if(this.lastNode.getEntity().equals(Entity.CentralOffice.description())){
						entity = Entity.OpticalSplitter;
					}
					EquipmentAgent equipmentAgent = new EquipmentAgent(
							TopologyBuilder.getGeography(),
							TopologyBuilder.getNextEquipmentId(),
							entity.description(),
							entity.network(),
							null, 
							this.lastNode, 
							currentNodeAgent);
					
					TopologyBuilder.getEquipmentContext().add(equipmentAgent);
					TopologyBuilder.getGeography().move(equipmentAgent, TopologyBuilder.getCentralOffice().getGeometry());
					
					equipmentAgent.add2OdpList(this);
					createCable(equipmentAgent);
					
					ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule(); 
					schedule.schedule(ScheduleParameters.createRepeating(schedule.getTickCount() + 1, 1, 0), equipmentAgent, "step");
				}
				
				this.opticalSplicesCreated++;
				this.lastNode = currentNodeAgent;
				
			}
		}
		
		return true;
	}

	/**
	 * The Equipment is at his destination when his coordinate is equals to the
	 * destination infranode
	 * 
	 * @return true if it has arrived to the destination infranode
	 */
	@Parameter(usageName = "arrivedDestination", displayName = "Agent at destination?")
	private boolean atDestination() {
		this.atDestination = TopologyBuilder.getGeography().getGeometry(this).getCoordinate()
				.equals(this.destinationNode.getGeometry().getCoordinate()) ? true
				: false;
		return this.atDestination;
	}
	
	/**
	 * Finds the shortest path between the C.O. and the destination InfraNode for this equipment.
	 * If the destination InfraNode isn't in the graph network the nearest InfraNode one must be compute. 
	 * The nearest InfraNode also has to exist in the graph network .
	 * 
	 * @throws NoSuchElementException
	 */
	private void setPath() throws NoSuchElementException {
		this.path = TopologyBuilder.getShortestPaths4Network().getPath(TopologyBuilder.getCentralOffice(), destinationNode);
		// this.pathLength =  TopologyBuilder.getShortestPaths4Network().getPathLength(TopologyBuilder.getCentralOffice(), destinationNode);
		if (this.path == null || this.path.isEmpty()){
			throw new NoSuchElementException("There isn't any path between the source "+TopologyBuilder.getCentralOffice().getLabel()+" and target "+destinationNode.getLabel()+"!");
		}
		IndexedIterable<RouteAgent> routeCollection = TopologyBuilder.getRouteContext().getObjects(RouteAgent.class);
		for (RepastEdge<InfraNodeAgent> edge : this.path){
			for(RouteAgent routeAgent : routeCollection){
				try {
					if (routeAgent.getNode1().equals(edge.getSource()) && routeAgent.getNode2().equals(edge.getTarget())){
						this.pathCoordinates.addAll(Arrays.asList(routeAgent.getCoordinates()));
						break;
					}
				} catch (Exception e) {
					System.out.println(routeAgent.toString());
					System.out.println(edge.toString());
					e.printStackTrace();
				}
			}
		}			
		if (this.pathCoordinates == null || this.pathCoordinates.isEmpty()){
			throw new NoSuchElementException("There isn't any coordinate array defining the path between the source and target infranodes!");
		}
		this.pathCoordinates = Utilities.removeDuplicateCoordinates(this.pathCoordinates);
		
	}
	
	private boolean createCable(EquipmentAgent equipmentAgent) {
		
		String entity = equipmentAgent.getEntity();
		if(entity.equals(Entity.OpticalSplice.description()) || entity.equals(Entity.OpticalSplitter.description()) ){

			Entity entityCable = Entity.SecondaryCable;
			if (equipmentAgent.getStartingNode().getEntity().equals(Entity.CentralOffice.description())){
				entityCable = Entity.PrimaryCable;
			}
			CableAgent cableAgent = new CableAgent(
					TopologyBuilder.getGeography(),
					TopologyBuilder.getNextCableId(),
					entityCable.description(),
					0,
					equipmentAgent.getStartingNode(),
					equipmentAgent.getDestinationNode());
			equipmentAgent.addCable2List(cableAgent);
			
		}
		
		return equipmentAgent.getCableList().size() < 1 ? false : true;
	}
	
	private Coordinate[] extractCableCoordinates(CableAgent cableAgent){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for(RepastEdge<InfraNodeAgent> edge : TopologyBuilder.getShortestPaths4Network().getPath(cableAgent.getNode1(), cableAgent.getNode2())){
			for(RouteAgent routeAgent : TopologyBuilder.getRouteContext().getObjects(RouteAgent.class)){
				if(edge.getSource().equals(routeAgent.getNode1()) && edge.getTarget().equals(routeAgent.getNode2())){
					for(Coordinate c : routeAgent.getCoordinates()){
						coordinates.add(c);
					}
				}
			}
		}
		return Utilities.removeDuplicateCoordinates(coordinates).toArray(new Coordinate[]{});
	}
	
	private boolean displayCables(){
		this.setDisplayCables(true);

		if(this.getEntity().equals(Entity.OpticalSplice.description())
				|| this.getEntity().equals(Entity.OpticalSplitter.description())){
			
			for(CableAgent cableAgent : this.getCableList()){
				// Primary, Secondary cables
				Geometry cableGeometry = TopologyBuilder.factory.createLineString(extractCableCoordinates(cableAgent));
				TopologyBuilder.getCableContext().add(cableAgent);
				TopologyBuilder.getGeography().move(cableAgent, cableGeometry);
			}
			
		} else if (this.getEntity().equals(Entity.OpticalDistributionPoint.description()) 
					|| this.getEntity().equals(Entity.OpticalDivider.description())){
			
			TopologyBuilder.factory = new GeometryFactory();
			for (SurveyAgent surveyAgent : this.getSurveyList()) {
				// Drop cables
				CableAgent cableAgent = new CableAgent(
						TopologyBuilder.getGeography(),
						TopologyBuilder.getNextCableId(),
						Entity.DropCable.description(),
						surveyAgent.getPotentialSubscribers(),
						this.destinationNode,
						null);
				Coordinate[] coordinates = new Coordinate[]{
						surveyAgent.getCoordinate(),
						this.destinationNode.getCoordinate()
				};
				Geometry cableGeometry = TopologyBuilder.factory.createLineString(coordinates);
				TopologyBuilder.getCableContext().add(cableAgent);
				TopologyBuilder.getGeography().move(cableAgent, cableGeometry);
			}
			
			if (this.getEntity().equals(Entity.OpticalDistributionPoint.description())){
				// Secondary cable
				CableAgent cableAgent = new CableAgent(
						TopologyBuilder.getGeography(),
						TopologyBuilder.getNextCableId(),
						Entity.SecondaryCable.description(),
						0,
						this.lastNode,
						this.destinationNode);
				
				Geometry cableGeometry = TopologyBuilder.factory.createLineString(extractCableCoordinates(cableAgent));
				TopologyBuilder.getCableContext().add(cableAgent);
				TopologyBuilder.getGeography().move(cableAgent, cableGeometry);
			}
			
		}
		
		return true;
	}
	
	/**
	 * Finds the InfraNode 1 from the Route Agent in which 
	 * a Equipment Agent is standing. The verification is done
	 * through the current equipment's coordinate
	 * 
	 * @param equipmentAgent an Equipment Agent to extract a coordinate from
	 * @return a infranode agent representing the starting node of the route agent
	 */
	private InfraNodeAgent getCurrentInfraNode(){
		InfraNodeAgent infraNodeAgent = null;
		// Geometry buffer = this.getGeometry().buffer(Utilities.convertFromMeters(5));
		Geometry buffer = this.getGeometry().buffer(.01);
		double minimalDistance = Double.MAX_VALUE;
		for(RouteAgent routeAgent : TopologyBuilder.getGeography().getObjectsWithin(buffer.getEnvelopeInternal(), RouteAgent.class)){
			DistanceOp distanceOp = new DistanceOp( this.getGeometry(), routeAgent.getGeometry());
			double currentDistance = distanceOp.distance();
			if (currentDistance < minimalDistance){
				minimalDistance = currentDistance;
				infraNodeAgent = routeAgent.getNode1();
			}
			/*
			if(Arrays.asList(routeAgent.getCoordinates()).contains(this.getCoordinate())){
				infraNodeAgent = routeAgent.getNode1();
				return infraNodeAgent;
			}
			*/
		}
		return infraNodeAgent;
	}
	
	@ProbeID
	public String toString(){
		return new String("Equipment." + this.getId());
	}
	
	/**
	 * @return the survey count
	 */
	@Parameter(usageName = "surveyCount", displayName = "Number of surveys supplied")
	public int getSurveyCount(){
		int count = 0;
		if (surveyList != null){
			count = surveyList.size();
		}
		return count;
	}

	/**
	 * @return the surveyList
	 */
	public List<SurveyAgent> getSurveyList() {
		return surveyList;
	}

	/**
	 * @param surveyList the surveyList to set
	 */
	public void setSurveyList(List<SurveyAgent> surveyList) {
		this.surveyList = surveyList;
	}

	/**
	 * @return the startingNode
	 */
	@Parameter(usageName = "startingNode", displayName = "Starting Infranode")
	public InfraNodeAgent getStartingNode() {
		return startingNode;
	}

	/**
	 * @param startingNode the startingNode to set
	 */
	public void setStartingNode(InfraNodeAgent startingNode) {
		this.startingNode = startingNode;
	}

	/**
	 * @return the destinationNode
	 */
	@Parameter(usageName = "destinationNode", displayName = "Destination Infranode")
	public InfraNodeAgent getDestinationNode() {
		return destinationNode;
	}

	/**
	 * @param destinationNode the destinationNode to set
	 */
	public void setDestinationNode(InfraNodeAgent destinationNode) {
		this.destinationNode = destinationNode;
	}
	
	/**
	 * @return the cable count
	 */
	@Parameter(usageName = "cableCount", displayName = "Number of cables")
	public int getCableCount() {
		return this.getCableList().size();
	}

	/**
	 * @return the cableList
	 */
	public List<CableAgent> getCableList() {
		return cableList;
	}

	/**
	 * @param cableList the cableList to set
	 */
	public void setCableList(List<CableAgent> cableList) {
		this.cableList = cableList;
	}
	/**
	 * @param cableList the cableList to set
	 */
	public void addCable2List(CableAgent cableAgent) {
		if(this.cableList == null){
			this.cableList = new ArrayList<CableAgent>();
		}
		this.cableList.add(cableAgent);
	}

	/**
	 * @return the displayCables
	 */
	public boolean isDisplayCables() {
		return displayCables;
	}

	/**
	 * @param displayCables the displayCables to set
	 */
	public void setDisplayCables(boolean displayCables) {
		this.displayCables = displayCables;
	}

	/**
	 * @return the odpList
	 */
	public List<EquipmentAgent> getOdpList() {
		return odpList;
	}

	/**
	 * @param odpList the odpList to set
	 */
	public void add2OdpList(EquipmentAgent agent) {
		if(this.odpList == null){
			this.odpList = new ArrayList<EquipmentAgent>();
		}
		this.odpList.add(agent);
	}

	/**
	 * @return the atDestination
	 */
	public boolean isAtDestination() {
		return atDestination;
	}

}
