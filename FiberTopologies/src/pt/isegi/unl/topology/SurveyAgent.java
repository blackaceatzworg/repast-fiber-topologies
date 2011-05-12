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
@AgentAnnot(displayName = "Survey Agent")
public class SurveyAgent extends Agent {

	private String street;
	private String streetNumber;
	private String locality;
	private int potentialSubscribers;
	private boolean nearToCentralOffice;

	/**
	 * 
	 */
	public SurveyAgent(Geography<Object> geography, int id, String label,
			String street, String streetNumber, String locality,
			int potentialSubscribers, boolean nearToCentralOffice) {
		setGeography(geography);
		setId(id);
		setLabel(label);
		this.street = street;
		this.streetNumber = streetNumber;
		this.locality = locality;
		this.potentialSubscribers = potentialSubscribers;
		this.nearToCentralOffice = nearToCentralOffice;
	}
	
	@ProbeID
	public String toString(){
		return new String("Agent = {id: " + getId() + ", street: "
				+ getStreet() + ", streetNumber: " + getStreetNumber() + ", locality: "
				+ getLocality() + ", potentialSubscribers: " + getPotentialSubscribers()
				+ ", nearToCentralOffice: " + isNearToCentralOffice() + ", wkt: " + getGeometry().toText() + "}");
	}

	/**
	 * @return the street
	 */
	@Parameter(usageName = "street", displayName = "Street")
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the streetNumber
	 */
	@Parameter(usageName = "streetNumber", displayName = "Street number")
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * @param streetNumber
	 *            the streetNumber to set
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	/**
	 * @return the locality
	 */
	@Parameter(usageName = "locality", displayName = "Locality")
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality
	 *            the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @return the potentialSubscrivers
	 */
	@Parameter(usageName = "potentialSubscribers", displayName = "Number of potential subscrivers")
	public int getPotentialSubscribers() {
		return potentialSubscribers;
	}

	/**
	 * @param potentialSubscrivers
	 *            the potentialSubscrivers to set
	 */
	public void setPotentialSubscribers(int potentialSubscribers) {
		this.potentialSubscribers = potentialSubscribers;
	}

	/**
	 * @return the nearToCentralOffice
	 */
	@Parameter(usageName = "nearToCentralOffice", displayName = "Near to the C.O.")
	public boolean isNearToCentralOffice() {
		return nearToCentralOffice;
	}

	/**
	 * @param nearToCentralOffice
	 *            the nearToCentralOffice to set
	 */
	public void setNearToCentralOffice(boolean nearToCentralOffice) {
		this.nearToCentralOffice = nearToCentralOffice;
	}

}
