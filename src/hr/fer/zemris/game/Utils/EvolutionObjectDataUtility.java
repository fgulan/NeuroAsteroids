package hr.fer.zemris.game.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hr.fer.zemris.game.Constants.EVOLUTION_ELEMENTS_FILES_DIR;
import static hr.fer.zemris.game.Constants.EVOLUTION_ELEMENTS_LIST_FILE;

public class EvolutionObjectDataUtility {
	private static final EvolutionObjectDataUtility instance = new EvolutionObjectDataUtility();

	List<EvolutionElement> elements;
	Map<String, Serializable> saveMap;

	private EvolutionObjectDataUtility(){
		saveMap = new HashMap<>();
		elements = new ArrayList<>();

		try {
			List<String> data = Files.readAllLines(Paths.get(EVOLUTION_ELEMENTS_LIST_FILE));
			//Curtsy of IntelliJ IDEA
			elements = data.stream().map(EvolutionElement::parse).collect(Collectors.toList());
		} catch (IOException e) {
			//elements.txt file not found
		}
	}

	public static EvolutionObjectDataUtility getInstance(){
		return instance;
	}

	public Serializable loadObject(String id){
		Serializable net;

		// Deserialize data to new class object
		try {
			FileInputStream fi = new FileInputStream(EVOLUTION_ELEMENTS_FILES_DIR + id+".ser");
			ObjectInputStream si = new ObjectInputStream(fi);
			net = (Serializable) si.readObject();
			si.close();
		} catch (Exception e) {
			//xxx.ser file not found, still not saved
			net = saveMap.get(id+".ser");
		}
		return net;

	}

	public void saveObject(Serializable object, String name, double fitness, String comment){
		EvolutionElement net = new EvolutionElement(name, fitness, comment);
		elements.remove(net);	//Remove duplicate if exists
		elements.add(net);
		saveMap.put(name+".ser", object);
	}

	public List<EvolutionElement> getNeuralObjects(){
		return elements;
	}

	public void flush(){

		StringBuilder sb = new StringBuilder();
		for (EvolutionElement ne: elements) {
			sb.append( ne.toString())
			  .append(System.lineSeparator());
		}					

		try {
			// Serialize data to file
			for(String fileName : saveMap.keySet()) {
				FileOutputStream fo = new FileOutputStream(EVOLUTION_ELEMENTS_FILES_DIR + fileName);
				ObjectOutputStream so = new ObjectOutputStream(fo);
				so.writeObject(saveMap.get(fileName));
				so.close();

				if(!elements.contains(saveMap.get(fileName))) {
					Files.write(Paths.get(EVOLUTION_ELEMENTS_LIST_FILE), sb.toString().getBytes());
				}
			}
		} catch (IOException e) {
			//xxx.ser files
			//or nerualNets.txt file could not be saved
			e.printStackTrace();
		}
	}
}
