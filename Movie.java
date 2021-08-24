package myfavouritemovies;

import java.util.ArrayList;

/**
 * Mar 24, 2021 Author @Jasmine Tian Assignment 3 - MyFavouriteMovies - Movie
 * Ms. Wong ICS4U Class Name: Movie
 */
public class Movie {

	private double rating;
	// movie rating
	private String title;
	// movie title
	private String genre;
	// movie genre
	private int ranking = 1;
	// movie ranking
	private static int totalNumMovies = 0;
	// class variable to hold total number of movies
	private static ArrayList<Double> arrRating = new ArrayList<Double>();
	// class variable to hold rating in descending order

	Movie(double rat, String tit, String gen) {
		rating = rat;
		title = tit;
		genre = gen;
		totalNumMovies++;
		addToRatingList(rat);
	}
	// constructor

	public double getRating() {
		return rating;
	}
	// get rating of movie

	public String getTitle() {
		return title;
	}
	// get title of movie

	public String getGenre() {
		return genre;
	}
	// get genre of movie

	public int getRanking() {
		return ranking;
	}
	// get ranking of movie

	public static int getTotalNumMovies() {
		return totalNumMovies;
	}
	// class method to get total number of movies

	public void setRating(int rating) {
		this.rating = rating;
	}
	// set rating of movie

	public void setTitle(String title) {
		this.title = title;
	}
	// set title of movie

	public void setGenre(String genre) {
		this.genre = genre;
	}
	// set genre of movie

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	// set ranking of movie

	public static void setTotalNumMovies(int totalNumMovies) {
		Movie.totalNumMovies = totalNumMovies;
	}
	// set the total number of movies

	private static void addToRatingList(double r) {
		Double d = r;
		// create a double d which equals to the addToRatingList
		boolean hasAdded = false;
		// set hasAdded as false
		if (arrRating.size() == 0) {
			// if the size of arrRating is zero
			arrRating.add(d);
			hasAdded = true;
		} else {
			for (int i = 0; i < arrRating.size(); i++) {
				// else add the arrRating
				if (r >= arrRating.get(i)) { // 90 80 80 70 70 60
					arrRating.add(i, d);
					hasAdded = true;
					break;
				}
			}
		}
		if (hasAdded == false) {
			arrRating.add(d);
		}
	}

	public static int calcRanking(double rat) {
		int ranking = totalNumMovies;
		// let total ranking equals to the totalNumMovies
		int idx;
		idx = binaryRatingSearch(arrRating, 0, totalNumMovies - 1, rat);
		// create the integer index
		ranking = idx + 1;
		// ranking adds as index as
		if (idx > 0) {
			for (int i = idx - 1; i >= 0; i--) {
				if (arrRating.get(i) == rat) {
					ranking = i + 1;
				} else {
					break;
				}
			}
		}
		return ranking;
	}

	// Returns index of rating if it is present in arr[l..r], else return -1
	public static int binaryRatingSearch(ArrayList<Double> arr, int l, int r, double rat) {
		if (r >= l) {
			int mid = l + (r - l) / 2;

			// If the element is present at the middle itself
			if (arr.get(mid) == rat) {
				return mid;
			}

			if (arr.get(mid) < rat) {
				// If rating is greater than mid point
				return binaryRatingSearch(arr, l, mid - 1, rat);
				// it can only be present in left sub-array since rating is in descending order
			}

			return binaryRatingSearch(arr, mid + 1, r, rat);
			// Else the element can only be present in right sub-array
		}

		return -1;
		// We reach here when element is not present in array
	}

}
