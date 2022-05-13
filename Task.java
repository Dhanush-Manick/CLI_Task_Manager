/**
 * This is a Task Manager Program
 * @author DHANUSH N
 * @version 1.o
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Custom sort;
 */
class Priority implements Comparator<List<String>>{

	/**
	 * This method is to sort tasks based on the priority
	 * @param task1 The object to be compared
	 * @param task2 The object to be compared
	 * @return Integer value based on the comparison result
	 */

	@Override
	public int compare(List<String> task1, List<String> task2) {
		if(task1.get(0).equals(task2.get(0)))
			return 0;
		else if (Integer.parseInt(task1.get(0)) > Integer.parseInt(task2.get(0)))
			return 1;
		return -1;
	}
}

/**
 * Main class
 */
public class Task {

	/**
	To store list of tasks to be done
	 */
	public List<List<String>> list_of_task = new ArrayList<>();
	/**
	To store the list of completed tasks
	 */
	public List<String> list_of_completedTask = new ArrayList<>();
	/**
	 * This method Prints the usage of the program
	 */
	public void helpMessage() {
		String add = "Usage :-\n$ ./task add 2 hello world    " +
				"# Add a new item with priority 2 and text \"hello world\" to the list";
		String list = "$ ./task ls                   " +
				"# Show incomplete priority list items sorted by priority in ascending order";
		String delete = "$ ./task del INDEX            " +
				"# Delete the incomplete item with the given index";
		String done = "$ ./task done INDEX           " +
				"# Mark the incomplete item with the given index as complete";
		String help = "$ ./task help                 # Show usage";
		String details = "$ ./task report               # Statistics";

		System.out.println(add);
		System.out.println(list);
		System.out.println(delete);
		System.out.println(done);
		System.out.println(help);
		System.out.println(details);
	}

	/**
	 * This method adds the task to be done
	 * @param newItem Task to be added received from command line
	 */
	public void addTask(String[] newItem) {
		if (newItem.length != 3) { //Check whether the command is fully specified
			System.out.println("Error: Missing tasks string. Nothing added!");
			return;
		}
		if (Integer.parseInt(newItem[1]) < 0) { // Check for negative priority
			System.out.println("Negative priority is not allowed");
			return;
		}
		ArrayList<String> newTask = new ArrayList<>(); // Create String array to add task
		String priority = newItem[1];
		String work = newItem[2];
		newTask.add(priority);
		newTask.add(work);
		list_of_task.add(newTask); // Add new task to the task list
		if (list_of_task.size() > 1) {
			list_of_task.sort(new Priority()); // Sort if there's more than one task in task list
		}
		System.out.println("Added task: \"" + work + "\" with priority " + priority);
	}

	/**
	 * This method deletes the specified task
	 * @param remove Task to be deleted received from command line
	 */
	public void delTask(String[] remove) {
		if (remove.length != 2) { // Check whether the command is fully specified
			System.out.println("Error: Missing NUMBER for deleting tasks.");
			return;
		}
		int index = Integer.parseInt(remove[1]);
		if (index <= list_of_task.size() && index >= 1) {
			list_of_task.remove(index - 1); // removes the task from task list
			System.out.println("Deleted task #" + index);
		} else {
			System.out.println("Error: task with index #" + index + " does not exist. Nothing deleted.");
		}
	}

	/**
	 * This method marks the completed tasks
	 * @param completed Completed task received from command line
	 */
	public void doneTask(String[] completed) {
		if (completed.length != 2) { // Check whether the command is fully specified
			System.out.println("Error: Missing NUMBER for marking tasks as done.");
			return;
		}
		int index = Integer.parseInt(completed[1]);
		if (index <= list_of_task.size() && index >= 1) {
			System.out.println("Marked item as done.");
			list_of_completedTask.add(list_of_task.get(index - 1).get(1)); // Add the completed task to completed list
			list_of_task.remove(index - 1); // remove the task from task list
		} else {
			System.out.println("Error: no incomplete item with index #" + index + " exists.");
		}
	}

	/**
	 * This method is to list the pending task
	 */
	public void pendingTask() {
		if (list_of_task.size() == 0) {
			System.out.println("There are no pending tasks!");
			return;
		}
		for (int i = 1; i <= list_of_task.size(); i++) {
			String work = list_of_task.get(i - 1).get(1); // get the task from the array
			String priority = list_of_task.get(i - 1).get(0); // get the priority from the array
			String res = i + ". " + work + " [" + priority + "]";
			System.out.println(stringEncoder(res));
		}
	}

	/**
	 * This method is to display the list of pending task and completed task
	 */
	public void report() {
		System.out.println("Pending : " + list_of_task.size());
		pendingTask(); // call to pending task

		System.out.println("Completed : " + list_of_completedTask.size());
		for (int i = 1; i <= list_of_completedTask.size(); i++) {
			// prints the list of completed task
			String work = i + ". " + list_of_completedTask.get(i - 1);
			System.out.println(stringEncoder(work));
		}
	}

