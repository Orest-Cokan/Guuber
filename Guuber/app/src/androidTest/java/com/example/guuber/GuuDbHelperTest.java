package com.example.guuber;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.internal.inject.InstrumentationContext;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule ;

import com.example.guuber.model.GuuDbHelper;
import com.example.guuber.model.User;
import com.example.guuber.model.Vehicle;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo ;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GuuDbHelperTest {
    private static FirebaseFirestore db;
    private static GuuDbHelper dbHelper;
//    private static CollectionReference users;
    private static User mockUser;
    private static Context instrumentationContext;
    private static Solo solo;
    private User dbUser;


    private static User mockUser(){
        return new User("780", "m@gmail.com", "Matt", "Dziubina", "1", "MattUserName",0,0);
    }
    private static User mockUser2(){
        return new User("404","k@gmail.com","k","kk","111","Kale",0,0);
    }
    private static User mockUser3() { return new User("777","cabbageplant@gmail.com","Randy","Cabbage","000","MachoPlantRandyCabbage",0,0);}
    private static Vehicle mockCar(){ return new Vehicle("Ford","F-150","blue","Randy Cabbage");
    }
    @ClassRule
    public static ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class,true,true);

    @BeforeClass
    public static void setUp() throws InterruptedException {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.waitForActivity(LoginActivity.class, 4000);

        instrumentationContext = InstrumentationRegistry.getInstrumentation().getContext();
        FirebaseApp.initializeApp(instrumentationContext);

        dbHelper = new GuuDbHelper(db);
        mockUser = mockUser();

        // These sleeps are important since the database is async so we need to wait a bit
        Thread.sleep(1000);

    }


    @Test
    public void UserTest() throws InterruptedException{
        dbHelper.checkEmail(mockUser());
        Thread.sleep(5000);
        User obtain;
        //add user and see if it can be read
        obtain = dbHelper.getUser("m@gmail.com");
        Thread.sleep(1000);

        assertEquals(obtain.getEmail(), mockUser.getEmail());
        dbHelper.deleteUser("m@gmail.com");
        Thread.sleep(5000);




    }


//    fails when run all test passes when ran individually
    @Test
    public void deleteTest() throws InterruptedException{
        dbHelper.checkEmail(mockUser2());
        Thread.sleep(5000);
        dbHelper.deleteUser("k@gmail.com");
        Thread.sleep(5000);
        User user;
        user = dbHelper.getUser("k@gmail.com");
        Thread.sleep(1000);
        assertNull(user.getEmail());
    }

    @Test
    public void updateUserTest() throws  InterruptedException{
        dbHelper.checkEmail(mockUser2());
        Thread.sleep(5000);
        dbHelper.updatePhoneNumber("k@gmail.com","696969");
        Thread.sleep(1000);
        dbHelper.updateUsername("k@gmail.com","IWantDie");
        Thread.sleep(1000);
        User user;
        user = dbHelper.getUser("k@gmail.com");
        Thread.sleep(1000);

        assertEquals("IWantDie",user.getUsername());
        assertEquals("696969",user.getPhoneNumber());
        dbHelper.deleteUser("k@gmail.com");
    }
    @Test
    public void createRequestTest() throws InterruptedException{
        dbHelper.checkEmail(mockUser);
        Thread.sleep(5000);
        User user = dbHelper.getUser("m@gmail.com");
        Thread.sleep(1000);
        dbHelper.makeReq(user, 60,"Kingdom of Corona","0000","0000","1000","1000");
        Thread.sleep(1000);
        Map<String,Object> reqDetail;
        reqDetail = dbHelper.getRiderRequest(mockUser());
        Thread.sleep(1000);
        assertEquals((long) 60,reqDetail.get("reqTip"));
        assertEquals("Kingdom of Corona",reqDetail.get("reqLocation"));
        assertEquals("0000",reqDetail.get("oriLat"));
        assertEquals("0000",reqDetail.get("oriLng"));
        assertEquals("1000",reqDetail.get("desLat"));
        assertEquals("1000",reqDetail.get("desLng"));
        dbHelper.cancelRequest(mockUser());
    }

    @Test
    public void requestTest() throws InterruptedException{
        dbHelper.checkEmail(mockUser());
        Thread.sleep(1000);
        dbHelper.checkEmail(mockUser2());
        Thread.sleep(1000);
        dbHelper.checkEmail(mockUser3());
        Thread.sleep(1000);
        User user = dbHelper.getUser("m@gmail.com");
        Thread.sleep(1000);
        dbHelper.makeReq(user, 60,"Kingdom of Corona","0000","0000","1000","1000");
        Thread.sleep(1000);
        user = dbHelper.getUser("k@gmail.com");
        Thread.sleep(1000);
        dbHelper.makeReq(user,10,"A deserted island","9999","9999","7777","7777");
        Thread.sleep(1000);
        ArrayList<Map<String,Object>> reqList = new ArrayList<Map<String,Object>>();
        reqList = dbHelper.getReqList();
        Thread.sleep(1000);
        assertEquals("m@gmail.com",reqList.get(1).get("email"));
        assertEquals("Kingdom of Corona",reqList.get(1).get("reqLocation"));
        assertEquals("k@gmail.com",reqList.get(0).get("email"));
        assertEquals("A deserted island",reqList.get(0).get("reqLocation"));

        dbHelper.cancelRequest(mockUser2());
        Thread.sleep(1000);
        reqList = dbHelper.getReqList();
        Thread.sleep(1000);
        assertEquals(1,reqList.size());

        dbHelper.reqAccepted(mockUser(),mockUser3());
        Thread.sleep(1000);
        reqList = dbHelper.getReqList();
        Thread.sleep(1000);
        Map<String,Object> driverCurReq= dbHelper.getDriverActiveReq(mockUser3());
        Thread.sleep(1000);
        assertEquals(0,reqList.size());
        assertEquals("m@gmail.com",driverCurReq.get("email"));

        dbHelper.cancelRequest(mockUser());
        Thread.sleep(1000);
        driverCurReq = dbHelper.getDriverActiveReq(mockUser3());
        Thread.sleep(1000);
        assertEquals(null,driverCurReq.get("email"));
    }
    @Test
    public void VehicleRegisterTest() throws InterruptedException{
        dbHelper.checkEmail(mockUser3());
        Thread.sleep(5000);
        dbHelper.addVehicle(mockUser3(),mockCar());
        Thread.sleep(1000);

        Vehicle car;
        car = dbHelper.getCarDetail(mockUser3());
        Thread.sleep(2000);
        Vehicle test = mockCar();
        assertEquals(test.getColor(),car.getColor());
        assertEquals(test.getMake(),car.getMake());
        assertEquals(test.getColor(),car.getColor());
        dbHelper.deleteUser("cabbageplant@gmail.com");
    }



    @AfterClass
    public static void tearDown() throws InterruptedException {
        // Delete the user we added to the db

        solo.finishOpenedActivities();
    }
}
