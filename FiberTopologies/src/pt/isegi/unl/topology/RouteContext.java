/**
 * 
 */
package pt.isegi.unl.topology;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;

import repast.simphony.context.DefaultContext;
import repast.simphony.space.gis.ShapefileLoader;
import repast.simphony.util.collections.IndexedIterable;

/**
 * @author pmendes
 *
 */
public class RouteContext extends DefaultContext<RouteAgent> {
	
	/**
	 * 
	 */
	public RouteContext() {
		super("RouteContext");
		
		/*
		 * route loading
		 */
		URL fileURL;
		FeatureIterator fIterator = null;
		IndexedIterable<InfraNodeAgent> infraNodeCollection = TopologyBuilder.getInfraNodeContext().getObjects(InfraNodeAgent.class);
		try {
			fileURL = new File("misc/shapefile/routes.shp").toURI().toURL();
			ShapefileDataStore store = new ShapefileDataStore(fileURL);
			FeatureCollection collection = store.getFeatureSource().getFeatures();
			ShapefileLoader<RouteAgent> loader = new ShapefileLoader<RouteAgent>(RouteAgent.class, fileURL, TopologyBuilder.getGeography(), this);
			fIterator = collection.features();
			System.out.println("Preparing to load a set of Route Agents... ");
			while (loader.hasNext()) {
				Feature feature = fIterator.next();
				RouteAgent agent = new RouteAgent(
							TopologyBuilder.getGeography(),
							TopologyBuilder.extractFeatureId(feature),
							(String) feature.getAttribute("label"),
							(String) feature.getAttribute("entity"),
							(String) feature.getAttribute("network"),
							(String) feature.getAttribute("instalation"),
							findInfraNodeAgent(infraNodeCollection, (Long) feature.getAttribute("node1")),
							findInfraNodeAgent(infraNodeCollection, (Long) feature.getAttribute("node2")),
							(Double) feature.getAttribute("length_m"));
				loader.next(agent);
								
			}
			fIterator.close();
			System.out.println(collection.size() + " Route Agents loaded!");
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private InfraNodeAgent findInfraNodeAgent(IndexedIterable<InfraNodeAgent> infraNodeCollection, Long id){
		InfraNodeAgent node = null;
		for (InfraNodeAgent infranode : infraNodeCollection){
			if (infranode.getNodeId().equals(id)){
				node = infranode;
				continue;
			}
		}
		/*if (node == null){
			throw new NoSuchElementException(featureId+": Unable to find the node "+id+" in the InfraNodeContext");
		}*/
		return node;
	}
	
	/**
	 * TODO find another way of removing null agents from the RouteContext
	 */
	@SuppressWarnings("unused")
	private void clearInvalidAgentsFromContext(){
		try {
			IndexedIterable<RouteAgent> routeCollection = this.getObjects(RouteAgent.class);
			for (RouteAgent agent : routeCollection) {
				if (agent.getNode1() == null || agent.getNode2() == null) {
					System.out.println("Removing Route." + agent.getId());
					this.remove(agent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
