package item;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailedMovieItems {

    private int id;
    private String detailedMovieTitle;
    private String detailedMovieTagline;
    private String detailedMovieStatus;
    private String detailedMovieRatings;
    private String detailedMovieRatingsVote;
    private String detailedMovieLanguages;
    private String detailedMovieGenres;
    private String detailedMovieReleaseDate;
    private String detailtedMovieOverview;
    private String detailedMoviePosterPath;

    public DetailedMovieItems(JSONObject object) {
        try {
            int id = object.getInt("id");
            String detailedTitle = object.getString("title");
            String detailedTagline = object.getString("tagline");
            String detailedStatus = object.getString("status");
            String detailedRating = object.getString("vote_average");
            String detailedRatingVotes = object.getString("vote_count");
            JSONArray languageArray = object.getJSONArray("spoken_languages");
            String detailedLanguages = null;
            // Cek jika languageArray ada datanya atau tidak
            if (languageArray.length() > 0) {
                // Iterate language array untuk mendapatkan language yang akan ditambahkan ke languages
                // fyi: languages itu adalah koleksi dari language field
                for (int i = 0; i < languageArray.length(); i++) {
                    JSONObject languageObject = languageArray.getJSONObject(i);
                    String language = languageObject.getString("name");
                    if (i == 0)
                        detailedLanguages = language + " ";
                    else
                        detailedLanguages += language + " ";
                }
            } else {
                detailedLanguages = "Language Unknown";
            }

            JSONArray genreArray = object.getJSONArray("genres");
            String detailedGenres = null;

            // Cek jika genreArray ada datanya atau tidak, jika tidak set default value untuk String
            // genres (isinya adalah item yg ada di array)
            if (genreArray.length() > 0) {
                // Iterate genre array untuk mendapatkan genre yang akan ditambahkan ke genres
                // fyi: genres itu adalah koleksi dari genre field
                for (int i = 0; i < genreArray.length(); i++) {
                    JSONObject genreObject = genreArray.getJSONObject(i);
                    String genre = genreObject.getString("name");
                    if (i == 0)
                        detailedGenres = genre + " ";
                    else
                        detailedGenres += genre + " ";
                }
            } else {
                detailedGenres = "Genre Unknown";
            }

            String detailedReleaseDate = object.getString("release_date");
            String detailedOverview = object.getString("overview");
            // Dapatkan detailed movie poster path untuk url {@link DetailActivity}
            String detailedPosterPath = object.getString("poster_path");

            // Set values bedasarkan variable-variable yang merepresentasikan field dari sebuah JSON
            // object
            this.id = id;
            this.detailedMovieTitle = detailedTitle;
            this.detailedMovieTagline = detailedTagline;
            this.detailedMovieStatus = detailedStatus;
            this.detailedMovieRatings = detailedRating;
            this.detailedMovieRatingsVote = detailedRatingVotes;
            this.detailedMovieLanguages = detailedLanguages;
            this.detailedMovieGenres = detailedGenres;
            this.detailedMovieReleaseDate = detailedReleaseDate;
            this.detailtedMovieOverview = detailedOverview;
            this.detailedMoviePosterPath = detailedPosterPath;

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

    public String getDetailedMovieTitle() {
        // Set default value for DetailedMovieTitle if DetailedMovieTitle is null or ""
        if (detailedMovieTitle != null && !detailedMovieTitle.isEmpty()) {
            return detailedMovieTitle;
        } else {
            return "Title Unknown";
        }
    }

    public String getDetailedMovieTagline() {
        // Set default value for DetailedMovieTagline if DetailedMovieTagline is null or ""
        if (detailedMovieTagline != null && !detailedMovieTagline.isEmpty()) {
            return detailedMovieTagline;
        } else {
            return "Tagline Unknown";
        }

    }

    public String getDetailedMovieStatus() {
        // Set default value for DetailedMovieStatus if DetailedMovieStatus is null or ""
        if (detailedMovieStatus != null && !detailedMovieStatus.isEmpty()) {
            return detailedMovieStatus;
        } else {
            return "Status Unknown";
        }

    }

    public String getDetailedMovieRatings() {
        return detailedMovieRatings;
    }

    public String getDetailedMovieRatingsVote() {
        return detailedMovieRatingsVote;
    }

    public String getDetailedMovieLanguages() {
        return detailedMovieLanguages;
    }

    public String getDetailedMovieGenres() {
        return detailedMovieGenres;
    }

    public String getDetailedMovieReleaseDate() {
        // Set default value for DetailedMovieReleaseDate if DetailedMovieReleaseDate is null or ""
        if (detailedMovieReleaseDate != null && !detailedMovieReleaseDate.isEmpty()) {
            return detailedMovieReleaseDate;
        } else {
            return "Release Date Unknown";
        }
    }

    public String getDetailtedMovieOverview() {
        // Set default value for DetailedMovieOverview if DetailedMovieOverview is null or ""
        if (detailtedMovieOverview != null && !detailtedMovieOverview.isEmpty()) {
            return detailtedMovieOverview;
        } else {
            return "Overview Unknown";
        }
    }

    public String getDetailedMoviePosterPath() {
        return detailedMoviePosterPath;
    }
}
