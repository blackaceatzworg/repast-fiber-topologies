/**
 * 
 */
package pt.isegi.unl.topology;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.gis.Geography;
import repast.simphony.ui.probe.ProbeID;

/**
 * @author pmendes
 *
 */
public class CableAgent extends Agent {
	
	private InfraNodeAgent node1;
	private InfraNodeAgent node2;
	private int numberOfFibers;

	public CableAgent(Geography<Object> geography, int id, String entity, int numberOfFibers, InfraNodeAgent node1, InfraNodeAgent node2){
		setGeography(geography);
		setId(id);
		setEntity(entity);
		this.numberOfFibers = numberOfFibers;
		this.node1 = node1;
		this.node2 = node2;
	}

	@ProbeID
	public String toString() {
		return new String("CableAgent = {id: " + getId() + ", label: "
				+ getLabel() + ", entity: " + getEntity() + ", network: "
				+ getNetwork() + ", installation: " + getInstallation()
				+ ", numberOfFibers: " + numberOfFibers + ", wkt: " + getGeometry().toText() + "}");
	}
	
	public boolean equals(CableAgent cableAgent){
		if (getId() == cableAgent.getId()){
			return true;
		}
		return false;
	}

	/**
	 * @return the numberOfFibers
	 */
	@Parameter(usageName = "numberOfFibers", displayName = "Cable capacity")
	public int getNumberOfFibers() {
		return numberOfFibers;
	}

	/**
	 * @param numberOfFibers the numberOfFibers to set
	 */
	public void setNumberOfFibers(int numberOfFibers) {
		this.numberOfFibers = numberOfFibers;
	}

	/**
	 * @return the node1
	 */
	@Parameter(usageName = "node1", displayName = "Node 1")
	public InfraNodeAgent getNode1() {
		return node1;
	}

	/**
	 * @param node1 the node1 to set
	 */
	public void setNode1(InfraNodeAgent node1) {
		this.node1 = node1;
	}

	/**
	 * @return the node2
	 */
	@Parameter(usageName = "node2", displayName = "Node 2")
	public InfraNodeAgent getNode2() {
		return node2;
	}

	/**
	 * @param node2 the node2 to set
	 */
	public void setNode2(InfraNodeAgent node2) {
		this.node2 = node2;
	}

}
