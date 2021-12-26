package port.cli;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.reflect.InvocationTargetException;

class Option {
	String description;
	String method;

	public Option(String d, String m) {
		description = d;
		method = m;
	}
}

public class Menu {
	ArrayList<Option> options = new ArrayList<Option>();
	String title = "Choose an option: ";
	Boolean exited = false;
	Scanner S = new Scanner(System.in);

	protected void setTitle(String t) {
		title = t;
	}

	protected void addOption(String d, String m) {
		options.add(new Option(d, m));
	}
	
	public boolean hasExited() {
		return exited;
	}

	public void execute() {
		int choice;
		exited = false;

		System.out.println(title);

		for (int i = 0; i < options.size(); i++) {
			System.out.println(String.format("  [%d] %s", i + 1, options.get(i).description));
		}

		System.out.println(String.format("  [%d] Esci dal menu", options.size() + 1));
		
		do {
			System.out.print(" > ");
			choice = S.hasNextInt() ? S.nextInt() : -1;
		} while(choice < 1 || choice > options.size() + 1);
		
		if (choice == options.size() + 1) {
			exited = true;
			return;
		}

		String methodName = options.get(choice - 1).method;
		
		Class<?> thisClass = this.getClass();

		Method method;
		try {
			method = thisClass.getDeclaredMethod(methodName);
			method.invoke(this);
		} catch (InvocationTargetException e) { }
		  catch (NoSuchMethodException e) { }
		  catch (IllegalAccessException e) { }
	}
}
