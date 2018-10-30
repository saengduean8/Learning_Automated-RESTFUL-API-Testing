import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


public class RestAPITesting {


    public static void main(String[] args) {



   try {

        RunTests();
   }
   catch (IOException e){
       System.out.println(e);
   }
    }

    public static void RunTests() throws  IOException{
        String url = "https://api.github.com/users/eeeee";
        String url1 = "https://api.github.com/users/";
        String url2 = "https://www.yelp.com/search";
        //https://www.yelp.com/search?find_desc=restaurants&find_loc=Portland,+OR
        String value1 = "restaurants";
        String value2 = "Portland, OR";
        String name = "jusere";

        runTest_statusCode200(url,HttpStatus.SC_OK);
        runTest_statusCode404(url1,name,HttpStatus.SC_NOT_FOUND);
        runTest_mimeType(url);
        setParameter(url2,value1,value2);

    }
    //Exists username: expected 200
    public static void runTest_statusCode200(String url,int statusCode) throws IOException{

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);


        CloseableHttpResponse response = httpclient.execute(httpGet);


        try {

            System.out.println(response.getStatusLine());
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


    }
    //Username does not exists: expected 404
    public static void runTest_statusCode404(String url, String userName, int statusCode)throws IOException{

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url + userName);


        CloseableHttpResponse response = httpclient.execute(httpGet);


        try {

            System.out.println(response.getStatusLine());
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

    }
    //Testing the media type: to ensure that the response contains the json data
    public static void runTest_mimeType(String url) throws IOException{

        String jsonMimeType = "application/json";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);


        CloseableHttpResponse response = httpclient.execute(httpGet);


        try {

            System.out.println(response.getStatusLine());
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
        System.out.println(jsonMimeType);
        System.out.println((mimeType));
        assert(jsonMimeType.equals(mimeType));

    }

    public static void setParameter(String url,String value1, String value2) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("find_desc",value1));
        params.add(new BasicNameValuePair("find_loc",value2));

        HttpGet httpget = new HttpGet(url+ "?" + URLEncodedUtils.format(params, "utf-8"));
        CloseableHttpResponse response = httpclient.execute(httpget);


        try {

            System.out.println(response.getStatusLine());
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

        

    }

    }







