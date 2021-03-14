package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.stext.stext.EventDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
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
		ArrayList<String> variableDefinitions = new ArrayList<String>();
		ArrayList<String> eventDefinitions = new ArrayList<String>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition variableDefinition = (VariableDefinition) content;
				variableDefinitions.add(variableDefinition.getName());
			}else {
				if(content instanceof EventDefinition) {
					EventDefinition eventDefinition = (EventDefinition) content;
					eventDefinitions.add(eventDefinition.getName());
				}
			}
		}
		
		System.out.println("public static void print(IExampleStatemachine s) {");
		for (String st : variableDefinitions) {
			System.out.println("System.out.println(\"" + st + " = \" + s.getSCInterface().get" + st.substring(0,1).toUpperCase() + st.substring(1) + "());");
		}
		System.out.println("}");
		
		System.out.println();
		
		System.out.println("public static void main(String[] args) throws IOException {");
		System.out.println("ExampleStatemachine s = new ExampleStatemachine();");
		System.out.println("s.setTimer(new TimerService());");
		System.out.println("RuntimeService.getInstance().registerStatemachine(s, 200);");
		System.out.println("s.init();");
		System.out.println("s.enter();");
		System.out.println("s.runCycle();");
		System.out.println("Scanner scanner = new Scanner(System.in);");
		System.out.println("String str=scanner.nextLine();");
		System.out.println("print(s);");
		System.out.println("while(!str.equals(\"exit\")) {");
		
		for(String r: eventDefinitions) {
			System.out.println("if(str.equals(\"" + r + "\")) {");
			System.out.println("s.raise" + r.substring(0,1).toUpperCase() + r.substring(1) + "();");
			System.out.println("s.runCycle();");
			System.out.println("}");
		}
		
		System.out.println("str=scanner.nextLine();");
		System.out.println("print(s);");
		System.out.println("}");
		System.out.println("System.exit(0);");
		System.out.println("}");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
