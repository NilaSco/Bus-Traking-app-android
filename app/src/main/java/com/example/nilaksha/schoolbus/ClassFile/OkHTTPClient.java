package com.example.nilaksha.schoolbus.ClassFile;

import android.annotation.TargetApi;
import android.os.Build;

import com.example.nilaksha.schoolbus.Login;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Nilaksha on 10/17/2019.
 */

public class OkHTTPClient  {

    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");

    @TargetApi(Build.VERSION_CODES.KITKAT)


//    call web api
    public String login(String email, String password) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\r\n\"userName\":\"" + email + "\",\"password\":\"" + password + "\"}");
        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php//WebService/login")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String register(String name, String nic, String address, String tel, String email, String password) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"name\":\"" + name + "\",\"nic\":\"" + nic + "\"," +
                "\"address\":\"" + address + "\",\"tel\":\"" + tel + "\",\"email\":\"" + email  + "\",\"password\":\"" + password + "\"}");

        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php/WebService/ParentRegister")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String saveChildren(String pid, String name, String school, String lat, String lng) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"pid\":\"" + pid + "\",\"name\":\"" + name + "\"," +
                "\"school\":\"" + school + "\",\"lat\":\"" + lat + "\",\"lng\":\"" + lng  + "\"}");

        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php//WebService/ChildrenRegister")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String saveAttendance(String parentNIC, String driverNIC, String childrenName) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"parentNIC\":\"" + parentNIC + "\",\"driverNIC\":\"" + driverNIC + "\"," +
                "\"childrenName\":\"" + childrenName + "\"}");

        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php//WebService/SaveAttendance")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String saveDriversChildren(String driverNIC, String parentNIC, String childrenName, String school, String fee) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"driverNIC\":\"" + driverNIC + "\",\"parentNIC\":\"" + parentNIC + "\"," +
                "\"childrenName\":\"" + childrenName + "\",\"school\":\"" + school + "\",\"fee\":\"" + fee  + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/SaveDriversChildren")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String savePayment(String name, String fee, String payment,String parentid) throws IOException {

        String driverNIC = Login.USERID;

        RequestBody body = RequestBody.create(mediaType, "{\"name\":\"" + name + "\",\"driverID\":\"" + driverNIC + "\"," +
                "\"fee\":\"" + fee + "\",\"payment\":\"" + payment + "\",\"parentid\":\"" + parentid + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/SavePayment")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String saveLocation(String lat, String lng) throws IOException {

        String driverNIC = Login.USERID;

        RequestBody body = RequestBody.create(mediaType, "{\"driverID\":\"" + driverNIC + "\",\"lat\":\"" + lat + "\"," +
                "\"lng\":\"" + lng + "\"}");

        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php/WebService/SaveLocation")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String saveDriversSchool(String name, String driver) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"driverNIC\":\"" + driver + "\",\"schoolName\":\"" + name +"\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/SaveDriversSchool")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String updateAttendancePickHome(String id) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"id\":\"" + id + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/UpdatePickFromHome")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String updateAttendanceDropSchool(String id) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"id\":\"" + id + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/UpdateDropSchool")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String updateAttendancePickSchool(String id) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"id\":\"" + id + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/UpdatePickFromSchool")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String updateAttendanceDropHome(String id) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"id\":\"" + id + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/UpdateDropHome")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String updateNotificationStatus(String id) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"id\":\"" + id + "\"}");

        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php/WebService/UpdateNotification")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getDriverLocation(String id) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"id\":\"" + id + "\"}");

        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php/WebService/getDriverLocation")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getDropPickLocation(String name) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"name\":\"" + name + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/getDropPickLocation")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String getSchool()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php//WebService/getAllSchool")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getFeeStatus()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php//WebService/feeStatus")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getParent()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/getAllParent")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public String getChildren()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/getAllChildren")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getDriversChildren()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/getAllDriversChildren")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getTodayAtt()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/WebService/getAllTodayAttendance")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getNotification()throws IOException {

        String token = "98253b5f-6640-1672-437d-5ba94874da78";
        RequestBody body = RequestBody.create(mediaType, "{\r\"token\":\""+token+"\"\r\r}");
        Request request = new Request.Builder()
                .url("http://ceylontravel.net/schoolbus/index.php/WebService/getAllNotifi")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    //******************* send email **************************

    public String SendEmailReport(String amount, String due, String income, String sendEmail) throws IOException {


        RequestBody body = RequestBody.create(mediaType, "{\"totalAmount\":\"" + amount + "\",\"totalDue\":\"" + due + "\"," +
                "\"income\":\"" + income + "\",\"sendEmail\":\"" + sendEmail + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/SendEmail/SendIncomeReport")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String SendEmailVehicleFee(String sendEmail) throws IOException {

        String parentid = Login.USERID;

        RequestBody body = RequestBody.create(mediaType, "{\"parentid\":\"" + parentid + "\",\"sendEmail\":\"" + sendEmail + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/SendEmail/SendfeeStatus")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String SendEmailDropPickStatus(String sendEmail,String name) throws IOException {

        RequestBody body = RequestBody.create(mediaType, "{\"name\":\"" + name + "\",\"sendEmail\":\"" + sendEmail + "\"}");

        Request request = new Request.Builder()
                .url("http://schoolbus.ceylontravel.net/index.php/SendEmail/SendDropPickReport")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
