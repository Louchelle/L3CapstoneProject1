package poise;
//POISED - Project Management System
//Everything works except finalix
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Poise {

	public static void main(String[] args) throws SQLException {
		// Connecting to the database
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/poisepms",
				"root",
				"Lb03022018!"
				);
		Statement statement = connection.createStatement();
					
		// Scanner for input
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to POISED project management system: \n"); 
		while (true) {
			System.out.println(menu());
			int option = input.nextInt();
			// Add a new project:
			if (option == 1) {
				newProject(statement);
				}
			// Update a project:
			if (option == 2) {
				updateProject(statement);
				}
			// Delete a project:
			if (option == 3) {
				deleteProject(statement);
				}
			// Search and find a spesific project
			if (option == 4) {
				searchProject(statement);
				}
			// See list of uncompleted projects
			if (option == 5) {
				uncompletedProjects(statement);
				}
			//Update Engineer
			if (option == 6) {
				updateEngineer(statement);
				}
			//Finalize a project 
			if (option == 7) {
				finalizeProject(statement);
				}
			// Exit
			if (option == 0) {
				connection.close();
				input.close();
				System.out.println("Goodbye!");
				break;
				}
			}
		}
	
	private static void finalizeProject(Statement statement) throws SQLException  {
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter the customers name of the project you want to finalize: ");
			String cuName = in.nextLine();
			ResultSet result = checkCuName(statement, cuName);
			// Check if customer exists
			if (!result.isBeforeFirst()) {
				System.out.println("Error! This customer does not exist");
			}
			else {
				ResultSet customer = statement.executeQuery("SELECT * FROM project WHERE Customer = '%s';".formatted(cuName));
				while (customer.next()) {
					int outstanding = customer.getInt("Outstanding");
					int amountDue = customer.getInt("AmountDue");
					int amountPaid = customer.getInt("AmountPaid");
					ResultSet customerDetails = statement.executeQuery("SELECT * FROM customer WHERE Name = '%s';".formatted(cuName));
					// Money outstanding - Create invoice
					if (outstanding != 0) {
						System.out.print("\nInvoice: \nTotal Fee: R" + amountDue);
						System.out.print("\nAmount paid: R" + amountPaid);
						System.out.println("\nOutstanding: R" + amountDue + " - R" + amountPaid + " = R" + outstanding); 
						while (customerDetails.next()) {
						String surname = customerDetails.getString("Surname");
						String eMail = customerDetails.getString("Email");
						String telephone = customerDetails.getString("Telephone");
						String address = customerDetails.getString("Address");
						System.out.println("\nCustomer's contact details: \nName: " + cuName 
								+ "\nSurname:  " + surname + "\nEmail:  " + eMail
								+ "\nTelephone: " + telephone + "\nAddress:  " + address);
								System.out.println("\nThe invoice has been sent to the customer.\n");
								break;
							}
						}
					// Finances settled
					if (outstanding == 0) {
						int update = statement.executeUpdate(
			                    "UPDATE project SET Completed = '%s' WHERE Customer = '%s';".formatted("yes", cuName)
			                );
						LocalDate date = LocalDate.now();
						statement.executeUpdate(
								"UPDATE project SET CompletionDate = '%s' WHERE Customer = '%s';".formatted(date, cuName)								);
						if (update != 0 ) {
							System.out.println("Project succesfully finalized");
						}
					}
					break;
				}
			break;
			}
		}
	}
	

	private static void uncompletedProjects(Statement statement) throws SQLException {
		ResultSet project = statement.executeQuery("SELECT * FROM project WHERE Completed = '%s';".formatted("no") 
				);
		System.out.println(printResults(project));
	    project.next();
	    System.out.println(printResults(project));
	}

	// Main menu to print
	public static String menu() {
		StringBuilder sbMenu = new StringBuilder();
		sbMenu.append("1. Enter new project");
		sbMenu.append("\n2. Update project");
		sbMenu.append("\n3. Delete project");
		sbMenu.append("\n4. Search project"); 
		sbMenu.append("\n5. See list of uncompleted projects"); 
		sbMenu.append("\n6. Update contactors contact details"); 
		sbMenu.append("\n7. Finalize a project"); 
		sbMenu.append("\n0. Exit");
	    return sbMenu.toString();
	}

	private static void newProject(Statement statement) throws SQLException {
		Scanner input = new Scanner(System.in);
		while (true) {
    		System.out.println("Enter the project number:");
    		int projectNumber = input.nextInt();
    		ResultSet result = checkID(statement, projectNumber);
    		// Check if project already exists
    		if (result.isBeforeFirst()) {
    			System.out.println("Error! This project already exists, try another one.");
    		}
    		// Enter the details of the new project
    		else {
    			//Projects'details
    			System.out.print("Project details:\n");
    		
    			System.out.print("Enter the project name:\n");
    			String projectName = input.nextLine();
    			System.out.print("Enter the project ERF number:\n");
    			String projectERFnumber = input.nextLine();
    			System.out.print("Enter the project type of building:\n");
    			String projectTOB = input.nextLine();
    			System.out.print("Enter the project address:\n");
    			String projectAddress = input.nextLine();
    			System.out.print("Enter the project deadline: dd/mm/yyyy \n");
    			String projectDeadline = input.nextLine();
    			System.out.print("Enter the total fee:\n");
    			int amtDue = input.nextInt();
    			input.nextLine();
    			System.out.print("Enter the amount paid to date:\n");
    			int amtPaid = input.nextInt();
    			input.nextLine();
    			
    			//Engineer' details
    			System.out.print("\nContractors' details:\n");
    		
    			System.out.print("Enter the contractor of the projects' name: \n");
    			String engineerName = input.nextLine();
    			System.out.print("Enter the contractor of the projects' surname: \n");
    			String engineerSurname = input.nextLine();
    			System.out.print("Enter the contractor of the projects' telephone: \n");
    			String engineerTelephone = input.nextLine();
    			System.out.print("Enter the contractor of the projects' email: \n");
    			String engineerEmail = input.nextLine();
    			System.out.print("Enter the contractor of the projects' address: \n");
    			String engineerAddress = input.nextLine();
    			
    			//Architects' details
    			System.out.print("\nArchitects' details\n");
    		
    			System.out.print("Enter the architect of the projects' name: \n");
    			String architectName = input.nextLine();
    			System.out.print("Enter the architect of the projects' surname: \n");
    			String architectSurname = input.nextLine();
    			System.out.print("Enter the architect of the projects' telephone: \n");
    			String architectTelephone = input.nextLine();
    			System.out.print("Enter the architect of the projects' email: \n");
    			String architectEmail = input.nextLine();
    			System.out.print("Enter the architect of the projects' address: \n");
    			String architectAddress = input.nextLine();
    			
    			//Customers' details
    			System.out.print("\nCustomers' details:\n");
    		
    			System.out.println("Enter the name of the customer:");
    			String cuName = input.nextLine(); 
    			System.out.print("Enter the customers' surname:\n");
    			String cuSurname = input.nextLine();
    			System.out.println("Enter the customers' telephone number:");
    			String cuTelephone = input.nextLine(); 
    			System.out.println("Enter the customers' email address:");
    			String cuEmail = input.nextLine(); 
    			System.out.println("Enter the customers' address:");
    			String cuAddress = input.nextLine(); 
    			
    			// Manager name
    			System.out.println("Enter the name of the manager:");
    			String managerName = input.nextLine(); 
    			System.out.println("Is the project completed: yes/no");
    			String completed = input.nextLine();
    		
    			// Query execution for inserting the new project
    			statement.executeUpdate(
                    "INSERT INTO project VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%d', '%d', '%s')".formatted(projectNumber, projectName, engineerName, architectName, cuName, managerName, projectERFnumber, projectTOB, projectAddress, projectDeadline, amtDue, amtPaid, completed)
                 );
    			statement.executeUpdate(
                    "INSERT INTO engineer VALUES ('%s', '%s', '%s', '%s', '%s')".formatted(engineerName, engineerSurname, engineerTelephone, engineerEmail, engineerAddress)
                 );
    			statement.executeUpdate(
                    "INSERT INTO customer VALUES ('%s', '%s', '%s', '%s', '%s')".formatted(cuName, cuSurname, cuTelephone, cuEmail, cuAddress)
                 );
    			statement.executeUpdate(
                    "INSERT INTO architect VALUES ('%s', '%s', '%s', '%s', '%s')".formatted(architectName, architectSurname, architectTelephone, architectEmail, architectAddress)
                 );
    			System.out.println("New project added");
    			break;
    			}
    		}
		}
		
	private static void updateProject(Statement statement) throws SQLException {
		Scanner input = new Scanner(System.in);
		Scanner updates = new Scanner(System.in);
		
		// Enter the number of the project you want to update
		System.out.println("Enter the number of the project you want to make changes to:");
		int update = input.nextInt();
		System.out.println(updateMenu());
		int choice = input.nextInt();
		
		// Update project deadline
		if (choice == 1) {
			System.out.println("Enter the new deadline");
			String newDeadline = updates.nextLine();
			int rowsAffected = statement.executeUpdate(
                    "UPDATE project SET Deadline = '%s' WHERE Number = '%d'".formatted(newDeadline, update)
                );
			if (rowsAffected != 0) {
				System.out.println("Query complete, " + rowsAffected + " rows updated.");
			}
		}
		// Update amount paid to date
		if (choice == 2) {
			System.out.println("Enter the new amount paid to date: ");
			int newAmtPaid = updates.nextInt();
			// Query execution for updating
			int rowsAffected = statement.executeUpdate(
                    "UPDATE project SET AmountPaid = '%d' WHERE Number = '%s'".formatted(newAmtPaid, update)
                );
			if (rowsAffected != 0) {
				System.out.println("Query complete, " + rowsAffected + " rows updated.");
			}
		}
		
   
    	
    			
		}
	private static ResultSet checkCuName(Statement statement, String cuName) throws SQLException {
		return statement.executeQuery("SELECT * FROM project WHERE Customer = '%s';".formatted(cuName)
				);
	}
	
	private static ResultSet check(Statement statement, String find) throws SQLException {
		 return statement.executeQuery("SELECT * FROM project WHERE Name = '%s';".formatted(find));
	}

	private static void updateEngineer(Statement statement) throws SQLException {
		Scanner input = new Scanner(System.in);
		// Update engineers' contact information
		System.out.println("Enter the name of the engineer you want to make changes to:");
		String find = input.nextLine();
	    ResultSet result = check(statement, find);
	    // Check if project already exists
	    if (result.isBeforeFirst()) {
	    	System.out.println("Error! The engineer does not exist");
	    	}
	    else {
	    	System.out.println("\nUpdate engineers': \n1: telephone \n2: email \n3: address");
	    	int opt = input.nextInt();
				
	    	//Update telephone
	    	if (opt == 1) {
	    		Scanner in = new Scanner(System.in);
	    		System.out.println("Enter the new telephone number:");
	    		String newTelephone = in.nextLine();
	    		int rowsAffected = statement.executeUpdate(
	    			"UPDATE engineer SET Telephone = '%s' WHERE Name = '%s'".formatted(newTelephone, find)
	    	         );
	    				
	    		if (rowsAffected != 0) {
	    			System.out.println("Query complete, " + rowsAffected + " rows updated.");
	    			}
	    		}
	    	//Update email
	   		if (opt == 2) {
	   			Scanner in = new Scanner(System.in);
	    		System.out.println("Enter the new email:\n");
	    		String newEmail = in.nextLine();
	    		int rowsAffected = statement.executeUpdate(
	    	                    "UPDATE engineer SET Email = '%s' WHERE Name = '%s'".formatted(newEmail, find)
	    	           );
	    		if (rowsAffected != 0) {
	    			System.out.println("Query complete, " + rowsAffected + " rows updated.");
	    			}
	   			}
	    	//Update address
	    	if(opt == 3) {
	    		Scanner in = new Scanner(System.in);
	    		System.out.println("Enter the new address:\n");
	    		String newAddress = in.nextLine();
	    		int rowsAffected = statement.executeUpdate(
	    	               "UPDATE engineer SET Address = '%s' WHERE Name = '%s'".formatted(newAddress, find)
	    	           );
	    		if (rowsAffected != 0) {
	    			System.out.println("Query complete, " + rowsAffected + " rows updated.");
	    			}
	    		}
	    	}
		}

	private static void deleteProject(Statement statement) throws SQLException {
		Scanner input = new Scanner(System.in);
		while (true) {
		System.out.println("Enter the number of the project you want to delete: ");
		int projectNumber = input.nextInt();
		ResultSet result = checkID(statement, projectNumber);
		// Check if number is non-existstant
		if (!result.isBeforeFirst()) {
			System.out.println("No project with this number exists.");
		}
		// If the number exists the delete the project
		else {
			// Query execution for deleting
			statement.executeUpdate("DELETE FROM project WHERE Number = '%d';".formatted(projectNumber));
			System.out.println("The project was succesfully removed.");
			break;
			}
		}
	}

	private static void searchProject(Statement statement) throws SQLException {
		Scanner searchInput = new Scanner(System.in);
		Scanner info = new Scanner(System.in);
		System.out.println("1: Search by project number \n2: Search by project name");
		int search = searchInput.nextInt();
		
		// Search for project by number
		if (search == 1) {
			while (true) {
			System.out.println("Enter the number you want to search for:");
			int number = info.nextInt();
			ResultSet numResult = checkID(statement, number);
			
			if (!numResult.isBeforeFirst()) {
				System.out.println("No project with this number exists.");
			}
			// Enter the details of the new project
			else {
				// Query execution for selecting
				ResultSet result = statement.executeQuery("SELECT * FROM project WHERE Number = '%d';".formatted(number) 
						);
				System.out.println(printResults(result));
				break;
			}
			}
		}
			
		// Search for project by name
		else if (search == 2) {
			System.out.println("Enter the name you want to search for:");
			String name = info.nextLine();
			// Query execution for selecting
			ResultSet result = statement.executeQuery("SELECT * FROM project WHERE Name = '%s';".formatted(name) 
				);
			System.out.println(printResults(result));
			}
			// If the user enters an option not available
		else {
			System.out.println("Please enter one of the options.");
		}
	}
		
	private static String printResults(ResultSet results) throws SQLException {
		StringBuilder sbResults = new StringBuilder();
        while (results.next()) {
           sbResults.append("Number: " + results.getInt("Number"));
           sbResults.append("\nName: " + results.getString("Name"));
           sbResults.append("\nEngineer: " + results.getString("Engineer"));
           sbResults.append("\nCustomer: " + results.getString("Customer"));
           sbResults.append("\nManager: " + results.getString("Manager"));
           sbResults.append("\nERFnumber: " + results.getString("ERFnumber"));
           sbResults.append("\nTypeOfBuilding: " + results.getString("TypeOfBuilding"));
           sbResults.append("\nAddress: " + results.getString("Address"));
           sbResults.append("\nDeadline: " + results.getString("Deadline"));
           sbResults.append("\nAmountDue: " + results.getInt("AmountDue"));
           sbResults.append("\nAmountPaid: " + results.getInt("AmountPaid"));
           sbResults.append("\nCompleted: " + results.getString("Completed"));
        }
		return sbResults.toString();
	}


	// Update menu
	private static String updateMenu() {
		StringBuilder sbUpdateMenu = new StringBuilder();
		sbUpdateMenu.append("1: Update project deadline");
		sbUpdateMenu.append("\n2: Update amount paid to date");
		return sbUpdateMenu.toString();
	}
		
    // Check if id is the same as the input
   	private static ResultSet checkID(Statement statement, int idSelection) throws SQLException {
  	// Query execution for selecting 
    return statement.executeQuery("SELECT * FROM project WHERE Number = '%d';".formatted(idSelection));
    }
   	
   	// Currect date
  	private static String date() {
  		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  		return dateFormatter.format(LocalDate.now());
  	}
}