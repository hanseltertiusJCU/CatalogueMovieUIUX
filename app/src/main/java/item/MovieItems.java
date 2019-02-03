package item;

import org.json.JSONObject;

public class MovieItems {

    private int id;
    private String movieTitle;
    private String movieRatings;
    private String movieReleaseDate;
    private String movieLanguage;
    private String moviePosterPath;

    public MovieItems(JSONObject object) {
        try {
            // Get JSON object fields
            int id = object.getInt("id");
            String title = object.getString("title");
            String rating = object.getString("vote_average");
            String releaseDate = object.getString("release_date");
            String language = object.getString("original_language");
            // Ubah language menjadi upper case
            String displayed_language = language.toUpperCase();
            // Dapatkan poster path untuk di extract ke url {@link MovieAdapter}
            String posterPath = object.getString("poster_path");

            // Set values bedasarkan variable-variable yang merepresentasikan field dari sebuah JSON
            // object
            this.id = id;
            this.movieTitle = title;
            this.movieRatings = rating;
            this.movieReleaseDate = releaseDate;
            this.movieLanguage = displayed_language;
            this.moviePosterPath = posterPath;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        // Set default value for MovieTitle if MovieTitle is null or ""
        if (movieTitle != null && !movieTitle.isEmpty()) {
            return movieTitle;
        } else {
            return "Title Unknown";
        }
    }

    public String getMovieRatings() {
        return movieRatings;
    }

    public String getMovieReleaseDate() {
        // Set default value for MovieReleaseDate if MovieReleaseDate is null or ""
        if (movieReleaseDate != null && !movieReleaseDate.isEmpty()) {
            return movieReleaseDate;
        } else {
            return "Release Date Unknown";
        }
    }

    public String getMovieLanguage() {
        // Set default value for MovieLanguage if MovieLanguage is null or ""
        if (movieLanguage != null && !movieLanguage.isEmpty()) {
            return movieLanguage;
        } else {
            return "Language Unknown";
        }
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }
}