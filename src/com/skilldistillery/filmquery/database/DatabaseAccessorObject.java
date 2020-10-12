package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private static final String user = "student";
	private static final String pass = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;

		Connection conn = DriverManager.getConnection(URL, user, pass);

		String sql = 
				"SELECT film.id as id, title, description, release_year, language_id,"
				+ " language.name as film_language, rental_duration, rental_rate, length, replacement_cost,"
				+ " rating, special_features, category.name as film_category" + " "
				+ " FROM film"
				+ " JOIN language on film.language_id = language.id"
				+ " JOIN film_category on film.id = film_category.film_id"
				+ " JOIN category on film_category.category_id = category.id" + " "
				+ " WHERE film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {

			rs.beforeFirst();
			while (rs.next()) {
				film = new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("release_year"), rs.getInt("language_id"), rs.getInt("rental_duration"),
						rs.getDouble("rental_rate"), rs.getInt("length"), rs.getDouble("replacement_cost"),
						rs.getString("rating"), rs.getString("special_features"), findActorsByFilmId(rs.getInt("id")));
			}
		}

		// Always close your utilities.
		rs.close();
		stmt.close();
		conn.close();
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = 
					"SELECT id, first_name, last_name "
					+ "FROM actor"
					+ "WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				actor = new Actor(rs.getInt(1), rs.getString(2), rs.getString(3));

			}
			// Always close your utilities.
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> cast = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select * from actor a join film_actor fa on fa.actor_id =a.id join film f on f.id = fa.film_id where f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next() == false && filmId != 0) {
				return null;
			} else {
				while (rs.next()) {
					int id = rs.getInt(1);
					String firstName = rs.getString(2);
					String lastName = rs.getString(3);
					Actor actor = new Actor(id, firstName, lastName);
					cast.add(actor);
				}
			}
			// Always close your utilities.
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cast;
	}

	public List<Film> getFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, title, description, release_year, language_id, rental_duration, ";
			sql += " rental_rate, length, replacement_cost, rating, special_features "
					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id " + " WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() == false && actorId != 0) {
				return null;
			} else {
				while (rs.next()) {
					int filmId = rs.getInt(1);
					String title = rs.getString(2);
					String desc = rs.getString(3);
					short releaseYear = rs.getShort(4);
					int langId = rs.getInt(5);
					int rentDur = rs.getInt(6);
					double rate = rs.getDouble(7);
					int length = rs.getInt(8);
					double repCost = rs.getDouble(9);
					String rating = rs.getString(10);
					String features = rs.getString(11);
					Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost,
							rating, features, findActorsByFilmId(filmId));
					films.add(film);
				}
			}
			// Always close your utilities.
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Film> findFilmsBySearch(String inputText) {
		List<Film> filmSearch = new ArrayList<>();

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select film.id as id, title, description, release_year, language_id,"
					+ " language.name as film_language, rental_duration, rental_rate, length, replacement_cost,"
					+ " rating, special_features, category.name as film_category"
					+ " from film"
					+ " join language on film.language_id = language.id"
					+ " join film_category on film.id = film_category.film_id"
					+ " join category on film_category.category_id = category.id"
					+ " where title like ? or description like ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + inputText + "%");
			stmt.setString(2, "%" + inputText + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				filmSearch.add(new Film(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("release_year"), rs.getInt("language_id"), rs.getInt("rental_duration"), rs.getDouble("rental_rate"), rs.getInt("length"),
						rs.getDouble("replacement_cost"), rs.getString("rating"), rs.getString("special_features"),
						findActorsByFilmId(rs.getInt("id"))));
			}
			
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.err.println(e);
		}

		return filmSearch;
	}


}
