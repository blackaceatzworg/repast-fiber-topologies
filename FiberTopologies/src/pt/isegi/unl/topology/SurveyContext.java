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

/**
 * @author pmendes
 *
 */
public class SurveyContext extends DefaultContext<SurveyAgent> {
	
	/**
	 * 
	 */
	public SurveyContext() {
		super("SurveyContext");
		
		/*
		 * survey loading
		 */
		URL fileURL;
		try {
			fileURL = new File("misc/shapefile/surveys.shp").toURI().toURL();
			ShapefileDataStore store = new ShapefileDataStore(fileURL);
			FeatureCollection collection = store.getFeatureSource().getFeatures();
			ShapefileLoader<SurveyAgent> loader = new ShapefileLoader<SurveyAgent>(SurveyAgent.class, fileURL, TopologyBuilder.getGeography(), this);
			FeatureIterator fIterator = collection.features();
			System.out.println("Preparing to load a set of Survey Agents... ");
			while (loader.hasNext()) {
				Feature feature = fIterator.next();
				SurveyAgent agent = new SurveyAgent(
						TopologyBuilder.getGeography(),
						TopologyBuilder.extractFeatureId(feature),
						(String) feature.getAttribute("CODE"),
						(String) feature.getAttribute("ARTERIA"),
						(String) feature.getAttribute("ARG1"),
						(String) feature.getAttribute("LOCALIDADE"),
						extractPotentialSubscribers(feature),
						false);
				loader.next(agent);
			}
			fIterator.close();
			System.out.println(collection.size() + " Survey Agents loaded!");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param feature
	 * @return the number of potential subscribers in this survey
	 */
	private int extractPotentialSubscribers(Feature feature){
		int n = 0;
		if(feature.getAttribute("TOTAL_UAS") != null){
			n =  ((Double) feature.getAttribute("TOTAL_UAS")).intValue();
		}
		return n;
	}

}
