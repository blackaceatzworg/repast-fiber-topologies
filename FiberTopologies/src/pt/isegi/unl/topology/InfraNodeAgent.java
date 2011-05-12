/**
 * 
 */
package pt.isegi.unl.topology;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.annotate.AgentAnnot;
import repast.simphony.space.gis.Geography;

/**
 * @author pmendes
 * 
 */
@AgentAnnot(displayName = "InfraNode Agent")
public class InfraNodeAgent extends Agent {

	private Long nodeId;
	private List<RouteAgent> routeList;

	/**
	 * Default constructor
	 */
	public InfraNodeAgent() {
	}

	public InfraNodeAgent(Geography<Object> geography, int id, String label, String entity, String network,
			String installation, Long nodeId) {
		setGeography(geography);
		setId(id);
		setLabel(label);
		setEntity(entity);
		setNetwork(network);
		setInstallation(installation);
		this.nodeId = nodeId;
	}

	public boolean equals(InfraNodeAgent agent) {
		return agent == null && this == null ? true : agent == null ? false
				: agent.getId() == this.getId() ? true : false;
	}

	/**
	 * @return the nodeId
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return the routeList
	 */
	public List<RouteAgent> getRouteList() {
		return routeList;
	}

	/**
	 * @param routeList
	 *            the routeList to set
	 */
	public void setRouteList(List<RouteAgent> routeList) {
		this.routeList = routeList;
	}

	/**
	 * @param routeAgent
	 *            the route to adds
	 */
	public void addRoute(RouteAgent routeAgent) {
		if (this.routeList == null) {
			this.routeList = new ArrayList<RouteAgent>();
		}
		this.routeList.add(routeAgent);

	}

}
