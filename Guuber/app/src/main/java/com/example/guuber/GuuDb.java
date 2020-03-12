package com.example.guuber;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


///TODO:
//    -creating a collection of request history and being adding to it
//    -other update methods most likely
//    -creating a collection of ratings and able to add to that (maybe)
//    -an average rating of the user (maybe)
//          - will add once ratings has been established in user?



public class GuuDb {
    private FirebaseFirestore db;

    //Root is the Users collection
    private CollectionReference root;
    // doc to be used to get user information
    private DocumentReference doc;

    public GuuDb(){
        db = FirebaseFirestore.getInstance();
        root = db.collection("Users");

    }

    /**
     * find the doc of user and uses it as a reference
     * @param username - the username to find in the database
     */

    public void findUser(final String username){
        doc = root.document(username);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(username,"Document exist");
                }
                else{
                    Log.d(username, "Document does not exist");
                }
            }
        });
    }
    /**
     * gets user's information
    * --------NOTE: That the user/Rider/Driver class constructor must have no aruguments---------------------
    *-------- Nebye can you try and see if there is another way to do this?-------------------------------
     */
    public User getUserInfo(){
        final User[] user = new User[1];
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user[0] = documentSnapshot.toObject(User.class);
                }
                else{

                }
            }
        });
        return user[0];
    }
    /**
     * Creates and add the minimal info of the user into the database
     * @param email - the user's email used as the key of the document id
     * @param username - the document id, as it has to be unique like a username
     * @param first - first name of the user
     * @param last - Last name of the user
     * @param username - the username that is displayed when they use the app
     * @param phone - the contact number of the user
     * @param type - the type of account being created either rider of driver
     * */
    public void setUpUser(String email, String first,String last,String username,String phone,String type){
        Map<String,Object> user = new HashMap<>();
        user.put("first",first);
        user.put("last",last);
        user.put("username",username);
        user.put("phone",phone);
        if (type.equals("rider")){
            user.put("rider", true);
//            user.put("driver",false);
        }
        else if(type.equals("driver")){
            user.put("driver",true);
//            user.put("rider", false);
        }

        root.document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Document", "DocumentSnapshot successfully written");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Document","Error writing document");
            }
        });
    }
    /***
     * updates the user's username
     * @param name - the new name they want to use
     */
    public void updateUsername(String name){
        doc.update("username",name);
    }
    /**
     * updates the user's contact number
     * @param number - new number to update to*/
    public void updatePhone(String number){
        doc.update("phone",number);
    }

//    public void  updatename ---------------do later not as important














    /**
     * Adds the ratings field if it doesn't exist
     *
     * @param NewRatingPositive - new POSITIVE rating for the user; add amount to add to AggPositive
     * @param NewRatingNegative - new NEGATIVE rating for the user; add amount to add to AggPositive
     * @param AggPositive - Aggregation of all POSITIVE ratings (NewRatingPositive)
     * @param AggNegative - Aggregation of all NEGATIVE ratings (NewRatingNegative)
     *
     * // Don't need the following (can be calculated at request with data above)
     * // Can handle in the database if you want, but for now will be left to the application
     * @param AggTotal - Aggregation of all the ratings of the user (AggPositive + AggNegative)
     * @param PositiveAvg - The percentage of POSITIVE ratings for each user; AggPositive/AggTotal
     * @param NegativeAvg - The percentage of NEGATIVE ratings for each user; AggNegative/AggTotal
     *
     * */
    public void RegRating(int NewRatingPositive, int NewNegativeRating, int AggPositive, int AggNegative)
    {
        Map<String, Integer> rating = new HashMap<>();
        rating.put("NewRatingPositive", NewRatingPostive);
        rating.put("NewRatingNegative", NewRatingNegative);
        rating.put("AggPositive", AggPositive);
        rating.put("AggNegative", AggNegative);

        doc.collection("rating").document("userRating").set(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                Log.d("ratingDoc","successfully created");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.w("ratingDoc","Error writing document");
            }
        });
    }

    /**
     * updates the rating field in the database
     *
     * @param NewRatingPositive - new POSITIVE rating for the user; add amount to add to AggPositive
     * @param NewRatingNegative - new NEGATIVE rating for the user; add amount to add to AggPositive
     * @param AggPositive - Aggregation of all POSITIVE ratings (NewRatingPositive)
     * @param AggNegative - Aggregation of all NEGATIVE ratings (NewRatingNegative)
     *
     * */
    public void UpdateRating(int NewRatingPositive, int NewNegativeRating, int AggPositive, int AggNegative)
    {
        Map<String, Integer> rating = new HashMap<>();
        rating.put("NewRatingPositive", NewRatingPostive);
        rating.put("NewRatingNegative", NewRatingNegative);
        rating.put("AggPositive", AggPositive);
        rating.put("AggNegative", AggNegative);

        doc.collection("rating").document("userRating").update(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("updateRating","Rating updated");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.w("updateRating","Update failed");
            }
        });
    }










    /**
     * Adds the balance field if it doesn't exist or updates it in the database
     * @param balance - amount to put in Database
     * */
    public void addWallet(double balance){
        doc.update("balance", balance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Balance","Documentsnapshot updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Balance"," Error updating document",e);
            }
        });
    }

    /**
     * Adds the registered car into the database
     * @param make - the maker of the car
     * @param model - the car model
     * @param colour - the colour of the car
     * */
    public void regCar(String make,String model, String colour){
        Map<String,Object> car = new HashMap<>();
        car.put("make",make);
        car.put("model", model);
        car.put("colour", colour);
        doc.collection("car").document("userCar").set(car).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("carDoc","successfully created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("carDoc","Error writing document");
            }
        });
    }
    /**
     * updates the registered car into the database
     * @param make - the maker of the car
     * @param model - the car model
     * @param colour - the colour of the car
     * */
    public void updateCar(String make,String model, String colour){
        Map<String,Object> car = new HashMap<>();
        car.put("make",make);
        car.put("model", model);
        car.put("colour", colour);
        doc.collection("car").document("userCar").update(car).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("updateCar","Car updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("updateCar","Update failed");
            }
        });
    }
    /**
     * Get the details of the vehicle the user has
     * @return
     *      DocumentSnapshot of the vehicle which contains info about it
     * */
    public DocumentSnapshot getVehicle(){
        return doc.collection("car").document("usercar").get().getResult();
    }

    /**
     * Put the completed request in the user history
     * if the collection of request history does not exist it will create it
     * @param cost - the cost of the request
     * @param driver - There driver that completed their request
     * */
    public void addToRequestHist(int cost, String driver){
        Calendar date = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date.getTime());

        Map<String,Object> requestDetail = new HashMap<>();
        requestDetail.put("cost",cost);
        requestDetail.put("driver",driver);
        requestDetail.put("date",time);
        doc.collection("reqHistory").add(requestDetail);
    }
    /**
     * Gets the request history of the user
     * @return
     *      Returns a list of documentssnapshots
     * */
    public List<DocumentSnapshot> getRequestHist(){
        return doc.collection("reqHistory").get().getResult().getDocuments();
    }


}
