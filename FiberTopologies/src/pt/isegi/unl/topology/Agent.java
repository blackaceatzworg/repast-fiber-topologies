/**
 * 
 */
package pt.isegi.unl.topology;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.gis.Geography;
import repast.simphony.ui.probe.ProbeID;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author pmendes
 *
 */
public class Agent {
	
	private int id;
	private String label;
	private String entity;
	private String network;
	private String installation;
	private Geography<Object> geography;
	
	@ProbeID
	public String toString(){
		return new String("Agent = {id: " + getId() + ", label: "
				+ getLabel() + ", entity: " + getEntity() + ", network: "
				+ getNetwork() + ", installation: " + getInstallation()
				+ ", wkt: " + getGeometry().toText() + "}");
	}
	
	/**
	 * Find the agent geometry
	 * @param a geography object
	 * @return the agent geometry on the given geography
	 */
	public Geometry getGeometry(){
		return geography.getGeometry(this);
	}
	
	/**
	 * Find the agent coordinate
	 * @param a geography object
	 * @return the agent coordinate on the given geography
	 */
	public Coordinate getCoordinate(){
		return geography.getGeometry(this).getCoordinate();
	}
	
	/**
	 * Find the agent coordinates
	 * @param a geography object
	 * @return the agent coordinates on the given geography
	 */
	public Coordinate[] getCoordinates(){
		return geography.getGeometry(this).getCoordinates();
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the entity
	 */
	@Parameter(usageName="entity", displayName="Entity type")
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * @return the network
	 */
	@Parameter(usageName="network", displayName="network type")
	public String getNetwork() {
		return network;
	}
	/**
	 * @param network the network to set
	 */
	public void setNetwork(String network) {
		this.network = network;
	}
	/**
	 * @return the installation
	 */
	public String getInstallation() {
		return installation;
	}
	/**
	 * @param installation the installation to set
	 */
	@Parameter(usageName="installation", displayName="installation type")
	public void setInstallation(String installation) {
		this.installation = installation;
	}

	/**
	 * @return the geography
	 */
	public Geography<Object> getGeography() {
		return geography;
	}

	/**
	 * @param geography the geography to set
	 */
	public void setGeography(Geography<Object> geography) {
		this.geography = geography;
	}
	
	/**
	 * @return the Well Known Text representation of the geoemtry
	 */
	@Parameter(usageName = "wkt", displayName = "Well Known Text")
	public String getWKT() {
		return getGeometry().toText();
	}

}
