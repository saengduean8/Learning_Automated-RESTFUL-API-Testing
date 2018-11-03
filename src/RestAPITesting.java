import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStreamReader;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class RestAPITesting {


    public static void main(String[] args) {



   try {

        RunTests();
   }
   catch (IOException e){
       System.out.println(e);
   }
   catch (ParseException p){
       System.out.println(p);
   }
    }

    public static void RunTests() throws  IOException , ParseException{
        String url = "https://api.github.com/users/aaaaa";
        String url1 = "https://api.github.com/users/";
        String url2 = "http://httpbin.org/get";
        String url3 = "http://httpbin.org/post";
        String name = "jusere";
        String value1 = "Restaurants";
        String value2 = "Portland,OR";
        String value3 = "12345";
        String value4 = "Anna";


       runTest_statusCode200(url,HttpStatus.SC_OK);
       runTest_statusCode404(url1,name,HttpStatus.SC_NOT_FOUND);
       runTest_mimeType(url);
       runTest_setParameter(url2,value1,value2);
       runTest_postRequest(url3,value3,value4);
       runTest_responseRequest(url);


    }
    //Given exists username
    public static void runTest_statusCode200(String url,int statusCode) throws IOException,ParseException{

        System.out.println("Calling statusCode200 method.......");
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        System.out.println("Sent 'Get' request to URL: " +  url );

        CloseableHttpResponse response = httpclient.execute(httpGet);

        JSONObject jsonObject = deserialize_Json(response);

        String login_value = (String)jsonObject.get("login");
        long id_value = (long)jsonObject.get("id");
        System.out.println("The login value from json response: " + login_value + "\nThe id value from json response: " + id_value);


        try {

            System.out.println("The response code: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            response.close();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            response.close();
        }

        assert  response.getStatusLine().getStatusCode() == statusCode : "status code is not 200";
        assert  (("aaaaa").equals(login_value)) && (12077657 == id_value): "The value does not match";

        System.out.println("***************************************************************\n");

    }

    //Given non-exists username does not exists
    public static void runTest_statusCode404(String url, String userName, int statusCode)throws IOException{

        System.out.println("Calling statusCode404 method.......");

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url + userName);
        System.out.println("Sent 'Get' request to URl: " + url + " follow by the non-exists username: " + userName);


        CloseableHttpResponse response = httpclient.execute(httpGet);


        try {

            System.out.println("The response code: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            response.close();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            response.close();
        }

        assert  response.getStatusLine().getStatusCode() == statusCode;

        System.out.println("***************************************************************\n");

    }
    //Testing the media type: to ensure that the response contains the json data
    public static void runTest_mimeType(String url) throws IOException,ParseException{

        System.out.println("Calling runTest_mimeType.......");

        String jsonMimeType = "application/json";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        System.out.println("Sent Get request to URL: " + url);


        CloseableHttpResponse response = httpclient.execute(httpGet);

        JSONObject jsonObject = deserialize_Json(response);

        String login_value = (String)jsonObject.get("login");
        long id_value = (long)jsonObject.get("id");
        System.out.println("The login value from json response: " + login_value);
        System.out.println("The id value from json response: " + id_value);


        try {

            System.out.println("The response code: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            response.close();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            response.close();
        }

        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
       // System.out.println(jsonMimeType); // return application/json
       // System.out.println((mimeType));  // return application/json
        assert(jsonMimeType.equals(mimeType));
        assert  (("aaaaa").equals(login_value)) && (12077657 == id_value): "The value does not match";

        System.out.println("******************************************************************\n");

    }

    public static void runTest_setParameter(String url,String value1, String value2) throws IOException,ParseException{

        System.out.println("Calling setParameter method.......");

        CloseableHttpClient httpclient = HttpClients.createDefault();


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Business_Type", value1));
        params.add(new BasicNameValuePair("Location", value2));

        HttpGet httpget = new HttpGet(url + "?" + URLEncodedUtils.format(params, "utf-8"));
        System.out.println("Sent Get request to Url: " + url + "?" + URLEncodedUtils.format(params, "utf-8"));
        System.out.println("First input parameter: " + value1 );
        System.out.println("Second input parameter: " + value2);

        CloseableHttpResponse response = httpclient.execute(httpget);

        JSONObject jsonObject = deserialize_Json(response);

        JSONObject args_data = (JSONObject)jsonObject.get("args");
        String businessType_value = (String)args_data.get("Business_Type");
        String location_value = (String)args_data.get("Location");
        System.out.println("The first parameter from json response: " + businessType_value);
        System.out.println("The second parameter from json response: " + location_value);



        try {

            System.out.println("The response code: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            response.close();
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            response.close();
        }

        assert  response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        assert  (("Restaurants").equals(businessType_value))
                && (("Portland,OR").equals(location_value)) : "The value does not match";

        System.out.println("*******************************************************************\n");

    }

    public static void runTest_postRequest(String url,String value1, String value2) throws IOException,ParseException{

        System.out.println("Calling postRequest method.......");
        CloseableHttpClient httpClient =  HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        System.out.println("Sent 'Post' request to URl: " + url);


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id" , value1));
        params.add(new BasicNameValuePair("name", value2));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println("The id value: " + value1 + "\nThe name value: " + value2);


        JSONObject jsonObject = deserialize_Json(response);

        JSONObject form = (JSONObject)jsonObject.get("form");
        String id_value = (String)form.get("id");
        String name_value = (String)form.get("name");
        System.out.println("The id value from json response: " + id_value + "\nThe name value from json response: " + name_value);


        try {

            System.out.println("The response code: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            response.close();

        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            response.close();
        }

        assert response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        assert (("12345").equals(id_value) && ("Anna").equals(name_value))  : "The parameter are not match";

        System.out.println("****************************************************************\n");

    }
    //testing for json payload with one parameter: Given the exists login expected 200. If the login is non-exists the exception will thrown
    public static void runTest_responseRequest(String url)throws IOException, ParseException {

        System.out.println("Calling responseRequest method.......");
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        System.out.print("Sent 'Get' request to URL: " + url);

        CloseableHttpResponse response = httpClient.execute(httpGet);


       JSONObject jsonObject = deserialize_Json(response);


        String login_value = (String)jsonObject.get("login");
        System.out.print("\nThe login value from json response: " + login_value);


        try {

            System.out.println("\nThe response code: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            response.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            response.close();

        }

        assert  ("aaaaa").equals(login_value): "The value does not match";
    }

    public static JSONObject deserialize_Json(CloseableHttpResponse response) throws IOException,ParseException {

        InputStream inputStream = response.getEntity().getContent();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(
                new InputStreamReader(inputStream, "UTF-8"));

        return jsonObject;
    }





}