	/**
	 * This method is to convert List of characters to String
	 * @param characterList List of characters
	 * @return returns String
	 */
	public String getStringRepresentation(ArrayList<Character> characterList) {
		StringBuilder builder = new StringBuilder(characterList.size());
		for (Character ch : characterList) {
			builder.append(ch); //appends characters to the string
		}
		return builder.toString();
	}

	/**
	 * This method reads the tasks from file
	 * @param fileReader FileReader object in which the task to be read
	 * @return String array of tasks read from file
	 * @throws IOException on file error
	 */
	private String[] read_file(FileReader fileReader) throws IOException {
		int letter;
		ArrayList<Character> characters = new ArrayList<>(); // create character array
		while ((letter = fileReader.read()) != -1) {
			characters.add((char) letter); // append the characters received from file to character array
		}
		String total_task = getStringRepresentation(characters);
		return total_task.split("\n");
	}

	/**
	 * This method is to read from task file
	 * @param file Task file to be read
	 * @param fileReader File reader object to read the file
	 * @throws IOException on file error
	 */
	public void readTaskFile(File file, FileReader fileReader) throws IOException {
		if (file.length() != 0) {
			String[] separateTask = read_file(fileReader); // Strings read from file
			for (String s : separateTask) {
				ArrayList<String> temp = new ArrayList<>();
				String[] task = s.split(" ", 2); // splits the string to get priority and task
				String priority = task[0];
				String taskName = task[1];
				temp.add(priority);
				temp.add(taskName);
				list_of_task.add(temp); // add the received task to the task list
			}
		}
	}

	/**
	 *  This method is to read from completed file
	 * @param file Completed file to be read
	 * @param fileReader File reader object to read file
	 * @throws IOException on file error
	 */
	public void readCompletedFile(File file, FileReader fileReader) throws IOException {
		if (file.length() != 0) {
			list_of_completedTask.addAll(Arrays.asList(read_file(fileReader))); //appends the task read from completed file to completed task list
		}
	}

	/**
	 *  This method is to encode string using UTF-8 encoding
	 * @param string String to be encoded
	 * @return Encoded string
	 */
	private String stringEncoder(String string){
		byte[] bytes = string.getBytes(StandardCharsets.UTF_8); // convert the string to bytes
		return new String(bytes, StandardCharsets.UTF_8).trim(); // return the encoded string
	}

	/**
	 * This method is to write to file
	 * @param fileWriter File writer object to write file
	 * @throws IOException on file error
	 */
	public void writeTaskFile(FileWriter fileWriter) throws IOException {
		for (List<String> task : list_of_task) {
			String string = task.get(0) + " " + task.get(1) + "\n"; // task to be written to task file
			fileWriter.write(stringEncoder(string) + "\n"); // write to task file
		}
	}

	/**
	 * This method is to write to completed file
	 * @param fileWriter File writer object to write file
	 * @throws IOException on file error
	 */
	public void writeCompletedFile(FileWriter fileWriter) throws IOException {
		for (String task : list_of_completedTask) {
			String string = task + "\n";  // task to be written to completed file
			fileWriter.write(stringEncoder(string) + "\n");  // write to completed file
		}
	}

	/**
	 * This is the main method which drives the entire program
	 * @param args Command line arguments
	 * @throws IOException on file error
	 */
		public static void main(String[] args) throws IOException {
		// create object for the Task class
			Task taskManager = new Task();
			// create file objects
			File taskFile = new File(System.getProperty("user.dir") + "/Task.txt");
			File completedFile = new File(System.getProperty("user.dir") + "/Completed.txt");
			// creates new file it file dose not exists
			taskFile.createNewFile();
			completedFile.createNewFile();
				if (taskFile.length() != -1) { // checks for empty file
					FileReader taskReader = new FileReader(taskFile); // create file reader
					taskManager.readTaskFile(taskFile, taskReader); // read file
					taskReader.close();
				}
				if (completedFile.length() != -1) {  // checks for empty file
					FileReader completeReader = new FileReader(completedFile); // create file reader
					taskManager.readCompletedFile(completedFile, completeReader); // read file
					completeReader.close();
				}

			// executes function calls based on the command provided
			if (args.length == 0 || args[0].equals("help")) {
				taskManager.helpMessage();
			} else if (args[0].equals("add")) {
				taskManager.addTask(args);
			} else if (args[0].equals("ls")) {
				taskManager.pendingTask();
			} else if (args[0].equals("del")) {
				taskManager.delTask(args);
			} else if (args[0].equals("done")) {
				taskManager.doneTask(args);
			} else if (args[0].equals("report")) {
				taskManager.report();
			}

			if (taskManager.list_of_task.size() != 0) { // checks for empty list
				FileWriter taskWriter = new FileWriter(taskFile); // create file writer
				taskManager.writeTaskFile(taskWriter); // write to task file
				taskWriter.close();
			}

			if (taskManager.list_of_completedTask.size() != 0) { // checks for empty list
				FileWriter completedWriter = new FileWriter(completedFile); // create file writer
				taskManager.writeCompletedFile(completedWriter); // write to completed file
				completedWriter.close();
			}
		}
	}

