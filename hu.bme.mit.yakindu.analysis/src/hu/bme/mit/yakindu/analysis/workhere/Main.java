package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Statechart;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<String> nemCsapda = new ArrayList<String>();
		ArrayList<String> states = new ArrayList<String>();
		ArrayList<String> transitions = new ArrayList<String>();
		
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				states.add(state.getName());
				
			}else {
				if(content instanceof Transition) {
					Transition transition = (Transition) content;
					nemCsapda.add(transition.getSource().getName());
					transitions.add(new String(transition.getSource().getName() + " -> " + transition.getTarget().getName()));
					System.out.println();
				}
			}
		}
		System.out.println("States:");
		for (String st : states) {
			System.out.println(st);
		}
		
		System.out.println("Transitions:");
		for (String tr : transitions) {
			System.out.println(tr);
		}
		
		System.out.println("Csapda allapotok:");
		for (String st : states) {
			if(!nemCsapda.contains(st)) {
				System.out.println(st);
			}
		}
		
	
		int nevtelenAllapotokSzama=0;
		for(String st : states) {
			if(st=="" || st==null) {
				nevtelenAllapotokSzama++;
			}
		}
		
		System.out.println("Talalt nevtelen allapotok: " + nevtelenAllapotokSzama + " db");
		if(nevtelenAllapotokSzama > 0) {
			System.out.println("Javasolt nevek:");
			int j=1;
			for(int i=1;i<=nevtelenAllapotokSzama;i++) {
				while(states.contains("state_" + j)) {
					j++;
				}
				System.out.println("state_" + j);
				j++;
			}
		}
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
