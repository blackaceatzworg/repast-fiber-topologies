/**
 * 
 */
package pt.isegi.unl.topology;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.parameter.Parameter;
import repast.simphony.space.gis.Geography;
import repast.simphony.ui.probe.ProbeID;

/**
 * @author pmendes
 * 
 */
@AgentAnnot(displayName = "Route Agent")
public class RouteAgent extends Agent {

	private InfraNodeAgent node1;
	private InfraNodeAgent node2;
	private Double length;


	/**
	 * Single constructor
	 * @param geography
	 * @param id
	 * @param label
	 * @param entity
	 * @param network
	 * @param installation
	 * @param node1
	 * @param node2
	 * @param length
	 */
	public RouteAgent(Geography<Object> geography, int id, String label, String entity, String network,
			String installation, InfraNodeAgent node1, InfraNodeAgent node2,
			Double length) {
		setGeography(geography);
		setId(id);
		setLabel(label);
		setEntity(entity);
		setNetwork(entity);
		setInstallation(installation);
		this.node1 = node1;
		this.node2 = node2;
		this.length = length;
	}

	@ProbeID
	public String toString() {
		return new String("RouteAgent = {id: " + getId() + ", label: "
				+ getLabel() + ", entity: " + getEntity() + ", network: "
				+ getNetwork() + ", installation: " + getInstallation()
				+ ", length: " + length + ", wkt: " + getGeometry().toText() + "}");
	}

	/**
	 * @return the node1
	 */
	@Parameter(usageName = "node1", displayName = "Node 1")
	public InfraNodeAgent getNode1() {
		return node1;
	}

	/**
	 * @param node1
	 *            the node1 to set
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
	 * @param node2
	 *            the node2 to set
	 */
	public void setNode2(InfraNodeAgent node2) {
		this.node2 = node2;
	}

	/**
	 * @return the length
	 */
	@Parameter(usageName = "length", displayName = "Route length")
	public Double getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(Double length) {
		this.length = length;
	}
}
