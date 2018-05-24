package gr.aueb.dsp.distributedsystemsproject;

import java.io.Serializable;
import java.util.ArrayList;

// RetObj, is the return object of the AsyncTask, when the AsyncTask has finished, the activity that called it gets the result in the form of this object
public class RetObj implements Serializable{
    private Boolean trained, in_bounds; // Booleans: trained, check too see if the matrices are trained, in_bounds, check to see if users are in bounds of the matrix
    private String fail; // Check to see the reason the recommendation failed
    private ArrayList<POI> recommendation; // Recommendation ArrayList to receive.

    // Constructor
    public RetObj(Boolean trained, Boolean in_bounds, String fail, ArrayList<POI> recommendation){
        this.trained = trained;
        this.in_bounds = in_bounds;
        this.fail = fail;
        this.recommendation = recommendation;
    }

    //Getters
    public Boolean isTrained() {
        return trained;
    }

    public Boolean isIn_bounds() {
        return in_bounds;
    }

    public String getMessage(){
        return fail;
    }

    public ArrayList<POI> getRecommendation() {
        return recommendation;
    }
}
