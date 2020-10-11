package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
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

	private void startUserInterface(Scanner input) throws SQLException {//	  User Story #1
		boolean keepGoing = true;

		System.out.println("Welcome to film Query Application.");
		System.out.println("Please choose from the following menu below.");
		System.out.println();

		while (keepGoing) {
			int choice;
			printMenu();
			System.out.print("Please Pick a choice from the menu: ");
			choice = kb.nextInt();
			System.out.println();

			switch (choice) {
			case 1:
//	        USER STORY #1 Look up film by ID!
				lookUpFilmById();
				break;
			case 2:
//			USER STORY #1 Look up film by search keyword.

			case 3:

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
	
	private void displayFilm(Film film) {
		System.out.println();
		System.out.println("Title: " + film.getTitle() + "\nRelease Year: " + film.getReleaseYear() + "\nRating: "
				+ film.getRating() + "\nDescription: " + film.getDescription() + "\nLanguage: " 
				+ film.getLanguageId());
		System.out.println("*Actors*");
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
		Film film = db.findFilmById(idChoice);
		if (film == null) {
			System.out.println("No Film with ID: " + idChoice);
		} else {
			displayFilm(film);
		}
	}
	
}
