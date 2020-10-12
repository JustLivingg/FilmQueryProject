package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();
	static Scanner kb = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//    app.test();
		app.launch();
	}

	private void test() throws SQLException {
		Film film = db.findFilmById(1);
		System.out.println(film);
	}

	private void launch() throws SQLException {

		startUserInterface(kb);

		kb.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {// User Story #1
		boolean keepGoing = true;

		System.out.println("Welcome to film Query Application.");
		System.out.println("Please choose from the following menu below.");
		System.out.println();
		// User Story #1 Film Menu
		while (keepGoing) {
			int choice;
			printMenu();
			System.out.print("Please pick a choice from the menu: ");
			choice = kb.nextInt();
			System.out.println();

			switch (choice) {
			case 1:
				// USER STORY #2 Look up film by ID!
				lookUpFilmById();
				break;
			case 2:
				// USER STORY #3 Look up film by search keyword.
				lookUpFilmByKeyword();
				break;
			case 3:
				System.out.println("You have exited the application.");
				System.out.println("Thanks for using Film Query App. Goodbye!");
				keepGoing = false;
				break;
			default:
				System.out.println("Invalid entry, please try again.");
				break;
			}
		}
	}

	public void printMenu() {
		System.out.println();
		System.out.println("================== MENU ==================");
		System.out.println("|  1. Look up film by ID.                |");
		System.out.println("|  2. Look up a film by a search keyword.|");
		System.out.println("|  3. Exit.                              |");
		System.out.println("=========================================");
		System.out.println();
	}

	//At first I was trying to display film from switch and getting stuck into a constant loop.
	//I needed to make additional methods to break the loop. 
	//This method displays toString from films by looking up the film Id.
	private void displayFilm(Film film) {
		System.out.println();
		System.out.println("Title: " + film.getTitle() + "\nRelease Year: " + film.getReleaseYear() + "\nRating: "
				+ film.getRating() + "\nDescription: " + film.getDescription() + "\nLanguage: " + film.getLanguageId());
		System.out.println("Notable actors in this film: ");
		int i = 1;
		for (Actor actor : film.getCast()) {
			System.out.println(i + ": " + actor.getFirstName() + " " + actor.getLastName());
			i++;
		}
	}

	private void lookUpFilmById() throws SQLException {

		int idChoice;
		System.out.print("Please enter the ID of your film (1-1000): ");
		idChoice = kb.nextInt();
		System.out.println("\nFilm Information:");
		Film film = db.findFilmById(idChoice);
		//User Story 2 with the if statement for null.		
		if (film == null) {
			System.out.println("No Film with ID: " + idChoice);
		} else {
			displayFilm(film);
		}
	}

	private void lookUpFilmByKeyword() {
		String searchKeyword = "";
		System.out.println("Please enter your search keyword: ");
		searchKeyword = kb.next();
		//User Story 3.		
		List<Film> filmSearch = db.findFilmsBySearch(searchKeyword);
		//User Story 3 and iteratting through the array of films found by searchword.		
		if (filmSearch.size() ==0 ) {
			System.out.println("No films match your search criteria: " + searchKeyword);
		} else {
			//This fore will list all films that match my keyword.			
			for (Film film : filmSearch) {
				displayFilm(film);
			}
		}
		
	}

}
