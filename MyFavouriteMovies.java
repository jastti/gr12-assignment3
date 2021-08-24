package myfavouritemovies;

import java.io.*;
import java.util.*;

/**
 * Mar 24, 2021
 * Author @Jasmine Tian
 * Assignment 3 - MyFavouriteMovies
 * Ms. Wong
 * ICS4U
 * Class Name: MyFavouriteMovies
 */

/**
 *
 * MyFavouriteMovies is the main class that reads movie records from a file and
 * create movie objects to hold movie information. And it prompts the use to
 * input movie title or genre to search from the movie list and return the
 * matched records in alphabetical order.
 */
public class MyFavouriteMovies {

	/**
	 * @param args the command line arguments
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader inFile = new BufferedReader(new FileReader("input.txt"));
		// create a text document called "input.txt"
		// the input file path and file name need to be changed for your testing.
		String line = " ";
		// used to hold one line data from the input file
		String[] items;
		// itemized array to hold all words in one line separated by space
		Movie curMovie;
		// Movie object to hold movie information read from input file
		ArrayList<Movie> movieList = new ArrayList<Movie>();
		// ArrryList used to hold all movies ordered by title
		ArrayList<Movie> genreList = new ArrayList<Movie>();
		// ArrryList used to hold all movies ordered by genre
		int count = 0;
		// count the current line number
		double rating;
		// rating information
		String title;
		// movie title variable
		String genre;
		// movie genre variable
		Scanner sc = new Scanner(System.in);
		// Scanner for user input

		line = inFile.readLine();
		// read file to parse all movies
		count++;
		while (line != null) {
			// check if the line of the number of rows is null
			System.out.println(line);
			// print the current row in one line
			items = line.split("\\s");
			// split the row into columns
			// check how many items in the movie data line
			if (items.length < 3) {
				// if the line contains incomplete data for movie, ignore it
				System.out.printf("Line#%d: only contains %d words, ignore this line due to missing data!\n", count,
						items.length);
			} else {
				// else check the first word to get the rating
				if (items[0].contains("%")) {
					try {
						// get the digits part
						rating = Double.parseDouble(items[0].substring(0, items[0].length() - 1));
						if (rating >= 0 && rating <= 100) {
							// if the rating is valid, create a new Movie object
							genre = items[items.length - 1];
							// get the movie genre
							title = "";
							// get the movie title
							for (int i = 1; i < items.length - 1; i++) {
								// get the array of the items
								if (i == 1) {
									title = items[i];
									// list one item in the array
								} else {
									title = title + " " + items[i];
									// else add more to the array
								}
							}

							curMovie = new Movie(rating, title, genre);
							// new a Movie object
							addToTitleList(movieList, curMovie);
							// add to array list ordered by title
							addToGenreList(genreList, curMovie);
							// add to array list ordered by genre and title
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						// catch to get the message and print the ignore
						System.out.printf("Line#%d: first word %s is not a valid rating, ignore this line!\n",
								items[0]);
					}
				} else {
					// rating does not contains %, ignore the line
					System.out.printf(
							"Line#%d: does not contain rating, ignore this line due to missing %% for rating!\n",
							count);
				}

			}
			line = inFile.readLine();
			count++;
		}
		processRanking(movieList);
		// calculate the movie ranking, has to wait until all movies are in

		String input = "";
		// start prompt user to enter movie title to search
		do {
			System.out.println("Please enter the movie name or genre (or exit to end the program):");
			input = sc.nextLine();
			if (input.equalsIgnoreCase("exit")) {
				// if user inputs "exit", quit the loop
				break;
			} else {
				// search the movie in the list
				int idx;
				idx = binaryTitleSearch(movieList, 0, movieList.size() - 1, input);
				// search the movie title
				if (idx >= 0) {
					printMovie(movieList.get(idx));
					// print the movie information if there is a match
				} else {
					boolean findGenre = false;
					// define variable to show if movie contains the input genre
					if (input.trim().indexOf(' ') > 0) {
						// check if the input is genre
						System.out.printf("Cannot find the movie: %s\n", input);
						// contains multiple words, user input is not a genre
					} else {
						idx = binaryGenreSearch(genreList, 0, genreList.size() - 1, input);
						// input is only one word, call genre search method to search the movie list

						if (idx >= 0) {
							// if there is a match, find the first item with the same genre in the movie
							// list
							int firstGenre = idx;
							for (int i = idx; i >= 0; i--) {
								// use a loop to find the first item matching the input genre
								if (!genreList.get(i).getGenre().equalsIgnoreCase(input.trim())) {
									firstGenre = i + 1;
									// first genre addition
									break;
								} else {
									firstGenre = i;
									// first genre doesn't change
								}

							}
							// starting from the firstGenre location,
							// print all the movie with the same genre,
							// titles are in alphbetical order
							for (int j = firstGenre; j < genreList.size(); j++) {
								if (genreList.get(j).getGenre().equalsIgnoreCase(input.trim())) {
									printMovie(genreList.get(j));
									// print the list of first genre
								} else {
									break;
									// else break
								}
							}
						} else {
							System.out.printf("Cannot find the genre: %s\n", input);
							// print the invalid sign
						}
					}

				}
			}
		} while (!input.equalsIgnoreCase("exit"));
		// user exit the loop, print good-bye message
		System.out.println("Thanks for using the movie search application. Good Bye!");
	}

	// add movie to arraylist ordered by title
	public static void addToTitleList(ArrayList<Movie> arr, Movie m) {
		boolean movieAdded = false;

		if (arr.isEmpty()) {
			arr.add(m);
			// add the movie directly if the array list is empty
		} else {
			for (int i = 0; i < arr.size(); i++) {
				// use the loop to find the right location in the array list to
				// add the movie to the list in alphabetical order
				if (arr.get(i).getTitle().compareToIgnoreCase(m.getTitle()) >= 0) {
					arr.add(i, m);
					movieAdded = true;
					break;
				}
			}
			if (movieAdded == false) {
				// if cannot find the location after completing the loop
				arr.add(m);
				// add the movie to the end of the list
			}
		}
	}

	// add movie to arraylist ordered by genre
	public static void addToGenreList(ArrayList<Movie> arr, Movie m) {
		boolean movieAdded = false;
		int insertLocation = 0;

		if (arr.isEmpty()) {
			arr.add(m);
			// add the movie directly if the array list is empty
		} else {
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getGenre().compareToIgnoreCase(m.getGenre()) > 0) {
					// if the movie's genre smaller than the current item in genre list
					arr.add(i, m);
					// add it to the current location since the genre is in ascending order
					movieAdded = true;
					break;
				} else {
					if (arr.get(i).getGenre().compareToIgnoreCase(m.getGenre()) == 0) {
						// if the movie's genre equals to the current item's genre
						// we need to check the title of the movie and order it in alphabetical way
						if (arr.get(i).getTitle().compareToIgnoreCase(m.getTitle()) >= 0) {
							// new movie's title smaller than the current item's title
							// insert the new movie to the current location
							arr.add(i, m);
							movieAdded = true;
							break;
						} else {
							// current item's title is greater than the new movie's title
							insertLocation = i + 1;
							// hold the next location to be the potential insert location for the new movie
						}
					}
				}
			}
			if (movieAdded == false) {
				// if cannot find the location after completing the loop
				arr.add(m);
				// add the movie to the end of the list
			}
		}
	}

	// Description: search the title in the movie list ordered by title
	// Return: index of x if it is present in array list [l..r], else return -1
	public static int binaryTitleSearch(ArrayList<Movie> arr, int l, int r, String x) {
		if (r >= l) {
			int mid = l + (r - l) / 2;

			if (arr.get(mid).getTitle().equalsIgnoreCase(x)) {
				// If the element is present at the middle itself
				return mid;
			}

			if (arr.get(mid).getTitle().compareToIgnoreCase(x) > 0) {
				// If element is smaller than mid
				return binaryTitleSearch(arr, l, mid - 1, x);
				// it can only be present in left sub-array
			}

			return binaryTitleSearch(arr, mid + 1, r, x);
			// Else the element can only be present in right sub-array
		}

		return -1;
		// We reach here when element is not present in array
	}

	// Description: search the genre in the movie list ordered by genre
	// Return: index of x if it is present in array list [l..r], else return -1
	public static int binaryGenreSearch(ArrayList<Movie> arr, int l, int r, String x) {
		if (r >= l) {
			int mid = l + (r - l) / 2;

			if (arr.get(mid).getGenre().equalsIgnoreCase(x)) {
				// If the element is present at the middle itself
				return mid;
			}

			if (arr.get(mid).getGenre().compareToIgnoreCase(x) > 0) {
				// If element is smaller than mid
				return binaryGenreSearch(arr, l, mid - 1, x);
				// it can only be present in left sub-array
			}

			return binaryGenreSearch(arr, mid + 1, r, x);
			// Else the element can only be present in right sub-array
		}

		return -1;
		// We reach here when element is not present in array
	}

	// print the movie information
	public static void printMovie(Movie m) {
		System.out.printf("Movie title: %s\n", m.getTitle());
		// print the movie title
		System.out.printf("Genre: %s\n", m.getGenre());
		// print the movie genre
		System.out.printf("Rating: %.1f%%\n", m.getRating());
		// print the movie rating
		System.out.printf("Ranking: %d out of %d\n", m.getRanking(), Movie.getTotalNumMovies());
		// print the movie ranking
	}

	public static void processRanking(ArrayList<Movie> movieList) {
		double rating;
		// calculate the ranking for all the movies

		for (int i = 0; i < movieList.size(); i++) {
			// get the rating of the movie
			rating = movieList.get(i).getRating();
			// set the ranking of the movie, ranking is calculated by calling the class
			// method "calcRanking"
			movieList.get(i).setRanking(Movie.calcRanking(rating));
		}
	}
}
