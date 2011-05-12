/**
 * 
 */
package pt.isegi.unl.topology;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;

import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.graph.NetworkFactory;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.space.gis.ShapefileLoader;

/**
 * @author pmendes
 *
 */
public class InfraNodeContext extends DefaultContext<InfraNodeAgent> {

	/**
	 * 
	 */
	public InfraNodeContext() {
		super("InfraNodeContext");
		
		NetworkFactory networkFactory = NetworkFactoryFinder.createNetworkFactory(new HashMap<String, Object>());
		networkFactory.createNetwork("InfraNodeNetwork", this, false);
		
		/*
		 * infranode loading
		 */
		URL fileURL;
		try {
			fileURL = new File("misc/shapefile/infranodes.shp").toURI().toURL();
			ShapefileDataStore store = new ShapefileDataStore(fileURL);
			FeatureCollection collection = store.getFeatureSource().getFeatures();
			ShapefileLoader<InfraNodeAgent> loader = new ShapefileLoader<InfraNodeAgent>(InfraNodeAgent.class, fileURL, TopologyBuilder.getGeography(), this);
			FeatureIterator fIterator = collection.features();
			System.out.println("Preparing to load a set of InfraNode  Agents... ");
			while (loader.hasNext()) {
				Feature feature = fIterator.next();
				InfraNodeAgent agent = new InfraNodeAgent(
						TopologyBuilder.getGeography(),
						TopologyBuilder.extractFeatureId(feature),
						(String) feature.getAttribute("label"),
						(String) feature.getAttribute("entity"),
						(String) feature.getAttribute("network"),
						(String) feature.getAttribute("instalatio"),
						(Long) feature.getAttribute("node_id"));
				loader.next(agent);
			}
			fIterator.close();
			System.out.println(collection.size() + " InfraNode Agents loaded!");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
