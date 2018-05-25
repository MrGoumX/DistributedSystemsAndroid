package gr.aueb.dsp.distributedsystemsproject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;


//AsyncTask class, gets the results from the master
public class Client extends AsyncTask<String, Void, RetObj>{

    BindActivity bind = null; // Binds the Activity that called the AsyncTask
    private RetObj result; // Result, is the response from master
    private ObjectOutputStream out = null; // ObjectOutputStream
    private ObjectInputStream in = null; // ObjectInputStream
    private String message; // Message from the master why the something failed

    @Override
    protected RetObj doInBackground(String... objects) {
        // Parses the objects passed from the Activity: User ID, User Latitude, Longitude, Top K recommendations, Range, IP & Port
        int user_id = Integer.parseInt(objects[0]);
        double user_lat = Double.parseDouble(objects[1]);
        double user_lng = Double.parseDouble(objects[2]);
        int k = Integer.parseInt(objects[3]);
        double range = Double.parseDouble(objects[4]);
        String ip = objects[5];
        int port = Integer.parseInt(objects[6]);
        // Connect to server
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 100);
            System.out.println("Here");
            if (socket.isConnected()) {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            }
        } catch (IOException e) { // if connection failed, then wait for 2 seconds and try again.
            System.err.println("Server not live");
            return result;
        }
        synchronized (this){
            // Bind to master
            try{
                message = (String) in.readObject();
            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
            if(message.equalsIgnoreCase("Hello, I'm Master")){
                recommendation(user_id, k, user_lat, user_lng, range); // send message to master that you are worker.
            }
        }
        return result;
    }

    private void recommendation(int user_id, int k, double user_lat, double user_lng, double range){
        try {
            // Send that I'm a client
            out.writeObject("Hello, I'm Client");
            out.flush();
            // Send information for recommendation to Master
            out.writeInt(user_id);
            out.writeInt(k);
            out.writeDouble(user_lat);
            out.writeDouble(user_lng);
            out.writeDouble(range);
            out.flush();
            // Get if the matrices are trained
            boolean trained = in.readBoolean();
            if (!trained) {
                // If not return new RetObj that declares that matrices are not trained
                result = new RetObj(trained, null, (String) in.readObject(), null);
            } else {
                // If trained then get if the user given is in bounds
                Boolean in_bounds = in.readBoolean();
                if (in_bounds) {
                    // If in bounds get the recommendation ArrayList and crete the RetObj to send to the Activity
                    ArrayList<POI> recommendation = (ArrayList<POI>) in.readObject();
                    result = new RetObj(trained, in_bounds, null, recommendation);
                } else {
                    // If not in bounds, create RetObj that declares that the user given is out of bounds
                    result = new RetObj(trained, in_bounds, "User given out of bounds", null);
                }
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // This method handles the return to Activity that called the AsyncTask
    @Override
    protected void onPostExecute(RetObj ret){
        // If the class is log that you are not bound with the Activity
        if(bind==null){
            Log.e("BindActivity", "No activity bound to this AsyncTask");
        }
        // If class not null return the RetObj result
        else{
            bind.bind(result);
        }
    }
}
