/**
 * 
 */
package pt.isegi.unl.topology;

/**
 * @author pmendes
 *
 */
public enum Entity {
	AerialPath("Aerial Path", "Physical Infrastructure Network"),
	Conduit("Conduit", "Physical Infrastructure Network"),
	Trenche("Trenche", "Physical Infrastructure Network"),
	Tower("Tower", "Physical Infrastructure Network"), 
	TechnicalUndergroundGallery("Technical Underground Gallery", "Physical Infrastructure Network"), 
	TechnicalBuilding("Technical Building", "Physical Infrastructure Network"), 
	Pole("Pole", "Physical Infrastructure Network"),
	Manhole("Manhole", "Physical Infrastructure Network"), 
	EquipmentCabinet("Equipment Cabinet", "Physical Infrastructure Network"), 
	CentralOffice("Central Office", "Physical Infrastructure Network"),
	OpticalDivider("Optical Divider", "Physical Infrastructure Network"),
	OpticalSplitter("Optical Splitter", "Physical Infrastructure Network"), 
	OpticalSplice("Optical Splice", "Physical Infrastructure Network"),  
	OpticalDistributionPoint("Optical Distribution Point", "Physical Infrastructure Network"),
	DropCable("Drop Cable", "Physical Infrastructure Network"),
	SecondaryCable("Secondary Cable", "Physical Infrastructure Network"),
	PrimaryCable("Primary Cable", "Physical Infrastructure Network");
	
	private final String description;
	private final String network;
	
	Entity(String description, String network){
		this.description = description;
		this.network = network;
	}
	
	String description(){
		return description;
	}
	String network(){
		return network;
	}
}
